package ae.anyorder.bigorder.service;

import ae.anyorder.bigorder.apiModel.UserRegister;
import ae.anyorder.bigorder.model.UserEntity;

/**
 * Created by Frank on 4/2/2018.
 */
public interface UserService {

    UserEntity findUserByEmail(String email) throws Exception;

    UserEntity findByUserName(String username) throws Exception;

    void saveMerchant(UserRegister merchant) throws Exception;

    void forgotPassword(String emailAddress) throws Exception;

    void setPassword(String password, String verificationCode) throws Exception;

    void saveCustomer(UserRegister customer) throws Exception;
}