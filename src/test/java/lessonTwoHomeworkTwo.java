import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class lessonTwoHomeworkTwo {
    @Test
    public void getHeaderLink(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        String responseRedirect = response.getHeader("Location");
        System.out.println(responseRedirect);



    }
}
