package ru.sberbank.id.sdk.exception;

/**
 * Исключение при ошибках инициализации {@link ru.sberbank.id.sdk.SberApiClient}
 *
 * @author shago.v.s
 */
public class SberApiClientException extends Exception {

    public SberApiClientException(String message) {
        super(message);
    }

    public SberApiClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
