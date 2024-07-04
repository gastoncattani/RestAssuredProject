package files;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class DynamicJson {

    @Test(dataProvider = "Booksdata")
    public void addBook(String isbn, String aisle) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().header("Content-Type", "application/json").
                body(Payload.AddBook(isbn, aisle)).
                when()
                .post("Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js = ReusableMethods.rawToJson(response);
        String id = js.getString("ID");
        System.out.println(id);

        // Delete book to reset
    }

    @DataProvider(name = "Booksdata")
    public Object[][] getData() {
        // Array collection of elements
        // Multidimensional array = collection of arrays
        return new Object[][]{{"qwer", "123"}, {"asdf", "456"}, {"zxcv", "789"}};
    }

    @Test
    public void useJsonFromFile() throws IOException {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        // Content of the file to String -> content of file into Byte -> Byte data to String

        var file = new String(Files.readAllBytes(Paths.get("C:\\Users\\gasto\\IdeaProjects\\RestAssuredProject\\src\\main\\resources\\addPlace.json")));

        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(file)
                .when().post("maps/api/place/add/json")
                .then()/*.log().all()*/.assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();

        // Add place -> Update Place with New Address -> Get Place to validate if New Address is present in response
        System.out.println(response);
    }
}
