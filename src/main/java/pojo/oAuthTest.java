package pojo;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;

public class oAuthTest {

    @Test
    public void oAuthTesting() {

        String[] courseTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};

        // Authenticate to get access token
        String response = given()
                .formParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParams("grant_type", "client_credentials")
                .formParams("scope", "trust")
                .when().log().all().post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token").asString();

        System.out.println(response);

        JsonPath js = new JsonPath(response);

        String accessToken = js.getString("access_token");

        // Get course detail with access token
        var getCourse = given().queryParam("access_token", accessToken)
                .when().log().all().get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourse.class); // Using POJO class to deserialization

        System.out.println(getCourse.getLinkedIn());
        System.out.println(getCourse.getInstructor());
        System.out.println(getCourse.getCourses().getApi().get(1).getCourseTitle());

        List<Api> apiCourses = getCourse.getCourses().getApi();
        for(Api apiCourse : apiCourses){
            if(apiCourse.getCourseTitle().equals("SoapUI Webservices testing")) {
                System.out.println(apiCourse.getPrice());
            }
        }

        // Get the course names of WebAutomation
        ArrayList<String> a = new ArrayList<>();
        List<WebAutomation> webAutomationCourses = getCourse.getCourses().getWebAutomation();
        for(WebAutomation webAutomationCourse : webAutomationCourses){
            a.add(webAutomationCourse.getCourseTitle());
        }

        List<String> expectedList = Arrays.asList(courseTitles);
        Assert.assertTrue(a.equals(expectedList));
    }
}
