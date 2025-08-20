import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.CourierModel;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierSteps.*;

public class LoginCourierAPITest extends BaseAPITest {

    CourierModel courierModel = new CourierModel(LOGIN, PASSWORD, FIRST_NAME);

    @Test
    @DisplayName("Тест на Логин курьера валидными данными")
    @Description("курьер может авторизоваться;" +
            "для авторизации нужно передать все обязательные поля;" +
            "успешный запрос возвращает id.")
    public void LoginCourierTest() {
        createCourier(courierModel);

        Response response = loginCourierWithValidData(LOGIN, PASSWORD)
                .then()
                .statusCode(HTTP_OK)
                .and()
                .extract().response();

        CourierModel.id = response.jsonPath().getInt("id");

        response.then().assertThat().body("id", equalTo(CourierModel.id));
    }

    @Test
    @DisplayName("Тест на Логин курьера с невалидными данными")
    @Description("система вернёт ошибку, если неправильно указать логин или пароль;" +
            "если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;")
    public void LoginWithWrongDataCourierTest() {
        loginCourierWithValidData("Oleg", PASSWORD)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Тест на Логин курьера с невалидными данными")
    @Description("если какого-то поля нет, запрос возвращает ошибку;")
    public void LoginWithOneFieldCourierTest() {
        loginCourierWithInvalidData(PASSWORD)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
}
