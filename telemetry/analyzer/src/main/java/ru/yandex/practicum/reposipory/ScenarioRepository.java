package ru.yandex.practicum.reposipory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.model.Scenario;

import java.util.List;
import java.util.Optional;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    @Query("SELECT s " +
            "FROM Scenario s " +
            "JOIN FETCH Condition c " +
            "JOIN FETCH actions a " +
            "WHERE s.hubId = :hubId")
    List<Scenario> findByHubId(@Param("hubId") String hubId);
    Optional<Scenario> findByHubIdAndName(String hubId, String name);
}
