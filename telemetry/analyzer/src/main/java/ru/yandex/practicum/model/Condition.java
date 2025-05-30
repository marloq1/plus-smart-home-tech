package ru.yandex.practicum.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conditions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Condition {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConditionType type;

    @Column(name = "operation", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConditionOperation operation;

    @Column(name = "value",nullable = false)
    private Integer value;
}
