
## Java SDK

### Общие сведения

Java-библиотека для серверных приложений на Java, упрощающая получение Access Token и UserInfo.

### Подключение

Установить в локальный maven-репозиторий артефакты:

```java
mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=sdk-2.0.2.jar -Djavadoc=sdk-2.0.2-javadoc.jar
```

Добавить в зависимости проекта SDK, прописав в pom.xml:

```java
<dependency>
    <groupId>ru.sberbank.id</groupId>
    <artifactId>sdk</artifactId>
    <version>2.0.2</version>
</dependency>
```

### Использование

Создать объект **SberApiClient**, указав свои значения **client_id, client_secret** (подробнее в [1.1.1.1. Параметры запроса](https://confluence.sberbank.ru/pages/viewpage.action?pageId=5967418110), шлюзы вызова API. Адреса для запроса access token перечислены в [1.2.2. Шлюзы вызова API ](https://confluence.sberbank.ru/pages/viewpage.action?pageId=6163174647), для получения пользовательских данных - [1.3.2. Шлюзы вызова API](https://confluence.sberbank.ru/pages/viewpage.action?pageId=6163176050).

```java
SberApiClient client = new SberApiClient(
    "6a0e103b-1873-4ed5-9903-b4fb51192b23", //client_id, нужно поменять на свой
    "oYBjtOQmQNX4cMAmnKxAnvoGrWeccGKzKxvCKqtm0jQ", //client_secret, нужно поменять на свой
    "https://api.sberbank.ru/ru/prod/tokens/v2/oidc", //url на получение access token (для подключений через двусторонний TLS)
	//https://sec.api.sberbank.ru/ru/prod/tokens/v2/oidc - для подключений через ФПСУ
    "https://api.sberbank.ru/ru/prod/sberbankid/v2.1/userinfo" //url на получение пользовательских данных (для подключений через двусторонний TLS)
	//https://sec.api.sberbank.ru/ru/prod/sberbankid/v2.1/userinfo - для подключений через ФПСУ
);
```

Если используется 2-х сторонний TLS, необходимо установить SSL-контекст, указав путь к выпущенному p12-сертификату:

```java
try (InputStream keyStoreStream = new FileInputStream("./cert.p12")) {
    client.setSslContext(keyStoreStream, "put_your_key_here");
}
```

Выполнить запрос на получение Access Token, Id Token, указав значения:

1. **Адреса** для возврата Auth Code;
2. **Auth Code**, полученного через фронтенд;
3. **Nonce** (который использовался при получении Auth Code);
4. **Code Verifier** (обязателен, если используется [PKCE](https://confluence.sberbank.ru/pages/viewpage.action?pageId=5967418110)).

```java
AuthData authData = client.authRequest(
	    "http://127.0.0.1:8080/login", //redirect_uri
	    "3C506040-15A9-226B-EFB4-6389A0C0C165", //auth_code
	    "q5P5afVZ1kdehAfbn5XvnCkIfe9kDV1nSRicU8v6efU", //nonce
	    "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk") //code_verifier (опционально)
	.execute();
```

Объект **AuthData** содержит поля (в соответствии с [параметрами ответа](https://confluence.sberbank.ru/pages/viewpage.action?pageId=6163174809)):
- **accessToken** - Сгенерированный Access token.
- **tokenType** - Тип запрашиваемого токена. Всегда передается значение «Bearer».
- **expiresIn** - Время в секундах, в течение которого действует Access Token.
- **scope** - Список групп персональных данных, на получение которых выдан данный токен. В список так же по умолчанию включается название сервиса API
- **idToken** - Набор атрибутов, необходимый для идентификации пользователя.

Используя полученный Access Token, выполнить запрос на получение пользовательских данных:

```java
UserInfoData userInfoData = client.userInfoRequest(authData.getAccessToken()).execute();
String sub = userInfoData.getSub(); //получить необходимый Claim
```

В классе **UserInfoData** описаны обязательные поля. При необходимости (для получения дополнительных полей) класс нужно расширить (см. [п.1.3.3. "Параметры ответа"](https://confluence.sberbank.ru/pages/viewpage.action?pageId=6163176151) документации), например:

```java
public class CustomUserInfoData extends UserInfoData {
@JsonProperty
private String family_name;

@JsonProperty
private String given_name;

@JsonProperty
private String birthdate;

@JsonProperty
private String email;

@JsonProperty
private Inn inn;

// Getters
...
}

class Inn {
@JsonProperty
private String number;

// Getters
...
}
```

В этом случае необходимо передать класс при формировании запроса:

```java
UserInfoData userInfoData = client.userInfoRequest(authData.getAccessToken(), CustomUserInfoData.class).execute();
String familyName = userInfoData.getFamilyName(); //получить фамилию
Inn inn = userInfoData.getInn(); //получить ИНН
```

Для генерации **nonce** при запросе Auth Code можно использовать метод формирования 32-байтной случайной строки:

```java
String nonce = Utils.randomString();
```

Исключительные ситуации:
- **ApiResponseException, ApiException** - в случае получения ошибки от API (с детализацией ошибки см. [п.1.2.4](https://confluence.sberbank.ru/pages/viewpage.action?pageId=6163175049), [1.3.4 "Описание ошибок"](https://confluence.sberbank.ru/pages/viewpage.action?pageId=6163177200) документации)
- **IncorrectNonceException** - в случае несовпадения Nonce с тем, который использовался для получение Auth Code.
- **SberApiClientException** - в случае ошибок чтения сертификата
- **IncorrectAudException** - в случае несовпадения полученного aud и client_id

### Логирование

Если требуется вывести в лог детальные http-запросы/ответы к Sberbank ID, то указать в VM параметрах:

```java
-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog
-Dorg.apache.commons.logging.simplelog.showdatetime=true
-Dorg.apache.commons.logging.simplelog.log.org.apache.http=DEBUG
-Dorg.apache.commons.logging.simplelog.log.org.apache.http.wire=ERROR
```

Если используется Spring Boot, то достаточно в файл application.properties добавить:

```java
logging.level.org.apache.http = DEBUG
```


