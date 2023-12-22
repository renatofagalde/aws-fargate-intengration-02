package br.com.likwi.awsfargate.service;

import br.com.likwi.awsfargate.model.*;
import br.com.likwi.awsfargate.repository.ProductEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
public class ProductEventConsumer {

    private ObjectMapper objectMapper;
    private ProductEventRepository repository;

    @Autowired
    public ProductEventConsumer(ObjectMapper objectMapper, ProductEventRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @JmsListener(destination = "${aws.sqs.queue.product.events.name}")
    public void receiveProductEvent(TextMessage textMessage) throws JMSException, IOException {

        final SNSMessage snsMessage = this.objectMapper.readValue(textMessage.getText(), SNSMessage.class);
        final Envelope envelope = objectMapper.readValue(snsMessage.getMessage(), Envelope.class);
        final ProductEvent productEvent = this.objectMapper.readValue(envelope.getData(), ProductEvent.class);

        log.info(String.format("Mensagem do Produto recebida! Event: %s, produto: %s, ID: %s",
                envelope.getEventType(), productEvent.getProductId(), snsMessage.getMessageId()));

        final ProductEventLog productEventLog = this.repository.save(this.buildProductEventLog(envelope, productEvent,
                snsMessage.getMessageId()));

        try {
            final String log = this.objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(productEventLog);
            ProductEventConsumer.log.info(String.format("Produto persistido no dynamo  %s", log));

        } catch (Exception e) {
            log.error("salvar documento", e.getMessage());
        }


    }

    private ProductEventLog buildProductEventLog(Envelope envelope,
                                                 ProductEvent productEvent, String messageId) {
        long timestamp = Instant.now().toEpochMilli();

        final ProductEventLog productEventLog = ProductEventLog.builder()
                .productEventKey(ProductEventKey.builder()
                        .pk(productEvent.getCode())
                        .sk(envelope.getEventType() + "_" + timestamp)
                        .build()
                )
                .eventType(envelope.getEventType())
                .productId(productEvent.getProductId())
                .userName(productEvent.getUserName())
                .timeStamp(timestamp)
                .messageId(messageId)
                .ttl(Instant.now().plus(Duration.ofMinutes(5)).getEpochSecond()).build(); //cinco minutos, apagar os elementos

        return productEventLog;
    }

}
