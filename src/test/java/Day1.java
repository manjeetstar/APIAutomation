import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.nio.file.Paths;
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
                      given()
                        .headers(Map.of("Content-Type", "application/json", "Accept", "application/json"))
                        .header("Authorization", "Bearer"+ Global_Token)
                        .pathParam("id", 1)                        
                      .when()
                        .get("/carts/{id}")
                      .then()
                        .statusCode(200)
                        .extract().response();
    }

    @Test
    public void Add_Product(){
        File image = Paths.get("src", "test", "resources", "download.jpg").toFile();
        Response r4= given()    
                        .headers(Map.of("Accept", "application/json"))
                        .header("Authorization", "Bearer" + Global_Token)
                        .multiPart("title", "iPhone 15 Pro")
                        .multiPart("description", "Latest Apple phone")
                        .multiPart("price", "1299")
                        .multiPart("brand", "Apple")
                        .multiPart("category", "smartphones")
                        .multiPart("images", image)
                     .when()
                        .post("/products/add")
                     .then()
                        .log().ifValidationFails()
                        .statusCode(201)
                        .extract()
                        .response();
        System.out.println("Remaining rate-limit: " + r4.getHeaders().getValue("x-ratelimit-remaining"));
    }
    @Test
    public void pagination_part1(){
                     given()
                        .queryParams(Map.of("limit", 1000, "skip", 2000))
                     .when()    
                        .get("/products")
                     .then()
                        .statusCode(200)
                        .body("skip", equalTo(2000))
                        .body("limit", equalTo(0))
                        .body("total", equalTo(194))
                        .body("products.size()", equalTo(0))
                        .header("server", "cloudflare");

    }
}