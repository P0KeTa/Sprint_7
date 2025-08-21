package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import models.CourierModel;
import io.restassured.response.Response;

import static data.TestData.*;
import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("Создание курьера")
    public static Response createCourier(CourierModel courierModel) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierModel)
                .post(PATH_CREATED)
                .then()
                .extract().response();
    }

    @Step("Авторизация курьера")
    public static Response loginCourier(CourierModel courierModel) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierModel)
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
