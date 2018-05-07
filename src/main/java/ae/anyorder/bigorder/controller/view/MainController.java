package ae.anyorder.bigorder.controller.view;

import ae.anyorder.bigorder.apiModel.SearchItem;
import ae.anyorder.bigorder.config.AuthenticatedUser;
import ae.anyorder.bigorder.dto.Constants;
import ae.anyorder.bigorder.enums.Role;
import ae.anyorder.bigorder.exception.MyException;
import ae.anyorder.bigorder.service.SystemPropertyService;
import ae.anyorder.bigorder.service.UserService;
import ae.anyorder.bigorder.util.GeneralUtil;
import ae.anyorder.bigorder.util.ServiceResponse;
import ae.anyorder.bigorder.util.SessionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
/**
 * Created by Frank on 4/8/2018.
 */
@Controller
public class MainController {

    private static Logger log = Logger.getLogger(MainController.class);
    @Autowired
    SystemPropertyService systemPropertyService;

    @Autowired
    UserService userService;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    SessionManager sessionManager;

    @GetMapping(value = "/")
    public ModelAndView defaultPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("title", Constants.TITLE);
        model.addObject("message", "Home Page!");
        /*if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null && !sessionManager.isAnonymousUser()) {
            Role userRole = sessionManager.getRole();
            if (userRole.equals(Role.ROLE_ADMIN)) {
                model.setViewName("organizer/dashboard");
            } else if (userRole.equals(Role.ROLE_MANAGER.toString())) {
                model.setViewName("organizer/dashboard");
            } else if (userRole.equals(Role.ROLE_MERCHANT)) {
                model.setViewName("smanager/dashboard");
            } else if (userRole.equals(Role.ROLE_STORE_MANAGER)) {
                model.setViewName("smanager/dashboard");
            }
        } else {
            model.setViewName("index");
        }*/
        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = "/items", method = RequestMethod.POST)
    public ModelAndView itemPage(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        model.addObject("event", request.getParameter("event"));
        model.addObject("location", request.getParameter("location"));
        model.addObject("date", request.getParameter("date"));
        model.addObject("number", request.getParameter("number"));
        model.setViewName("items");
        return model;
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public ModelAndView recommendate() {
        ModelAndView model = new ModelAndView();
        model.setViewName("items");
        return model;
    }


    @GetMapping(value = {"/welcome"})
    public Object loginDefaultPage() {
        try {
            String url = "";
            AuthenticatedUser userDetails = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null && !sessionManager.isAnonymousUser()) {
                Role userRole = sessionManager.getRole();
                if (userRole.equals(Role.ROLE_ADMIN)) {
                    url = "smanager/dashboard";
                } else if (userRole.equals(Role.ROLE_MANAGER.toString())) {
                    url = "smanager/dashboard";
                } else if (userRole.equals(Role.ROLE_MERCHANT)) {
                    url = "smanager/dashboard";
                } else if (userRole.equals(Role.ROLE_STORE_MANAGER)) {
                    url = "smanager/dashboard";
                }
            }
            ServiceResponse serviceResponse = new ServiceResponse("User has been logged in successfully");
            serviceResponse.setParams(userDetails);
            return new ResponseEntity<>(serviceResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(value = "/failure", method = RequestMethod.GET)
    public void failure(HttpServletRequest request) {
        String message = getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION");
        throw new MyException("USR001");
    }


    @GetMapping(value = "/auth_failed")
    public ResponseEntity<Object> auth_failed(HttpServletRequest request) throws Exception{
        try {
            String key = "SPRING_SECURITY_LAST_EXCEPTION";
            Exception exception = (Exception) request.getSession().getAttribute(key);
            if (exception instanceof BadCredentialsException) {
                throw new MyException("USR007");
            } else if (exception instanceof InternalAuthenticationServiceException) {
                throw new MyException(((MyException)exception.getCause()).getErrorCode());
            } else {
                throw new MyException("USR007");
            }
        } catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while login", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/access_denied")
    @ResponseBody
    public ModelAndView merchantStore(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/access_denied");
        return modelAndView;
    }

    @RequestMapping(value = "/signup")
    @ResponseBody
    public ModelAndView customerSignup(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/signup");
        return modelAndView;
    }

    @RequestMapping(value = "/register")
    public ModelAndView merchantRegister(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/register");
        return modelAndView;
    }

    @GetMapping(value = "/login")
    public ServiceResponse login(@RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {
        String message = "";
        if (error != null) {
            message = getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION");
        }
        if (logout != null) {
            message = "You've been logged out successfully.";
        }
        return new ServiceResponse(message);
    }

    // customize the error message
    private String getErrorMessage(HttpServletRequest request, String key) {
        Exception exception = (Exception) request.getSession().getAttribute(key);
        String error = "";
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username and password!";
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else {
            error = "Invalid username and password!";
        }
        return error;
    }

    // for 403 access denied page
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied() {
        ModelAndView model = new ModelAndView();
        // check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            System.out.println(userDetail);
            model.addObject("username", userDetail.getUsername());
        }
        model.setViewName("403");
        return model;

    }

    @RequestMapping(value = "/assistance/**", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView forgotPassword(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("assistance");
        return modelAndView;
    }

    @RequestMapping(value = "/forgot_password", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView forgotPasswordMobile(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("assistance/forgot_password");
        return modelAndView;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle(NoHandlerFoundException ex) {
        return "error";
    }


}