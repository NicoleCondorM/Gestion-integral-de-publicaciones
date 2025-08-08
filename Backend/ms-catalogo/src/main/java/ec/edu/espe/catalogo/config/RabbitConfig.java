package ec.edu.espe.catalogo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbitMQ;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbitMQ
public class RabbitConfig {
    
    public static final String PUBLICACION_EXCHANGE = "publicacion.exchange";
    public static final String PUBLICACION_PUBLICADA_QUEUE = "publicacion.publicada";
    public static final String PUBLICACION_RETIRADA_QUEUE = "publicacion.retirada";
    
    @Bean
    public TopicExchange publicacionExchange() {
        return new TopicExchange(PUBLICACION_EXCHANGE);
    }
    
    @Bean
    public Queue publicacionPublicadaQueue() {
        return new Queue(PUBLICACION_PUBLICADA_QUEUE, true);
    }
    
    @Bean
    public Queue publicacionRetiradaQueue() {
        return new Queue(PUBLICACION_RETIRADA_QUEUE, true);
    }
    
    @Bean
    public Binding publicacionPublicadaBinding() {
        return BindingBuilder
            .bind(publicacionPublicadaQueue())
            .to(publicacionExchange())
            .with("publicacion.publicada");
    }
    
    @Bean
    public Binding publicacionRetiradaBinding() {
        return BindingBuilder
            .bind(publicacionRetiradaQueue())
            .to(publicacionExchange())
            .with("publicacion.retirada");
    }
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
