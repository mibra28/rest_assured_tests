import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@JsonAutoDetect
public class CarCreationTests {

    private static final String BASE_URL = "https://retoolapi.dev/KCuEUu/cars";
    private static final String BRAND = "Brand";
    private static final String PRICE = "price";
    private static final String SUBARU = "Subaru";
    private static final String ID = "id";

    @Test
    public void createCarResponseIsSuccessWithoutId() {
        Map<String, Object> map = new HashMap<>();
        map.put(BRAND, SUBARU);
        map.put(PRICE, 42);

        createCarScenario(201, map)
                .and()
                .body(BRAND, equalTo(SUBARU),
                        PRICE, equalTo(42),
                        ID, notNullValue());
    }

    @Test
    public void createCarResponseIsSuccessWithStringPrice() {
        Map<String, Object> map = new HashMap<>();
        map.put(BRAND, SUBARU);
        map.put(PRICE, "42");

        createCarScenario(201, map)
                .and()
                .body(BRAND, equalTo(SUBARU),
                        PRICE, equalTo("42"),
                        ID, notNullValue());
    }

    @Test
    public void createCarResponseIsSuccessWithOnlyPrice() {
        Map<String, Object> map = new HashMap<>();
        map.put(PRICE, 42);

        createCarScenario(201, map)
                .and()
                .body(BRAND, nullValue(),
                        PRICE, equalTo(42),
                        ID, notNullValue());
    }

    @Test
    public void createCarResponseIsSuccessWithOnlyBrand() {
        Map<String, Object> map = new HashMap<>();
        map.put(BRAND, SUBARU);

        createCarScenario(201, map)
                .and()
                .body(BRAND, equalTo(SUBARU),
                        PRICE, nullValue(),
                        ID, notNullValue());
    }

    @Test
    public void createCarWithDuplicatedIdFails() {
        Map<String, Object> map = new HashMap<>();
        map.put(BRAND, SUBARU);
        map.put(PRICE, 42);
        map.put(ID, 1);
        String expectedResponse = "Insert failed, duplicate id";

        createCarScenario(500, map)
                .and()
                .body(containsString(expectedResponse));

    }

    private ValidatableResponse createCarScenario(int statusCode, Map<String, Object> params) {
        return given()
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(statusCode);
    }

}
