package lukasztracz195.barnpjee.chat.common.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lukasztracz195.barnpjee.chat.common.dto.response.GetMessagesResponse;

public class JsonObjectConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T convertJsonToPOJO(String json, Class<T> object) {
        try {
            return objectMapper.readValue(json, object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> String convertPOJOToJson(T pojo) {
        try {
            return objectMapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
