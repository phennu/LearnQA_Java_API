package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequest;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    @Test
    public void testGetUserDataNotAuth(){
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertions.assertJsonHasField(responseUserData,"username");
        Assertions.assertJsonHasNotField(responseUserData,"firstName");
        Assertions.assertJsonHasNotField(responseUserData,"lastName");
        Assertions.assertJsonHasNotField(responseUserData,"email");
    }
    @Test
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token",header)
                .cookie("auth_sid",cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        String[] expectedFiels = {"username","firstName", "lastName", "email"};

        Assertions.assertJsonHasFields(responseUserData,expectedFiels);

    }
//
    @Test
    @Description("This test auth with one user and get info with another")
    @DisplayName("test get another user")
    public void testGetUserDetailsAuthAsDiffUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequest.makeGetRequestAuth("https://playground.learnqa.ru/api/user/login", authData);
        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        int userId = this.getIntFromJson(responseGetAuth,"user_id");

        Response responseUserData = apiCoreRequest.makeGetRequestWithDifferentUser("https://playground.learnqa.ru/api/user/", header, cookie, userId+1);
        Assertions.assertJsonHasField(responseUserData,"username");
        Assertions.assertJsonHasNotField(responseUserData,"firstName");
        Assertions.assertJsonHasNotField(responseUserData,"lastName");
        Assertions.assertJsonHasNotField(responseUserData,"email");
    }
}
