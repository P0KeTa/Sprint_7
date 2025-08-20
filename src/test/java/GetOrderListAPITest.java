import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.CourierModel;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static steps.CourierSteps.createCourier;
import static steps.CourierSteps.loginCourierWithValidData;
import static steps.OrderSteps.getOrderList;

public class GetOrderListAPITest extends BaseAPITest {

    private final CourierModel courierModel = new CourierModel(LOGIN, PASSWORD, FIRST_NAME);

    @Test
    @DisplayName("Проверка списка заказов")
    @Description("Тело ответа возвращается список заказов.")
    public void getOrderListTest() {
        createCourier(courierModel);
        Response response = loginCourierWithValidData(LOGIN,PASSWORD);
        CourierModel.id = response.jsonPath().getInt("id");
        getOrderList()
                .then()
                .statusCode(HTTP_OK)
                .and()
                .body("orders", notNullValue())
                .and()
                .body("orders.size()", greaterThan(0));
    }
}
