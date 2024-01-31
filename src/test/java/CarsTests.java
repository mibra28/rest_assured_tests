import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@JsonAutoDetect
public class CarsTests {

    private static final String BASE_URL = "https://retoolapi.dev/KCuEUu/cars";

    @Test
    public void createCarResponseIsSuccess() {
        createCarScenario(201, "");
    }

    @Test
    public void createCarWitDuplicatedIdFails() {
        createCarScenario(500, "1")
                .and()
                .body(containsString("Insert failed, duplicate id"));
    }

    private Map<String, String> createCarParams(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("Brand", "Subaru");
        map.put("price", "42");
        map.put("id", id);
        return map;
    }

    private ValidatableResponse createCarScenario(int statusCode, String carId) {
        return given()
                .contentType(ContentType.JSON)
                .body(createCarParams(carId))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat().statusCode(statusCode);
    }

}
