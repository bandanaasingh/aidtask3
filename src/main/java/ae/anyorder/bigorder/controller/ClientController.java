package ae.anyorder.bigorder.controller;

import ae.anyorder.bigorder.apiModel.Cart;
import ae.anyorder.bigorder.apiModel.Order;
import ae.anyorder.bigorder.service.ClientService;
import ae.anyorder.bigorder.util.GeneralUtil;
import ae.anyorder.bigorder.util.ServiceResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ClientController {
    private static final Logger log = Logger.getLogger(ClientController.class);

    @Autowired
    ClientService clientService;

    @Autowired
    GeneralUtil generalUtil;

    @RequestMapping(value = "/save_cart", method = RequestMethod.PUT)
    public Object saveCart(@RequestBody Cart cart) {
        try {
            clientService.saveCart(cart);
            ServiceResponse serviceResponse = new ServiceResponse("Cart saved successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error occurred while saving cart");
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/checkout_info", method = RequestMethod.GET)
    public Object saveCart(@RequestHeader String clientId) {
        try {
            clientService.getCheckoutInfo(Long.parseLong(clientId));
            ServiceResponse serviceResponse = new ServiceResponse("Checkout info has been retrieved successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error occurred while retrieving checkout info");
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/save_order", method = RequestMethod.PUT)
    public Object saveOrder(@RequestHeader String clientId, @RequestHeader String addressId, @RequestHeader String brandId, @RequestBody Order order) {
        try {
            clientService.saveOrder(order);
            ServiceResponse serviceResponse = new ServiceResponse("Order saved successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error occurred while saving order");
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
