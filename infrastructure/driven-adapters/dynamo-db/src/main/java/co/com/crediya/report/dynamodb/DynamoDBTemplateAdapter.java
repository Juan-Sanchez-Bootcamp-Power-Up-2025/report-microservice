package co.com.crediya.report.dynamodb;

import co.com.crediya.report.dynamodb.helper.TemplateAdapterOperations;
import co.com.crediya.report.model.report.Report;
import co.com.crediya.report.model.report.gateways.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<
        Report,
        String,
        ReportEntity>
        implements ReportRepository {

    private final DynamoDbAsyncTable<ReportEntity> table;

    private final DynamoDbAsyncClient dynamo;

    private final String tableName;

    private final String pkAttrName;

    private final String metricKey;

    public DynamoDBTemplateAdapter(
            DynamoDbEnhancedAsyncClient connectionFactory,
            DynamoDbAsyncClient dynamo,
            ObjectMapper mapper,
            @Value("${adapters.aws.dynamodb.tableName}") String tableName,
            @Value("${adapters.aws.dynamodb.pkAttrName}") String pkAttrName,
            @Value("${adapters.aws.dynamodb.metricKey}") String metricKey
    ) {
        super(connectionFactory, mapper, entity -> mapper.map(entity, Report.class), tableName);
        this.table = connectionFactory.table(tableName, TableSchema.fromBean(ReportEntity.class));
        this.dynamo = dynamo;
        this.tableName = tableName;
        this.pkAttrName = pkAttrName;
        this.metricKey = metricKey;
    }

    @Override
    public Mono<Report> getReport() {
        return Mono.fromCompletionStage(() ->
                table.getItem(r -> r.key(Key.builder().partitionValue(metricKey).build())))
                .map(reportEntity -> reportEntity == null
                        ? Report.builder().metricKey(metricKey).approvedLoansCount(0L).build()
                        : Report.builder()
                        .metricKey(reportEntity.getMetricKey())
                        .approvedLoansCount(reportEntity.getApprovedLoansCount() == null ? 0L : reportEntity.getApprovedLoansCount())
                        .build()
                )
                .doOnSubscribe(s -> log.debug("Querying metric {}", metricKey))
                .doOnSuccess(report -> log.debug("Metric {} = {}", metricKey, report.getApprovedLoansCount()));
    }

    @Override
    public Mono<Void> incrementApprovedLoansCount() {
        return putIfAbsent()
                .then(doAtomicAdd())
                .doOnSubscribe(s -> log.debug("Incrementing metric {}", metricKey))
                .doOnSuccess(n -> log.debug("Metric {} incremented to {}", metricKey, n))
                .doOnError(ex -> log.error("Error incrementing metric {}", metricKey, ex))
                .then(); // devolvemos Mono<Void>
    }

    /** Crea el registro si no existe (idempotente). */
    private Mono<Void> putIfAbsent() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(pkAttrName, AttributeValue.builder().s(metricKey).build());
        item.put("approvedLoansCount", AttributeValue.builder().n("0").build());

        PutItemRequest put = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .conditionExpression("attribute_not_exists(#pk)")  // solo crea si NO existe
                .expressionAttributeNames(Map.of("#pk", pkAttrName))
                .build();

        return Mono.fromCompletionStage(() -> dynamo.putItem(put))
                .then()
                // si ya existía, se ignora el error de condición
                .onErrorResume(ConditionalCheckFailedException.class, e -> Mono.empty());
    }

    /** Incremento atómico con ADD; devuelve el nuevo conteo. */
    private Mono<Long> doAtomicAdd() {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(pkAttrName, AttributeValue.builder().s(metricKey).build());

        Map<String, AttributeValue> values = new HashMap<>();
        values.put(":incr", AttributeValue.builder().n("1").build());

        UpdateItemRequest req = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .updateExpression("ADD approvedLoansCount :incr")
                .expressionAttributeValues(values)
                .returnValues(ReturnValue.UPDATED_NEW)
                .build();

        return Mono.fromCompletionStage(() -> dynamo.updateItem(req))
                .map(resp -> Long.parseLong(
                        resp.attributes()
                                .getOrDefault("approvedLoansCount", AttributeValue.builder().n("0").build())
                                .n()
                ))
                .doOnSuccess(s -> log.debug("Added {}", s))
                .doOnError(ex -> log.error("Failed :( {}", String.valueOf(ex)));
    }

}
