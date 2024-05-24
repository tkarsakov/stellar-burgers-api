import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.service.PropertiesFile;
import com.intexsoft.stellarburgersapi.service.PropertiesService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthRegisterTests extends BaseTest {

    @Before
    public void setUp() {
        newUser = NewUser.buildFakeUser();
    }

    @Test
    public void createUniqueUserExpectSuccess() {
        accessToken = super.registerUser(newUser);
    }

    @Test
    public void createUserThenAttemptToCreateSameUserAgain() {
        accessToken = super.registerUser(newUser);
        super.registerUserCompareResponseExpectingStatusCode(newUser, PropertiesService.getProperty(PropertiesFile.TESTDATA, "auth.user-exists-response"), 403);
    }

    @Test
    public void tryToCreateUserWhileMissingFields() {
        newUser.setName(null);
        newUser.setWillBeRegistered(false);
        super.registerUserCompareResponseExpectingStatusCode(newUser, PropertiesService.getProperty(PropertiesFile.TESTDATA, "auth.missing-fields-response"), 403);
    }

    @After
    public void tearDown() {
        super.deleteCreatedUser(accessToken);
    }
}
