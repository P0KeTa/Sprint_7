package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.OrderModel;

import static data.TestData.*;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Создание заказа")
    public static Response createOrder(OrderModel orderModel) {
        return given()
                .contentType(ContentType.JSON)
                .body(orderModel)
                .post(PATH_CREATE_ORDER)
                .then()
                .extract().response();
    }

    @Step("Получение списка заказов")
    public static Response getOrderList() {
        return given()
                .get(PATH_GET_ORDER_LIST)
                .then()
                .extract().response();
    }

    @Step("Принять заказ с валидными данными")
    public static Response setOrderWithValidData(int orderId, int courierId) {
        return given()
                .queryParam("courierId", courierId)
                .put(PATH_SET_ORDER + orderId)
                .then()
                .extract().response();
    }

    @Step("Принять заказ без id курьера")
    public static Response setOrderWithoutCourierId(int orderId) {
        return given()
                .log().all()
                .queryParam("courierId", "")
                .put(PATH_SET_ORDER + orderId)
                .then()
                .extract().response();
    }

    @Step("Принять заказ без id заказа")
    public static Response setOrderWithoutOrderId(int courierId) {
        return given()
                .queryParam("courierId", courierId)
                .put(PATH_SET_ORDER)
                .then()
                .extract().response();
    }

    @Step("Получить заказ по его номеру")
    public static Response getOrderWithData(int t) {
        return given()
                .queryParam("t", t)
                .get(PATH_GET_ORDER)
                .then()
                .extract().response();
    }

    @Step("Получить заказ без номера")
    public static Response getOrderWithoutData() {
        return given()
                .get(PATH_GET_ORDER)
                .then()
                .extract().response();
    }

}
