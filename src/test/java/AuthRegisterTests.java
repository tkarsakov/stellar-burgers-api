import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.service.EndpointService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AuthRegisterTests {
    private NewUser newUser;
    private JsonSchemaFactory jsonSchemaFactory;
    private String accessToken;

    @Before
    public void setUp() {
        newUser = NewUser.buildFakeUser();
        jsonSchemaFactory = JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze())
                .freeze();
    }

    @Test
    public void createUniqueUserExpectSuccess() {
        accessToken = given().contentType("application/json")
                .body(newUser).
                when()
                .post(EndpointService.AUTH_REGISTER).
                then()
                .assertThat()
                .statusCode(200).
                and()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("POST_auth-register/response.json")
                        .using(jsonSchemaFactory))
                .extract()
                .path("accessToken");
    }

    @After
    public void tearDown() {
        given().header("Authorization", accessToken).
                when()
                .delete(EndpointService.AUTH_USER).
                then()
                .assertThat()
                .statusCode(202);
    }
}
