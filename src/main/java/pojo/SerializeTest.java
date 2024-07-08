package pojo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class SerializeTest {

    @Test
    public void serializeTest() {

        AddPlace p = new AddPlace();
        p.setAccuracy(50);
        p.setAddress("J A Frias 7777");
        p.setLanguague("French-IN");
        p.setPhone_number("+5491111111111");
        p.setWebSite("www.google.com");
        p.setName("Gaston");
        List<String> myList = new ArrayList<>();
        myList.add("Shoe Park");
        myList.add("Shop");
        p.setTypes(myList);
        Location l = new Location();
        l.setLat(2.3);
        l.setLng(-3.2);
        p.setLocation(l);

        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
                addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();

        ResponseSpecification resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

        //RestAssured.baseURI = "https://rahulshettyacademy.com";

        //Response res = given().log().all().queryParam("key", "qaclick123")
        RequestSpecification request = given().spec(req).log().all()
                .body(p);

        Response response = request.when().post("/maps/api/place/add/json")
                //.then().assertThat().statusCode(200).extract().response();
                .then().spec(resspec).extract().response();

        String responseString = response.asString();
        System.out.println(responseString);
    }
}
