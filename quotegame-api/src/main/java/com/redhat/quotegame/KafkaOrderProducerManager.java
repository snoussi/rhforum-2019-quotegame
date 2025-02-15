package com.redhat.quotegame;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.redhat.quotegame.model.Order;
import com.redhat.quotegame.util.OrderSerializer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

@ApplicationScoped
public class KafkaOrderProducerManager {

    private Producer<String, Order> producer;

    @PostConstruct
    public void create() {
        producer = createProducer();
    }

    public static Producer<String, Order> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "quotegame-api");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderSerializer.class.getName());
        return new KafkaProducer<String, Order>(props);
    }

    public void publish(Order order) {
        producer.send(new ProducerRecord<>("quotegame-orders", order.getQuote(), order));
        producer.flush();
    }
}