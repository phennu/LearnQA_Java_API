package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSomeCookie {
    @Test
    public void getSomeCookie(){

        Response responseGetCookie = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Map<String, String > cookies = responseGetCookie.getCookies();
        String cookieName = responseGetCookie.getCookies().keySet().iterator().next();
        assertTrue(cookies.containsKey(cookieName),"Response doesnt't have cookie with that name: " + cookieName);

    }
}
