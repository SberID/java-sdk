package ru.sberbank.id.sdk.exception;

/**
 * Общее исключение при ошибке от API
 *
 * @author shago.v.s
 */
public class ApiException extends Exception {

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
