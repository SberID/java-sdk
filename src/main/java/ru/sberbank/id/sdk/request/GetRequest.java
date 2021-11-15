package ru.sberbank.id.sdk.request;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import ru.sberbank.id.sdk.SberApiClient;
import ru.sberbank.id.sdk.exception.ApiException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Специфика построения Get-запроса
 *
 * @author shago.v.s
 */
public abstract class GetRequest<T> extends AbstractRequest<T> {
    HttpGet method;

    public GetRequest(SberApiClient client) {
        super(client);
        method = new HttpGet();
    }

    @Override
    public HttpGet getMethod() {
        return method;
    }

    @Override
    protected void preExecute() throws ApiException {
        URI uri = null;
        try {
            uri = new URIBuilder(method.getURI()).addParameters(getParams()).build();
            method.setURI(uri);
        } catch (URISyntaxException e) {
            throw new ApiException("Неверно задан URI ресурса", e);
        }
    }
}
