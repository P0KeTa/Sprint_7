import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.CourierModel;
import org.junit.After;
import org.junit.Before;

import static data.TestData.*;
import static steps.CourierSteps.deleteCourier;
import static steps.CourierSteps.loginCourierWithValidData;

public class BaseAPITest {

    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

    @DisplayName("Установка стартовой ссылки")
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @DisplayName("Получение ID и удаление курьера после каждого теста")
    @After
    public void logOutAndDelete() {
        try {
            if (CourierModel.id != 0) {
                CourierModel.id = loginCourierWithValidData(LOGIN, PASSWORD).jsonPath().getInt("id");
                deleteCourier(CourierModel.id);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при удалении курьера в @After: " + e.getMessage());
        }
    }

}
