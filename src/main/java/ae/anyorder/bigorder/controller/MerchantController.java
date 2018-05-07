package ae.anyorder.bigorder.controller;

import ae.anyorder.bigorder.apiModel.StoreBrand;
import ae.anyorder.bigorder.dto.HeaderDto;
import ae.anyorder.bigorder.dto.Page;
import ae.anyorder.bigorder.dto.PaginationDto;
import ae.anyorder.bigorder.model.CategoryEntity;
import ae.anyorder.bigorder.model.ComboItemEntity;
import ae.anyorder.bigorder.model.StoreBrandEntity;
import ae.anyorder.bigorder.service.MerchantService;
import ae.anyorder.bigorder.util.GeneralUtil;
import ae.anyorder.bigorder.util.ServiceResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Frank on 4/8/2018.
 */
@RestController
@RequestMapping(value = "/merchant")
public class MerchantController {
    private static final Logger log = Logger.getLogger(MerchantController.class);

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    MerchantService merchantService;

    @RequestMapping(value="/save_store_brand", method = RequestMethod.PUT)
    public ResponseEntity<Object> saveStoresBrand(@RequestHeader String merchantId, @RequestBody StoreBrand storeBrand){
        try{
            merchantService.saveStoreBrand(Long.parseLong(merchantId), storeBrand);
            ServiceResponse serviceResponse = new ServiceResponse("Store saved successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            generalUtil.logError(log, "Error Occurred while saving stores brands", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/update_store_brand", method = RequestMethod.PUT)
    public Object updateStoresBrand(@RequestHeader String merchantId, @RequestHeader String brandId, @RequestBody StoreBrand storeBrand){
        try{
            merchantService.updateStoreBrand(Long.parseLong(merchantId), Long.parseLong(brandId), storeBrand);
            ServiceResponse serviceResponse = new ServiceResponse("Stores Brand updated successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            generalUtil.logError(log, "Error Occurred while updating stores brands", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/get_brand_detail", method = RequestMethod.GET)
    public Object getBrandDetails(@RequestHeader String brandId){
        try{
            StoreBrandEntity storeBrand =  merchantService.getBrandDetails(Long.parseLong(brandId));
            ServiceResponse serviceResponse = new ServiceResponse("Stores Brand detail retrieved successfully");
            serviceResponse.setParams(storeBrand);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            generalUtil.logError(log, "Error Occurred while retrieving stores brands detail", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/save_store_serving_area", method = RequestMethod.PUT)
    public Object saveStoreServingArea(@RequestHeader String storeId, @RequestBody List<Long> areaIds){
        try{
            merchantService.saveServingArea(Long.parseLong(storeId), areaIds);
            ServiceResponse serviceResponse = new ServiceResponse("Serving Area of store has been added successfully.");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            generalUtil.logError(log, "Error Occurred while adding serving area of store..", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/update_store_serving_area", method = RequestMethod.PUT)
    public Object updateStoreServingArea(@RequestHeader String storeId, @RequestBody List<Long> areaIds){
        try{
            merchantService.updateServingArea(Long.parseLong(storeId), areaIds);
            ServiceResponse serviceResponse = new ServiceResponse("Serving Area of store has been updated successfully.");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            generalUtil.logError(log, "Error Occurred while updating serving area of store", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/stores_by_status", method = RequestMethod.POST)
    public ResponseEntity<Object> storeListByStatus(@RequestHeader String userId, @RequestBody Page page) {
        try {
            PaginationDto paginationDto = merchantService.storeListByStatus(Long.parseLong(userId), page);
            ServiceResponse serviceResponse = new ServiceResponse("Store list has been retrieved successfully.");
            serviceResponse.setParams(paginationDto);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while retrieving store list by status .", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/get_parent_categories", method = RequestMethod.GET)
    public ResponseEntity<Object> getParentCategories(){
        try {
            List<CategoryEntity> categories =  merchantService.getParentCategories();
            ServiceResponse serviceResponse = new ServiceResponse("Categories retrieved successfully");
            serviceResponse.setParams(categories);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while retrieving categories", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/get_store_item_list", method = RequestMethod.GET)
    public ResponseEntity<Object> getStoreItemList(@RequestHeader String brandId){
        try {
            List<ComboItemEntity> comboItems =  merchantService.getStoreItemList(Long.parseLong(brandId));
            ServiceResponse serviceResponse = new ServiceResponse("Store Items retrieved successfully");
            serviceResponse.setParams(comboItems);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while retrieving store items", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/get_store_category_item_list", method = RequestMethod.GET)
    public ResponseEntity<Object> getStoreCategoryWiseItemList(@RequestHeader String brandId){
        try {
            List<CategoryEntity> categories =  merchantService.getStoreCategoryWiseItemList(Long.parseLong(brandId));
            ServiceResponse serviceResponse = new ServiceResponse("Store Category wise items retrieved successfully");
            serviceResponse.setParams(categories);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while retrieving store category wise items", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
