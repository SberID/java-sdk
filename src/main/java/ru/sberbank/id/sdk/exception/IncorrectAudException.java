package ru.sberbank.id.sdk.exception;

/**
 * Исключение при несовпадении clintId c aud в IdToken
 *
 * @author shago.v.s
 */
public class IncorrectAudException extends ApiException {

    /**
     * @param expected ожидаемое значение
     * @param actual полученное значение
     */
    public IncorrectAudException(String expected, String actual) {
        super(String.format("Incorrect received aud (expected: %s, actual: %s)", expected, actual));
    }
}
