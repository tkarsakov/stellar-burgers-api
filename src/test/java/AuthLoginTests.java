import com.intexsoft.stellarburgersapi.model.ExistingUser;
import com.intexsoft.stellarburgersapi.model.NewUser;
import com.intexsoft.stellarburgersapi.service.EndpointService;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AuthLoginTests extends BaseTest {

    @Before
    public void setUp() {
        newUser = NewUser.buildFakeUser();
    }

    @Test
    public void logInWithExistingUser() {
        accessToken = super.registerUser(newUser);
        ExistingUser existingUser = ExistingUser.buildFromNewUser(newUser);

        given()
                .contentType(ContentType.JSON)
                .body(existingUser).
                when()
                .post(EndpointService.AUTH_LOGIN).
                then()
                .assertThat()
                .statusCode(200);
        //TODO: добавить проверку тела ответа
    }

    @Test
    public void logInWithInvalidUser() {
        newUser.setWillBeRegistered(false);
        ExistingUser existingUser = ExistingUser.buildFromNewUser(newUser);
    }

    @After
    public void tearDown() {
        super.deleteCreatedUser(accessToken);
    }
}
