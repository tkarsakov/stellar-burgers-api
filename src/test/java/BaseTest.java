import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.service.EndpointService;
import com.intexsoft.stellarburgersapi.service.PropertiesFile;
import com.intexsoft.stellarburgersapi.service.PropertiesService;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;

public abstract class BaseTest {
    protected JsonSchemaFactory jsonSchemaFactory;
    protected NewUser newUser;
    protected String accessToken;
    private String response;

    @BeforeClass
    public static void setUpLogging() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void baseSetUp() {
        jsonSchemaFactory = JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze())
                .freeze();
    }

    protected String registerUserValidateResponseExpectingStatusCode(NewUser newUser, String schemaLocation, Integer statusCode) {
        return given().contentType(ContentType.JSON)
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

    protected String registerUser(NewUser newUser) {
        return given().contentType(ContentType.JSON)
                .body(newUser).
                when()
                .post(EndpointService.AUTH_REGISTER).
                then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(PropertiesService.getProperty(PropertiesFile.TESTDATA, "auth.positive-response-schema")).using(jsonSchemaFactory)).
                and()
                .statusCode(200)
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

        given().contentType(ContentType.JSON)
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
        if (newUser.getWillBeRegistered()) {
            given().header("Authorization", accessToken).
                    when()
                    .delete(EndpointService.AUTH_USER).
                    then()
                    .assertThat()
                    .statusCode(202);
        }
    }
}
