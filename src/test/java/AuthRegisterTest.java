import com.intexsoft.stellarburgersapi.model.NewUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthRegisterTest extends BaseTest {
    private final String successfulRegistrationResponseSchema = "POST_auth-register/positive_response_schema.json";
    private final String userExistsResponse = "src/test/resources/POST_auth-register/response(user_exists).json";
    private final String missingFieldsResponse = "src/test/resources/POST_auth-register/response(missing_field).json";
    private String accessToken;

    @Before
    public void setUp() {
        newUser = NewUser.buildFakeUser();
    }

    @Test
    public void createUniqueUserExpectSuccess() {
        accessToken = super.registerUserValidateResponseExpectingStatusCode(newUser, successfulRegistrationResponseSchema, 200);
    }

    @Test
    public void createUserThenAttemptToCreateSameUserAgain() {
        accessToken = super.registerUserValidateResponseExpectingStatusCode(newUser, successfulRegistrationResponseSchema, 200);
        super.registerUserCompareResponseExpectingStatusCode(newUser, userExistsResponse, 403);
    }

    @Test
    public void tryToCreateUserWhileMissingFields() {
        newUser.setName(null);
        newUser.setMissingFields(true);
        super.registerUserCompareResponseExpectingStatusCode(newUser, missingFieldsResponse, 403);
    }

    @After
    public void tearDown() {
        super.deleteCreatedUser(accessToken);
    }
}
