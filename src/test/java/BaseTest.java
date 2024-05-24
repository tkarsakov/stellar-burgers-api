import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.service.EndpointService;
import org.junit.Before;

import java.io.File;
import java.io.IOException;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;

public abstract class BaseTest {
    protected JsonSchemaFactory jsonSchemaFactory;
    protected NewUser newUser;
    private String response;

    @Before
    public void baseSetUp() {
        jsonSchemaFactory = JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze())
                .freeze();
    }

    protected String registerUserValidateResponseExpectingStatusCode(NewUser newUser, String schemaLocation, Integer statusCode) {
        return given().contentType("application/json")
                .body(newUser).
                when()
                .post(EndpointService.AUTH_REGISTER).
                then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(schemaLocation).using(jsonSchemaFactory)).
                and()
                .statusCode(statusCode)
                .extract()
                .path("accessToken");
    }

    protected void registerUserCompareResponseExpectingStatusCode(NewUser newUser, String responseLocation, Integer statusCode) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readTree(new File(responseLocation)).toString();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read response file at " + responseLocation);
        }

        given().contentType("application/json")
                .body(newUser).
                when()
                .post(EndpointService.AUTH_REGISTER).
                then()
                .assertThat()
                .body(equalTo(response)).
                and()
                .statusCode(statusCode);
    }

    protected void deleteCreatedUser(String accessToken) {
        if (!newUser.isMissingFields()) {
            given().header("Authorization", accessToken).
                    when()
                    .delete(EndpointService.AUTH_USER).
                    then()
                    .assertThat()
                    .statusCode(202);
        }
    }
}
