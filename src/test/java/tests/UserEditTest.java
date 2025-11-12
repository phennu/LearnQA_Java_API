package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequest;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Edit User Data Cases")
@Feature("Edition")
@Owner("Ilyas Nurtazin")
@Tag("api")
@Tag("homework")
@Tag("learnqa")

public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    @Test
    @Tag("Positive")
    public void testEditJustCreatedTest(){
    //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);
        int userId = this.getIntFromJson(responseCreateAuth,"id");

        //LOGIN
        Map<String ,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api_dev/user/login")
                .andReturn();

        //EDIT
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth,"auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api_dev/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth,"auth_sid"))
                .get("https://playground.learnqa.ru/api_dev/user/" + userId)
                .andReturn();

        Assertions.assertJsonByName(responseUserData,"firstName", newName);
    }

    @Test
    @Tag("Negative")
    @Description("This test create user and edit with not authorized user")
    @DisplayName("test edit not auth")
    public void testEditNotAuthUser(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);
        int userId = this.getIntFromJson(responseCreateAuth,"id");

        //EDIT
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequest.makePutRequestNotAuthorized("https://playground.learnqa.ru/api_dev/user/", editData, userId);
        Assertions.assertJsonByName(responseEditUser, "error", "Auth token not supplied");

    }

    @Test
    @Tag("Negative")
    @Description("This test create user, authorize user, and edit info with diff user")
    @DisplayName("test edit  diff auth")
    public void testEditDiffUser(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);

        //LOGIN
        Map<String ,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequest.makeGetRequestAuth("https://playground.learnqa.ru/api_dev/user/login", authData);
        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        int userId = this.getIntFromJson(responseGetAuth,"user_id");

        //EDIT
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequest.makePutRequestWithDifferentUser("https://playground.learnqa.ru/api_dev/user/", editData, header, cookie, userId+1);
        Assertions.assertJsonByName(responseEditUser, "error", "This user can only edit their own data.");

    }
    @Test
    @Tag("Negative")
    @Description("This test create user, authorize user, and edit email to incorrect format")
    @DisplayName("test edit email incorrect format")
    public void testEditIncorrectEmail(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);

        //LOGIN
        Map<String ,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequest.makeGetRequestAuth("https://playground.learnqa.ru/api_dev/user/login", authData);
        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        int userId = this.getIntFromJson(responseGetAuth,"user_id");

        //EDIT
        String newEmail = "mister.sosister";
        Map<String,String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequest.makePutRequestAuthUser("https://playground.learnqa.ru/api_dev/user/", editData, header, cookie, userId);
        //System.out.println(responseEditUser.asString());
        Assertions.assertJsonByName(responseEditUser, "error", "Invalid email format");

    }
    @Test
    @Tag("Negative")
    @Description("This test create user, authorize user, and edit info with short firstName")
    @DisplayName("test edit short firstName")
    public void testEditShortFirstname(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);

        //LOGIN
        Map<String ,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequest.makeGetRequestAuth("https://playground.learnqa.ru/api_dev/user/login", authData);
        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        int userId = this.getIntFromJson(responseGetAuth,"user_id");

        //EDIT
        String newName = DataGenerator.generateRandomString(0);
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequest.makePutRequestWithDifferentUser("https://playground.learnqa.ru/api_dev/user/", editData, header, cookie, userId);
        //System.out.println(responseEditUser.asString());
        Assertions.assertJsonByName(responseEditUser, "error", "The value for field `firstName` is too short");

    }
}

