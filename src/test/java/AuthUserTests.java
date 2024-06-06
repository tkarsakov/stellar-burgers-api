import com.github.javafaker.Faker;
import com.intexsoft.stellarburgersapi.model.UserField;
import com.intexsoft.stellarburgersapi.service.PropertiesFile;
import com.intexsoft.stellarburgersapi.service.PropertiesService;
import com.intexsoft.stellarburgersapi.util.JSONUtil;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class AuthUserTests extends BaseTest {

    private static Faker faker = new Faker();
    private final UserField field;
    private final String data;

    public AuthUserTests(UserField field, String data) {
        this.field = field;
        this.data = data;
    }

    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {UserField.EMAIL, faker.internet().emailAddress()},
                {UserField.NAME, faker.name().username()}
        };
    }

    @Test
    public void changeUserNameWithAuthExpectSuccess() {
        accessToken = steps.registerUser(newUser).path("accessToken");
        String schemaLocation = PropertiesService.getProperty(PropertiesFile.TESTDATA, "user.positive-response-schema");
        Response response = steps.changeUserData(field, data, accessToken);

        Assert.assertEquals(statusCodeMessage, 200, response.statusCode());
        assertThat(bodyMessage, response.body().asString(), matchesJsonSchemaInClasspath(schemaLocation));
    }

    @Test
    public void changeUserDataWithoutAuthExpectFailure() {
        accessToken = steps.registerUser(newUser).path("accessToken");
        String responsePath = PropertiesService.getProperty(PropertiesFile.TESTDATA, "user.not-authorized-response");
        Response response = steps.changeUserData(field, data);

        Assert.assertEquals(statusCodeMessage, 401, response.statusCode());
        Assert.assertEquals(bodyMessage, JSONUtil.readJsonFromPath(responsePath), response.body().asString());
    }

}
