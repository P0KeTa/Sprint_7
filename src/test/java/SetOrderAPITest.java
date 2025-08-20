import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.CourierModel;
import models.OrderModel;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierSteps.*;
import static steps.OrderSteps.*;

public class SetOrderAPITest extends BaseAPITest {

    private final CourierModel courierModel = new CourierModel(LOGIN, PASSWORD, FIRST_NAME);
    private final OrderModel orderModel = new OrderModel(
            "David", "Taziashvili", "Engels",
            "Centre", "89091234567", 10,
            "01.01.2025", "...", new String[]{"GRAY"});

    @Test
    @DisplayName("Успешный запрос на принятие заказа")
    @Description("успешный запрос возвращает ok: true;")
    public void setValidOrderTest() {
        createCourier(courierModel);
        CourierModel.id = loginCourierWithValidData(LOGIN, PASSWORD).jsonPath().getInt("id");
        OrderModel.track = createOrder(orderModel).jsonPath().getInt("track");
        OrderModel.id = getOrderWithData(OrderModel.track).jsonPath().getInt("order.id");

        setOrderWithValidData(OrderModel.id, CourierModel.id)
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Запрос на принятие заказа с невалидным ID курьера")
    @Description("если передать неверный id курьера, запрос вернёт ошибку;")
    public void setInvalidIdOrderTest() {
        createCourier(courierModel);
        CourierModel.id = 123;
        OrderModel.track = createOrder(orderModel).jsonPath().getInt("track");
        OrderModel.id = getOrderWithData(OrderModel.track).jsonPath().getInt("order.id");

        setOrderWithValidData(OrderModel.id, CourierModel.id)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Запрос на принятие заказа без ID курьера")
    @Description("если не передать id курьера, запрос вернёт ошибку;")
    public void setWithoutIdOrderTest() {
        OrderModel.track = createOrder(orderModel).jsonPath().getInt("track");
        OrderModel.id = getOrderWithData(OrderModel.track).jsonPath().getInt("order.id");

        setOrderWithoutCourierId(OrderModel.id)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Запрос на принятие заказа без номера заказа")
    @Description("если не передать номер заказа, запрос вернёт ошибку;")
    public void setWithoutOrderNumberTest() {
        createCourier(courierModel);
        CourierModel.id = 123;
        OrderModel.track = createOrder(orderModel).jsonPath().getInt("track");
        OrderModel.id = getOrderWithData(OrderModel.track).jsonPath().getInt("order.id");

        setOrderWithoutOrderId(CourierModel.id)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Запрос на принятие заказа с неверным номером заказа")
    @Description("если передать неверный номер заказа, запрос вернёт ошибку.")
    public void setInvalidOrderNumberTest() {
        createCourier(courierModel);
        CourierModel.id = loginCourierWithValidData(LOGIN, PASSWORD).jsonPath().getInt("id");
        OrderModel.id = 123;

        setOrderWithValidData(OrderModel.id, CourierModel.id)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Заказа с таким id не существует"));
    }
}
