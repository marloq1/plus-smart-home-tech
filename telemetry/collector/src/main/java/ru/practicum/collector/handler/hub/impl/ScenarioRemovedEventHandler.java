package ru.practicum.collector.handler.hub.impl;

import org.springframework.stereotype.Component;
import ru.practicum.collector.handler.hub.BaseHubEventHandler;
import ru.practicum.collector.kafka.KafkaEventProducer;
import ru.practicum.collector.model.hubs.HubEvent;
import ru.practicum.collector.model.hubs.HubEventType;
import ru.practicum.collector.model.hubs.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    public ScenarioRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
        ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }



}
