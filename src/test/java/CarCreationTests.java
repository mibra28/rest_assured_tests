import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.testng.Assert;

import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.testng.Assert.assertEquals;

@JsonAutoDetect
public class CarCreationTests {

    private static final String BASE_URL = "https://retoolapi.dev/KCuEUu/cars";
    private static final String BRAND = "Brand";
    private static final String PRICE = "price";
    private static final String SUBARU = "Subaru";
    private static final String ID = "id";

    @Test
    public void createCarResponseIsSuccessWithId() {
        Map<String, Object> map = new HashMap<>();
        map.put(BRAND, SUBARU);
        map.put(PRICE, 42);
        map.put(ID, 37);

        createCarScenario(201, map)
                .and()
                .body(BRAND, equalTo(SUBARU),
                        PRICE, equalTo(42),
                        ID, equalTo(37));

        delete(BASE_URL + "/37");
    }

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

    @Test
    public void createCarWIthObjectAsParameter() {
        Car car = new Car("Lexus", 123);
        Response post = given()
                .contentType(ContentType.JSON)
                .body(car)
                .when()
                .post(BASE_URL);
        post.then()
                .assertThat()
                .statusCode(201);
        Car createdCar = post.then()
                .extract()
                .body().as(Car.class);
        assertEquals(createdCar.getPrice(),123);
        assertEquals(createdCar.getBrand(),"Lexus");

        delete(BASE_URL + createdCar.getId());
    }

    @Test
    public void changeBodyToListOfCars() {
        List<Car> allCarsList = Arrays.stream(
                given().get(BASE_URL)
                .then()
                .extract()
                .body()
                .as(Car[].class)).toList();

        List<Car> subaruList = allCarsList.stream().filter(c -> c.getBrand().equals("Subaru")).toList();
        Assert.assertFalse(allCarsList.stream().anyMatch(c -> c.getBrand().equals("Toyota")));
        assertEquals(subaruList.size(), 4);

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
