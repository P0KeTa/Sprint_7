import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.CourierModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierSteps.*;
import static steps.CourierSteps.createCourier;

public class CreateCourierAPITest extends BaseAPITest {

    Response response;
    CourierModel courierModel;

    @Before
    public void createCourierModel() {
        courierModel = new CourierModel(LOGIN, PASSWORD, FIRST_NAME);
        response = createCourier(courierModel);
        courierModel.setFirstName("");
        CourierModel.id = loginCourier(courierModel).jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Создание курьера с валидными данными")
    @Description("курьера можно создать;" +
            "чтобы создать курьера, нужно передать в ручку все обязательные поля;" +
            "запрос возвращает правильный код ответа;" +
            "успешный запрос возвращает ok: true;")
    public void canCreteCourierTest() {
        response
                .then()
                .statusCode(HTTP_CREATED)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера с невалидными данными")
    @Description("нельзя создать двух одинаковых курьеров;" +
            "если создать пользователя с логином, который уже есть, возвращается ошибка.")
    public void canSameCreteCourierTest() {
        createCourier(courierModel)
                .then()
                .statusCode(HTTP_CONFLICT)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера с одним полем Логин")
    @Description("если одного из полей нет, запрос возвращает ошибку;")
    public void canCreteCourierWithOneLoginTest() {
        courierModel.setPassword("");
        courierModel.setFirstName("");
        createCourier(courierModel)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с одним полем Пароль")
    @Description("если одного из полей нет, запрос возвращает ошибку;")
    public void canCreteCourierWithOnePasswordTest() {
        courierModel.setFirstName("");
        courierModel.setLogin("");
        createCourier(courierModel)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
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
