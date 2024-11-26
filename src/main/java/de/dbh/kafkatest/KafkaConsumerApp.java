package de.dbh.kafkatest;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class KafkaConsumerApp {

    // The Kafka Topic to subscribe to and poll for new messages
    private static final String TOPIC = "test-topic";

    // The max number of poll cycles until this app closes the consumer and terminates
    private static final int MAX_POLL_CYCLES = 10;

    private KafkaConsumer<String, String> consumer;

    public static void main(String[] args) {
        final KafkaConsumerApp app = new KafkaConsumerApp();
        app.setupConsumer();
        app.subscribe();
        try {
            app.poll();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            app.closeConsumer();
        }
    }

    private void setupConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(props);
    }

    private void subscribe() {
        this.consumer.subscribe(Collections.singletonList(TOPIC));
        System.out.println("Listening to messages on topic: " + TOPIC);
    }

    private void poll() throws IOException {
        int count = 0;
        while (count < MAX_POLL_CYCLES) {
            count++;
            System.out.printf("--> Poll cycle %d of %d%n", count, MAX_POLL_CYCLES );
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                final String key = record.key();
                final String message = record.value();
                System.out.printf("--> Consumed message: key = %s, message = %s%n", key, message);

                // create target output dir if not exists
                File outDir = new File("target", "out");
                if (!outDir.exists()) {
                    boolean created = outDir.mkdir();
                    if (created) {
                      System.out.println("Output directory created: " + outDir.getAbsolutePath());
                    } else {
                        throw new IOException("Could not create output directory: " + outDir.getAbsolutePath());
                    }
                }

                try (final FileOutputStream outputStream = new FileOutputStream(new File(outDir, UUID.randomUUID() + ".xml"));) {
                    byte[] strToBytes = message.getBytes();
                    outputStream.write(strToBytes);
                }
            }
        }
    }

    private void closeConsumer() {
        this.consumer.close();
    }
}