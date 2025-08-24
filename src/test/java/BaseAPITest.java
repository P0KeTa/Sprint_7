import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;

public class BaseAPITest {

    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

    @DisplayName("Установка стартовой ссылки")
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }


}
