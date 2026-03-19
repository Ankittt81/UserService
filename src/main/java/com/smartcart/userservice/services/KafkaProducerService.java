package com.smartcart.userservice.services;

import com.smartcart.userservice.events.ResetPasswordEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class KafkaProducerService {
    private KafkaTemplate<String,String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendResetPasswordEvent(ResetPasswordEvent resetPasswordEvent){
        try{
            String message=objectMapper.writeValueAsString(resetPasswordEvent);
            kafkaTemplate.send("resetPassword",message);
            System.out.println("🔥 Kafka Event Sent: " + message);
        }catch (Exception e){
            throw new RuntimeException("Error sending Kafka message", e);
        }
    }
}
