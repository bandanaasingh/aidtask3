package ae.anyorder.bigorder.controller;

import ae.anyorder.bigorder.apiModel.UserRegister;
import ae.anyorder.bigorder.model.AreaEntity;
import ae.anyorder.bigorder.model.CategoryEntity;
import ae.anyorder.bigorder.model.ComboItemEntity;
import ae.anyorder.bigorder.service.MerchantService;
import ae.anyorder.bigorder.service.UserService;
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
@RequestMapping("/anon")
public class AnonController {
    private static final Logger log = Logger.getLogger(AnonController.class);

    @Autowired
    UserService userService;

    @Autowired
    GeneralUtil generalUtil;

    //@Autowired
    //AuthenticationManager authenticationManager;

    @Autowired
    MerchantService merchantService;

    /*@CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/auth/generate-token", method = RequestMethod.POST)
    public Object register(@RequestBody GenerateToken generateToken) throws AuthenticationException {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            generateToken.getUsername(),
                            generateToken.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserEntity userEntity = userService.findByUserName(generateToken.getUsername());
            final String token = jwtTokenUtil.generateToken(userEntity);
            return ResponseEntity.ok(new AuthToken(token));
        } catch (Exception e) {
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }*/

    @RequestMapping(value="/merchant_register", method = RequestMethod.POST)
    public Object createMerchant(@RequestBody UserRegister merchant){
        try{
            userService.saveMerchant(merchant);
            ServiceResponse serviceResponse = new ServiceResponse("Merchant saved successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/customer_register", method = RequestMethod.POST)
    public Object registerCustomer(@RequestBody UserRegister customer){
        try{
            userService.saveCustomer(customer);
            ServiceResponse serviceResponse = new ServiceResponse("Customer saved successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/set_password", method = RequestMethod.POST)
    public Object setPassword(@RequestHeader String password, @RequestHeader String code){
        try{
            userService.setPassword(password, code);
            ServiceResponse serviceResponse = new ServiceResponse("Password has been set successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value="/forgot_password", method = RequestMethod.POST)
    public Object forgotPassword(@RequestHeader String username){
        try{
            userService.forgotPassword(username);
            ServiceResponse serviceResponse = new ServiceResponse("Email has been sent successfully.");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/get_active_parent_categories", method = RequestMethod.GET)
    public ResponseEntity<Object> getParentCategories(){
        try {
            List<CategoryEntity> categories =  merchantService.getActiveParentCategories();
            ServiceResponse serviceResponse = new ServiceResponse("Active Categories retrieved successfully");
            serviceResponse.setParams(categories);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while retrieving categories", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/get_items_detail", method = RequestMethod.GET)
    public ResponseEntity<Object> getItemDetail(@RequestHeader String id) {
        try {
            ComboItemEntity comboItem = merchantService.getComboItemDetail(Long.parseLong(id));
            ServiceResponse serviceResponse = new ServiceResponse("Items has been retrieved successfully");
            serviceResponse.setParams(comboItem);
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while fetching items", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
