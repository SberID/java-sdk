package ru.sberbank.id.sdk.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Данные об ошибке
 *
 * @author shago.v.s
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorData {

    @JsonProperty("httpCode")
    private String httpCode;

    @JsonProperty("httpMessage")
    private String httpMessage;

    @JsonProperty("moreInformation")
    private String moreInformation;

    public String getHttpCode() {
        return httpCode;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public String getMoreInformation() {
        return moreInformation;
    }
}
