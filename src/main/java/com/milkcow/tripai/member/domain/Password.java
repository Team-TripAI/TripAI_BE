package com.milkcow.tripai.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.Embeddable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    public static final Password EMPTY = new Password();

    private static final String EMPTY_PASSWORD = "{EMPTY}";

    private String password = EMPTY_PASSWORD;


    public Password(final String password) {
        this.password = TripAIPasswordEncoder.encode(password);
    }

    public boolean matches(final String password) {
        if (this.isEmpty()) {
            return false;
        }
        return TripAIPasswordEncoder.matches(password, this.password);
    }

    public boolean isEmpty() {
        return password.equals(EMPTY_PASSWORD);
    }
}
