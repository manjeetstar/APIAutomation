package filter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class JacksonConfig {
    private static final ObjectMapper objMap= new ObjectMapper();

    static {
        objMap.registerModule(new JavaTimeModule());
        objMap.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        // objMap.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public static ObjectMapper getMapper(){
        return objMap;
    }    
}
