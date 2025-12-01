package airbnb.project.Allcontrollers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import vmm.RDBMS_TO_JSON;

@Controller
public class ownerController {
     @GetMapping("/owner")
    public String owner()
    {
        System.out.println("In Controller");
        System.out.println("Hello World");
        return "owner";
    }
    
   
     @GetMapping("/ologin")
    public String OLogin()
    {
        System.out.println("In Controller");
        return "ownerlogin";
    }
    

    
    @GetMapping("/ownerhome")
    public String Onwerhome(HttpSession session)
    {
       String owner_email = (String) session.getAttribute("email"); 
           if (owner_email == null) {
            return "redirect:/ologin";
        }
        else{
               return "ownerhome";
           }
    }
    
    @GetMapping("/ownerproperties")
    public String Onwerproperties(HttpSession session)
    {
        String owner_email = (String) session.getAttribute("email"); 
        if(owner_email==null){
         return "redirect:/ologin";
    }
    else{
          return "ownerproperties";
        
    }
}
    
    
    @GetMapping("/manageownerproperties")
    public String manageonwerproperties(HttpSession session)
    {
        String owner_email = (String) session.getAttribute("email"); 
        if(owner_email==null){
           return "redirect:/ologin";
        }
        else{
        return "manageownerproperties";
    }
    }
    
    @GetMapping("/managepropphotos")
    public String managepropphotos(HttpSession session)
    {
        String owner_email = (String) session.getAttribute("email"); 
        if(owner_email==null){
           return "redirect:/ologin";
        }
        else{
          return "managepropphotos";
    } 
    }
    
    @GetMapping("/editowner")
    public String editowner(HttpSession session)
    {
        String owner_email = (String) session.getAttribute("email"); 
        if(owner_email==null){
           return "redirect:/ologin";
        }
        else{
            return "editowner";
        }
    }
     @GetMapping("/ownerviewbooking")
    public String viewbooking()
    {
        System.out.println("In Controller");
        return "ownerviewbooking";
    }
}
