package me.silloy.expression;

import lombok.ToString;

@ToString
public class TokenDescriptor {

    private String rawWord;

    private TokenTypeEnum tokenType;

    public TokenDescriptor(String rawWord, TokenTypeEnum tokenType) {
        this.rawWord = rawWord;
        this.tokenType = tokenType;
    }

    public TokenDescriptor(char rawWord, TokenTypeEnum tokenType) {
        this.rawWord = rawWord + "";
        this.tokenType = tokenType;
    }

    public void changeTokenType(TokenTypeEnum tokenType) {
        this.tokenType = tokenType;
    }

    public String getRawWord() {
        return rawWord;
    }

    public TokenTypeEnum getTokenType() {
        return tokenType;
    }

}
