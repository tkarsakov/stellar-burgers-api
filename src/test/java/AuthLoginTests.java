import com.intexsoft.stellarburgersapi.model.ExistingUser;
import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.request.RequestSteps;
import com.intexsoft.stellarburgersapi.service.PropertiesFile;
import com.intexsoft.stellarburgersapi.service.PropertiesService;
import com.intexsoft.stellarburgersapi.util.JSONUtil;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class AuthLoginTests extends BaseTest {
    private RequestSteps requestSteps = new RequestSteps();

    @Before
    public void setUp() {
        newUser = NewUser.buildFakeUser();
    }

    @Test
    public void logInWithExistingUser() {
        accessToken = requestSteps.registerUser(newUser).path("accessToken");
        String schemaLocation = PropertiesService.getProperty(PropertiesFile.TESTDATA, "login.positive-response-schema");
        ExistingUser existingUser = ExistingUser.buildFromNewUser(newUser);
        Response response = requestSteps.logIn(existingUser);

        Assert.assertEquals(statusCodeMessage, 200, response.statusCode());
        assertThat(bodyMessage, response.body().asString(), matchesJsonSchemaInClasspath(schemaLocation));
    }

    @Test
    public void logInWithInvalidUser() {
        String responsePath = PropertiesService.getProperty(PropertiesFile.TESTDATA, "login.invalid_field_response");
        String expectedResponse = JSONUtil.readJsonFromPath(responsePath);
        newUser.setWillBeRegistered(false);
        ExistingUser existingUser = ExistingUser.buildFromNewUser(newUser);
        Response response = requestSteps.logIn(existingUser);

        Assert.assertEquals(statusCodeMessage, 401, response.statusCode());
        Assert.assertEquals(bodyMessage, expectedResponse, response.body().asString());
    }
}
