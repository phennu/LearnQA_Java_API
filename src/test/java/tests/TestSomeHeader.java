package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSomeHeader {
    @Test
    public void getSomeHeader(){

        Response responseGetHeader = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        System.out.println("Headers: " + responseGetHeader.getHeaders());
        String headerName = "x-secret-homework-header";
        assertTrue(responseGetHeader.getHeaders().hasHeaderWithName(headerName), "Header with name " + headerName + " is not presented in response");

        String headerValue = responseGetHeader.getHeader(headerName);
        System.out.println("Header value is: " + headerValue);
        assertEquals("Some secret value", headerValue, "Header value is not equal to expected");
    }
}
