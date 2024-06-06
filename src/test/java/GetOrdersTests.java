import com.intexsoft.stellarburgersapi.model.Order;
import com.intexsoft.stellarburgersapi.service.PropertiesService;
import com.intexsoft.stellarburgersapi.util.JSONUtil;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.intexsoft.stellarburgersapi.service.PropertiesFile.TESTDATA;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetOrdersTests extends BaseTest {
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
    public void registerCreateOrderThenGetOrdersWithAuthExpectSuccess() {
        String schemaLocation = PropertiesService.getProperty(TESTDATA, "orders.GET-positive-response-schema");
        accessToken = steps.registerUser(newUser).path("accessToken");
        steps.createOrder(accessToken, order);
        Response response = steps.getUsersOrders(accessToken);

        Assert.assertEquals(statusCodeMessage, 200, response.statusCode());
        assertThat(bodyMessage, response.body().asString(), matchesJsonSchemaInClasspath(schemaLocation));
    }

    @Test
    public void createOrderThenGetOrdersWithoutAuthExpectFailure() {
        String responseLocation = PropertiesService.getProperty(TESTDATA, "orders.GET-not-authorized-response");
        steps.createOrder(order);
        Response response = steps.getOrders();

        Assert.assertEquals(statusCodeMessage, 401, response.statusCode());
        Assert.assertEquals(bodyMessage, response.body().asString(), JSONUtil.readJsonFromPath(responseLocation));
    }
}
