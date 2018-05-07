package ae.anyorder.bigorder.service.impl;

import ae.anyorder.bigorder.apiModel.Cart;
import ae.anyorder.bigorder.apiModel.CheckOut;
import ae.anyorder.bigorder.apiModel.Order;
import ae.anyorder.bigorder.exception.MyException;
import ae.anyorder.bigorder.model.*;
import ae.anyorder.bigorder.repository.*;
import ae.anyorder.bigorder.service.ClientService;
import ae.anyorder.bigorder.util.DateUtil;
import ae.anyorder.bigorder.util.ReturnJsonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 4/22/2018.
 */
@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger log = Logger.getLogger(ClientService.class);

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ComboItemRepository comboItemRepository;

    @Autowired
    StoreBrandRepository storeBrandRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ReturnJsonUtil returnJsonUtil;

    @Override
    public void saveCart(Cart cart) throws Exception {
        log.info("Saving cart item");
        ClientEntity clientEntity = clientRepository.findByClientId(cart.getClientId());
        if (clientEntity == null)
            throw new MyException("CLI001");
        ComboItemEntity comboItemEntity = comboItemRepository.findByComboItemId(cart.getComboItemId());
        if (comboItemEntity == null)
            throw new MyException("CMB001");
        StoreBrandEntity storeBrandEntity = storeBrandRepository.findByBrandId(cart.getStoreId());
        if (storeBrandEntity == null)
            throw new MyException("STR001");
        List<CartEntity> carts = cartRepository.findCartByClientId(clientEntity.getId());
        if (carts.size() > 0) {
            if (!carts.get(0).getStoreBrand().getId().equals(storeBrandEntity.getId())) {
                cartRepository.deleteByCustomerId(clientEntity.getId());
            }
        }
        CartEntity cartEntity = new CartEntity();
        cartEntity.setStoreBrand(storeBrandEntity);
        cartEntity.setClient(clientEntity);
        cartEntity.setComboItem(comboItemEntity);
        cartEntity.setQuantity(cart.getQuantity());
        cartRepository.saveAndFlush(cartEntity);

    }

    @Override
    public CheckOut getCheckoutInfo(Long clientId) throws Exception {
        log.info("Getting checkout info");
        ClientEntity clientEntity = clientRepository.findByClientId(clientId);
        if (clientEntity == null)
            throw new MyException("CLI001");
        List<CartEntity> carts = cartRepository.findCartByClientId(clientId);
        CheckOut checkOut = new CheckOut();
        if (carts.size() > 0) {
            checkOut.setStoreBrand(carts.get(0).getStoreBrand());
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (CartEntity cart : carts) {
                totalAmount.add(cart.getComboItem().getUnitPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
            }
            checkOut.setCarts(carts);
            checkOut.setGrandTotal(totalAmount);
        }
        String fields = "storeBrand,carts,grandTotal";
        Map<String, String> assoc = new HashMap<>();
        Map<String, String> subAssoc = new HashMap<>();
        assoc.put("storeBrand", "name,logo");
        assoc.put("carts", "quantity,comboItem");
        assoc.put("comboItem", "name,price,imageUrl,description");
        return (CheckOut) returnJsonUtil.getJsonObject(checkOut, fields, assoc, subAssoc);
    }

    @Override
    public void saveOrder(Order order) throws Exception {
        log.info("Saving order ");
        ClientEntity clientEntity = clientRepository.findByClientId(order.getClientId());
        if (clientEntity == null)
            throw new MyException("CLI011");
        AddressEntity addressEntity = addressRepository.findByAddressId(order.getAddressId());
        if (addressEntity == null)
            throw new MyException("ADD014");
        StoreBrandEntity storesBrand = storeBrandRepository.findByBrandId(order.getStoreBrandId());
        if (storesBrand == null)
            throw new MyException("STR001");
        List<CartEntity> carts = cartRepository.findCartByClientId(order.getClientId());
        if (carts.size() == 0)
            throw new MyException("CRT001");
        OrderEntity orderEntity = new OrderEntity();
        List<OrderComboItemEntity> orderComboItems = new ArrayList<>();
        for (CartEntity cart : carts) {
            if (cart.getComboItem() != null) {
                /*if (!cart.getItem().getStatus().equals(Status.ACTIVE))
                    throw new YSException("ITM002", "Item Name: " + cart.getItem().getName());*/
            }
            OrderComboItemEntity orderComboItemEntity = new OrderComboItemEntity();
            orderComboItemEntity.setOrder(orderEntity);
            orderComboItemEntity.setComboItem(cart.getComboItem());
            orderComboItemEntity.setQuantity(cart.getQuantity());
            orderComboItems.add(orderComboItemEntity);
        }
        orderEntity.setOrderComboItems(orderComboItems);
        orderEntity.setClient(clientEntity);
        orderEntity.setAddress(addressEntity);
        orderEntity.setStore(storesBrand.getStores().get(0));
        orderEntity.setCreatedDate(DateUtil.getCurrentTimestampSQL());
        orderEntity.setPaymentMode(order.getPaymentMode());
        orderEntity.setNote(order.getNote());

        orderRepository.saveAndFlush(orderEntity);
    }
}
