package ru.yandex.practicum.model;


import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "scenarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"hub_id", "name"})
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hub_id", nullable = false)
    private String hubId;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST)
    @MapKeyColumn(
            table = "scenario_conditions",
            name = "sensor_id"
    )
    @JoinTable (
            name = "scenario_conditions",
            joinColumns = @JoinColumn(name="scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id")
    )
    private Map<String,Condition> conditions = new HashMap<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @MapKeyColumn(
            table = "scenario_conditions",
            name = "sensor_id")
    @JoinTable(
            name = "scenario_actions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    private Map<String, Action> actions = new HashMap<>();
}
