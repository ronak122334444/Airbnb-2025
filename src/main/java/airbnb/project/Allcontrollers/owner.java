package airbnb.project.Allcontrollers;

import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vmm.DBLoader;
import vmm.RDBMS_TO_JSON;

@RestController
public class owner {

    @GetMapping("/getCities")
    public String getCities() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from managecities");
        return ans;
    }

    @PostMapping("/owner")
    public String Owner(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam String cpassword, @RequestParam String city, @RequestParam String contact, @RequestParam String property) {
        String ans = "";

        try {
            ResultSet rs = DBLoader.executeSQL("select * from owner where email = \'" + email + "\' ");
            if (rs.next()) {
                ans = "fail";
            } else {
                rs.moveToInsertRow();
                rs.updateString("name", name);
                rs.updateString("email", email);
                rs.updateString("password", password);
                rs.updateString("cpassword", cpassword);

                rs.updateString("city", city);
                rs.updateString("contact", contact);
                rs.updateString("property", property);
                rs.updateString("status", "pending");
                rs.insertRow();
                ans = "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ans;

    }

    @PostMapping("/ownerLogin")
    public String ownerLogin(@RequestParam String em, @RequestParam String pass, HttpSession session) {
        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from owner where email=\'" + em + "\' and password=\'" + pass + "\'");
            if (rs.next()) {
                ans = "Login Successful";
                String name = rs.getString("name");
//                session.setAttribute("name", name);
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

    @GetMapping("/alreadyAddedowners")
    public String alreadyAddedowners() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from owner");
        return ans;
    }

    @GetMapping("/blockowners")
    public String blockowners(@RequestParam String email) {
        String ans = "";
        try {

            ResultSet rs = DBLoader.executeSQL("select * from owner  where email=\'" + email + "\' ");
            if (rs.next()) {

                rs.updateString("status", "pending");
                rs.updateRow();
//                rs.updateString(email, ans);
                ans = "success";
            } else {
                ans = "fail";
            }
        } catch (Exception ex) {

            ex.printStackTrace();
            return "exception Occured" + ex.toString();
        }
        return ans;
    }

    @GetMapping("/approveowners")
    public String approveowners(@RequestParam String email) {
        String ans = "";
        try {

            ResultSet rs = DBLoader.executeSQL("select * from owner  where email=\'" + email + "\' ");
            if (rs.next()) {

                rs.updateString("status", "approve");
                rs.updateRow();
//                rs.updateString(email, ans);
                ans = "success";
            } else {
                ans = "fail";
            }
        } catch (Exception ex) {

            ex.printStackTrace();
            return "exception Occured" + ex.toString();
        }
        return ans;
    }

    @GetMapping("/getownerCities")
    public String getOcities() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from managecities");
        return ans;
    }

    @GetMapping("/getownerProperties")
    public String getOproperties() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from manageproperties");
        return ans;
    }

    @PostMapping("/ownerproperties")
    public String ownerproperties(HttpSession session, @RequestParam String pn, @RequestParam String pname, @RequestParam String price, @RequestParam String oprice, @RequestParam String city, @RequestParam String address, @RequestParam String pdes,@RequestParam String latitude, @RequestParam String longitude,@RequestParam MultipartFile pphoto) {
        String ans = "";

        String oname = pphoto.getOriginalFilename();
        try {
            ResultSet rs = DBLoader.executeSQL("select * from ownerproperties");
//            if (rs.next()) {
            String projectpath = System.getProperty("user.dir");
            String internal_path = "/src/main/resources/static/";
            String folder = "myuploads";
            String abspath = projectpath + internal_path + folder + "/" + oname;
            byte b[] = pphoto.getBytes();

            FileOutputStream fos = new FileOutputStream(abspath);
            fos.write(b);
            String path = folder + "/" + oname;
            System.out.println("Property Type " + pname);
            String email = (String) session.getAttribute("email");
            rs.moveToInsertRow();
            rs.updateString("property_name", pn);
            rs.updateString("property_type", pname);
            rs.updateString("price", price);
            rs.updateString("offer_price", oprice);
            rs.updateString("owner_email", email);

            rs.updateString("city", city);
            rs.updateString("address", address);
            rs.updateString("description", pdes);
             rs.updateString("latitude", latitude);
            rs.updateString("longitude", longitude);
            rs.updateString("status", "approve");
            rs.updateString("photo", path);
            rs.insertRow();
            ans = "success";

            System.out.println("Answer " + ans);
//            } else {
//                ans = "fail";
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ans;
    }

    @GetMapping("/alreadyaddedownerproperties")
    public String alreadyaddedownerproperties(HttpSession session) {

        String email = (String) session.getAttribute("email");
        System.out.println("Email " + email);
//        String ans = new RDBMS_TO_JSON().generateJSON("select * from ownerproperties where owner_email= '"+email+"' ");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from ownerproperties");
        return ans;
    }

    @GetMapping("/deletedownerproperty")
    public String deletedownerproperties(@RequestParam String id) {
        String ans = "";
        try {

            ResultSet rs = DBLoader.executeSQL("select * from ownerproperties where id=\'" + id + "\' ");
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

    @PostMapping("/addphotos")
    public String addPhotos(@RequestParam String id, @RequestParam MultipartFile photo) {
        String oname = photo.getOriginalFilename();
        String ans = "";
        try {
            String query = "SELECT * FROM propertiesphotos";
            System.out.println(query);
            ResultSet rs = DBLoader.executeSQL(query);

            String projectPath = System.getProperty("user.dir");
            String internalPath = "/src/main/resources/static/";
            String folder = "myuploads";
            String absPath = projectPath + internalPath + folder + "/" + oname;
            byte[] bytes = photo.getBytes();

            FileOutputStream fos = new FileOutputStream(absPath);
            fos.write(bytes);
            fos.close();

            String path = folder + "/" + oname;

            rs.moveToInsertRow();
            rs.updateString("photo", path);
            rs.updateInt("id", Integer.parseInt(id));
            rs.insertRow();
            ans = "success";

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ans;
    }

    @GetMapping("/alreadyaddedphotos")
    public String alreadyaddedPhotos(@RequestParam String id) {
        System.out.println("ID " + id);
        String ans = new RDBMS_TO_JSON().generateJSON("select * from propertiesphotos where id=\'" + id + "\' ");
        return ans;
    }
    

    @GetMapping("/deletephotos")
    public String deletephotos(@RequestParam String photoid) {
        String ans = "";
        try {

            ResultSet rs = DBLoader.executeSQL("select * from propertiesphotos where photoid=\'" + photoid + "\' ");
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

    
    @GetMapping("/editownerreturn")
    public String editowner(@RequestParam String id) {
        System.out.println("ID " + id);
        String ans = new RDBMS_TO_JSON().generateJSON("select * from ownerproperties where id=\'" + id + "\' ");
        return ans;
    }

@PostMapping("/editowner")
public String editowner(@RequestParam String email, @RequestParam String id, @RequestParam String propertyname, @RequestParam String price, @RequestParam String oprice, @RequestParam String address, @RequestParam String pdes) {
    String ans = "";

    try {
        String query = "select * from ownerproperties where id='" + id + "'";
        System.out.println("Query " + query);
        ResultSet rs = DBLoader.executeSQL(query);
        
        if (rs.next()) { // Move to the first row if available
            rs.updateString("property_name", propertyname);
            rs.updateString("price", price);
            rs.updateString("offer_price", oprice);
            rs.updateString("address", address);
            rs.updateString("description", pdes);

            rs.updateRow();
            ans = "success";
        } else {
            ans = "fail"; // No row found with the given email
        }
        
        System.out.println("Answer " + ans);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return ans;
}
 @GetMapping("/viewbooking")
    public String booking(HttpSession session) {
        String ownerEmail = (String) session.getAttribute("email");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from user_payment where owner_email='"+ownerEmail+"' ");
        return ans;
    }
    
    @PostMapping("/propertyPhotos")
    public String propertyPhotos(@RequestParam String id) {
        System.out.println("ID " + id);
        String ans = new RDBMS_TO_JSON().generateJSON("select * from propertiesphotos where id=\'" + id + "\' ");
        return ans;
    }
}
