package ru.yandex.practicum.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.handler.Handler;
import ru.yandex.practicum.kafka.KafkaSnapshotConsumer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SnapshotProcessor {

    private final KafkaSnapshotConsumer consumer;
    private final Handler handler;
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);
    private static final List<String> TOPICS = List.of("telemetry.snapshots.v1");
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public void start() {
        try {
            consumer.subscribe(TOPICS);
            while (true) {

                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                int count = 0;
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    handler.handle((SensorsSnapshotAvro) record.value());
                    manageOffsets(record, count, consumer);
                    count++;

                }
                try {
                    consumer.commitAsync();
                    log.debug("Коммит смещений выполнен успешно");
                } catch (Exception e) {
                    log.error("Ошибка при коммите смещений", e);
                }
            }

        } catch (
                WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {

                consumer.commitAsync();


            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
            }
        }

    }

    private static void manageOffsets(ConsumerRecord<String, SpecificRecordBase> record, int count,
                                      KafkaSnapshotConsumer consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }

    }
}
