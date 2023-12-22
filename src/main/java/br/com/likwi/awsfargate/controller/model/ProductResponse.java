package br.com.likwi.awsfargate.controller.model;

import br.com.likwi.awsfargate.enums.EventType;
import br.com.likwi.awsfargate.model.ProductEventLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProductResponse {

    private String code;
    private EventType eventType;
    private long productId;
    private String username;
    private long timestamp;
    private String messageId;

    //poderia ter sido via contrutor
    public static ProductResponse toReponse(ProductEventLog productEventLog) {
        return ProductResponse.builder()
                .code(productEventLog.getPk())
                .eventType(productEventLog.getEventType())
                .productId(productEventLog.getProductId())
                .username(productEventLog.getUserName())
                .timestamp(productEventLog.getTimeStamp())
                .messageId(productEventLog.getMessageId())
                .build();
    }
}
