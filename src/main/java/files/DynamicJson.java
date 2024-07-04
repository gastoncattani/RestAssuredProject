package files;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class DynamicJson {

    @Test(dataProvider = "Booksdata")
    public void addBook() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().header("Content-Type", "application/json").
                body(Payload.AddBook("asdf", "777")).
                when()
                .post("Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js = ReusableMethods.rawToJson(response);
        String id = js.getString("ID");
        System.out.println(id);
    }

    @DataProvider(name = "Booksdata")
    public Object[][] getData() {
        // Array collection of elements
        // Multidimensional array = collection of arrays
        return new Object[][]{{"qwer", "123"}, {"asdf", "456"}, {"zxcv", "789"}};
    }
}
