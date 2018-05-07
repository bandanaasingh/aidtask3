package ae.anyorder.bigorder.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
/**
 * Created by Frank on 4/8/2018.
 */
@Controller
@RequestMapping(value = "/merchant")
public class MerchantViewController {

    @RequestMapping(value = "/**/{page}/**", method = RequestMethod.GET)
    public ModelAndView merchantSignup(@PathVariable String page){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("merchant/" + page);
        return modelAndView;
    }
}
