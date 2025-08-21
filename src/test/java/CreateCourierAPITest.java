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

public class CreateCourierAPITest extends BaseAPITest {

    private CourierModel courierModel;

    @Before
    public void createClass() {
        courierModel = new CourierModel(LOGIN, PASSWORD, FIRST_NAME);
    }

    @Test
    @DisplayName("Создание курьера с валидными данными")
    @Description("курьера можно создать;" +
            "чтобы создать курьера, нужно передать в ручку все обязательные поля;" +
            "запрос возвращает правильный код ответа;" +
            "успешный запрос возвращает ok: true;")
    public void canCreteCourierTest() {
        createCourier(courierModel)
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
        createCourier(courierModel);
        createCourier(courierModel)
                .then()
                .statusCode(HTTP_CONFLICT)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера с одним полем")
    @Description("если одного из полей нет, запрос возвращает ошибку;")
    public void canCreteCourierWithOneLoginTest() {
        courierModel.setPassword("");
        courierModel.setPassword("");
        createCourier(courierModel)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с одним полем")
    @Description("если одного из полей нет, запрос возвращает ошибку;")
    public void canCreteCourierWithOneFieldTest() {
        courierModel.setLogin("");
        courierModel.setFirstName("");
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
                courierModel.setFirstName("");
                CourierModel.id = loginCourier(courierModel).jsonPath().getInt("id");
                deleteCourier(CourierModel.id);
        } catch (Exception e) {
            System.err.println("Ошибка при удалении курьера в @After: " + e.getMessage());
        }
    }
}
