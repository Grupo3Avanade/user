package com.avanade.user.amqp;

import com.avanade.user.payloads.request.RequestUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class UserListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserListener.class);
    private final ObjectMapper mapper;

    public UserListener(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @RabbitListener(queues = "user.create")
    public void receiveUser(Message message) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            RequestUser requestUser = mapper.readValue(json, RequestUser.class);

            LOGGER.info("Received message with content: {}", requestUser);

        } catch (JsonProcessingException e) {
            LOGGER.error("JSON processing error: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred: {}", e.getMessage());
        }
    }

}
