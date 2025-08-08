package publicaciones.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ para eventos de publicaciones
 */
@Configuration
public class RabbitMQConfig {

    // Exchange central para eventos de publicaciones
    public static final String PUBLICACIONES_EXCHANGE = "publicaciones.events";
    
    // Exchange para catálogo
    public static final String CATALOG_EXCHANGE = "catalog.exchange";
    
    // Colas para diferentes tipos de eventos
    public static final String PUBLICATION_SUBMITTED_QUEUE = "publicaciones.submitted";
    public static final String REVIEW_REQUESTED_QUEUE = "publicaciones.review.requested";
    public static final String PUBLICATION_APPROVED_QUEUE = "publicaciones.approved";
    public static final String PUBLICATION_PUBLISHED_QUEUE = "publicaciones.published";
    public static final String CHANGES_REQUESTED_QUEUE = "publicaciones.changes.requested";
    public static final String PUBLICATION_WITHDRAWN_QUEUE = "publicaciones.withdrawn";
    
    // Cola para sincronización con catálogo
    public static final String CATALOG_SYNC_QUEUE = "catalog.publication.sync";
    
    // Routing keys
    public static final String PUBLICATION_SUBMITTED_ROUTING_KEY = "publicaciones.publication_submitted";
    public static final String REVIEW_REQUESTED_ROUTING_KEY = "publicaciones.review_requested";
    public static final String PUBLICATION_APPROVED_ROUTING_KEY = "publicaciones.publication_approved";
    public static final String PUBLICATION_PUBLISHED_ROUTING_KEY = "publicaciones.publication_published";
    public static final String CHANGES_REQUESTED_ROUTING_KEY = "publicaciones.changes_requested";
    public static final String PUBLICATION_WITHDRAWN_ROUTING_KEY = "publicaciones.publication_withdrawn";
    public static final String CATALOG_READY_ROUTING_KEY = "catalog.publication.ready";

    @Value("${spring.rabbitmq.host:localhost}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.port:5672}")
    private int rabbitPort;

    @Value("${spring.rabbitmq.username:guest}")
    private String rabbitUsername;

    @Value("${spring.rabbitmq.password:guest}")
    private String rabbitPassword;

    // Configuración del convertidor JSON
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setMandatory(true); // Asegurar que los mensajes lleguen a una cola
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    // ===== EXCHANGES =====
    
    @Bean
    public TopicExchange publicacionesExchange() {
        return ExchangeBuilder
                .topicExchange(PUBLICACIONES_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public TopicExchange catalogExchange() {
        return ExchangeBuilder
                .topicExchange(CATALOG_EXCHANGE)
                .durable(true)
                .build();
    }

    // ===== COLAS =====

    @Bean
    public Queue publicationSubmittedQueue() {
        return QueueBuilder
                .durable(PUBLICATION_SUBMITTED_QUEUE)
                .withArgument("x-dead-letter-exchange", PUBLICACIONES_EXCHANGE + ".dlx")
                .build();
    }

    @Bean
    public Queue reviewRequestedQueue() {
        return QueueBuilder
                .durable(REVIEW_REQUESTED_QUEUE)
                .withArgument("x-dead-letter-exchange", PUBLICACIONES_EXCHANGE + ".dlx")
                .build();
    }

    @Bean
    public Queue publicationApprovedQueue() {
        return QueueBuilder
                .durable(PUBLICATION_APPROVED_QUEUE)
                .withArgument("x-dead-letter-exchange", PUBLICACIONES_EXCHANGE + ".dlx")
                .build();
    }

    @Bean
    public Queue publicationPublishedQueue() {
        return QueueBuilder
                .durable(PUBLICATION_PUBLISHED_QUEUE)
                .withArgument("x-dead-letter-exchange", PUBLICACIONES_EXCHANGE + ".dlx")
                .build();
    }

    @Bean
    public Queue changesRequestedQueue() {
        return QueueBuilder
                .durable(CHANGES_REQUESTED_QUEUE)
                .withArgument("x-dead-letter-exchange", PUBLICACIONES_EXCHANGE + ".dlx")
                .build();
    }

    @Bean
    public Queue publicationWithdrawnQueue() {
        return QueueBuilder
                .durable(PUBLICATION_WITHDRAWN_QUEUE)
                .withArgument("x-dead-letter-exchange", PUBLICACIONES_EXCHANGE + ".dlx")
                .build();
    }

    @Bean
    public Queue catalogSyncQueue() {
        return QueueBuilder
                .durable(CATALOG_SYNC_QUEUE)
                .withArgument("x-dead-letter-exchange", CATALOG_EXCHANGE + ".dlx")
                .build();
    }

    // ===== BINDINGS =====

    @Bean
    public Binding publicationSubmittedBinding() {
        return BindingBuilder
                .bind(publicationSubmittedQueue())
                .to(publicacionesExchange())
                .with(PUBLICATION_SUBMITTED_ROUTING_KEY);
    }

    @Bean
    public Binding reviewRequestedBinding() {
        return BindingBuilder
                .bind(reviewRequestedQueue())
                .to(publicacionesExchange())
                .with(REVIEW_REQUESTED_ROUTING_KEY);
    }

    @Bean
    public Binding publicationApprovedBinding() {
        return BindingBuilder
                .bind(publicationApprovedQueue())
                .to(publicacionesExchange())
                .with(PUBLICATION_APPROVED_ROUTING_KEY);
    }

    @Bean
    public Binding publicationPublishedBinding() {
        return BindingBuilder
                .bind(publicationPublishedQueue())
                .to(publicacionesExchange())
                .with(PUBLICATION_PUBLISHED_ROUTING_KEY);
    }

    @Bean
    public Binding changesRequestedBinding() {
        return BindingBuilder
                .bind(changesRequestedQueue())
                .to(publicacionesExchange())
                .with(CHANGES_REQUESTED_ROUTING_KEY);
    }

    @Bean
    public Binding publicationWithdrawnBinding() {
        return BindingBuilder
                .bind(publicationWithdrawnQueue())
                .to(publicacionesExchange())
                .with(PUBLICATION_WITHDRAWN_ROUTING_KEY);
    }

    @Bean
    public Binding catalogSyncBinding() {
        return BindingBuilder
                .bind(catalogSyncQueue())
                .to(catalogExchange())
                .with(CATALOG_READY_ROUTING_KEY);
    }

    // ===== DEAD LETTER QUEUES =====

    @Bean
    public DirectExchange publicacionesDeadLetterExchange() {
        return ExchangeBuilder
                .directExchange(PUBLICACIONES_EXCHANGE + ".dlx")
                .durable(true)
                .build();
    }

    @Bean
    public Queue publicacionesDeadLetterQueue() {
        return QueueBuilder
                .durable(PUBLICACIONES_EXCHANGE + ".dlq")
                .build();
    }

    @Bean
    public Binding publicacionesDeadLetterBinding() {
        return BindingBuilder
                .bind(publicacionesDeadLetterQueue())
                .to(publicacionesDeadLetterExchange())
                .with("");
    }
}
