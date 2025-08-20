import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.OrderModel;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.*;
import static steps.OrderSteps.*;

public class GetOrderAPITest extends BaseAPITest {

    private final OrderModel orderModel = new OrderModel(
            "David", "Taziashvili", "Engels",
            "Centre", "89091234567", 10,
            "01.01.2025", "...", new String[]{"GRAY"});

    @Test
    @DisplayName("Получить заказ по валидному номеру")
    @Description("успешный запрос возвращает объект с заказом;")
    public void getOrderValidDataTest(){
        OrderModel.track = createOrder(orderModel).jsonPath().getInt("track");
        getOrderWithData(OrderModel.track)
                .then()
                .statusCode(HTTP_OK)
                .and()
                .body("order", notNullValue())
                .and()
                .body("order.id", greaterThan(0));
    }

    @Test
    @DisplayName("Получить заказ без номера заказа")
    @Description("запрос без номера заказа возвращает ошибку;")
    public void getOrderWithoutDataTest(){
        getOrderWithoutData()
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Получить заказ по невалидному номеру")
    @Description("запрос с несуществующим заказом возвращает ошибку.")
    public void getOrderInvalidDataTest(){
        getOrderWithData(0)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .body("message", equalTo("Заказ не найден"));
    }
}
