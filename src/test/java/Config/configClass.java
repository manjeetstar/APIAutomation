package Config;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;

public final class configClass {

    private configClass(){        
    }

    public static void init(){
        RestAssured.config= RestAssuredConfig.config().logConfig(
            LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()
        );     
    }
    
}
