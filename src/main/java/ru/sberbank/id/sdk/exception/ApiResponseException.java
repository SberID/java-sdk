package ru.sberbank.id.sdk.exception;

import ru.sberbank.id.sdk.data.ErrorData;

/**
 * Исключение при получении ошибки от API. Содержит объект {@link ErrorData} с описанием ошибки.
 *
 * @author shago.v.s
 */
public class ApiResponseException extends ApiException{
    private final ErrorData errorData;

    public ApiResponseException(String message, ErrorData errorData) {
        super(message);

        this.errorData = errorData;
    }

    public ErrorData getErrorData() {
        return errorData;
    }
}
