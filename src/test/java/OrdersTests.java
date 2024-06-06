import com.intexsoft.stellarburgersapi.model.Order;
import com.intexsoft.stellarburgersapi.service.PropertiesService;
import com.intexsoft.stellarburgersapi.util.JSONUtil;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.intexsoft.stellarburgersapi.service.PropertiesFile.TESTDATA;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrdersTests extends BaseTest {
    private Order order;

    @Before
    public void setUp() {
        String ingredient1 = PropertiesService.getProperty(TESTDATA, "orders.ingredient1");
        String ingredient2 = PropertiesService.getProperty(TESTDATA, "orders.ingredient2");
        order = new Order();
        order.getIngredients().add(ingredient1);
        order.getIngredients().add(ingredient2);
    }

    @Test
    public void createOrderWithAuthExpectSuccess() {
        String schemaLocation = PropertiesService.getProperty(TESTDATA, "orders.positive-response-schema-auth");
        accessToken = steps.registerUser(newUser).path("accessToken");
        Response response = steps.createOrder(accessToken, order);

        Assert.assertEquals(statusCodeMessage, 200, response.statusCode());
        assertThat(bodyMessage, response.body().asString(), matchesJsonSchemaInClasspath(schemaLocation));
    }

    @Test
    public void createOrderWithoutAuthExpectSuccess() {
        String schemaLocation = PropertiesService.getProperty(TESTDATA, "orders.positive-response-schema-no-auth");
        Response response = steps.createOrder(order);

        Assert.assertEquals(statusCodeMessage, 200, response.statusCode());
        assertThat(bodyMessage, response.body().asString(), matchesJsonSchemaInClasspath(schemaLocation));
    }

    @Test
    public void createOrderWithoutIngredientsExpectFailure() {
        order.getIngredients().clear();
        String responseLocation = PropertiesService.getProperty(TESTDATA, "orders.no-ingredients-response");
        Response response = steps.createOrder(order);

        Assert.assertEquals(statusCodeMessage, 400, response.statusCode());
        Assert.assertEquals(bodyMessage, JSONUtil.readJsonFromPath(responseLocation), response.body().asString());
    }

    @Test
    public void createOrderWithWrongIngredientHashesExpectFailure() throws IOException {
        order.getIngredients().add("Lorem ipsum");
        String responseLocation = PropertiesService.getProperty(TESTDATA, "orders.wrong-ingredient-hash-response");
        Response response = steps.createOrder(order);

        Assert.assertEquals(statusCodeMessage, 500, response.statusCode());
        Assert.assertEquals(bodyMessage, Files.readString(Path.of(responseLocation)), response.body().asString());
    }
}
