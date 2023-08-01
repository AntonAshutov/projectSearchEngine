package searchengine.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import searchengine.services.StatisticsService;
import searchengine.services.StatisticsServiceImpl;

@Configuration
public class JpaConfig {
    @Bean
    public StatisticsService statisticsService() {
        return new StatisticsServiceImpl();
    }
}
