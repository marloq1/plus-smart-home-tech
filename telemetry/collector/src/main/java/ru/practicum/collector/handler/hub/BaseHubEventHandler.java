package ru.practicum.collector.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.practicum.collector.model.hubs.HubEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {


    private final KafkaEventProducer producer;

    protected abstract T mapToAvro(HubEvent event);

    @Override
    public void handle(HubEvent event, String topic) {
        T recordBase = mapToAvro(event);
        HubEventAvro record = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(recordBase).build();
        String eventClass = event.getClass().getSimpleName();
        log.trace("Сохраняю событие {} связанное с хабом {} в топик {}",
                eventClass, event.getHubId(), topic);
        producer.send(new ProducerRecord<>(topic, record));


    }
}
