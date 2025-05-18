package ru.practicum.collector.model.hubs;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceAddedEvent extends HubEvent {

    private String id;
    private DeviceType deviceType;
    private HubEventType type = HubEventType.DEVICE_ADDED;

}
