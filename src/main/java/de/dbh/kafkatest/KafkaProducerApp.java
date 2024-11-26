package kafkatest;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class KafkaProducerApp {
    public static void main(String[] args) {
        String topic = "test-topic";

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        String xmlMessage = "<message><content>Hello, Kafka!</content></message>";
        producer.send(new ProducerRecord<>(topic, "key1", xmlMessage));

        System.out.println("Message sent to topic: " + topic);
        producer.close();
    }
}