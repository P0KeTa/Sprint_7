import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.OrderModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.Matchers.equalTo;
import static steps.OrderSteps.createOrder;

@RunWith(Parameterized.class)
public class CreateOrderAPITest extends BaseAPITest {

    private final String[] color;

    public CreateOrderAPITest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Object[][] data() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}},
        };
    }

    @Test
    @DisplayName("Создание заказа с разными комбинациями цвета")
    @Description("можно указать один из цветов — BLACK или GREY;" +
            "тело ответа содержит track." +
            "можно указать оба цвета;" +
            "можно совсем не указывать цвет;")
    public void createOrderTest() {
        OrderModel orderModel = new OrderModel(
                "David", "Taziashvili", "Engels",
                "Centre", "89091234567", 10,
                "01.01.2025", "...", color);

        Response response = createOrder(orderModel)
                .then()
                .statusCode(HTTP_CREATED)
                .and()
                .extract().response();

        int track = response.jsonPath().getInt("track");

        response.then().assertThat().body("track", equalTo(track));
    }


}
