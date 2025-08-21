import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.CourierModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static steps.CourierSteps.*;
import static steps.OrderSteps.getOrderList;

public class GetOrderListAPITest extends BaseAPITest {

    private CourierModel courierModel;

    @Before
    public void createClass() {
        courierModel = new CourierModel(LOGIN, PASSWORD, FIRST_NAME);
    }

    @Test
    @DisplayName("Проверка списка заказов")
    @Description("Тело ответа возвращается список заказов.")
    public void getOrderListTest() {
        createCourier(courierModel);
        courierModel.setFirstName("");
        Response response = loginCourier(courierModel);
        CourierModel.id = response.jsonPath().getInt("id");
        getOrderList()
                .then()
                .statusCode(HTTP_OK)
                .and()
                .body("orders", notNullValue())
                .and()
                .body("orders.size()", greaterThan(0));
    }

    @After
    @DisplayName("Получение ID и удаление курьера после каждого теста")
    public void logOutAndDelete() {
        try {
            courierModel.setFirstName("");
            CourierModel.id = loginCourier(courierModel).jsonPath().getInt("id");
            deleteCourier(CourierModel.id);
        } catch (Exception e) {
            System.err.println("Ошибка при удалении курьера в @After: " + e.getMessage());
        }
    }
}
