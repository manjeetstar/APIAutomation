import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import java.util.Map;
import java.util.Random;
import io.restassured.response.Response; 

public class Day1 {
    public Long PetID=new Random().nextLong(100000);
    @BeforeClass
    public void setup() {
        baseURI = "https://petstore.swagger.io";
        basePath="/v2";        
    }
    @Test
    public void Create_Pet() {
        Response r1= given()
           .headers(Map.of("Content-Type","application/JSON", "Accept", "application/json"))
           .auth().none()
           .relaxedHTTPSValidation()      
           .body("""
                {
                "id": %d,
                "category": {
                    "id": 112,
                    "name": "Sample"
                },
                "name": "Apple Desktop",
                "photoUrls": [
                    "Sample URL"
                ],
                "tags": [
                    {
                    "id": 113,
                    "name": "Sample Website"
                    }
                ],
                "status": "available"
                }
            """.formatted(PetID))                              
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            .extract()
            .response(); 
            
    this.PetID= r1.jsonPath().getLong("id");   
    System.out.println("Pet ID is " + PetID);
    }

    @Test(enabled = true)
    public void Display_Pet() {
        given()
           .headers(Map.of("Content-Type","application/JSON", "Accept", "application/json"))          
           .pathParam("petId", this.PetID)                              
        .when()
            .get("/pet/{petId}")
        .then()            
            .statusCode(200)
            .extract()
            .response();             
    }

     @Test(enabled = true)
    public void Update_Pet() throws InterruptedException {
       Response r2=given()
           .headers(Map.of("Content-Type","application/JSON", "Accept", "application/json"))         
           .body("""
                {
                "id": %d,
                "category": {
                    "id": 112,
                    "name": "ABC"
                },
                "name": "%s",
                "photoUrls": [
                    "Put queries"
                ],
                "tags": [
                    {
                    "id": 113,
                    "name": "Put queries"
                    }
                ],
                "status": "available"
                }
            """.formatted(this.PetID, "Apple Desktop - Updated"))                              
        .when()
            .put("/pet")
        .then()            
            .statusCode(200)
            .extract()
            .response();   
            
      System.out.println(r2.jsonPath().getLong("id"));
      r2.getHeaders().forEach(header ->{
        System.out.println(header.getName() + " - " + header.getValue());
      });
    }    
}
