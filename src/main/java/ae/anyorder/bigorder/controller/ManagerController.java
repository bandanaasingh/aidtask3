package ae.anyorder.bigorder.controller;

import ae.anyorder.bigorder.apiModel.Area;
import ae.anyorder.bigorder.apiModel.Category;
import ae.anyorder.bigorder.apiModel.UpdateStatus;
import ae.anyorder.bigorder.dto.Page;
import ae.anyorder.bigorder.dto.PaginationDto;
import ae.anyorder.bigorder.dto.SendNotification;
import ae.anyorder.bigorder.model.CategoryEntity;
import ae.anyorder.bigorder.service.ManagerService;
import ae.anyorder.bigorder.util.GeneralUtil;
import ae.anyorder.bigorder.util.ServiceResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/organizer")
public class ManagerController {
    private static Logger log = Logger.getLogger(ManagerController.class);

    @Autowired
    ManagerService managerService;

    @Autowired
    GeneralUtil generalUtil;


    @RequestMapping(value="/save_area", method = RequestMethod.PUT)
    public Object saveArea(@RequestBody Area area) {
        try{
            managerService.saveArea(area);
            ServiceResponse serviceResponse = new ServiceResponse("Area saved successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e){
            log.info("Error occured while saving area");
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/update_area", method = RequestMethod.PUT)
    public Object updateArea(@RequestHeader Long id, @RequestBody Area area){
        try{
            managerService.updateArea(id, area);
            ServiceResponse serviceResponse = new ServiceResponse("Area updated successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e){
            log.info("Error occured while updating area");
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/save_category", method = RequestMethod.PUT)
    public Object saveCategory(@RequestBody Category category) {
        try{
            managerService.saveCategory(category);
            ServiceResponse serviceResponse = new ServiceResponse("Category has been saved successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e){
            log.info("Error occured while saving category");
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/update_category", method = RequestMethod.PUT)
    public Object updateCategory(@RequestHeader Long id, @RequestBody Category category){
        try{
            managerService.updateCategory(id, category);
            ServiceResponse serviceResponse = new ServiceResponse("Category has been updated successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e){
            log.info("Error occurred while updating category");
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/send_notification", method = RequestMethod.POST)
    public Object sendPushNotification(@RequestBody SendNotification notification) {
        try{
            managerService.sendPushNotification(notification);
            ServiceResponse serviceResponse = new ServiceResponse("Notification sent successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            generalUtil.logError(log, "Error occurred while sending push notification", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/get_merchant_list", method = RequestMethod.POST)
    public ResponseEntity<Object> getMerchantList(@RequestBody Page page){
        try{
            PaginationDto merchantList = managerService.getMerchantList(page);
            ServiceResponse serviceResponse = new ServiceResponse("Merchant list has been retrieved successfully.");
            serviceResponse.setParams(merchantList);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            generalUtil.logError(log, "Error Occurred while retrieving merchant list.", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/change_merchant_status", method = RequestMethod.PUT)
    public ResponseEntity<Object> changeMerchantStatus(@RequestHeader String merchantId, @RequestBody UpdateStatus status) {
        try {
            //test
            managerService.changeMerchantStatus(Long.parseLong(merchantId), status);
            ServiceResponse serviceResponse = new ServiceResponse("Merchant's status has been changed successfully.");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while changing merchant's status.", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    /*@RequestMapping(value = "/get_default_categories", method = RequestMethod.GET)
    public  ResponseEntity<Object> getDefaultCategories() {
        try {
            List<CategoryEntity> categories = managerService.getDefaultCategories();
            ServiceResponse serviceResponse = new ServiceResponse("Category has been retrieved successfully");
            serviceResponse.setParams(categories);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while retrieving category", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }*/
}
