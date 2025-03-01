package com.ecommerce.order_service.service.impl;

import com.ecommerce.order_service.dao.OrderRepository;
import com.ecommerce.order_service.entity.*;
import com.ecommerce.order_service.payload.OrderRequestDTO;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import com.ecommerce.order_service.service.OrderService;
import com.ecommerce.order_service.service.PaymentServiceClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private PaymentServiceClient paymentServiceClient;

    public OrderServiceImpl(OrderRepository orderRepository,
                            PaymentServiceClient paymentServiceClient) {
        this.orderRepository = orderRepository;
        this.paymentServiceClient = paymentServiceClient;
    }

    @Override
    @Transactional
    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        LocalDateTime now = LocalDateTime.now();

        OrderPrimaryKey key = new OrderPrimaryKey(
                orderRequestDTO.getUserId(),
                UUID.randomUUID().toString(),
                now);

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

            order.setTotalAmount(totalAmount.doubleValue());
            if (totalAmount.doubleValue() > 0) {
                // Synchronously non-blocking call Payment Service to process payment
                paymentServiceClient.initiatePayment(order, totalAmount);
            } else {
                order.setOrderStatus(OrderStatus.FAILED.toString());
            }

        });

        orderRepository.save(order);
        return order;
    }

    @Override
    public Order cancelOrder(String userId, String orderId) {
        System.out.println("in cancel order");
        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(userId, orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");

        LocalDateTime now = LocalDateTime.now();
        Order order = orderOpt.get();
        OrderPrimaryKey key = order.getKey();
        System.out.println("order status " + order.getOrderStatus());
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
        System.out.println("In update order, ");
        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(orderRequestDTO.getUserId(), orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");

        LocalDateTime now = LocalDateTime.now();

        Order order = orderOpt.get();
//        if (order.getOrderStatus().equals(OrderStatus.))
        OrderPrimaryKey key = order.getKey();
        key.setCreatedAt(now);
        order.setUpdatedAt(now);
        order.setOrderStatus(OrderStatus.UPDATED.toString());
        order.setPaymentStatus(PaymentStatus.PENDING.toString());
        order.setTotalQuantity(orderRequestDTO.getItems().size());
        order.setTotalAmount(0);
        order.setDetails(orderRequestDTO.getDetails());
        order.setItems(orderRequestDTO.getItems());
        order.setUpdatedAt(LocalDateTime.now());

        // Synchronously validate with Item Service inventory
//        CompletableFuture<Map.Entry<Boolean, BigDecimal>> validationFuture = validateAndCalculateTotal(orderRequestDTO);
//        // When the future completes, process the result
//        validationFuture.thenAccept(result -> {
//            boolean allItemsValid = result.getKey();  // Extract validation result
//            BigDecimal totalAmount = result.getValue(); // Extract total price
//
//            if (allItemsValid) {
//                // Synchronously call Payment Service to process payment
//                paymentServiceClient.initiatePayment(order, totalAmount);
//            } else {
//                order.setOrderStatus(OrderStatus.FAILED.toString());
//            }
//
//        });

        // If you need to block (not recommended in async flows), use:
        // Map.Entry<Boolean, BigDecimal> result = validationFuture.join();
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrderById(String userId, String orderId) {
        System.out.println("in get order by id - user id " + userId + ", order id " + orderId);

        Optional<Order> orderOpt = orderRepository.findLatestOrderByUserIdAndOrderId(userId, orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");

        System.out.println("found");
        return orderOpt.get();
    }

    @Override
    public void processPaymentResponse(PaymentEvent event){
        System.out.println("In process payment response");
        System.out.println(event);
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

        // Fetch prices only, removing stock checks
        List<CompletableFuture<Map.Entry<String, BigDecimal>>> futures = request.getItems().stream()
                .map(item -> fetchSingleItemPrice(item.getItemId())
                        .thenApply(price -> (Map.Entry<String, BigDecimal>) new AbstractMap.SimpleEntry<>(item.getItemId(), price))) // Explicit cast
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {

                    // Create a map of itemId to price
                    Map<String, BigDecimal> itemPriceMap = futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    // No stock validation anymore, assume all items are valid
                    boolean isValid = true;

                    // Calculate total price
                    BigDecimal total = request.getItems().stream()
                            .map(item -> itemPriceMap.getOrDefault(item.getItemId(), BigDecimal.ZERO)
                                    .multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return (Map.Entry<Boolean, BigDecimal>) new AbstractMap.SimpleEntry<>(isValid, total); // Explicit cast
                });
    }


    private CompletableFuture<BigDecimal> fetchSingleItemPrice(String itemId) {
        String query = """
        { getItem(id: "%s") { price stock }}""".formatted(itemId);

        return executeGraphQLQuery(query)
                .thenApply(response -> {
                    if (response != null && response.containsKey("data")) {
                        Map<String, Object> data = (Map<String, Object>) response.get("data");
                        if (data.containsKey("getItem")) {
                            Map<String, Object> itemData = (Map<String, Object>) data.get("getItem");

                            // Extract values safely
                            BigDecimal price = new BigDecimal(String.valueOf(itemData.get("price")));
                            BigDecimal quantity = new BigDecimal(String.valueOf(itemData.get("stock")));

                            return price.multiply(quantity);
                        }
                    }
                    return BigDecimal.ZERO;
                });
    }

    public CompletableFuture<Map> executeGraphQLQuery(String query) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query);

        return webClient.post()
                .uri("/api/items/graphql")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .toFuture();  // Converts Mono to CompletableFuture
    }

}
