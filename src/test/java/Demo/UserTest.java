package Demo;

import com.mjy.entity.User;
import com.mjy.service.UserService;
import com.mjy.service.impl.UserServiceImpl;
import com.mjy.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 * @description
 * @create 2021-07-31 14:55
 */
public class UserTest {

    @Test
    public void test(){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = sdf.parse("2001-09-10");
            User user = new User();
            user.setBirthday(parse);
            System.out.println(user.getAge());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConn(){

        try {
            Connection conn = JDBCUtils.getConnection();
            Connection conn1 = JDBCUtils.getConnection();
            System.out.println(conn);
            System.out.println(conn1);
            JDBCUtils.closeResource(null,conn,null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testCount(){
        UserService service = new UserServiceImpl();

        System.out.println(service.getRoles());
    }
}
