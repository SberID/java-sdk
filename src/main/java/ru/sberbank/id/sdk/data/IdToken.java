package ru.sberbank.id.sdk.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Идентификационный токен
 *
 * @author shago.v.s
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdToken {

    @JsonProperty("iss")
    private String iss;

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("aud")
    private String aud;

    @JsonProperty("exp")
    private int exp;

    @JsonProperty("iat")
    private int iat;

    @JsonProperty("auth_time")
    private int authTime;

    @JsonProperty("nonce")
    private String nonce;

    @JsonProperty("sid")
    private String sid;

    @JsonProperty("sub_alt")
    private List<String> subAlt;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getIat() {
        return iat;
    }

    public void setIat(int iat) {
        this.iat = iat;
    }

    public int getAuthTime() {
        return authTime;
    }

    public void setAuthTime(int authTime) {
        this.authTime = authTime;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public List<String> getSubAlt() {
        return subAlt;
    }

    public void setSubAlt(List<String> subAlt) {
        this.subAlt = subAlt;
    }
}
