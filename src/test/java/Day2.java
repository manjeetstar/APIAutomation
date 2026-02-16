import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import DataClass.ReceipeResponseDetails;
import io.restassured.response.Response;

@SuppressWarnings("unused")
public class Day2 extends Day1 {

	@Test
	public void addReceipe() {
		Response res = given()
					.spec(reqSpec)
				.when()
					.get("/recipes")
				.then()
					.statusCode(200)
					.extract()
					.response();

		System.out.println("Meal Type is " + res.jsonPath().getList("recipes.findAll{it.difficulty == 'Easy'}").size());
	}
}