package ru.yandex.practicum;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan

public class AggregationApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AggregationApp.class, args);
        AggregationStarter aggregator = context.getBean(AggregationStarter.class);
        aggregator.start();
    }
}