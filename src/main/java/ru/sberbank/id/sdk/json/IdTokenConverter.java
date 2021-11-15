package ru.sberbank.id.sdk.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdConverter;
import ru.sberbank.id.sdk.Utils;
import ru.sberbank.id.sdk.data.IdToken;

/**
 * Конвертер из jwt в json
 *
 * @author shago.v.s
 */
public class IdTokenConverter extends StdConverter<String, IdToken> {

    @Override
    public IdToken convert(String s) {
        String json = Utils.getPayloadFromJwt(s);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, IdToken.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
