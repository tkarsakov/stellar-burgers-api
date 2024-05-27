import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.request.Steps;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Optional;

public abstract class BaseTest {
    protected final String statusCodeMessage = "Status code differs from expected";
    protected final String bodyMessage = "Response body doesn't match provided response or schema";
    protected NewUser newUser;
    protected String accessToken;
    protected Steps steps = new Steps();

    @BeforeClass
    public static void setUpLogging() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void baseSetUp() {
        newUser = NewUser.buildFakeUser();
    }

    @After
    public void tearDown() {
        Optional<Response> responseOptional = steps.deleteCreatedUser(accessToken);
        if (responseOptional.isPresent()) {
            Response response = responseOptional.get();
            Assert.assertEquals(response.statusCode(), 202);
        }
    }
}
