package ru.sberbank.id.sdk.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import ru.sberbank.id.sdk.SberApiClient;
import ru.sberbank.id.sdk.Utils;
import ru.sberbank.id.sdk.data.ErrorData;
import ru.sberbank.id.sdk.exception.ApiException;
import ru.sberbank.id.sdk.exception.ApiResponseException;
import ru.sberbank.id.sdk.response.ApiResponse;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Логика выполнения абстрактного запроса
 *
 * @author shago.v.s
 */
public abstract class AbstractRequest<T> {
    private SberApiClient client;
    private CloseableHttpClient httpClient;

    private List<NameValuePair> params = new ArrayList<>();


    public AbstractRequest(SberApiClient client) {
        this.client = client;
        if (client.getSslContext() == null)
            httpClient = HttpClients.createDefault();
        else
            httpClient = HttpClients.custom()
                    .setUserAgent("JavaSDKClient/2.0.3")
                    .setSSLContext(client.getSslContext()).build();
    }

    public SberApiClient getClient() {
        return client;
    }

    public List<NameValuePair> getParams() {
        return params;
    }

    protected void setHeader(String name, String value) {
        getMethod().setHeader(name, value);
    }

    protected void setParam(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    private ApiResponse sendRequest(HttpRequestBase method) throws IOException {

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(method);

            int code = httpResponse.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

            return new ApiResponse(code, content);
        } finally {
            httpClient.close();
        }
    }

    protected abstract HttpRequestBase getMethod();
    protected abstract Class<T> getResponseType();
    protected abstract String getEndpoint();

    protected abstract void preExecute() throws ApiException;
    protected void postCheck(T response) throws ApiException {}
    protected abstract void build();

    /**
     * Выполнить запрос
     *
     * @return объект ответа
     * @throws ApiException в случае возвращения ошибки от api
     * @throws IOException
     */
    public T execute() throws ApiException, IOException {
        build();
        getMethod().setURI(URI.create(getEndpoint()));

        preExecute();

        ApiResponse apiResponse = sendRequest(getMethod());

        ObjectMapper mapper = new ObjectMapper();
        int code = apiResponse.getCode();
        if (code == HttpStatus.SC_OK) {
            if (Utils.isJWTString(apiResponse.getContent())) {
                apiResponse = new ApiResponse(apiResponse.getCode(), Utils.getPayloadFromJwt(apiResponse.getContent()));
            }

            T response = mapper.readValue(apiResponse.getContent(), getResponseType());
            postCheck(response);

            return response;
        } else if (code == HttpStatus.SC_BAD_REQUEST
                || code == HttpStatus.SC_UNAUTHORIZED
                || code == HttpStatus.SC_METHOD_NOT_ALLOWED
                || code == HttpStatus.SC_INTERNAL_SERVER_ERROR
                || code == 429) {

            ErrorData errorData = mapper.readValue(apiResponse.getContent(), ErrorData.class);
            throw new ApiResponseException(String.format("%s: %s (%s)", errorData.getHttpMessage(), errorData.getMoreInformation(), apiResponse.getContent()), errorData);
        } else {
            throw new ApiException(apiResponse.toString());
        }
    }

}
