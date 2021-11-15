package ru.sberbank.id.sdk.exception;

/**
 * Исключение при несовпадении nonce, использованного при получении auth code и полученного при запросе access token
 *
 * @author shago.v.s
 */
public class IncorrectNonceException extends ApiException {

    /**
     * @param expected ожидаемое значение
     * @param actual полученное значение
     */
    public IncorrectNonceException(String expected, String actual) {
        super(String.format("Incorrect nonce (expected: %s, actual: %s)", expected, actual));
    }
}
