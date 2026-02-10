import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Random;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification; 

public class Day1 {
    public Long PetID=new Random().nextLong(100000);
    public String Global_Token;
    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;

   @BeforeSuite
   public void setup(){
        reqSpec = new RequestSpecBuilder()
                  .setBaseUri("https://dummyjson.com")
                  .addHeaders(Map.of("Accept", "application/json"))
                  .build();
        resSpec = new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .build();
   }

    @Test
    public void get_global_token(){
         Response r3 = given()  
                        .spec(reqSpec)
                        .header("Content-Type", "application/json")                      
                        .body("""
                            {
                                "username": "emilys",
                                "password": "emilyspass"
                            }
                        """)                        
                    .when()
                        .post("/auth/login")
                    .then()
                        .spec(resSpec)
                        .extract()
                        .response();

        this.Global_Token= r3.jsonPath().get("accessToken");        
    }

    @Test
    public void get_user_details(){
                      given()
                        .spec(reqSpec)
                        .header("Authorization", "Bearer"+ Global_Token)
                        .pathParam("id", 1)                        
                      .when()
                        .get("/carts/{id}")
                      .then()
                        .spec(resSpec)
                        .extract().response();
    }

    @Test
    public void Add_Product(){
        File image = Paths.get("src", "test", "resources", "download.jpg").toFile();
        Response r4= given()    
                        .spec(reqSpec)
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
                        .queryParams(Map.of("limit", 1, "skip", 0))
                        .spec(reqSpec)
                     .when()    
                        .get("/products")
                     .then()
                        .spec(resSpec)
                        .body("skip", equalTo(0))
                        .body("limit", equalTo(1))
                        .body("total", equalTo(194))
                        .body("products.size()", equalTo(1))
                        .header("server", "cloudflare");
    }
}