package ru.practicum.collector.handler.sensor;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.practicum.collector.model.sensors.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {


    private final KafkaEventProducer producer;

    protected abstract T mapToAvro(SensorEvent event);

    @Override
    public void handle(SensorEvent event, String topic) {
        T recordBase = mapToAvro(event);
        SensorEventAvro record = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(recordBase).build();
        String eventClass = event.getClass().getSimpleName();
        log.trace("Сохраняю событие {} связанное с хабом {} в топик {}",
                eventClass, event.getHubId(), topic);
        producer.send(new ProducerRecord<>(topic, record));

    }


}
