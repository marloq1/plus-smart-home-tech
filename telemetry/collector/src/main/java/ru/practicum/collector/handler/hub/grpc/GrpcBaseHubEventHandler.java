package ru.practicum.collector.handler.hub.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class GrpcBaseHubEventHandler<T extends SpecificRecordBase> implements GrpcHubEventHandler {


    private String topic = "telemetry.hubs.v1";
    private final KafkaEventProducer producer;

    protected abstract T mapToAvro(HubEventProto event);

    @Override
    public void handle(HubEventProto event) {
        T recordBase = mapToAvro(event);
        HubEventAvro record = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(),
                        event.getTimestamp().getNanos()))
                .setPayload(recordBase).build();
        String eventClass = event.getClass().getSimpleName();
        log.trace("Сохраняю событие {} связанное с хабом {} в топик {}",
                eventClass, event.getHubId(), topic);
        producer.send(new ProducerRecord<>(topic, record));


    }
}
