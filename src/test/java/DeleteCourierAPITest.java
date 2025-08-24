import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.CourierModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierSteps.*;

public class DeleteCourierAPITest extends BaseAPITest{

    CourierModel courierModel;

    @Before
    public void createCourierModel() {
        courierModel = new CourierModel(LOGIN, PASSWORD, FIRST_NAME);
        createCourier(courierModel);
        courierModel.setFirstName("");
        CourierModel.id = loginCourier(courierModel).jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Успешное удаление курьера при вводе валидного значения")
    @Description("успешный запрос возвращает ok: true;")
    public void deleteCourierWithValidDataTest() {
        deleteCourier(CourierModel.id)
                .then()
                .statusCode(HTTP_OK)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Запрос без ID")
    @Description("неуспешный запрос возвращает соответствующую ошибку;" +
            "если отправить запрос без id, вернётся ошибка;")
    public void deleteCourierWithoutDataTest() {
        CourierModel.id = 0;
        deleteCourier(CourierModel.id)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Запрос с несуществующим ID")
    @Description("неуспешный запрос возвращает соответствующую ошибку;" +
            "если отправить запрос с несуществующим id, вернётся ошибка.")
    public void deleteCourierWithInvalidDataTest() {
        CourierModel.id = 1234;
        deleteCourier(CourierModel.id)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Курьера с таким id нет."));
    }

    @After
    @DisplayName("Получение ID и удаление курьера после каждого теста")
    public void logOutAndDelete() {
        try {
            CourierModel.id = loginCourier(courierModel).jsonPath().getInt("id");
            deleteCourier(CourierModel.id);
        } catch (Exception e) {
            System.err.println("Ошибка при удалении курьера в @After: " + e.getMessage());
        }
    }
}
