package ru.sberbank.id.sdk.request;

import ru.sberbank.id.sdk.SberApiClient;
import ru.sberbank.id.sdk.Utils;
import ru.sberbank.id.sdk.data.UserInfoData;
import ru.sberbank.id.sdk.exception.ApiException;
import ru.sberbank.id.sdk.exception.IncorrectAudException;

/**
 * Запрос на получение пользовательских данных
 *
 * @author shago.v.s
 */
public class UserInfoRequest<T extends UserInfoData> extends GetRequest<T>{

    private final String accessToken;
    private final Class<T> clazz;

    public UserInfoRequest(SberApiClient client, String accessToken, Class<T> clazz) {
        super(client);
        this.accessToken = accessToken;
        this.clazz = clazz;
    }

    @Override
    protected Class<T> getResponseType() {
        return clazz;
    }

    @Override
    protected String getEndpoint() {
        return getClient().getApiEndpoint();
    }

    @Override
    public void build() {
        setHeader("accept", "application/json");
        setHeader("authorization", "Bearer " + accessToken);
        setHeader("x-introspect-rquid", Utils.randomStringUUID());
        setHeader("x-ibm-client-id", getClient().getClienId());
    }

    @Override
    protected void postCheck(UserInfoData response) throws ApiException {
        if (!response.getAud().equalsIgnoreCase(getClient().getClienId()))
            throw new IncorrectAudException(getClient().getClienId(), response.getAud());
    }
}
