package ae.anyorder.bigorder.controller;

import ae.anyorder.bigorder.apiModel.FCMWebToken;
import ae.anyorder.bigorder.apiModel.ComboItem;
import ae.anyorder.bigorder.model.AreaEntity;
import ae.anyorder.bigorder.service.StoreManagerService;
import ae.anyorder.bigorder.util.GeneralUtil;
import ae.anyorder.bigorder.util.ServiceResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Frank on 4/8/2018.
 */
@RestController
@RequestMapping("/smanager")
public class StoreManagerController {
    public static final Logger log = Logger.getLogger(StoreManagerController.class);

    @Autowired
    GeneralUtil FCMWebToken;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    StoreManagerService storeManagerService;

    @RequestMapping(value = "/save_fcm_web_token", method = RequestMethod.POST)
    public Object saveFCMWebToken(@RequestHeader Long userId, @RequestBody FCMWebToken fcmWebToken) {
        try {
            storeManagerService.saveFCMWebToken(userId, fcmWebToken);
            ServiceResponse serviceResponse = new ServiceResponse("FCM Web Token has been saved successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    //save item
    @RequestMapping(value = "/save_combo_item", method = RequestMethod.POST)
    public Object saveItem(@RequestHeader String brandId, @RequestBody ComboItem comboItem) {
        try {
            storeManagerService.saveItem(Long.parseLong(brandId), comboItem);
            ServiceResponse serviceResponse = new ServiceResponse("Item has been saved successfully");
            return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while saving item", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/update_combo_item", method = RequestMethod.POST)
    public Object updateItem(@RequestHeader String itemId, @RequestBody ComboItem comboItem) {
        try {
            storeManagerService.updateItem(itemId, comboItem);
            ServiceResponse serviceResponse = new ServiceResponse("Item has been updated successfully");
            return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while saving item", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/get_area_list", method = RequestMethod.GET)
    public ResponseEntity<Object> getAreasList(){
        try{
            List<AreaEntity> area = storeManagerService.getAreas();
            ServiceResponse serviceResponse = new ServiceResponse("Areas list has been retrieved successfully");
            serviceResponse.setParams(area);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            generalUtil.logError(log, "Error Occurred while retrieving areas list", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/get_area", method = RequestMethod.GET)
    public ResponseEntity<Object> getArea(@RequestHeader String areaId){
        try{
            AreaEntity area = storeManagerService.getArea(Long.parseLong(areaId));
            ServiceResponse serviceResponse = new ServiceResponse("Area has been retrieved successfully");
            serviceResponse.setParams(area);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            generalUtil.logError(log, "Error Occurred while retrieving area", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/get_active_area", method = RequestMethod.GET)
    public ResponseEntity<Object> getActiveAreas() {
        try {
            List<AreaEntity> area = storeManagerService.getActiveAreas();
            ServiceResponse serviceResponse = new ServiceResponse("Active Areas list has been retrieved successfully");
            serviceResponse.setParams(area);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while retrieving active areas list", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
