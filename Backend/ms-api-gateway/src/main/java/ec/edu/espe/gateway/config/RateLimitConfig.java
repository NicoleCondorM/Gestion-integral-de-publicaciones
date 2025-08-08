package ec.edu.espe.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {
    
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        // 10 requests per second, burst capacity of 20
        return new RedisRateLimiter(10, 20, 1);
    }
    
    @Bean
    @Primary
    public KeyResolver userKeyResolver() {
        return exchange -> {
            // Rate limit por IP del cliente
            String clientIP = "unknown";
            var remoteAddress = exchange.getRequest().getRemoteAddress();
            if (remoteAddress != null && remoteAddress.getAddress() != null) {
                clientIP = remoteAddress.getAddress().getHostAddress();
            }
            return Mono.just(clientIP);
        };
    }
    
    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }
}
