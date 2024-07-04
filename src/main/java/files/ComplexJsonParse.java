package files;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ComplexJsonParse {
    public static void main(String[] args) {

    }

    @Test
    public void Test(){
        // Como no hay un servicio que retorne esto, estamos mockeando para simular el retorno,
        // cuando el servicio en el futuro este listo, se puede reemplazar el mock
        JsonPath js = new JsonPath(Payload.CoursePrice());

        // 1. Print No of courses returned by API
        System.out.println("1. Print No of courses returned by API");
        var coursesCount = js.getInt("courses.size()");
        System.out.println(coursesCount);

        // 2.Print Purchase Amount
        System.out.println("2.Print Purchase Amount");
        var totalPurchaseAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println(totalPurchaseAmount);

        // 3. Print Title of the first course
        System.out.println("3. Print Title of the first course");
        var courseTitle = js.getString("courses[0].title");
        System.out.println(courseTitle);

        // 4. Print All course titles and their respective Prices
        System.out.println("4. Print All course titles and their respective Prices");
        for (int i = 0; i < coursesCount; i++) {
            var courseTitles = js.getString("courses[" + i + "].title");
            var coursePrices = js.getInt("courses[" + i + "].price");

            System.out.println(courseTitles);
            System.out.println(coursePrices);
        }

        // 5. Print no of copies sold by RPA Course
        System.out.println("5. Print no of copies sold by RPA Course");
        for (int i = 0; i < coursesCount; i++) {
            String courseTitles = js.getString("courses[" + i + "].title");
            if (courseTitles.equalsIgnoreCase("RPA")) {
                // Copies sold
                int copies = js.getInt("courses[" + i + "].copies");
                System.out.println(copies);
                break;
            }
        }

        // 6. Verify if Sum of all Course prices matches with Purchase Amount
        System.out.println("6. Verify if Sum of all Course prices matches with Purchase Amount");
        int coursesPrice = 0;
        for (int i = 0; i < coursesCount; i++) {
            // Sum Copies sold
            coursesPrice = coursesPrice + (js.getInt("courses[" + i + "].price") * js.getInt("courses[" + i + "].copies"));
        }

        System.out.println("Courses price: " + coursesPrice);
        Assert.assertEquals(coursesPrice, totalPurchaseAmount);
    }
}
