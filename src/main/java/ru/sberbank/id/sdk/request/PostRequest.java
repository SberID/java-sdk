package ru.sberbank.id.sdk.request;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import ru.sberbank.id.sdk.SberApiClient;
import ru.sberbank.id.sdk.exception.ApiException;

import java.io.UnsupportedEncodingException;

/**
 * Специфика построения Post-запроса
 *
 * @author shago.v.s
 */
public abstract class PostRequest<T> extends AbstractRequest<T> {

    HttpPost method;

    public PostRequest(SberApiClient client) {
        super(client);
        method = new HttpPost();
    }

    @Override
    protected HttpRequestBase getMethod() {
        return method;
    }

    @Override
    protected void preExecute() throws ApiException {
        try {
            method.setEntity(new UrlEncodedFormEntity(getParams()));
        } catch (UnsupportedEncodingException e) {
            throw new ApiException("", e);
        }
    }
}
