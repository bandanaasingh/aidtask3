package ae.anyorder.bigorder.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Dell Inspiron 3847 on 5/30/2016.
 */
@Controller
@RequestMapping(value = "/smanager")
public class StoreManagerViewController {

    @RequestMapping(value = "/{page}/**", method = RequestMethod.GET)
    public ModelAndView merchantSignup(@PathVariable String page) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("smanager/" + page);
        return modelAndView;
    }

}
