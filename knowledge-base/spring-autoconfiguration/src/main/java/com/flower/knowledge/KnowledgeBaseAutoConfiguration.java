package com.flower.knowledge;

import com.flower.knowledge.repository.FlowerRepository;
import com.flower.knowledge.repository.FlowerSightingRepository;
import com.flower.knowledge.repository.UserRepository;
import com.flower.knowledge.repository.UserSightingLikeRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.time.Clock;

@EntityScan("com.flower")
@EnableJpaRepositories(basePackages = {"com.flower.knowledge.repository"})
@Configuration(proxyBeanMethods = false)
public class KnowledgeBaseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Clock.class)
    Clock utcClock() {
        return Clock.systemUTC();
    }

    @Bean
    @Primary
    DataSourceProperties knowledgeBaseDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    HikariDataSource knowledgeBaseDataSource(final DataSourceProperties properties) {
        final HikariDataSource dataSource = createDataSource(properties, HikariDataSource.class);

        if (StringUtils.hasText(properties.getName())) {
            dataSource.setPoolName(properties.getName());
        }

        return dataSource;
    }


    @Bean
    @ConditionalOnClass(JpaKnowledgeBase.class)
    @ConditionalOnMissingBean(KnowledgeBase.class)
    KnowledgeBase extendedJpaKnowledgeBase(final UserRepository userRepository,
                                           final FlowerRepository flowerRepository,
                                           final FlowerSightingRepository sightingRepository,
                                           final UserSightingLikeRepository likeRepository) {
        return new JpaKnowledgeBase(userRepository, flowerRepository, sightingRepository, likeRepository);
    }

    @SuppressWarnings("unchecked")
    static <T> T createDataSource(final DataSourceProperties properties, final Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }
}
