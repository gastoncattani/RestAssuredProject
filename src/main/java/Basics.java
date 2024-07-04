import files.ReusableMethods;
import files.Payload;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Basics {

    public static void main(String[] args) {
        // Given - All input details
        // When - Submit the API - Resource,http method
        // Then - Validate the response

        // Validate if Add Place PAI is working as expected
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(Payload.AddPlace())
                .when().post("maps/api/place/add/json")
                .then()/*.log().all()*/.assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();

        // Add place -> Update Place with New Address -> Get Place to validate if New Address is present in response
        System.out.println(response);

        // Parse Json response
        /*JsonPath js = new JsonPath(response); // For parsing Json
        String placeID = js.getString("place_id");*/
        String placeID = ReusableMethods.rawToJson(response).getString("place_id");

        System.out.println(placeID);

        // Update Place
        String newAddress = "70 winter walk, USA";

        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"place_id\": \"" + placeID + "\",\n" +
                        "    \"address\": \"" + newAddress + "\",\n" +
                        "    \"key\": \"qaclick123\"\n" +
                        "}").
                when().put("maps/api/place/update/json").
                then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));

        // Get Place
        given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeID).
                when().get("maps/api/place/get/json").
                then().assertThat().log().all().statusCode(200).body("address", equalTo(newAddress));
    }
}
