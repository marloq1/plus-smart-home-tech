package ru.practicum.collector.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.Future;


@Component
public class KafkaEventProducer {

    private KafkaProducer<Void, SpecificRecordBase> producer = createProducer();


    public KafkaProducer<Void, SpecificRecordBase> createProducer() {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // укажи свои адреса брокеров
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public Future<RecordMetadata> send(ProducerRecord<Void, SpecificRecordBase> record) {
        return producer.send(record);
    }

}
