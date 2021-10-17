package com.flower.ws.security;

import com.flower.ws.auth.JwtEntryPoint;
import com.flower.ws.auth.JwtFilter;
import com.flower.ws.auth.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebServiceSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtFilter jwtFilter;
    private final String[] unprotectedEndpoints = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/api/v1/auth/**",
            "/health",
            "/api/v1/flower/**",
    };

    private final String[] getUnprotectedEndpoints = {"/api/v1/flower/**"};

    public WebServiceSecurityConfiguration(final AuthService authService,
                                           final PasswordEncoder passwordEncoder,
                                           final JwtEntryPoint jwtEntryPoint,
                                           final JwtFilter jwtFilter) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEntryPoint = jwtEntryPoint;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers(unprotectedEndpoints)
                .permitAll()
                .antMatchers(HttpMethod.GET, getUnprotectedEndpoints)
                .permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(passwordEncoder);
    }
}
