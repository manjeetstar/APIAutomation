import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import java.util.Map;
import io.restassured.response.Response; 

public class Day1 {
    public Long PetID;
    @BeforeClass
    public void setup() {
        baseURI = "https://petstore.swagger.io";
        basePath="/v2";        
    }
    @Test
    public void Test1() {
        Response r1= given()
           .headers(Map.of("Content-Type","application/JSON", "Accept", "application/json"))
           .auth().none()
           .relaxedHTTPSValidation()      
           .body("""
                {
                "id": 0,
                "category": {
                    "id": 0,
                    "name": "string"
                },
                "name": "Apple Desktop",
                "photoUrls": [
                    "string"
                ],
                "tags": [
                    {
                    "id": 0,
                    "name": "string"
                    }
                ],
                "status": "available"
                }
            """)                              
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            .extract()
            .response(); 
            
    PetID= r1.jsonPath().getLong("id");   
    }

    @Test(enabled = true)
    public void Test2() {
        given()
           .headers(Map.of("Content-Type","application/JSON", "Accept", "application/json"))          
           .pathParam("petId", PetID)                              
        .when()
            .get("/pet/{petId}")
        .then()            
            .statusCode(200)
            .extract()
            .response();             
    }
}
