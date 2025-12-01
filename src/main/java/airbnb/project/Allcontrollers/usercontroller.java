package airbnb.project.Allcontrollers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class usercontroller {

    @GetMapping("/")
    public String index() {
        System.out.println("In Controller");
        return "index";
    }

//    @GetMapping("/testing")
//    public String demo()     
//    {
//        System.out.println("In Controller demo");
//        return "demo";
//    }
    @GetMapping("/property")
    public String property() {
        System.out.println("In Controller demo");
        return "property";
    }

    @GetMapping("/usersignup")
    public String usersignup() {
        System.out.println("In Controller demo");
        return "usersignup";
    }

    @GetMapping("/userlogin")
    public String userlogin() {
        System.out.println("In Controller demo");
        return "userlogin";
    }

    @GetMapping("/propertydetail")
    public String propertydetail(HttpSession session) {
        String user_email = (String) session.getAttribute("uemail");
        System.out.println("Email in property Detail"+user_email);
        if (user_email == null) {
            return "redirect:/userlogin";
        }
        else{
        return "propertydetail";
        }
    }

    @GetMapping("/payment")
    public String Payment(HttpSession session) {
       String user_email=(String) session.getAttribute("uemail");
       if(user_email==null){
           return "redirect:/userlogin";
       }else{
        return "payment";
    }
   }
    @GetMapping("/payment_done_icon")
    public String payment_done_icon() {
        System.out.println("In Controller demo");
        return "payment_done_icon";
    }
    
     @GetMapping("/otp")
    public String otp() {
        System.out.println("In Controller demo");
        return "otp";
    }
    @GetMapping("/userchangepassword")
    public String userchangepassword() {
        System.out.println("In Controller demo");
        return "userchangepassword";
    }
    @GetMapping("/forgot")
    public String forgot() {
        System.out.println("In Controller demo");
        return "forgot";
    }
    
    @GetMapping("/ulogin")
    public String login() {
        System.out.println("In Controller demo");
        return "ulogin";
    }
    
    @GetMapping("/userLogout")
    public String userLogout(HttpSession session) {
        session.removeAttribute("uemail");
        return "redirect:/";
    }
    
    @GetMapping("/userbookinghistory")
    public String userbookinghistory() {
        System.out.println("In Controller demo");
        return "userbookinghistory";
    }
}
