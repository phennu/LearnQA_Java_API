package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequest;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    @Test
    public void testEditJustCreatedTest(){
    //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        int userId = this.getIntFromJson(responseCreateAuth,"id");

        //LOGIN
        Map<String ,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
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
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
        System.out.println(responseEditUser.asString());

//        //GET
//        Response responseUserData = RestAssured
//                .given()
//                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
//                .cookie("auth_sid", this.getCookie(responseGetAuth,"auth_sid"))
//                .get("https://playground.learnqa.ru/api/user/" + userId)
//                .andReturn();
//
//        Assertions.assertJsonByName(responseUserData,"firstName", newName);
    }

    @Test
    @Description("This test create user and edit with not authorized user")
    @DisplayName("test edit not auth")
    public void testEditNotAuthUser(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        int userId = this.getIntFromJson(responseCreateAuth,"id");

        //EDIT
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequest.makePutRequestNotAuthorized("https://playground.learnqa.ru/api/user/", editData, userId);
        Assertions.assertJsonByName(responseEditUser, "error", "Auth token not supplied");

    }

    @Test
    @Description("This test create user, authorize user, and edit info with diff user")
    @DisplayName("test edit  diff auth")
    public void testEditDiffUser(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //LOGIN
        Map<String ,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequest.makeGetRequestAuth("https://playground.learnqa.ru/api/user/login", authData);
        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        int userId = this.getIntFromJson(responseGetAuth,"user_id");

        //EDIT
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequest.makePutRequestWithDifferentUser("https://playground.learnqa.ru/api/user/", editData, header, cookie, userId+1);
        Assertions.assertJsonByName(responseEditUser, "error", "This user can only edit their own data.");

    }
    @Test
    @Description("This test create user, authorize user, and edit email to incorrect format")
    @DisplayName("test edit email incorrect format")
    public void testEditIncorrectEmail(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //LOGIN
        Map<String ,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequest.makeGetRequestAuth("https://playground.learnqa.ru/api/user/login", authData);
        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        int userId = this.getIntFromJson(responseGetAuth,"user_id");

        //EDIT
        String newEmail = "mister.sosister";
        Map<String,String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequest.makePutRequestAuthUser("https://playground.learnqa.ru/api/user/", editData, header, cookie, userId);
        //System.out.println(responseEditUser.asString());
        Assertions.assertJsonByName(responseEditUser, "error", "Invalid email format");

    }
    @Test
    @Description("This test create user, authorize user, and edit info with short firstName")
    @DisplayName("test edit short firstName")
    public void testEditShortFirstname(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //LOGIN
        Map<String ,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequest.makeGetRequestAuth("https://playground.learnqa.ru/api/user/login", authData);
        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        int userId = this.getIntFromJson(responseGetAuth,"user_id");

        //EDIT
        String newName = DataGenerator.generateRandomString(0);
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequest.makePutRequestWithDifferentUser("https://playground.learnqa.ru/api/user/", editData, header, cookie, userId);
        //System.out.println(responseEditUser.asString());
        Assertions.assertJsonByName(responseEditUser, "error", "The value for field `firstName` is too short");

    }
}

