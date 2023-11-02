package com.milkcow.tripai.jwt;

public class RefreshTokenProvider extends AccessTokenProvider {

    public RefreshTokenProvider(String secret, long tokenValidityInSeconds) {
        super(secret, tokenValidityInSeconds);
    }

}
