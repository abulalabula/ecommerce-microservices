package com.ecommerce.order_service.service.impl;

import com.ecommerce.order_service.dao.OrderRepository;
import com.ecommerce.order_service.entity.*;
import com.ecommerce.order_service.kafka.OrderEventProducer;
import com.ecommerce.order_service.payload.OrderRequestDTO;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import com.ecommerce.order_service.service.OrderService;
import com.ecommerce.order_service.service.PaymentServiceClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private RestTemplate restTemplate;
    private OrderEventProducer orderEventProducer;
    private PaymentServiceClient paymentServiceClient;

    private static final String PAYMENT_SERVICE_URL = "http://payment-service/api/payments/process";
    private static final String REFUND_SERVICE_URL = "http://payment-service/api/payments/refund";
    private static final String ITEM_SERVICE_URL = "http://item-service/api/items/graphql";

    public OrderServiceImpl(OrderRepository orderRepository,
                            RestTemplate restTemplate,
                            OrderEventProducer orderEventProducer,
                            PaymentServiceClient paymentServiceClient) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.orderEventProducer = orderEventProducer;
        this.paymentServiceClient = paymentServiceClient;
    }
    // PENDING, COMPLETED, FAILED, REFUNDED
    @Override
    @Transactional
    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        LocalDateTime now = LocalDateTime.now();

        OrderPrimaryKey key = new OrderPrimaryKey(
                orderRequestDTO.getUserId(),
                now,
                UUID.randomUUID().toString());

        Order order = new Order(
                key,
                OrderStatus.CREATED.toString(),
                PaymentStatus.PENDING.toString(),
                orderRequestDTO.getItems().size(),
                0,
                orderRequestDTO.getDetails(),
                orderRequestDTO.getItems(),
                now);

        // Synchronously validate with Item Service inventory
        CompletableFuture<Map.Entry<Boolean, BigDecimal>> validationFuture = validateAndCalculateTotal(orderRequestDTO);
        // When the future completes, process the result
        validationFuture.thenAccept(result -> {
            boolean allItemsValid = result.getKey();  // Extract validation result
            BigDecimal totalAmount = result.getValue(); // Extract total price

            if (allItemsValid) {
                // Synchronously call Payment Service to process payment
                paymentServiceClient.initiatePayment(order, totalAmount);
            } else {
                order.setOrderStatus(OrderStatus.FAILED.toString());
            }

        });

        // If you need to block (not recommended in async flows), use:
        // Map.Entry<Boolean, BigDecimal> result = validationFuture.join();
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order cancelOrder(String userId, String orderId) {
        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(userId, orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");

        LocalDateTime now = LocalDateTime.now();
        Order order = orderOpt.get();
        OrderPrimaryKey key = order.getKey();

        if (order.getOrderStatus().equals(OrderStatus.PAID.toString())) {
            order.setOrderStatus(OrderStatus.CANCELED.toString());
            order.setPaymentStatus(PaymentStatus.PENDING.toString());
            paymentServiceClient.initiateRefund(order, BigDecimal.valueOf(order.getTotalAmount()));
            key.setCreatedAt(now);
            order.setUpdatedAt(now);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order cannot be canceled");
        }
        return order;
    }

    @Override
    public Order updateOrder(String orderId, OrderRequestDTO orderRequestDTO) {
        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(orderRequestDTO.getUserId(), orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");

        LocalDateTime now = LocalDateTime.now();

        Order order = orderOpt.get();
        OrderPrimaryKey key = order.getKey();
        key.setCreatedAt(now);
        order.setUpdatedAt(now);
        order.setOrderStatus(OrderStatus.CREATED.toString());
        order.setPaymentStatus(PaymentStatus.PENDING.toString());
        order.setTotalQuantity(orderRequestDTO.getItems().size());
        order.setTotalAmount(0);
        order.setDetails(orderRequestDTO.getDetails());
        order.setItems(orderRequestDTO.getItems());
        order.setUpdatedAt(LocalDateTime.now());

        // Synchronously validate with Item Service inventory
        CompletableFuture<Map.Entry<Boolean, BigDecimal>> validationFuture = validateAndCalculateTotal(orderRequestDTO);
        // When the future completes, process the result
        validationFuture.thenAccept(result -> {
            boolean allItemsValid = result.getKey();  // Extract validation result
            BigDecimal totalAmount = result.getValue(); // Extract total price

            if (allItemsValid) {
                // Synchronously call Payment Service to process payment
                paymentServiceClient.initiatePayment(order, totalAmount);
            } else {
                order.setOrderStatus(OrderStatus.FAILED.toString());
            }

        });

        // If you need to block (not recommended in async flows), use:
        // Map.Entry<Boolean, BigDecimal> result = validationFuture.join();
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrderById(String userId, String orderId) {
        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(userId, orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");
        return orderOpt.get();
    }

    @Override
    public void processPaymentResponse(PaymentEvent event){
        String orderId = event.getOrderId();
        String userId = event.getUserId().toString();
        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(userId, orderId);
        if (orderOpt.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();
        Order order = orderOpt.get();
        OrderPrimaryKey key = order.getKey();
        key.setCreatedAt(now);
        order.setUpdatedAt(now);

        OrderEvent newEvent = new OrderEvent();
        newEvent.setId(UUID.randomUUID());
        newEvent.setOrderId(key.getOrderId());
        newEvent.setUserId(key.getUserId());
        newEvent.setCreatedAt(LocalDateTime.now());

        if (event.getEventType().equals("payment_successed")) {
            order.setOrderStatus(OrderStatus.PAID.toString());
            order.setPaymentStatus(PaymentStatus.COMPLETED.toString());
            newEvent.setOrderStatus(OrderStatus.PAID.toString());
        } else if (event.getEventType().equals("payment_authorization_failed")) {
            order.setOrderStatus(OrderStatus.FAILED.toString());
            order.setPaymentStatus(PaymentStatus.FAILED.toString());
            newEvent.setOrderStatus(OrderStatus.FAILED.toString());
        } else if(event.getEventType().equals("payment_refunded")) {
            order.setOrderStatus(OrderStatus.CANCELCOMPLETED.toString());
            order.setPaymentStatus(PaymentStatus.REFUNDED.toString());
            newEvent.setOrderStatus(OrderStatus.CANCELCOMPLETED.toString());
            orderRepository.save(order);
            return;
        }
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
//        orderEventProducer.produce(newEvent);
    }


    public CompletableFuture<Map.Entry<Boolean, BigDecimal>> validateAndCalculateTotal(OrderRequestDTO request) {
        List<CompletableFuture<Map.Entry<String, Map.Entry<Integer, BigDecimal>>>> futures = request.getItems().stream()
                .map(item -> fetchStockAndPrice(item.getItemId())
                        .thenApply(result -> (Map.Entry<String, Map.Entry<Integer, BigDecimal>>)
                                new AbstractMap.SimpleEntry<>(item.getItemId(), result)))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    Map<String, Map.Entry<Integer, BigDecimal>> itemDataMap = futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    boolean isValid = request.getItems().stream()
                            .allMatch(item -> itemDataMap.getOrDefault(item.getItemId(), new AbstractMap.SimpleEntry<>(0, BigDecimal.ZERO))
                                    .getKey() >= item.getQuantity());

                    BigDecimal total = request.getItems().stream()
                            .map(item -> itemDataMap.getOrDefault(item.getItemId(), new AbstractMap.SimpleEntry<>(0, BigDecimal.ZERO))
                                    .getValue().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return (Map.Entry<Boolean, BigDecimal>) new AbstractMap.SimpleEntry<>(isValid, total);
                });
    }

    private CompletableFuture<Map.Entry<Integer, BigDecimal>> fetchStockAndPrice(String itemId) {
        CompletableFuture<Integer> stockFuture = fetchSingleStock(itemId);
        CompletableFuture<BigDecimal> priceFuture = fetchSingleItemPrice(itemId);

        return stockFuture.thenCombine(priceFuture, (stock, price) ->
                (Map.Entry<Integer, BigDecimal>) new AbstractMap.SimpleEntry<>(stock, price));
    }

    private CompletableFuture<Integer> fetchSingleStock(String itemId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
            {
              getAvailableStock(id: "%s")
            }
            """.formatted(itemId);

            ResponseEntity<Map> response = executeGraphQLQuery(query);
            return response.getBody() != null ? (int) response.getBody().get("getAvailableStock") : 0;
        });
    }

    private CompletableFuture<BigDecimal> fetchSingleItemPrice(String itemId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = """
            {
              getItem(id: "%s") {
                price
              }
            }
            """.formatted(itemId);

            ResponseEntity<Map> response = executeGraphQLQuery(query);
            if (response.getBody() != null && response.getBody().containsKey("getItem")) {
                Map<String, Object> itemData = (Map<String, Object>) response.getBody().get("getItem");
                return new BigDecimal(String.valueOf(itemData.get("price")));
            }
            return BigDecimal.ZERO;
        });
    }

    private ResponseEntity<Map> executeGraphQLQuery(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForEntity(ITEM_SERVICE_URL, requestEntity, Map.class);
    }



    //    private static final String ITEM_SERVICE_URL = "http://item-service/api/items/check-stock";


    // Wait for async validation result
//        CompletableFuture<Boolean> isValidFuture = validateOrderItems(orderRequestDTO);
//        double totalAmount = calculateTotal(orderItems);
//
//        boolean isValid = isValidFuture.join();
//
//        if (!isValid) {
//            order.setOrderStatus(OrderStatus.FAILED.toString());
//        } else {
//            // Synchronously call Payment Service to process payment
//            paymentServiceClient.initiatePayment(order, totalAmount);
//        }

//    private CompletableFuture<Boolean> validateOrderItems(OrderRequestDTO request) {
//        return CompletableFuture.supplyAsync(() -> {
//            Map<String, Integer> stockMap = fetchStockForItems(request.getItems());
//
//            for (OrderItem item : request.getItems()) {
//                int availableStock = stockMap.getOrDefault(item.getItemId(), 0);
//                if (availableStock < item.getQuantity()) {
//                    return false; // Item is out of stock
//                }
//            }
//            return true;
//        });
//    }
//
//
//    private double calculateTotal(List<OrderItem> orderItems) {
//        // Fetch prices for all items in one GraphQL request
//        Map<String, Double> priceMap = fetchPricesForItems(orderItems);
//
//        return orderItems.stream()
//                .mapToDouble(item -> item.getQuantity() * priceMap.getOrDefault(item.getItemId(), 0.0))
//                .sum();
//    }

//    private Map<String, Integer> fetchStockForItems(List<OrderItem> items) {
//        StringBuilder queryBuilder = new StringBuilder("query { ");
//        for (OrderItem item : items) {
//            queryBuilder.append("item").append(item.getItemId())
//                    .append(": getAvailableStock(id: \"").append(item.getItemId()).append("\") ");
//        }
//        queryBuilder.append("}");
//
//        return executeGraphQLQuery(queryBuilder.toString(), "getAvailableStock", Integer.class);
//    }
//
//    private Map<String, Double> fetchPricesForItems(List<OrderItem> items) {
//        StringBuilder queryBuilder = new StringBuilder("query { ");
//        for (OrderItem item : items) {
//            queryBuilder.append("item").append(item.getItemId())
//                    .append(": getItem(id: \"").append(item.getItemId()).append("\") { price } ");
//        }
//        queryBuilder.append("}");
//
//        return executeGraphQLQuery(queryBuilder.toString(), "price", Double.class);
//    }
//
//    private <T> Map<String, T> executeGraphQLQuery(String query, String fieldName, Class<T> valueType) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("query", query);
//
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
//        ResponseEntity<Map> response = restTemplate.postForEntity(ITEM_SERVICE_URL, requestEntity, Map.class);
//
//        Map<String, T> result = new HashMap<>();
//        if (response.getBody() != null && response.getBody().containsKey("data")) {
//            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
//
//            for (String key : data.keySet()) {
//                Object value = data.get(key);
//                if (value instanceof Number) {
//                    result.put(key.replace("item", ""), valueType.cast(value));
//                } else if (value instanceof Map && ((Map<?, ?>) value).containsKey(fieldName)) {
//                    result.put(key.replace("item", ""), valueType.cast(((Map<?, ?>) value).get(fieldName)));
//                }
//            }
//        }
//        return result;
//    }
//
//    private boolean initiatePayment(Order order, double total) {
//        Map<String, Object> paymentRequest = Map.of(
//                "transactionId", UUID.randomUUID().toString(),
//                "orderId", order.getOrderId(),
//                "userId", order.getUserId(),
//                "amount", total,
//                "currency", "USD",
//                "paymentMethod", "CARD",
//                "cardLast4", String.format("%04d", new Random().nextInt(10000)), // generate last 4 digits of card
//                "isNewCard", new Random().nextBoolean()
//        );
//
//
//        ResponseEntity<Map> response = restTemplate.postForEntity(PAYMENT_SERVICE_URL, paymentRequest, Map.class);
//
//        if (response.getBody() != null) {
//            Map<String, Object> paymentDetails = (Map<String, Object>) response.getBody().get("paymentDetails");
//            if (paymentDetails != null) {
//                String paymentStatus = (String) paymentDetails.get("paymentStatus");
//                return paymentStatus.equals("COMPLETED");
//            }
//        }
//        return false;
//    }

}
