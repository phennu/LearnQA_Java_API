package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMultipleUserAgents {
    static final String userAgent1 = "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    static final String userAgent2 = "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1";
    static final String userAgent3 = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    static final String userAgent4 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0";
    static final String userAgent5 = "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";
    @ParameterizedTest
    @ValueSource(strings = {userAgent1, userAgent2, userAgent3, userAgent4, userAgent5})
    public void fewUserAgents(String userAgent){

        Response response = RestAssured
                .given()
                .header("User-Agent",userAgent)
                        .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                                .andReturn();
        String platformJson = response.jsonPath().getJsonObject("platform");
        String browserJson = response.jsonPath().getJsonObject("browser");
        String deviceJson = response.jsonPath().getJsonObject("device");
switch (userAgent){
    case userAgent1:
        assertEquals("Mobile",platformJson,"Platform is not correct");
        assertEquals("No",browserJson,"Platform is not correct");
        assertEquals("Android",deviceJson,"Platform is not correct");
        break;
    case userAgent2:
        assertEquals("Mobile",platformJson,"Platform is not correct");
        assertEquals("Chrome",browserJson,"Platform is not correct");
        assertEquals("iOS",deviceJson,"Platform is not correct");
    case userAgent3:
        assertEquals("Googlebot",platformJson,"Platform is not correct");
        assertEquals("Unknown",browserJson,"Platform is not correct");
        assertEquals("Unknown",deviceJson,"Platform is not correct");
    case userAgent4:
        assertEquals("Web",platformJson,"Platform is not correct");
        assertEquals("Chrome",browserJson,"Platform is not correct");
        assertEquals("No",deviceJson,"Platform is not correct");
    case userAgent5:
        assertEquals("Mobile",platformJson,"Platform is not correct");
        assertEquals("No",browserJson,"Platform is not correct");
        assertEquals("iPhone",deviceJson,"Platform is not correct");
}


    }
}
