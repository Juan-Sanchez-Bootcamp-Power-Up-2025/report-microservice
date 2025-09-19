package co.com.crediya.report.dynamodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntity {

    private String metricKey;

    private Long approvedLoansCount;

    private BigDecimal approvedLoansAmount;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("metricKey")
    public String getMetricKey() { return metricKey; }

    @DynamoDbAttribute("approvedLoansCount")
    public Long getApprovedLoansCount() { return approvedLoansCount;}

    @DynamoDbAttribute("approvedLoansAmount")
    public BigDecimal getApprovedLoansAmount() { return approvedLoansAmount;}

}
