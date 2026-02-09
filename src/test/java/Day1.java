import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.Map;
import java.util.Random;
import io.restassured.response.Response; 

public class Day1 {
    public Long PetID=new Random().nextLong(100000);
    public String Global_Token;

    @BeforeClass
    public void setup() {
        baseURI = "https://dummyjson.com";             
    }
    @Test
    public void get_global_token(){
         Response r3 = given()  
                        .headers(Map.of("Content-Type", "application/json", "Accept", "application/json"))                      
                        .body("""
                            {
                                "username": "emilys",
                                "password": "emilyspass"
                            }
                        """)                        
                    .when()
                        .post("/auth/login")
                    .then()
                        .statusCode(200)
                        .extract()
                        .response();

        this.Global_Token= r3.jsonPath().get("accessToken");        
    }

    @Test
    public void get_user_details(){
        Response r4 = given()
                        .headers(Map.of("Content-Type", "application/json", "Accept", "application/json"))
                        .header("Authorization", "Bearer"+ Global_Token)
                        .pathParam("id", 1)                        
                      .when()
                        .get("/carts/{id}")
                      .then()
                        .statusCode(200)
                        .extract().response();
        r4.prettyPrint();
    }

    @Test
    public void Add_Product(){
        Response r4= given()    
                        .headers(Map.of("Accept", "application/json"))
                        .header("Authorizaton", "Bearer" + Global_Token)
                        .multiPart("title", "iPhone 15 Pro")
                        .multiPart("description", "Latest Apple phone")
                        .multiPart("price", "1299")
                        .multiPart("brand", "Apple")
                        .multiPart("category", "smartphones")
                        .multiPart("images", new File("src/test/resources/download.jpg"))
                     .when()
                        .post("/products/add")
                     .then()
                        .statusCode(201)
                        .extract()
                        .response();
        r4.prettyPrint();
    }
}