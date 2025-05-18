package ru.practicum.collector.kafka;

import jakarta.annotation.PostConstruct;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import serializer.AvroSerializer;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.Future;


@Component
public class KafkaEventProducer implements AutoCloseable {

    private KafkaProducer<Void, SpecificRecordBase> producer;

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @PostConstruct
    public void init() {
        this.producer = createProducer();
    }

    public KafkaProducer<Void, SpecificRecordBase> createProducer() {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public Future<RecordMetadata> send(ProducerRecord<Void, SpecificRecordBase> record) {
        Future<RecordMetadata> futureResult = producer.send(record);
        producer.flush();
        return futureResult;
    }

    @Override
    public void close() throws Exception {
        producer.flush();
        producer.close(Duration.ofSeconds(10));
    }
}
