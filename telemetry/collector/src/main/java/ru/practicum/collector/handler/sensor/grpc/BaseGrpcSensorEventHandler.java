package ru.practicum.collector.handler.sensor.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseGrpcSensorEventHandler<T extends SpecificRecordBase> implements GrpcSensorEventHandler {

    private String topic = "telemetry.sensors.v1";
    private final KafkaEventProducer producer;

    protected abstract T mapToAvro(SensorEventProto event);

    @Override
    public void handle(SensorEventProto event) {
        T recordBase = mapToAvro(event);
        SensorEventAvro record = SensorEventAvro.newBuilder()
                .setId(event.getId())
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