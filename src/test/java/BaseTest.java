import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.request.RequestSteps;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Optional;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;

public abstract class BaseTest {
    protected final String statusCodeMessage = "Status code differs from expected";
    protected final String bodyMessage = "Response body doesn't match provided response or schema";
    protected JsonSchemaFactory jsonSchemaFactory;
    protected NewUser newUser;
    protected String accessToken;
    protected RequestSteps requestSteps = new RequestSteps();

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

    @After
    public void tearDown() {
        Optional<Response> responseOptional = requestSteps.deleteCreatedUser(accessToken);
        if (responseOptional.isPresent()) {
            Response response = responseOptional.get();
            Assert.assertEquals(response.statusCode(), 202);
        }
    }
}
