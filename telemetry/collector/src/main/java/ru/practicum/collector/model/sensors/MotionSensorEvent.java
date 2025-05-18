package ru.practicum.collector.model.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MotionSensorEvent extends SensorEvent {

    private int linkQuality;
    private boolean motion;
    private int voltage;
    private SensorEventType type = SensorEventType.MOTION_SENSOR_EVENT;

}
