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

@Epic("Registration Cases")
@Feature("Registration")

public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    @Test
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }
    @Test
    @Description("This test create user with incorrect email")
    @DisplayName("test incorrect email")
    public void testIncorrectEmail(){
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotovexample.com");
        userData = DataGenerator.getRegistrationData(userData);

        Response responseRegisterIncorrectEmail = apiCoreRequest.makePostRequestIncorrectEmail("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseTextEquals(responseRegisterIncorrectEmail, "Invalid email format");
    }

    @Description("This test create user with empty field")
    @DisplayName("test empty field")
    @ParameterizedTest
    @ValueSource(strings = {"email", "password","username", "firstName", "lastName"})
    public void createUserEmptyField(String param){
        Map<String,String > userData = DataGenerator.getRegistrationData();
        userData.remove(param);

        Response responseRegisterEmptyField = apiCoreRequest.makePostRequestNoField("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseTextEquals(responseRegisterEmptyField, "The following required params are missed: " + param);

    }

    @Test
    @Description("This test create user with short firstName")
    @DisplayName("test short firstName")
    public void testShortFirstname(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.put("firstName", "");

        Response responseRegisterShortFirstname = apiCoreRequest.makePostRequestWithShortFirstname("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseTextEquals(responseRegisterShortFirstname, "The value of 'firstName' field is too short");
    }

    @Test
    @Description("This test create user with long firstName")
    @DisplayName("test long firstName")
    public void testLongFirstname(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.put("firstName", DataGenerator.generateRandomString(251));

        Response responseRegisterLongFirstname = apiCoreRequest.makePostRequestWithLongFirstname("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseTextEquals(responseRegisterLongFirstname, "The value of 'firstName' field is too long");
    }

    @Test
    public void createUserSuccessfully(){
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");

    }
}
