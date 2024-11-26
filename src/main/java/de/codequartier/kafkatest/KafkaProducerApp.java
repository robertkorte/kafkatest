package de.codequartier.kafkatest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

public class KafkaProducerApp {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducerApp.class);

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
        LOG.info("---------- START PUBLISH ----------");
        LOG.info(String.format("About to send message%n%s%nto kafka topic [%s]", message, TOPIC));

        producer.send(new ProducerRecord<>(TOPIC, "key1", message));

        LOG.info(String.format("Message sent to topic: [%s]", TOPIC));
        LOG.info("---------- END PUBLISH -------------");
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