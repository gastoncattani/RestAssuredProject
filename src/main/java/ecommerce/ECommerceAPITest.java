package ecommerce;

import ecommerce.pojo.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class ECommerceAPITest {

    private static final Logger log = LoggerFactory.getLogger(ECommerceAPITest.class);

    @Test
    public void E2ETest() throws InterruptedException {
        // Login
        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail("gastoncattani@gmail.com");
        loginRequest.setUserPassword("Homero$09");

        //relaxedHTTPSValidation() -> Bypass SSL certification validation
        RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);
        LoginResponse loginResponse = reqLogin.post("/api/ecom/auth/login").then().log().all().extract().response()
                .as(LoginResponse.class);

        System.out.println(loginResponse.getToken());
        String token = loginResponse.getToken();
        System.out.println(loginResponse.getUserId());
        String userId = loginResponse.getUserId();;

        // Add Product
        RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",token).build();

        RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseReq)
                .param("productName", "Laptop")
                .param("productAddedBy", userId)
                .param("productCategory", "fashion")
                .param("productSubCategory", "shirts")
                .param("productPrice", "999")
                .param("productDescription", "Lenovo 995T")
                .param("productFor", "women")
                .multiPart("productImage",
                        new File("C:\\Users\\gasto\\IdeaProjects\\RestAssuredProject\\src\\main\\resources\\" +
                                "Lenovo_ThinkPad_X1_Ultrabook.jpg"));

        String addProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product").then().log().all().extract().response().asString();
        JsonPath js = new JsonPath(addProductResponse);
        String productId = js.getString("productId");
        System.out.println(productId);

        Thread.sleep(2000);

        // Create Order
        RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",token).setContentType(ContentType.JSON)
                .build();

        OrdersRequest ordersRequest = new OrdersRequest();

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCountry("Argentina");
        orderDetail.setProductOrderedId(productId);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(orderDetail);

        ordersRequest.setOrders(orderDetailList);

        RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseReq).body(ordersRequest);

        /*String responseCreateOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all()
                .extract().response().asString();
        System.out.println(responseCreateOrder);*/
        var responseCreateOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all()
                .extract().response().as(OrdersResponse.class);
        System.out.println(responseCreateOrder);

        String orderId = responseCreateOrder.getOrders().get(0);

        // Delete Order
        RequestSpecification deleteProductBase = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",token).setContentType(ContentType.JSON)
                .build();

        RequestSpecification deleteProductReq = given().log().all().spec(deleteProductBase).pathParams("productId", productId);

        String deleteProductResponse = deleteProductReq.when().delete("/api/ecom/product/delete-product/{productId}")
                .then().log().all().extract().response().asString();

        js = new JsonPath(deleteProductResponse);
        String message = js.getString("message");

        Assert.assertEquals("Product Deleted Successfully", message);

        // Delete Order
        RequestSpecification deleteOrderBase = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",token).setContentType(ContentType.JSON)
                .build();

        RequestSpecification deleteOrderReq = given().log().all().spec(deleteOrderBase).pathParams("orderId", orderId);

        String deleteOrderResponse = deleteOrderReq.when().delete("/api/ecom/order/delete-order/{orderId}")
                .then().log().all().extract().response().asString();

        js = new JsonPath(deleteOrderResponse);
        System.out.println(js.getString("message"));
    }
}
