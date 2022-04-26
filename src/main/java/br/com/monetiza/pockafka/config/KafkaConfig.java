package br.com.monetiza.pockafka.config;

import br.com.monetiza.pockafka.payload.Usuario;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAdress;

    @Value(value = "${topic.name}")
    private String topic;

    @Value(value = "${spring.kafka.group-id}")
    private String groupId;

    @Bean
    public NewTopic createTopic() {
        return new NewTopic(topic, 3, (short) 1);
    }

    @Bean
    public ProducerFactory<String, Usuario> UsuarioProducerFactory() {
        Map<String, Object> configProp = new HashMap<>();
        configProp.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAdress);
        configProp.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProp);
    }

    @Bean
    public ConsumerFactory<String, Usuario> usuarioConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAdress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Usuario.class, false));
    }

    @Bean
    public KafkaTemplate<String, Usuario> usuarioKafkaTemplate() {
        return new KafkaTemplate<>(UsuarioProducerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Usuario> usuarioConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Usuario> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(usuarioConsumerFactory());
        return factory;
    }
}
