import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
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

    @Before
    public void createCourierModel() {
        CourierModel courierModel = new CourierModel(LOGIN, PASSWORD, FIRST_NAME);
        createCourier(courierModel);
        courierModel.setFirstName("");
        CourierModel.id = loginCourier(courierModel).jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Проверка списка заказов")
    @Description("Тело ответа возвращается список заказов.")
    public void getOrderListTest() {
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
            deleteCourier(CourierModel.id);
        } catch (Exception e) {
            System.err.println("Ошибка при удалении курьера в @After: " + e.getMessage());
        }
    }
}
