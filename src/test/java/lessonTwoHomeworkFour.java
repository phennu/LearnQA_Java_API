import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.path.json.JsonPath;

import java.util.Objects;


public class lessonTwoHomeworkFour {
    @Test
    public void token() throws InterruptedException {
        String baseUrl = "https://playground.learnqa.ru/ajax/api/longtime_job";
        String jobToken = "";
        int timeSleep = 0;
        JsonPath responseBasic = RestAssured
                .get(baseUrl)
                .jsonPath();
        jobToken = responseBasic.get("token");
        timeSleep = responseBasic.get("seconds");

        JsonPath responseBeforeTime = RestAssured
                .given()
                .param("token",jobToken)
                .get(baseUrl)
                .jsonPath();
        String jobIsNotReady = responseBeforeTime.get("status");
        if (Objects.equals(jobIsNotReady, "Job is NOT ready")){
            timeSleep*=1000;
            Thread.sleep(timeSleep);
            JsonPath responseAfterTime = RestAssured
                    .given()
                    .param("token",jobToken)
                    .get(baseUrl)
                    .jsonPath();
            String jobIsReady = responseAfterTime.get("status");
            if(Objects.equals(jobIsReady, "Job is ready")){
                String result = responseAfterTime.get("result");
                System.out.println("Result is "+result+". Job is done!");
            }
            else {System.out.println("Error");}
        }
        else {
            System.out.println("Error");
        }
    }
}
