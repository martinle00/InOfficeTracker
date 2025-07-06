package com.project.InOfficeTracker.Config;

import com.project.InOfficeTracker.Services.ParameterStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Autowired
    ParameterStoreService parameterStoreService;

    String localhostUrl = parameterStoreService.getSecret("LocalHostURL");
    String prodUrl = parameterStoreService.getSecret("ProdURL");

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(localhostUrl);
        config.addAllowedOrigin(prodUrl);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
