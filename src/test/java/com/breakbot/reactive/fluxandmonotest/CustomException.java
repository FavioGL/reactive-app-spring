package com.breakbot.reactive.fluxandmonotest;

public class CustomException extends Throwable {
    private String message;
    public CustomException(Throwable t){
        this.message = t.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
