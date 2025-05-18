package ru.practicum.collector.model.hubs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceRemovedEvent extends HubEvent {

    private String id;
    private HubEventType type = HubEventType.DEVICE_REMOVED;
}
