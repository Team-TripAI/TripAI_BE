package com.milkcow.tripai.global.config.jwt;

import com.milkcow.tripai.jwt.AccessTokenProvider;
import com.milkcow.tripai.jwt.RefreshTokenProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    @Bean(name = "accessTokenProvider")
    public AccessTokenProvider accessTokenProvider(JwtProperties properties) {
        return new AccessTokenProvider(properties.getSecret(), properties.getAccessTokenValidityInSeconds());
    }

    @Bean(name = "refreshTokenProvider")
    public RefreshTokenProvider refreshTokenProvider(JwtProperties jwtProperties) {
        return new RefreshTokenProvider(jwtProperties.getRefreshTokenSecret(),
                jwtProperties.getRefreshTokenValidityInSeconds());
    }
}
