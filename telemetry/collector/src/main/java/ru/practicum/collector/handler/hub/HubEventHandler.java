package ru.practicum.collector.handler.hub;

import ru.practicum.collector.model.hubs.HubEvent;
import ru.practicum.collector.model.hubs.HubEventType;

public interface HubEventHandler {

    HubEventType getMessageType();

    void handle(HubEvent event, String topic);
}
