package ru.sberbank.id.sdk.response;

/**
 * DTO ответа от API
 *
 * @author shago.v.s
 */
public class ApiResponse {
    private final int code;
    private final String content;

    public ApiResponse(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", content='" + content + '\'' +
                '}';
    }
}
