package com.bridgeIt.mvcDemo;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bridgeIt.mvcDemo.exceptionHandler.EmployeeNotFound;
import com.bridgeIt.mvcDemo.model.Employee;

/**
 * Handles requests for the application home page.
 */

@Component
@Controller
@RequestMapping(value="/")
public class HomeController {
	
	//private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/" ,method = RequestMethod.GET)
	public String toIndex(Model model) {
		model.addAttribute("message", "welcome to index page");
		return "index";
	}
	
	@RequestMapping(value="/home" , method = RequestMethod.POST)
	public String home(@ModelAttribute Employee employee,Locale locale,Model model )throws EmployeeNotFound {
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		System.out.println(employee);
		
		if (employee.getId()==2) {
			throw new EmployeeNotFound(2);
			
		}
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		model.addAttribute("message","Welcome to home page");
		
		return "home";
	}
	
	@RequestMapping(value="back")
	public ModelAndView backToHome() {
		
	
		return new ModelAndView("redirect:/");
	}
	
	@ExceptionHandler(EmployeeNotFound.class)
	public ModelAndView handleEmployeeNotFound(HttpServletRequest request , Exception ex) {
		
		ModelAndView model = new ModelAndView();
		model.addObject("Exception", ex);
		model.addObject("url", request.getRequestURI());
		model.setViewName("errorPage");
		return model;
	}
	
	
}
