package edu.upenn.cis.cis455.exceptions;

public class BadPacketException extends RuntimeException {
    public BadPacketException() {
    }

    public BadPacketException(String message) {
        super(message);
    }

    public BadPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadPacketException(Throwable cause) {
        super(cause);
    }

    public BadPacketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
