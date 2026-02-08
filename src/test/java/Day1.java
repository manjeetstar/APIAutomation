import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import io.restassured.response.Response;

public class Day1 {
    @BeforeClass
    public void setup() {
        baseURI = "https://petstore.swagger.io";
        basePath="/v2";        
    }
    @Test
    public void Test1() {
        Response r1= given()
           .pathParam("petId", 203192)
        .when()
            .get("/pet/{petId}")
        .then()
            .statusCode(200)
            .extract()
            .response(); 
            
        System.out.println(r1.prettyPrint());
    }

    @Test
    public void Test2() {
        System.out.println("This is the second sample test");
    }
}
