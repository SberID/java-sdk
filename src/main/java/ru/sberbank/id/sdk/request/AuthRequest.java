package ru.sberbank.id.sdk.request;

import ru.sberbank.id.sdk.SberApiClient;
import ru.sberbank.id.sdk.Utils;
import ru.sberbank.id.sdk.data.AuthData;
import ru.sberbank.id.sdk.exception.ApiException;
import ru.sberbank.id.sdk.exception.IncorrectAudException;
import ru.sberbank.id.sdk.exception.IncorrectNonceException;

/**
 * Запрос на получение AccessToken и IdToken
 *
 * @author shago.v.s
 */
public class AuthRequest extends PostRequest<AuthData> {

    private final String redirectUri;
    private final String code;
    private final String nonce;
    private final String codeVerifier;

    public AuthRequest(SberApiClient sberApiClient, String redirectUri, String code, String nonce) {
        this(sberApiClient, redirectUri, code, nonce, null);
    }

    public AuthRequest(SberApiClient sberApiClient, String redirectUri, String code, String nonce, String codeVerifier) {
        super(sberApiClient);

        this.redirectUri = redirectUri;
        this.code = code;
        this.nonce = nonce;
        this.codeVerifier = codeVerifier;
    }

    @Override
    protected Class<AuthData> getResponseType() {
        return AuthData.class;
    }

    @Override
    protected String getEndpoint() {
        return getClient().getAuthEndpoint();
    }

    @Override
    public void build() {
            setHeader("accept", "application/json");
            setHeader("content-type", "application/x-www-form-urlencoded");
            setHeader("rquid", Utils.randomStringUUID());
            setHeader("x-ibm-client-id", getClient().getClienId());

        setParam("grant_type", "authorization_code");
        setParam("code", code);
        setParam("client_id", getClient().getClienId());
        setParam("client_secret", getClient().getClientSecret());
        setParam("redirect_uri", redirectUri);
        if(codeVerifier != null)
            setParam("code_verifier", codeVerifier);

    }

    @Override
    protected void postCheck(AuthData response) throws ApiException {
        if (!response.getIdToken().getNonce().equals(nonce))
            throw new IncorrectNonceException(nonce, response.getIdToken().getNonce());
        if (!response.getIdToken().getAud().equalsIgnoreCase(getClient().getClienId()))
            throw new IncorrectAudException(getClient().getClienId(), response.getIdToken().getAud());
    }
}
