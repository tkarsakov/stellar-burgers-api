import com.intexsoft.stellarburgersapi.model.ExistingUser;
import com.intexsoft.stellarburgersapi.model.NewUser;
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

public class AuthLoginTests extends BaseTest {

    @Before
    public void setUp() {
        newUser = NewUser.buildFakeUser();
    }

    @Test
    public void logInWithExistingUser() {
        accessToken = super.registerUser(newUser);
        String schemaLocation = PropertiesService.getProperty(PropertiesFile.TESTDATA, "login.positive-response-schema");
        ExistingUser existingUser = ExistingUser.buildFromNewUser(newUser);

        given()
                .contentType(ContentType.JSON)
                .body(existingUser).
                when()
                .post(EndpointService.AUTH_LOGIN).
                then()
                .assertThat()
                .statusCode(200).
                and()
                .body(matchesJsonSchemaInClasspath(schemaLocation));
    }

    @Test
    public void logInWithInvalidUser() {
        String responsePath = PropertiesService.getProperty(PropertiesFile.TESTDATA, "login.invalid_field_response");
        String response = super.readJsonFromPath(responsePath);
        newUser.setWillBeRegistered(false);
        ExistingUser existingUser = ExistingUser.buildFromNewUser(newUser);

        given()
                .contentType(ContentType.JSON)
                .body(existingUser).
                when()
                .post(EndpointService.AUTH_LOGIN).
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
