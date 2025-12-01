package airbnb.project.Allcontrollers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    @GetMapping("/adminLogin")
    public String Adminlogin() {
        System.out.println("In Controller");
        return "Adminlogin";
    }

    @GetMapping("/adminhome")
    public String Adminhome(HttpSession session) {
        String admin_email = (String) session.getAttribute("email");
        if (admin_email == null) {
            return "redirect:/adminLogin";
        } else {
            return "adminhome";
        }
    }

    @GetMapping("/managecities")
    public String Cities(HttpSession session) {
        String admin_email = (String) session.getAttribute("email");
        if (admin_email == null) {
            return "redirect:/adminLogin";
        } else {
            return "managecities";
        }
    }

    @GetMapping("/manageproperties")
    public String properties(HttpSession session) {
        String admin_email = (String) session.getAttribute("email");
        if (admin_email == null) {
            return "redirect:/adminLogin";
        } else {
            return "manageproperties";
        }
    }

    @GetMapping("/manageowners")
    public String Manageowners(HttpSession session) {
        String owner_email = (String) session.getAttribute("email");
        if (owner_email == null) {
            return "redirect:/adminLogin";
        } else {

            return "manageowners";
        }
    }
}
