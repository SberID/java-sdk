package ru.sberbank.id.sdk.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Данные пользователя. Обязательные поля.
 *
 * @author shago.v.s
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoData {
    @JsonProperty
    private String sub;

    @JsonProperty
    private String iss;

    @JsonProperty
    private String aud;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }
}
