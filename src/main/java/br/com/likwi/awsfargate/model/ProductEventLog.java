package br.com.likwi.awsfargate.model;

import br.com.likwi.awsfargate.enums.EventType;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@DynamoDBTable(tableName = "product-event")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductEventLog {

    @Id
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private ProductEventKey productEventKey;

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = "event_type")
    private EventType eventType;

    @DynamoDBAttribute(attributeName = "ttl")
    private long ttl;

    @DynamoDBAttribute(attributeName = "product_id")
    private long productId;

    @DynamoDBAttribute(attributeName = "user_name")
    private String userName;

    @DynamoDBAttribute(attributeName = "time_stamp")
    private long timeStamp;

    @DynamoDBAttribute(attributeName = "message_id")
    private String messageId;

    @DynamoDBHashKey(attributeName = "pk")
    public String getPk() {
        return this.productEventKey != null ? this.productEventKey.getPk() : null;
    }

    public void setPk(String pk) {
        if (this.productEventKey == null) {
            this.productEventKey = new ProductEventKey();
        }
        if (StringUtils.isEmpty(pk)) {
            pk = UUID.randomUUID().toString().split("-")[0];
        }
        this.productEventKey.setPk(pk);
    }

    @DynamoDBRangeKey(attributeName = "sk")
    public String getSk() {
        return this.productEventKey != null ? this.productEventKey.getSk() : null;
    }

    public void setSk(String sk) {
        if (this.productEventKey == null) {
            this.productEventKey = new ProductEventKey();
        }

        this.productEventKey.setSk(sk);
    }


}
