import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.CourierModel;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierSteps.*;

public class DeleteCourierAPITest extends BaseAPITest{

    private final CourierModel courierModel = new CourierModel(LOGIN, PASSWORD, FIRST_NAME);

    @Test
    @DisplayName("Успешное удаление курьера при вводе валидного значения")
    @Description("успешный запрос возвращает ok: true;")
    public void deleteCourierWithValidDataTest() {
        createCourier(courierModel);
        CourierModel.id = loginCourierWithValidData(LOGIN, PASSWORD).jsonPath().getInt("id");
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
}
