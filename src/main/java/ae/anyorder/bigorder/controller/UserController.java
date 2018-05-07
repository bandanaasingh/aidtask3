package ae.anyorder.bigorder.controller;

import ae.anyorder.bigorder.model.UserEntity;
import ae.anyorder.bigorder.model.UserEntity;
import ae.anyorder.bigorder.repository.UserRepository;
import ae.anyorder.bigorder.util.ReturnJsonUtil;
import ae.anyorder.bigorder.util.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Frank on 4/3/2018.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = { "/save" }, method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserEntity userEntity) {
        userEntity.setPassword(userEntity.getPassword()!=null?passwordEncoder.encode(userEntity.getPassword()):null);
        userRepository.saveAndFlush(userEntity);
        return ResponseEntity.ok(userEntity);
    }

    @RequestMapping(value = { "/update" }, method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(@RequestBody UserEntity userEntity) {
        /*User user1 = userRepository.findOne(user.getId());
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());*/
        userRepository.saveAndFlush(userEntity);
        return ResponseEntity.ok(userEntity);
    }

    @RequestMapping(value = { "/list" }, method = RequestMethod.GET)
    public ResponseEntity<?> listUser() {
        List<UserEntity> userEntities = userRepository.findAll();
        return ResponseEntity.ok(userEntities);
    }

    @RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
    public Object userDetail(@PathVariable Long id) {
        try {
            UserEntity users = userRepository.findByUserId(id);
            String fields ="id,username,firstName,merchant";
            Map<String, String> assoc = new HashMap<>();
            assoc.put("merchant","id,businessTitle");
            UserEntity returnUserEntity = (UserEntity) ReturnJsonUtil.getJsonObject(users, fields, assoc);
            return ResponseEntity.ok(returnUserEntity);
        } catch (Exception e){}
        ServiceResponse serviceResponse = new ServiceResponse("User detail retrieved successfully");
        return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
    }
}
