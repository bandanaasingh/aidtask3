package ae.anyorder.bigorder.service;

import ae.anyorder.bigorder.apiModel.Cart;
import ae.anyorder.bigorder.apiModel.CheckOut;
import ae.anyorder.bigorder.apiModel.Order;

/**
 * Created by Frank on 4/22/2018.
 */
public interface ClientService {

    void saveCart(Cart cart) throws Exception;

    CheckOut getCheckoutInfo(Long clientId) throws Exception;

    void saveOrder(Order order) throws Exception;
}
