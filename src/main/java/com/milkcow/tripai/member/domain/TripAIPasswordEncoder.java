package com.milkcow.tripai.member.domain;


import org.springframework.security.crypto.bcrypt.BCrypt;

    public class TripAIPasswordEncoder {

        public static String encode(final String plainText) {
            return BCrypt.hashpw(plainText, BCrypt.gensalt());
        }

        public static boolean matches(final String plainText, final String encoded) {
            return BCrypt.checkpw(plainText, encoded);
        }
    }


