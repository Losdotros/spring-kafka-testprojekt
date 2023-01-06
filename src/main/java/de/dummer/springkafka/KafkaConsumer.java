package de.dummer.springkafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerProperties;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Bean;

import de.dummer.springkafka.serialization.Message;

@Component
public class KafkaConsumer {

	@Value("${topic.name}")
	private String topicName;

//    @KafkaListener(id = "my-client-application", topics = "${topic.name}")
//    public void consumer(ConsumerRecord<String, Message> consumerRecord) {
//        System.out.println("Consumed Record Details: " + consumerRecord);
//        Message message = consumerRecord.value();
//        System.out.println("Consumed Message" + message);
//    }

/*
	@InboundChannelAdapter(channel = "from", poller = @Poller(fixedDelay = "5000"))
	@Bean
	public KafkaMessageSource<String, String> source(ConsumerFactory<String, String> cf)  {
	    KafkaMessageSource<String, String> source = new KafkaMessageSource<>(cf, new ConsumerProperties(topicName));
	    source.getConsumerProperties().setGroupId("myGroupId");
	    source.getConsumerProperties().setClientId("myClientId");
	    return source;
	}*/

@Bean
public IntegrationFlow flow(ConsumerFactory<String, String> cf)  {
	return IntegrationFlow.from(Kafka.inboundChannelAdapter(cf, new ConsumerProperties("first-topic"))
					, e -> e.poller(Pollers.fixedDelay(5000).maxMessagesPerPoll(10)))
			.handle(x -> System.out.println("Consumed: " + x.getPayload()))
			.get();
}

}