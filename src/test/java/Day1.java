import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.ObjectInputFilter.Config;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification; 

import Config.configClass;
import filter.RetryOnFailureFilter;
import filter.RetryOnFailureFilter;

@SuppressWarnings("unused")
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
                    .expectContentType(ContentType.JSON)
                    .build();
        configClass.init();
        RestAssured.filters(
            new RetryOnFailureFilter(2,200)
        );
   }

    @Test(enabled= false)
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
                        .statusCode(anyOf(is(200), is(403)))
                        .extract()
                        .response();

        this.Global_Token= r3.jsonPath().get("accessToken");        
    }

    @Test(enabled= false)
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

    @Test(enabled= true)
    public void Add_Product(){
        File image = Paths.get("src", "test", "resources", "download.jpg").toFile();
                    
        IntStream.range(0, 3).forEach(i->{
                    given()    
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
                        .spec(resSpec);
            System.out.println("Request #" + (i + 1));
        });
    }
    @Test(enabled= false)
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