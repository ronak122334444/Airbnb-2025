package airbnb.project.Allcontrollers;

import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vmm.DBLoader;
import vmm.RDBMS_TO_JSON;

@RestController
public class user {

    @GetMapping("/Cities")
    public String Cities() {
        System.out.println("In Get Cities Rest Controller");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from managecities");
        return ans;
    }

    @PostMapping("/Usersignup")
    public String Usersignup(@RequestParam String uname, @RequestParam String uemail, @RequestParam String upassword, @RequestParam String ucontact, @RequestParam String uaddress) {

        String ans = "";
        try {
//            System.out.println("photo");
            ResultSet rs = DBLoader.executeSQL("select * from user where uemail = \'" + uemail + "\' ");
            if (rs.next()) {
                System.out.println("In IF");
                ans = "fail";
            } else {
                System.out.println("In Else");

                rs.moveToInsertRow();
                rs.updateString("uname", uname);
                rs.updateString("uemail", uemail);
                rs.updateString("upassword", upassword);
                rs.updateString("ucontact", ucontact);
                rs.updateString("uaddress", uaddress);

                rs.insertRow();
                ans = "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ans;

    }

    @PostMapping("/userLogin")
    public String userLogin(@RequestParam String em, @RequestParam String pass, HttpSession session) {
        String ans = "";
        try {
            System.out.println("In Login controller");
            ResultSet rs = DBLoader.executeSQL("select * from user where uemail=\'" + em + "\' and upassword=\'" + pass + "\'");
            if (rs.next()) {
                ans = "Login Successful";
                String name = rs.getString("uname");
//                session.setAttribute("name", name);
                session.setAttribute("uemail", em);
            } else {
                ans = "Login Failed";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Exception Occoured" + ex.toString();
        }
        return ans;

    }

    @PostMapping("/properties")
    public String Properties(@RequestParam String city) {
        System.out.println("city " + city);
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT ownerproperties.* FROM ownerproperties inner join owner on ownerproperties.owner_email=owner.email where owner.status='approve' and ownerproperties.city='" + city + "'");
        return ans;
    }

    @PostMapping("/propertyDetail")
    public String PropertyDetail(@RequestParam String id) {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from ownerproperties where id = \'" + id + "\' ");
        return ans;
    }

    @GetMapping("/check_date")
    String check_date(@RequestParam int id, @RequestParam String start, @RequestParam String end) {
        int flag = 1;
        int paymentid = 0;
        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select id from user_payment where detail_id=" + id);
            while (rs.next()) {
                paymentid = rs.getInt("id");
                ResultSet rs1 = DBLoader.executeSQL("select * from payment_detail where payment_id=" + paymentid);
                while (rs1.next()) {
                    if (start.equals(rs1.getString("start_date")) || end.equals(rs1.getString("end_date"))) {
                        flag = 0;
                        break;
                    }
                }
                if (flag == 0) {
                    break;
                }
            }
            if (flag == 1) {
                ans = ans + "success";
            } else {
                ans = ans + "Fail";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return ans;
    }

    @GetMapping("/cash")
    String payment(@RequestParam int detail_id, @RequestParam int grand, @RequestParam String start,
            @RequestParam String end, HttpSession session, @RequestParam String type) {
        String ans = "";
        int payment_id = 0;
        String owner_email = "";
        String address = "";
        String user_email = (String) session.getAttribute("uemail");
        try {
            ResultSet rs1 = DBLoader.executeSQL("select * from ownerproperties where id=" + detail_id);
            if (rs1.next()) {
                owner_email = rs1.getString("owner_email");
                address = rs1.getString("address");
            }
            ResultSet rs2 = DBLoader.executeSQL("select * from user_payment");
            rs2.moveToInsertRow();
            rs2.updateString("owner_email", owner_email);
            rs2.updateString("user_email", user_email);
            rs2.updateInt("detail_id", detail_id);
            rs2.updateInt("total_price", grand);
            rs2.updateString("payment_type", type);
            rs2.updateString("address", address);
            rs2.updateString("start_date", start);
            rs2.updateString("end_date", end);
            rs2.insertRow();
            ResultSet rs3 = DBLoader.executeSQL("select max(id) as id from user_payment");
            if (rs3.next()) {
                payment_id = rs3.getInt("id");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate ldStartDate = LocalDate.parse(start, formatter);
            LocalDate ldEndDate = LocalDate.parse(end, formatter);
            for (LocalDate date = ldStartDate; date.isBefore(ldEndDate); date = date.plusDays(1)) {
                ResultSet rs4 = DBLoader.executeSQL("select * from payment_detail");
                rs4.moveToInsertRow();
                rs4.updateString("start_date", date.format(formatter));
                rs4.updateString("end_date", date.plusDays(1).format(formatter));
                rs4.updateInt("payment_id", payment_id);
                rs4.insertRow();

            }
            
            sendemail obj = new sendemail(user_email, 
    "Booking Confirmation - Bon Voyage", 
    "Dear " + user_email + ",\n\n" +
    "We are delighted to confirm your booking! Below are the details of your reservation:\n\n" +
    "Booking Details:\n" +
    "Property Address: " + address + "\n" +
    "Check-in Date: " + start + "\n" +
    "Check-out Date: " + end + "\n" +
    "Total Price: RS. " + grand + "\n" +
    "Payment Method: " + type + "\n\n" +
    "Additional Information:\n" +
    "Please ensure you arrive at the property on the check-in date specified above. If you have any special requests or need assistance during your stay, feel free to contact the property owner at: " + owner_email + ".\n" +
    "If you need to modify or cancel your booking, kindly notify us at least 48 hours in advance.\n\n" +
    "Thank you for choosing our platform for your stay. We hope you have a wonderful experience!\n\n" +
    "Best regards,\n" +
    "The Airbnb Team");

            ans = ans + "success";
        } catch (Exception ex) {
            return ex.toString();
        }

        return ans;
    }

    @GetMapping("/online")
    String online(@RequestParam int detail_id, @RequestParam int grand, @RequestParam String start,
            @RequestParam String end, HttpSession session) {

        String ans = "";
        int payment_id = 0;
        String owner_email = "";
        String address = "";
        String user_email = (String) session.getAttribute("uemail");
        try {
            ResultSet rs1 = DBLoader.executeSQL("select * from ownerproperties where id=" + detail_id);
            if (rs1.next()) {
                owner_email = rs1.getString("owner_email");
                address = rs1.getString("address");
            }
            ResultSet rs2 = DBLoader.executeSQL("select * from user_payment");
            rs2.moveToInsertRow();
            rs2.updateString("owner_email", owner_email);
            rs2.updateString("user_email", user_email);
            rs2.updateInt("detail_id", detail_id);
            rs2.updateInt("total_price", grand);
            rs2.updateString("payment_type", "online");
            rs2.updateString("address", address);
            rs2.updateString("start_date", start);
            rs2.updateString("end_date", end);
            rs2.insertRow();
            ResultSet rs3 = DBLoader.executeSQL("select max(id) as id from user_payment");
            if (rs3.next()) {
                payment_id = rs3.getInt("id");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate ldStartDate = LocalDate.parse(start, formatter);
            LocalDate ldEndDate = LocalDate.parse(end, formatter);
            for (LocalDate date = ldStartDate; date.isBefore(ldEndDate); date = date.plusDays(1)) {
                ResultSet rs4 = DBLoader.executeSQL("select * from payment_detail");
                rs4.moveToInsertRow();
                rs4.updateString("start_date", date.format(formatter));
                rs4.updateString("end_date", date.plusDays(1).format(formatter));
                rs4.updateInt("payment_id", payment_id);
                rs4.insertRow();

            }
            
            
            sendemail obj = new sendemail(user_email, 
    "Booking Confirmation - Bon Voyage", 
    "Dear " + user_email + ",\n\n" +
    "We are delighted to confirm your booking! Below are the details of your reservation:\n\n" +
    "Booking Details:\n" +
    "Property Address: " + address + "\n" +
    "Check-in Date: " + start + "\n" +
    "Check-out Date: " + end + "\n" +
    "Total Price: RS. " + grand + "\n" +
    "Payment Method: " + "online" + "\n\n" +
    "Additional Information:\n" +
    "Please ensure you arrive at the property on the check-in date specified above. If you have any special requests or need assistance during your stay, feel free to contact the property owner at: " + owner_email + ".\n" +
    "If you need to modify or cancel your booking, kindly notify us at least 48 hours in advance.\n\n" +
    "Thank you for choosing our platform for your stay. We hope you have a wonderful experience!\n\n" +
    "Best regards,\n" +
    "The Airbnb Team");
            
            ans = ans + "success";
        } catch (Exception ex) {
            return ex.toString();
        }

        return ans;
    }

    @GetMapping("/userShowAverageRatings")
    public String userShowAverageRatings(@RequestParam String doctor_email) {

        // Assuming RDBMS_TO_JSON is available as a service or component
        String ans = new RDBMS_TO_JSON().generateJSON("select avg(rating) as r1 from review_table where owner_email='" + doctor_email + "' ");
        System.out.println(ans);
        return ans;

    }

    @GetMapping("/userShowRatings")
    public String userShowRatings(@RequestParam String doctor_email) {

        // Assuming RDBMS_TO_JSON is available as a service or component
        String ans = new RDBMS_TO_JSON().generateJSON("select * from review_table where owner_email='" + doctor_email + "' ");
        System.out.println(ans);
        return ans;

    }

    @GetMapping("/userAddReview")
    public String userAddReview(@RequestParam String doctor_email, @RequestParam int rating, @RequestParam String comment, HttpSession session) {
        String user_email = (String) session.getAttribute("uemail");
        System.out.println(user_email);
//        System.out.println(rating);
        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("Select * from review_table");

            rs.moveToInsertRow();
            rs.updateString("owner_email", doctor_email);
            rs.updateString("user_email", user_email);
            rs.updateString("comment", comment);
            rs.updateInt("rating", rating);
            rs.insertRow();
            ans = "success";

        } catch (Exception e) {
            ans = e.toString();
        }

        return ans;
    }

    @PostMapping("/passwordchange")
    public String passchange(@RequestParam String oldpassword, @RequestParam String newpassword, HttpSession session) {

        String ans = "";
        try {
            String useremail = (String) session.getAttribute("uemail");
            ResultSet rs = DBLoader.executeSQL("Select * from user where uemail='" + useremail + "' and upassword='" + oldpassword + "' ");
            if (rs.next()) {
                rs.updateString("upassword", newpassword);
                rs.updateRow();
                
                session.removeAttribute("uemail");
                
                ans = "Password changed";
            } else {
                ans = "old password is incorrect";
            }

        } catch (Exception e) {
            ans = e.toString();
        }

        return ans;
    }
    
    private Map<String, String> otpStorage = new HashMap<>(); // Temporary in-memory OTP storage

    @PostMapping("/sendOTP")
    public String sendOTP(@RequestParam String Email) {
        try {
            // Check if email exists in the database
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM user WHERE uemail='" + Email + "'");
            if (rs.next()) {
                // Generate a random OTP
                String otp = String.valueOf((int) (1000 + Math.random() * 9000));
                // Store OTP with email as the key in in-memory map
                otpStorage.put(Email, otp);

                // Send OTP via email
                String subject = "Your OTP Code";
                String message = "Your OTP is: " + otp;
                sendemail obj = new sendemail(Email, subject, message);

                return "OTP sent successfully to: " + Email;
            } else {
                return "Email not present";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Failed to send OTP: " + ex.getMessage();
        }
    }

    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestParam String Email, @RequestParam String otp) {
        String storedOtp = otpStorage.get(Email);

        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(Email); // Clear OTP after verification
            return "OTP verified successfully!";
        } else {
            return "Invalid OTP or OTP expired.";
        }
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String Email, @RequestParam String newPassword) {
        try {
            // Check if email exists and update the password
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM user WHERE uemail='" + Email + "'");
            if (rs.next()) {
                rs.updateString("upassword", newPassword);
                rs.updateRow(); // Save changes to the database

                return "Password changed successfully!";
            } else {
                return "Email not found.";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Failed to reset password: " + ex.getMessage();
        }
    }
     @GetMapping("/userbooking")
    public String userbooking(HttpSession session) {
        String user_email = (String) session.getAttribute("uemail");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from user_payment where user_email='"+user_email+"' ");
        return ans;
    }

}
