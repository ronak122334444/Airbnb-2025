package airbnb.project.Allcontrollers;

import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vmm.DBLoader;
import vmm.RDBMS_TO_JSON;

@RestController
public class admin {

    @PostMapping("/Admin")
    public String Admin(@RequestParam String em, @RequestParam String pass, HttpSession session) {
        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from admin where email=\'" + em + "\' and password=\'" + pass + "\'");
            if (rs.next()) {
                ans = "Login Successful";
                String name=rs.getString("email");
//                session.setAttribute("aname", name);
                session.setAttribute("email", em);
            } else {
                ans = "Login Failed";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Exception Occoured" + ex.toString();
        }
        return ans;

    }

    @PostMapping("/managecities")
    public String managecities(@RequestParam String city, @RequestParam String description, @RequestParam MultipartFile photo) {
        String ans = "";

        String oname = photo.getOriginalFilename();
        try {
            ResultSet rs = DBLoader.executeSQL("select * from managecities where city = \'" + city + "\' ");
            if (rs.next()) {
                ans = "fail";
            } else {
                String projectpath = System.getProperty("user.dir");
                String internal_path = "/src/main/resources/static/";
                String folder = "myuploads";
                String abspath = projectpath + internal_path + folder + "/" + oname;
                byte b[] = photo.getBytes();

                FileOutputStream fos = new FileOutputStream(abspath);
                fos.write(b);
                String path = folder + "/" + oname;
                rs.moveToInsertRow();
                rs.updateString("city", city);
                rs.updateString("description", description);
                rs.updateString("photo", path);
                rs.insertRow();
                ans = "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ans;

    }

    @GetMapping("/alreadyAddedCities")
    public String alreadyAddedCities() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from managecities");
        return ans;
    }

    @GetMapping("/deletedcities")
    public String deletedcities(@RequestParam String city) {
        String ans = "";
        try {

            ResultSet rs = DBLoader.executeSQL("select * from managecities where city=\'" + city + "\' ");
            if (rs.next()) {

                rs.deleteRow();
                ans = "delete";

            }
        } catch (Exception ex) {

            ex.printStackTrace();
            return "exception Occured" + ex.toString();
        }
        return ans;
    }
    
     @PostMapping("/manageproperties")
    public String manageproperties(@RequestParam String pname, @RequestParam String pdes, @RequestParam MultipartFile pphoto) {
        String ans = "";

        String oname = pphoto.getOriginalFilename();
        try {
            ResultSet rs = DBLoader.executeSQL("select * from manageproperties where pname = \'" + pname  + "\' ");
            if (rs.next()) {
                ans = "fail";
            } else {
                String projectpath = System.getProperty("user.dir");
                String internal_path = "/src/main/resources/static/";
                String folder = "myuploads";
                String abspath = projectpath + internal_path + folder + "/" + oname;
                byte b[] = pphoto.getBytes();

                FileOutputStream fos = new FileOutputStream(abspath);
                fos.write(b);
                String path = folder + "/" + oname;
                rs.moveToInsertRow();
                rs.updateString("pname", pname);
                rs.updateString("pdes", pdes);
                rs.updateString("pphoto", path);
                rs.insertRow();
                ans = "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ans;

    }
     @GetMapping("/alreadyAddedProperties")
    public String alreadyAddedProperties() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from manageproperties");
        return ans;
    }

    @GetMapping("/deletedproperty")
    public String deletedproperties(@RequestParam String pname) {
        String ans = "";
        try {

            ResultSet rs = DBLoader.executeSQL("select * from manageproperties where pname=\'" + pname + "\' ");
            if (rs.next()) {

                rs.deleteRow();
                ans = "delete";

            }
        } catch (Exception ex) {

            ex.printStackTrace();
            return "exception Occured" + ex.toString();
        }
        return ans;
    }

}
