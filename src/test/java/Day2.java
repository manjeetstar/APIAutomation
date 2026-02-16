import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import DataClass.ReceipeResponseDetails;
import io.restassured.response.Response;

@SuppressWarnings("unused")
public class Day2 extends Day1 {

	@Test
	public void addReceipe() {
		ReceipeResponseDetails res = given()
					.spec(reqSpec)
				.when()
					.get("/recipes/1")
				.then()
					.statusCode(200)
					.extract()
					.as(ReceipeResponseDetails.class);

		System.out.println("Rating Type is " + res.getRating() );
	}
}