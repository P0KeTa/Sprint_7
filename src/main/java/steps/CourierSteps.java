package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import models.CourierModel;
import io.restassured.response.Response;

import static data.TestData.*;
import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("Создание курьера с использованием класса")
    public static Response createCourier(CourierModel courierModel) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierModel)
                .post(PATH_CREATED)
                .then()
                .extract().response();
    }

    @Step("Создание курьера с использованием тела")
    public static Response createCourier(String password) {
        String body = String.format
                ("{\n" + "\"password\": \"%s\"\n}", password);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(PATH_CREATED)
                .then()
                .extract().response();
    }

    @Step("Авторизация курьера с валидными данными")
    public static Response loginCourierWithValidData(String login, String password) {
        String body = String.format
                ("{\n\"login\": \"%s\"" + ",\n" + "\"password\": \"%s\"\n}", login, password);
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(PATH_COURIER_LOGIN)
                .then()
                .extract().response();
    }

    @Step("Авторизация курьера с невалидными данными")
    public static Response loginCourierWithInvalidData(String password) {
        String body = String.format
                ("{\n" +"\"password\": \"%s\"\n}", password);
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(PATH_COURIER_LOGIN)
                .then()
                .extract().response();
    }

    @Step("Удаление курьера")
    public static Response deleteCourier(int id){
        return given()
                .delete(PATH_DELETE_COURIER + id)
                .then()
                .extract().response();
    }
}
