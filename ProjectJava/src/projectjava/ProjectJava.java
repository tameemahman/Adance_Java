
package projectjava;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import uitility.Dbconnection;


public class ProjectJava {

    public static void main(String[] args) throws SQLException {
        
        
        
        Dbconnection con=new Dbconnection();
        String sql ="insert into student(name,age) Value(?,?)";
        
        
        PreparedStatement ps =con.getCon().prepareStatement(sql);
       
        ps.setString(1,"Tylor");
        ps.setInt(2, 21);
        
        ps.execute();
        
        System.out.println("ok");
    }
    
}
