package com.ecommerce.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakfaConfig {
    @Value("${order.event.topicName}")
    private  String topicName;

    @Bean
    public NewTopic createTopic() {
        return new NewTopic(topicName, 3, (short) 1);
    }
}

//@Configuration
//public class KafkaConsumerConfig {
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    @Bean
//    public ConsumerFactory<String, PaymentEvent> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order_service");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
//                new JsonDeserializer<>(PaymentEvent.class));
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//}