import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.model.NewUserCredentials;
import com.intexsoft.stellarburgersapi.service.EndpointService;
import com.intexsoft.stellarburgersapi.service.PropertiesFile;
import com.intexsoft.stellarburgersapi.service.PropertiesService;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;

public class AuthUserTests extends BaseTest {

    @Before
    public void setUp() {
        newUser = NewUser.buildFakeUser();
    }

    @Test
    public void changeUserDataWithAuth() {
        accessToken = super.registerUser(newUser);
        String schemaLocation = PropertiesService.getProperty(PropertiesFile.TESTDATA, "user.positive-response-schema");
        NewUserCredentials userCredentials = NewUserCredentials.buildFakeCredentials();

        given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body("{\"name\":" + "\"" + userCredentials.getName() + "\"}").
                when()
                .patch(EndpointService.AUTH_USER).
                then()
                .assertThat()
                .statusCode(200).
                and()
                .body(matchesJsonSchemaInClasspath(schemaLocation));

        given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body("{\"email\":" + "\"" + userCredentials.getEmail() + "\"}").
                when()
                .patch(EndpointService.AUTH_USER).
                then()
                .assertThat()
                .statusCode(200).
                and()
                .body(matchesJsonSchemaInClasspath(schemaLocation));
    }

    @Test
    public void changeUserDataWithoutAuth() {
        accessToken = super.registerUser(newUser);
        String responseLocation = PropertiesService.getProperty(PropertiesFile.TESTDATA, "user.not-authorized-response");
        String response = super.readJsonFromPath(responseLocation);
        NewUserCredentials userCredentials = NewUserCredentials.buildFakeCredentials();

        given()
                .contentType(ContentType.JSON)
                .body("{\"name\":" + "\"" + userCredentials.getName() + "\"}").
                when()
                .patch(EndpointService.AUTH_USER).
                then()
                .assertThat()
                .statusCode(401).
                and()
                .body(equalTo(response));

        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":" + "\"" + userCredentials.getEmail() + "\"}").
                when()
                .patch(EndpointService.AUTH_USER).
                then()
                .assertThat()
                .statusCode(401).
                and()
                .body(equalTo(response));
    }

    @After
    public void tearDown() {
        super.deleteCreatedUser(accessToken);
    }
}
