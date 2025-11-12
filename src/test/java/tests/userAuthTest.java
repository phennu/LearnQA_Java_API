package tests;

import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import lib.Assertions;
import lib.ApiCoreRequest;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;


@Epic("Authorisation Cases")
@Feature("Authorization")
@Owner("Ilyas Nurtazin")
@Tag("api")
@Tag("homework")
@Tag("learnqa")

public class userAuthTest extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();

    @BeforeEach
    public void loginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequest.makePostRequest("https://playground.learnqa.ru/api_dev/user/login", authData);


        this.cookie = this.getCookie(responseGetAuth,"auth_sid");
        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Tag("Positive")
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    public void testAuthUser(){

        Response responseCheckAuth = apiCoreRequest.makeGetRequest("https://playground.learnqa.ru/api_dev/user/auth", this.header, this.cookie);

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }
    @Tag("Negative")
    @Description("This test checks authorization status without sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "header"})
    public void testNegativeAuthUser(String condition){
        if(condition.equals("cookie")){
            Response responseForCheck = apiCoreRequest.makeGetRequestWithCookie("https://playground.learnqa.ru/api_dev/user/auth", this.cookie);
            Assertions.assertJsonByName(responseForCheck,"user_id", 0);
        } else if(condition.equals("header")){
            Response responseForCheck = apiCoreRequest.makeGetRequestWithToken("https://playground.learnqa.ru/api_dev/user/auth",this.header);
            Assertions.assertJsonByName(responseForCheck,"user_id", 0);
        } else{
            throw new IllegalArgumentException("Condition value is not known: " + condition);
        }

    }
}
