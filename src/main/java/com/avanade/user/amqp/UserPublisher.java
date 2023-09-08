package com.avanade.user.amqp;

import com.avanade.user.payloads.request.RequestUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    public UserPublisher(RabbitTemplate rabbitTemplate, ObjectMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    public void createUser(RequestUser requestUser) {
        try {
            String json = mapper.writeValueAsString(requestUser);
            rabbitTemplate.convertAndSend("user.create", json);

            LOGGER.info("Successfully published message: {}", json);
        } catch (JsonProcessingException e) {
            LOGGER.error("JSON processing error: {}", e.getMessage());
        } catch (AmqpException e) {
            LOGGER.error("RabbitMQ publishing error: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred: {}", e.getMessage());
        }
    }

}
