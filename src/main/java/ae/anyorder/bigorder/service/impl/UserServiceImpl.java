package ae.anyorder.bigorder.service.impl;

import ae.anyorder.bigorder.abs.AbstractManager;
import ae.anyorder.bigorder.apiModel.UserRegister;
import ae.anyorder.bigorder.enums.PreferenceType;
import ae.anyorder.bigorder.enums.Role;
import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.exception.MyException;
import ae.anyorder.bigorder.model.ClientEntity;
import ae.anyorder.bigorder.model.MerchantEntity;
import ae.anyorder.bigorder.model.UserEntity;
import ae.anyorder.bigorder.repository.ClientRepository;
import ae.anyorder.bigorder.repository.MerchantRepository;
import ae.anyorder.bigorder.repository.UserRepository;
import ae.anyorder.bigorder.service.SystemPropertyService;
import ae.anyorder.bigorder.service.UserService;
import ae.anyorder.bigorder.util.DateUtil;
import ae.anyorder.bigorder.util.EmailUtil;
import ae.anyorder.bigorder.util.GeneralUtil;
import ae.anyorder.bigorder.util.MessageBundle;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import org.thymeleaf.context.Context;

/**
 * Created by Frank on 4/7/2018.
 */
@Service(value = "userService")
@Transactional
public class UserServiceImpl extends AbstractManager implements UserService {
    private static final Logger log = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    EmailUtil emailUtil;

    @Autowired
    SystemPropertyService systemPropertyService;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    DateUtil dateUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    MessageBundle messageBundle;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    JavaMailSender emailSender;

    @Override
    public UserEntity findUserByEmail(String email) throws Exception {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUserName(String username) throws Exception{
        return userRepository.findByUsername(username);
    }

    @Override
    public void saveCustomer(UserRegister customer) throws Exception {
        log.info("Saving merchant");
        if (userRepository.findByEmail(customer.getUsername())!=null)
            throw new MyException("USR003");

        ClientEntity clientEntity = new ClientEntity();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(customer.getUsername());
        userEntity.setPassword(generalUtil.encryptPassword(customer.getPassword().trim()));
        userEntity.setFirstName(customer.getFirstName());
        userEntity.setLastName(customer.getLastName());
        userEntity.setEmail(customer.getLastName());
        userEntity.setMobileNumber(customer.getMobileNumber());
        userEntity.setEmail(customer.getEmail());

        userEntity.setStatus(Status.ACTIVE);
        userEntity.setVerifiedStatus(Boolean.TRUE);
        userEntity.setCreatedDate(dateUtil.getCurrentTimestampSQL());
        userEntity.setRole(Role.ROLE_CUSTOMER);
        clientEntity.setUser(userEntity);
        userEntity.setClient(clientEntity);

        //Sending email
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("Sending welcome email to " + clientEntity.getUser().getUsername());
                    //String body = emailUtil.welcomeEmailForNewUser(clientEntity.getUser().getFirstName() + " " + clientEntity.getUser().getLastName(), clientEntity.getUser().getUsername(), getServerUrl());
                    final String appName = systemPropertyService.readPrefValue(PreferenceType.APPLICATION_NAME);
                    final String subject = appName + ": You have been added as Merchant ";
                    //Mi
                    //emailSender.send();
                    /*Context context = new Context();
                    context.setVariable("appName", appName);
                    context.setVariable("username", clientEntity.getUser().getUsername());
                    context.setVariable("serverUrl", getServerUrl());
                    context.setVariable("name", clientEntity.getUser().getFirstName() + " " + clientEntity.getUser().getLastName());
                    emailUtil.sendHTMLEmail(userEntity.getUsername(), subject, "/email/welcome", context);*/
                    log.info("----- Merchant Created Successfully -----");
                } catch (Exception e) {}
            }
        });
        executor.shutdown();
        final String appName = systemPropertyService.readPrefValue(PreferenceType.APPLICATION_NAME);
        final String subject = appName + ": You have been added as Merchant ";
        /*Context context = new Context();
        context.setVariable("appName", appName);
        context.setVariable("username", clientEntity.getUser().getUsername());
        context.setVariable("serverUrl", getServerUrl());
        context.setVariable("logoImage", getServerUrl() + "/resources/custom/images/logo.png");

        context.setVariable("name", clientEntity.getUser().getFirstName() + " " + clientEntity.getUser().getLastName());
        emailUtil.sendHTMLEmail(userEntity.getUsername(), subject, "/email/welcome", context);*/
        clientRepository.saveAndFlush(clientEntity);
    }

    @Override
    public void saveMerchant(UserRegister merchant) throws Exception {
        log.info("Saving merchant");
        if (userRepository.findByEmail(merchant.getUsername())!=null)
            throw new MyException("USR003");

        if (merchantRepository.findByBusinessTitle(merchant.getBusinessTitle())!=null)
            throw new MyException("MER002");
        MerchantEntity merchantEntity = new MerchantEntity();
        merchantEntity.setBusinessTitle(merchant.getBusinessTitle());
        merchantEntity.setUrl(merchant.getUrl());

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(merchant.getUsername());
        userEntity.setPassword(generalUtil.encryptPassword(merchant.getPassword().trim()));
        userEntity.setFirstName(merchant.getFirstName());
        userEntity.setLastName(merchant.getLastName());
        userEntity.setEmail(merchant.getLastName());
        userEntity.setMobileNumber(merchant.getMobileNumber());
        userEntity.setEmail(merchant.getEmail());

        userEntity.setStatus(Status.ACTIVE);
        userEntity.setVerifiedStatus(Boolean.TRUE);
        userEntity.setCreatedDate(dateUtil.getCurrentTimestampSQL());
        userEntity.setRole(Role.ROLE_MERCHANT);
        merchantEntity.setUser(userEntity);

        //Sending email
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("Sending welcome email to " + merchantEntity.getUser().getUsername());
                    //String body = emailUtil.welcomeEmailForNewUser(merchantEntity.getUser().getFirstName() + " " + merchantEntity.getUser().getLastName(), merchantEntity.getUser().getUsername(), getServerUrl());
                    final String appName = systemPropertyService.readPrefValue(PreferenceType.APPLICATION_NAME);
                    final String subject = appName + ": You have been added as Merchant ";
                    /*Context context = new Context();
                    context.setVariable("appName", appName);
                    context.setVariable("username", merchantEntity.getUser().getUsername());
                    context.setVariable("serverUrl", getServerUrl());
                    context.setVariable("name", merchantEntity.getUser().getFirstName() + " " + merchantEntity.getUser().getLastName());
                    emailUtil.sendHTMLEmail(userEntity.getUsername(), subject, "/email/welcome", context);*/
                    log.info("----- Merchant Created Successfully -----");
                } catch (Exception e) {}
            }
        });
        executor.shutdown();
        final String appName = systemPropertyService.readPrefValue(PreferenceType.APPLICATION_NAME);
        final String subject = appName + ": You have been added as Merchant ";
        /*Context context = new Context();
        context.setVariable("appName", appName);
        context.setVariable("username", merchantEntity.getUser().getUsername());
        context.setVariable("serverUrl", getServerUrl());
        context.setVariable("logoImage", getServerUrl() + "/resources/custom/images/logo.png");

        context.setVariable("name", merchantEntity.getUser().getFirstName() + " " + merchantEntity.getUser().getLastName());
        emailUtil.sendHTMLEmail(userEntity.getUsername(), subject, "/email/welcome", context);*/
        merchantRepository.saveAndFlush(merchantEntity);
    }

    @Override
    public void setPassword(String password, String code) throws Exception {
        log.info("Setting password for user");
        UserEntity userEntity = userRepository.findByVerificationCode(code);
        if (userEntity == null)
            throw new MyException("USR001");

        userEntity.setPassword(GeneralUtil.encryptPassword(password.trim()));
        userEntity.setVerificationCode("");
        userEntity.setVerifiedStatus(true);
        userEntity.setStatus(Status.ACTIVE);
        userRepository.saveAndFlush(userEntity);
    }

    @Override
    public void forgotPassword(String username) throws Exception {
        log.info("Performing forgot password of userEntity: " + username);
        String verificationCode = messageBundle.generateTokenString() + "_" + System.currentTimeMillis();
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null)
            throw new MyException("USR001");

        userEntity.setVerificationCode(verificationCode);
        userRepository.saveAndFlush(userEntity);

        /*ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String hostName = getServerUrl();
                    String url = hostName + "/assistance/reset_password?code=" + verificationCode;
                    log.info("Sending mail to " + username + " with password reset url: " + url);

                    String subject = systemPropertyService.readPrefValue(PreferenceType.APPLICATION_NAME) + ": Forgot your Password!";
                    Context context = new Context();
                    context.setVariable("url", "url");
                    context.setVariable("username", userEntity.getFirstName() + " " + userEntity.getLastName());
                    context.setVariable("serverUrl", getServerUrl());
                    emailUtil.sendHTMLEmail(userEntity.getUsername(), subject, "/email/reset", context);
                } catch (Exception e) {}
            }
        });
        executor.shutdown();*/
        String hostName = getServerUrl();
        String url = hostName + "/assistance/reset_password?code=" + verificationCode;
        log.info("Sending mail to " + username + " with password reset url: " + url);

        String subject = systemPropertyService.readPrefValue(PreferenceType.APPLICATION_NAME) + ": Forgot your Password!";
        /*Context context = new Context();
        context.setVariable("url", "url");
        context.setVariable("username", username);
        context.setVariable("name", userEntity.getFirstName()+" "+userEntity.getLastName());
        context.setVariable("serverUrl", getServerUrl());
        emailUtil.sendHTMLEmail(userEntity.getUsername(), subject, "/email/reset", context);
        */log.info("----- Email sent Successfully -----");
    }
}