package files;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;

public class BugTest {

    @Test
    public void CreateBug(){
        RestAssured.baseURI = "https://gastoncattani.atlassian.net/";

        // Create bug first
        String createIssueResponse = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic Z2FzdG9uY2F0dGFuaUBnbWFpbC5jb206QVRBVFQzeEZmR0YwbUs1eDlMTHJXWE9RaUFoV1V0NjktRE40ZXBPVWFZTnF6bm9qZGZycFdEYjJwdm15MW53ZmdoVlFWbHIwLXZYN0stMmdqa21ac0xtZUU4dUZxM2czYm9MSDhNX25HSW5UYmI3V19CX2s3azJuaEdmY2RXaFRSUnVaOVhra0Nza3cwS25IVGJaY1hCbmhqZ2dzT29aN19Ha2ZsN1VjUWFQTTdLb0hkVmYzeTVNPUU1NjM0NzBB")
                .body("{\n" +
                        "    \"fields\": {\n" +
                        "        \"project\": {\n" +
                        "            \"key\": \"API\"\n" +
                        "        },\n" +
                        "        \"summary\": \"Website are not working - Automation Rest Assured\",\n" +
                        "        \"description\": \"Creating of an issue using project keys and issue type names using the REST API\",\n" +
                        "        \"issuetype\": {\n" +
                        "            \"name\": \"Bug\"\n" +
                        "        }\n" +
                        "    }\n" +
                        "}")
                .log().all()
                .post("rest/api/2/issue").then().log().all().assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath js = new JsonPath(createIssueResponse);
        var issueId = js.getString("id");

        System.out.println(issueId);

        // Add attachment
        given()
                .pathParams("key", issueId)
                .header("X-Atlassian-Token", "no-check")
                .header("Authorization", "Basic Z2FzdG9uY2F0dGFuaUBnbWFpbC5jb206QVRBVFQzeEZmR0YwbUs1eDlMTHJXWE9RaUFoV1V0NjktRE40ZXBPVWFZTnF6bm9qZGZycFdEYjJwdm15MW53ZmdoVlFWbHIwLXZYN0stMmdqa21ac0xtZUU4dUZxM2czYm9MSDhNX25HSW5UYmI3V19CX2s3azJuaEdmY2RXaFRSUnVaOVhra0Nza3cwS25IVGJaY1hCbmhqZ2dzT29aN19Ha2ZsN1VjUWFQTTdLb0hkVmYzeTVNPUU1NjM0NzBB")
                .multiPart("file", new File("C:\\Users\\gasto\\Pictures\\Screenshots\\Captura de pantalla_20230305_204445.png")).log().all()
                .post("rest/api/3/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
    }
}
