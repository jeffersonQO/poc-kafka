package br.com.monetiza.pockafka.producer;

import br.com.monetiza.pockafka.payload.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsuarioProducer {

    private final String topic;
    private final KafkaTemplate<String, Usuario> kafkaTemplate;

    public UsuarioProducer(@Value("${topic.name}") String topic, KafkaTemplate<String, Usuario> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Usuario usuario) {
        kafkaTemplate.send(topic, usuario).addCallback(
                sucess -> log.info("Sucesso ao enviar mensagem" + sucess.getProducerRecord().value()),
                failure -> log.info("Falha ao enviar mensagem" + failure.getMessage())
        );
    }
}
