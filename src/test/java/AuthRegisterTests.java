import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.service.PropertiesService;
import com.intexsoft.stellarburgersapi.util.JSONUtil;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.intexsoft.stellarburgersapi.service.PropertiesFile.TESTDATA;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class AuthRegisterTests extends BaseTest {

    @Before
    public void setUp() {
        newUser = NewUser.buildFakeUser();
    }

    @Test
    public void createUniqueUserExpectSuccess() {
        Response response = requestSteps.registerUser(newUser);
        accessToken = response.path("accessToken");
        String pathToSchema = PropertiesService.getProperty(TESTDATA, "auth.positive-response-schema");

        Assert.assertEquals(statusCodeMessage, 200, response.statusCode());
        assertThat(bodyMessage, response.body().asString(), matchesJsonSchemaInClasspath(pathToSchema));
    }

    @Test
    public void createUserThenAttemptToCreateSameUserAgain() {
        String responsePath = PropertiesService.getProperty(TESTDATA, "auth.user-exists-response");
        accessToken = requestSteps.registerUser(newUser).path("accessToken");
        Response response = requestSteps.registerUser(newUser);

        Assert.assertEquals(statusCodeMessage, 403, response.statusCode());
        Assert.assertEquals(bodyMessage, JSONUtil.readJsonFromPath(responsePath), response.body().asString());
    }

    @Test
    public void tryToCreateUserWhileMissingFields() {
        newUser.setName(null);
        newUser.setWillBeRegistered(false);
        String responsePath = PropertiesService.getProperty(TESTDATA, "auth.missing-fields-response");
        Response response = requestSteps.registerUser(newUser);

        Assert.assertEquals(statusCodeMessage, 403, response.statusCode());
        Assert.assertEquals(bodyMessage, JSONUtil.readJsonFromPath(responsePath), response.body().asString());
    }
}
