import com.intexsoft.stellarburgersapi.model.Order;
import com.intexsoft.stellarburgersapi.service.PropertiesService;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static com.intexsoft.stellarburgersapi.service.PropertiesFile.TESTDATA;

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
    public void registerCreateOrderThenGetOrderWithAuthExpectSuccess() {
        accessToken = steps.registerUser(newUser).path("accessToken");
        steps.createOrder(accessToken, order);
        Response response = steps.getUsersOrders(accessToken);
        response.print();
        //TODO: доделать
    }
}
