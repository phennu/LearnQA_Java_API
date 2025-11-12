package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequest;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
        @Test
        @Description("this test deletes user with ID 2")
        @DisplayName("test delete user with id 2")
        public void testDeleteUserTwo(){
            //AUTH
            Map<String, String> authData = new HashMap<>();
            authData.put("email", "vinkotov@example.com");
            authData.put("password", "1234");

            Response responseGetAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
            //GET AUTH INFO
            String cookie = this.getCookie(responseGetAuth,"auth_sid");
            String header = this.getHeader(responseGetAuth,"x-csrf-token");
            int userId = this.getIntFromJson(responseGetAuth, "user_id");
            //DELETE
            Response responseDeleteUser = apiCoreRequest.makeDeleteRequestAuthUser("https://playground.learnqa.ru/api/user/", header, cookie, userId);
            //System.out.println(responseDeleteUser.asString());
            Assertions.assertJsonByName(responseDeleteUser, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("this test creates user and deletes it")
    @DisplayName("test create and delete user")
    public void testCreateAndDeleteUser(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //LOGIN
        Map<String ,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        //GET AUTH INFO
        Response responseGetAuth = apiCoreRequest.makeGetRequestAuth("https://playground.learnqa.ru/api/user/login", authData);
        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        int userId = this.getIntFromJson(responseGetAuth,"user_id");
        //DELETE
        Response responseDeleteUser = apiCoreRequest.makeDeleteRequestAuthUser("https://playground.learnqa.ru/api/user/", header, cookie, userId);
        //GET USER
        Response responseGetUserById = apiCoreRequest.makeGetRequestWithUserId("https://playground.learnqa.ru/api/user/", userId);
        Assertions.assertResponseTextEquals(responseGetUserById, "User not found");

    }
    @Test
    @Description("this test creates user and tries to delete different user")
    @DisplayName("test create and delete diff user")
    public void testCreateAndDeleteDiffUser(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //LOGIN
        Map<String ,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        //GET AUTH INFO
        Response responseGetAuth = apiCoreRequest.makeGetRequestAuth("https://playground.learnqa.ru/api/user/login", authData);
        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        int userId = this.getIntFromJson(responseGetAuth,"user_id");
        //DELETE
        Response responseDeleteUser = apiCoreRequest.makeDeleteRequestWithDifferentUser("https://playground.learnqa.ru/api/user/", header, cookie, userId+1);
        Assertions.assertJsonByName(responseDeleteUser, "error", "This user can only delete their own account.");

    }
}
