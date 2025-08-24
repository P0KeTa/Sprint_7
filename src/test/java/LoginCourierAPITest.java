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

public class LoginCourierAPITest extends BaseAPITest {

    Response response;
    CourierModel courierModel;

    @Before
    public void createCourierModel() {
        courierModel = new CourierModel(LOGIN, PASSWORD, FIRST_NAME);
        createCourier(courierModel);
        courierModel.setFirstName("");
        response = loginCourier(courierModel);
        CourierModel.id = response.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Тест на Логин курьера валидными данными")
    @Description("курьер может авторизоваться;" +
            "для авторизации нужно передать все обязательные поля;" +
            "успешный запрос возвращает id.")
    public void loginCourierTest() {
        response
                .then()
                .statusCode(HTTP_OK)
                .and()
                .extract().response();

        response.then().assertThat().body("id", equalTo(CourierModel.id));
    }

    @Test
    @DisplayName("Тест на Логин курьера с невалидным Логином и Паролем")
    @Description("если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;")
    public void loginWithWrongLoginAndPasswordCourierTest() {
        courierModel.setLogin("Oleg");
        courierModel.setPassword("4321");
        courierModel.setFirstName("");
        loginCourier(courierModel)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Тест на Логин курьера с невалидным Логином")
    @Description("система вернёт ошибку, если неправильно указать логин или пароль;")
    public void loginWithWrongLoginCourierTest() {
        courierModel.setLogin("Oleg");
        courierModel.setFirstName("");
        loginCourier(courierModel)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Тест на Логин курьера с невалидным Паролем")
    @Description("система вернёт ошибку, если неправильно указать логин или пароль;")
    public void loginWithWrongPasswordCourierTest() {
        courierModel.setPassword("4321");
        courierModel.setFirstName("");
        loginCourier(courierModel)
                .then()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Тест на Логин курьера с пустым полем Пароль")
    @Description("если какого-то поля нет, запрос возвращает ошибку;")
    public void loginWithOneFieldPasswordCourierTest() {
        courierModel.setPassword("");
        courierModel.setFirstName("");
        loginCourier(courierModel)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Тест на Логин курьера с пустым полем Логин")
    @Description("если какого-то поля нет, запрос возвращает ошибку;")
    public void loginWithOneFieldLoginCourierTest() {
        courierModel.setLogin("");
        courierModel.setFirstName("");
        loginCourier(courierModel)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
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
