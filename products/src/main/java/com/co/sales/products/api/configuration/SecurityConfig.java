package com.co.sales.products.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.co.sales.products.api.security.AuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableRetry
public class SecurityConfig {
  
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> 
          authorizationManagerRequestMatcherRegistry.requestMatchers("/**").authenticated())
          .httpBasic(Customizer.withDefaults())
          .sessionManagement(httpSecuritySessionManagementConfigurer -> 
          httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
      
        return http.build();
    }

}