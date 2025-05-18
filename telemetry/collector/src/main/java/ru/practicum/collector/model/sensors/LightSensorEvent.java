package ru.practicum.collector.model.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LightSensorEvent extends SensorEvent {

    private int linkQuality;
    private int luminosity;
    private SensorEventType type = SensorEventType.LIGHT_SENSOR_EVENT;
}
