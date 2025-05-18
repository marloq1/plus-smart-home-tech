package ru.practicum.collector.model.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SwitchSensorEvent extends SensorEvent {

    private boolean state;
    private SensorEventType type = SensorEventType.SWITCH_SENSOR_EVENT;
}
