import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import DataClass.ReceipeResponseDetails;

public class Day2 extends Day1{
   
   @Test
   public void addReceipe(){
    ReceipeResponseDetails res=given()
								.spec(reqSpec)
								.pathParam("receipeID",1)
							.when()
								.get("recipes/{receipeID}")
							.then()
								.extract()
								.as(ReceipeResponseDetails.class);

	for(String a: res.getIngredients()){
		System.out.println(a);
	}
   }
}