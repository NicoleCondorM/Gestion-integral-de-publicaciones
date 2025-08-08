package ec.edu.espe.auth.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    // Exchange para eventos de auditoría
    public static final String AUDIT_EXCHANGE = "audit.exchange";
    
    // Queue para notificaciones de auditoría
    public static final String AUDIT_QUEUE = "queue.audit";
    
    // Routing key para eventos de autenticación
    public static final String AUDIT_ROUTING_KEY = "audit.auth";
    
    @Bean
    public TopicExchange auditExchange() {
        return new TopicExchange(AUDIT_EXCHANGE);
    }
    
    @Bean
    public Queue auditQueue() {
        return QueueBuilder.durable(AUDIT_QUEUE).build();
    }
    
    @Bean
    public Binding auditBinding() {
        return BindingBuilder
                .bind(auditQueue())
                .to(auditExchange())
                .with(AUDIT_ROUTING_KEY);
    }
}
