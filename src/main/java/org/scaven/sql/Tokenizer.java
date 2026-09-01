package org.scaven.sql;

public class Tokenizer {
    private String token;
    private TokenType type;

    public Tokenizer(String token, TokenType type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }
    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        if (type == TokenType.IDENTIFIER) {
            return "IDENTIFIER(" + token + ")";
        }

        return type.toString();
    }

}
