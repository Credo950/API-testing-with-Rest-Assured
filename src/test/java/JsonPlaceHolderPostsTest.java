import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class JsonPlaceHolderPostsTest {
    private static RequestSpecification reqSpec;
    private static ResponseSpecification respSpec;
    private static final String URI = "https://jsonplaceholder.typicode.com/posts/";

    @BeforeAll
    public static void initSpec(){
        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(URI)
                .build();
        respSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 5, 10 })
    public void filteringByUserId(int userId){
        List<Integer> listUserId = given()
                .spec(reqSpec)
                .param("userId", userId)
                .get()
                .then()
                .spec(respSpec)
                .body("every {it.containsKey('id')}", is(true),
                        "every {it.containsKey('userId')}", is(true),
                        "every {it.containsKey('title')}", is(true),
                        "every {it.containsKey('body')}", is(true))
                .extract().jsonPath().getList("userId");

        int sizeActual = listUserId.size();
        int[] expectedList= new int[sizeActual];
        for(int i = 0; i < sizeActual; i++)
                expectedList[i] = userId;

        assertThat(listUserId.toArray(), equalTo(expectedList));
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 0, Integer.MAX_VALUE})
    public void filteringByUserIdWrong(int userId){
        String emptyJson = given()
                .spec(reqSpec)
                .param("userId", userId)
                .get()
                .then()
                .spec(respSpec)
                .extract().asString();
        assertThat(emptyJson, equalTo("[]"));
    }


    @ParameterizedTest
    @ValueSource(ints = { 1, 50, 100 })
    public void filteringById(int id){
        given()
                .spec(reqSpec)
                .param("id", id)
                .get()
                .then()
                .spec(respSpec)
                .body("every {it.containsKey('id')}", is(true),
                        "every {it.containsKey('userId')}", is(true),
                        "every {it.containsKey('title')}", is(true),
                        "every {it.containsKey('body')}", is(true))
                .body("id[0]", equalTo(id));
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 0, Integer.MAX_VALUE})
    public void filteringByIdWrong(int id) {
        String emptyJson = given()
                .spec(reqSpec)
                .param("id", id)
                .get()
                .then()
                .spec(respSpec)
                .extract().asString();
        assertThat(emptyJson, equalTo("[]"));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 50, 100 })
    public void filteringByTitle(int id){
        String title = given()
                .spec(reqSpec)
                .get("{Id}", id)
                .then()
                .spec(respSpec)
                .extract().jsonPath().get("title");
        given()
                .spec(reqSpec)
                .param("title", title)
                .get()
                .then()
                .spec(respSpec)
                .body("title[0]", equalTo(title));
    }

    @Test
    public void filteringByTitleWrong(){
        String emptyJson = given()
                .spec(reqSpec)
                .param("title", "--Wrong--")
                .get()
                .then()
                .spec(respSpec)
                .extract().asString();
        assertThat(emptyJson, equalTo("[]"));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 50, 100 })
    public void getResourceById(int id){
        given()
                .spec(reqSpec)
                .get("{Id}", id)
                .then()
                .spec(respSpec)
                .body("id", equalTo(id));
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 0, Integer.MAX_VALUE})
    public void getResourceByIdWrong(int id){
        given()
                .spec(reqSpec)
                .get("{Id}", id)
                .then()
                .statusCode(404)
                .contentType(ContentType.JSON);
    }

    @Test
    public void getAllResources(){
        given()
                .spec(reqSpec)
                .get()
                .then()
                .spec(respSpec)
                .body("every {it.containsKey('id')}", is(true),
                        "every {it.containsKey('userId')}", is(true),
                        "every {it.containsKey('title')}", is(true),
                        "every {it.containsKey('body')}", is(true));
    }

}
