package br.com.monetiza.pockafka.consumer;

import br.com.monetiza.pockafka.payload.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UsuarioConsumer {

    @Value(value = "${topic.name}")
    private String topic;

    @Value(value = "${spring.kafka.group-id}")
    private String groupId;

    @KafkaListener(topics = "${topic.name}", groupId = "${spring.kafka.group-id}", containerFactory = "usuarioConcurrentKafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, Usuario> record) {
        log.info("Consumindo mensagem... ");
        log.info(" " + (record.value()));
    }
}
