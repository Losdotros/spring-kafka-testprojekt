package de.dummer.springkafka;

import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.dummer.springkafka.serialization.Message;

@Lazy(false)
@Component
public class KafkaProducer {

    @Value("${topic.name}")
    private String topicName;

    private int payloadCounter = 0;

    private KafkaTemplate kafkaTemplate;

    public KafkaProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(cron = "*/2 * * * * *")
    public void sendMessage() {
        UUID key = UUID.randomUUID();
        payloadCounter += 1;
        Message payload = new Message(Integer.toString(payloadCounter));
        System.out.println("Sending Data " + payload);

        ProducerRecord<String, Message> record = new ProducerRecord<String, Message>(topicName,
                key.toString(),
                payload);

        kafkaTemplate.send(record);
    }
}
