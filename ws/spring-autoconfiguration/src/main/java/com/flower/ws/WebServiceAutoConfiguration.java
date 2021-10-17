package com.flower.ws;

import com.flower.knowledge.KnowledgeBase;
import com.flower.ws.auth.JwtEntryPoint;
import com.flower.ws.auth.JwtFilter;
import com.flower.ws.auth.JwtProvider;
import com.flower.ws.auth.service.AuthService;
import com.flower.ws.security.BeansConfiguration;
import com.flower.ws.security.JacksonConfiguration;
import com.flower.ws.security.WebMvcConfiguration;
import com.flower.ws.security.WebServiceSecurityConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration(proxyBeanMethods = false)
@Import({
        BeansConfiguration.class,
        SwaggerConfiguration.class,
        JacksonConfiguration.class,
        WebMvcConfiguration.class,
        WebServiceSecurityConfiguration.class
})
@ComponentScan("com.flower.ws.rest")
public class WebServiceAutoConfiguration {

    @Bean
    public JwtEntryPoint jwtEntryPoint() {
        return new JwtEntryPoint();
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }

    @Bean
    public AuthService authService(final PasswordEncoder passwordEncoder,
                                   final KnowledgeBase knowledgeBase,
                                   final JwtProvider jwtProvider) {
        return new AuthService(passwordEncoder, knowledgeBase, jwtProvider);
    }

    @Bean
    public JwtFilter jwtFilter(final JwtProvider jwtProvider,
                               final AuthService authService) {
        return new JwtFilter(jwtProvider, authService);
    }

    @Bean
    public TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }

}
