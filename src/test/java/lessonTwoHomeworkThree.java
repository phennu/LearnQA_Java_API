import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class lessonTwoHomeworkThree {
    @Test
    public void getHeaderLink(){
        String baseUrl = "https://playground.learnqa.ru/api/long_redirect";
        String redirectUrl = "";
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get(baseUrl)
                .andReturn();
        int responseCode = response.getStatusCode();
        int responseCount = 1;
        while (responseCode !=200){
            redirectUrl = response.getHeader("Location");
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(redirectUrl)
                    .andReturn();
            redirectUrl = response.getHeader("Location");
            responseCode = response.getStatusCode();
            responseCount++;
        }
        System.out.println(responseCount);



    }
}
