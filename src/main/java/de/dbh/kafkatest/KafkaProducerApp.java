package de.dbh.kafkatest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

public class KafkaProducerApp {

    // The Kafka Topic to send messages to
    private static final String TOPIC = "test-topic";

    // The number of messages to send to the Kafka topic
    private static final int NUMBER_OF_MESSAGES_TO_PRODUCE = 10;

    // The resource path to the test message to send to Kafka
    private static final String PATH_TO_TEST_XML_FILE = "testnachricht.xml";

    private KafkaProducer<String, String> producer;

    public static void main(String[] args) {
        final KafkaProducerApp app = new KafkaProducerApp();
        app.setupProducer();

        final String message = app.loadXmlTestMessage();
        for (int i = 0; i < NUMBER_OF_MESSAGES_TO_PRODUCE; i++) {
            app.publishMessage(message);
        }

        app.closeProducer();
    }

    private void publishMessage(String message) {
        System.out.println("---------- START PUBLISH ----------");
        System.out.printf("About to send message%n%s%nto kafka topic [%s]%n", message, TOPIC);

        producer.send(new ProducerRecord<>(TOPIC, "key1", message));

        System.out.printf("Message sent to topic: [%s]%n", TOPIC);
        System.out.println("---------- END PUBLISH -------------");
    }

    private void closeProducer() {
        producer.close();
    }

    private void setupProducer() {
        final Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }

    private String loadXmlTestMessage() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(PATH_TO_TEST_XML_FILE)) {
            if (in == null) {
                throw new RuntimeException("Test XML message resource not found: " + PATH_TO_TEST_XML_FILE);
            }
            return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load XML test message", e);
        }
    }
}