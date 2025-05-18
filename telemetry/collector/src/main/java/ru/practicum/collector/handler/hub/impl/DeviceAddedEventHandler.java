package ru.practicum.collector.handler.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.hub.BaseHubEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.practicum.collector.model.hubs.DeviceAddedEvent;
import ru.practicum.collector.model.hubs.HubEvent;
import ru.practicum.collector.model.hubs.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent event) {
        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;
        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name()))
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }
}
