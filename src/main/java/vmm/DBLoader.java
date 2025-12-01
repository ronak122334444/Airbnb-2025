
package vmm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBLoader {

    public static ResultSet executeSQL(String sql) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loading done");

            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/airbnb", "root", "system");
            System.out.println("Connection done");

            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            System.out.println("Statement done");

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("ResultSet Created");

            return rs;
        } catch (Exception e) {
          e.printStackTrace();

        }
        return null;

    }


}
