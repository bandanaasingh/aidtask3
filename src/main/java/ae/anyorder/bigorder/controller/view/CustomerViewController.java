package ae.anyorder.bigorder.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Frank on 4/8/2018.
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerViewController {

    @RequestMapping(value = "/order/**", method = RequestMethod.GET)
    public ModelAndView merchantSignup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/order");
        return modelAndView;
    }

}
