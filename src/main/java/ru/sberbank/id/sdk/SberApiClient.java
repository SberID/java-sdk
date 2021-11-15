package ru.sberbank.id.sdk;

import org.apache.http.ssl.SSLContexts;
import ru.sberbank.id.sdk.data.UserInfoData;
import ru.sberbank.id.sdk.exception.SberApiClientException;
import ru.sberbank.id.sdk.request.AuthRequest;
import ru.sberbank.id.sdk.request.UserInfoRequest;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Клиент для доступа к методам API
 *
 * @author shago.v.s
 */
public class SberApiClient {

    private String clienId;
    private String clientSecret;
    private String authEndpoint;
    private String apiEndpoint;
    private SSLContext sslContext;

    /**
     * Клиент для доступа к методам API
     *
     * @param clienId id клиента
     * @param clientSecret secret клиента
     * @param authEndpoint адрес запроса Access Token и Id Token
     * @param apiEndpoint адрес запроса пользовательсктх данных
     */
    public SberApiClient(String clienId, String clientSecret, String authEndpoint, String apiEndpoint) {
        this.clienId = clienId;
        this.clientSecret = clientSecret;
        this.authEndpoint = authEndpoint;
        this.apiEndpoint = apiEndpoint;
    }

    public String getClienId() {
        return clienId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAuthEndpoint() {
        return authEndpoint;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    /**
     * Установка ssl-контекста для доступа к api
     * @param sslContext
     */
    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    /**
     * Установка ssl-контекста для доступа к api
     *
     * @param keyStoreStream InputStream клиентского p12 сертификата
     * @param key ключ доступа для сертификата
     *
     * @throws SberApiClientException при ошибках чтения сертификата
     */
    public void setSslContext(InputStream keyStoreStream, String key) throws SberApiClientException {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(keyStoreStream, key.toCharArray());

            this.sslContext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, key.toCharArray())
                    .build();

        } catch (KeyStoreException e) {
            throw new SberApiClientException("Не найден провайдер KeyStoreSpi", e);
        } catch (IOException e) {
            throw new SberApiClientException("Ошибка чтения", e);
        } catch (NoSuchAlgorithmException e) {
            throw new SberApiClientException("Не найден алгоритм", e);
        } catch (CertificateException e) {
            throw new SberApiClientException("Не удалось загрузить сертификат", e);
        } catch (UnrecoverableKeyException | KeyManagementException e) {
            throw new SberApiClientException("Ошибка ключа", e);
        }
    }

    /**
     * Запрос на получение Access Token и Id Token
     *
     * @param redirectUri редирект на сайт мерчанта
     * @param code auth code
     * @param nonce параметр, используемый для получения auth code
     *
     * @return объект запроса
     */
    public AuthRequest authRequest(String redirectUri, String code, String nonce){
        return new AuthRequest(this, redirectUri, code, nonce);
    }

    /**
     * Запрос на получение Access Token и Id Token
     *
     * @param redirectUri редирект на сайт мерчанта
     * @param code auth code
     * @param nonce параметр, используемый для получения auth code
     * @param codeVerifier если используется PKCE
     *
     * @return объект запроса
     */
    public AuthRequest authRequest(String redirectUri, String code, String nonce, String codeVerifier){
        return new AuthRequest(this, redirectUri, code, nonce, codeVerifier);
    }

    /**
     * Запрос на получение пользовательских данных
     *
     * @param accessToken токен доступа
     * @return объект запроса
     */
    public UserInfoRequest<UserInfoData> userInfoRequest(String accessToken){
        return new UserInfoRequest<>(this, accessToken, UserInfoData.class);
    }

    /**
     * Запрос на получение пользовательских данных
     *
     * @param accessToken токен доступа
     * @param clazz класс объекта для десериализации ответа
     * @return объект запроса
     */
    public <T extends UserInfoData> UserInfoRequest<T> userInfoRequest(String accessToken, Class<T> clazz){
        return new UserInfoRequest<>(this, accessToken, clazz);
    }
}
