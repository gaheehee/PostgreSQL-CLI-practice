import  java.io.* ;
import java.util.*;
import java.sql.*;

public class SqlTest {

    public static void main(String[] args) throws SQLException
    {
        Connection conn = null;
        Statement st = null;
        PreparedStatement ps =  null;
        ResultSet rs = null;
        try
        {
            Scanner scan = new Scanner(System.in);
            System.out.println("SQL Programming Test");

            System.out.println("Connecting PostgreSQL database");
            // JDBC를 이용해 PostgreSQL 서버 및 데이터베이스 연결
            //Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/","postgres", "zxcvbnm147");



            System.out.println("Creating College, Student, Apply relations");
            // 3개 테이블 생성: Create table문 이용
            st = conn.createStatement();
            st.execute("Create table College(cName varchar(20), state char(2), enrollment int)");
            st.execute("Create table Student(sID int, sName varchar(20), GPA numeric(2,1), sizeHS int)");
            st.execute("Create table Apply(sID int, cName varchar(20), major varchar(20), decision char)");



            System.out.println("Inserting tuples to College, Student, Apply relations");
            // 3개 테이블에 튜플 생성: Insert문 이용
            st.execute( "insert into College values ('Stanford', 'CA', 15000)");
            st.execute("insert into College values ('Berkeley', 'CA', 36000)");
            st.execute("insert into College values ('MIT', 'MA', 10000)");
            st.execute("insert into College values ('Cornell', 'NY', 21000)");

            st.execute("insert into Student values (123, 'Amy', 3.9, 1000)");
            st.execute("insert into Student values (234, 'Bob', 3.6, 1500)");
            st.execute("insert into Student values (345, 'Craig', 3.5, 500)");
            st.execute("insert into Student values (456, 'Doris', 3.9, 1000)");
            st.execute("insert into Student values (567, 'Edward', 2.9, 2000)");
            st.execute("insert into Student values (678, 'Fay', 3.8, 200)");
            st.execute("insert into Student values (789, 'Gary', 3.4, 800)");
            st.execute("insert into Student values (987, 'Helen', 3.7, 800)");
            st.execute("insert into Student values (876, 'Irene', 3.9, 400)");
            st.execute("insert into Student values (765, 'Jay', 2.9, 1500)");
            st.execute("insert into Student values (654, 'Amy', 3.9, 1000)");
            st.execute("insert into Student values (543, 'Craig', 3.4, 2000)");

            st.execute("insert into Apply values (123, 'Stanford', 'CS', 'Y')");
            st.execute("insert into Apply values (123, 'Stanford', 'EE', 'N')");
            st.execute("insert into Apply values (123, 'Berkeley', 'CS', 'Y')");
            st.execute("insert into Apply values (123, 'Cornell', 'EE', 'Y')");
            st.execute("insert into Apply values (234, 'Berkeley', 'biology', 'N')");
            st.execute("insert into Apply values (345, 'MIT', 'bioengineering', 'Y')");
            st.execute("insert into Apply values (345, 'Cornell', 'bioengineering', 'N')");
            st.execute("insert into Apply values (345, 'Cornell', 'CS', 'Y')");
            st.execute("insert into Apply values (345, 'Cornell', 'EE', 'N')");
            st.execute("insert into Apply values (678, 'Stanford', 'history', 'Y')");
            st.execute("insert into Apply values (987, 'Stanford', 'CS', 'Y')");
            st.execute("insert into Apply values (987, 'Berkeley', 'CS', 'Y')");
            st.execute("insert into Apply values (876, 'Stanford', 'CS', 'N')");
            st.execute("insert into Apply values (876, 'MIT', 'biology', 'Y')");
            st.execute("insert into Apply values (876, 'MIT', 'marine biology', 'N')");
            st.execute("insert into Apply values (765, 'Stanford', 'history', 'Y')");
            st.execute("insert into Apply values (765, 'Cornell', 'history', 'N')");
            st.execute("insert into Apply values (765, 'Cornell', 'psychology', 'Y')");
            st.execute("insert into Apply values (543, 'MIT', 'CS', 'N')");

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();



            System.out.println("Query 1-------------------------------------------------------------");
            // Query 1을 실행: Select문 이용
            // Query 처리 결과는 적절한 Print문을 이용해 Display
            String stmt1 = "select * from College;";
            rs = st.executeQuery(stmt1);
            while(rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();

            // Query 2 ~ Query 5에 대해 Query 1과 같은 방식으로 실행: Select문 이용
            // Query 처리 결과는 적절한 Print문을 이용해 Display
            System.out.println("Query 2-------------------------------------------------------------");
            String stmt2 = "select * from Student;";
            rs = st.executeQuery(stmt2);
            while(rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4));
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();

            System.out.println("Query 3-------------------------------------------------------------");
            String stmt3 = "select * from Apply;";
            rs = st.executeQuery(stmt3);
            while(rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4));
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();

            System.out.println("Query 4-------------------------------------------------------------");
            String stmt4 = "select cName, major, min(GPA), max(GPA) from Student, Apply where Student.sID = Apply.sID  group by cName, major having min(GPA) > 3.0 order by cName, major;";
            rs = st.executeQuery(stmt4);
            while(rs.next()){
                System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4));
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();

            System.out.println("Query 5-------------------------------------------------------------");
            String stmt5 = "select distinct cName from Apply A1 where 6 > (select count(*) from Apply A2 where A2.cName = A1.cName);";
            rs = st.executeQuery(stmt5);
            while(rs.next()){
                System.out.println(rs.getString(1));
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();



            System.out.println("Query 6-------------------------------------------------------------");
            // Query 6을 실행: Select문 이용
            // 사용자에게 major1, major2 값으로 CS, EE 입력 받음
            // 입력 받은 값을 넣어 Query를 처리하고
            // 결과는 적절한 Print문을 이용해 Display
            String major1, major2;
            String stmt6 = "select sID, sName from Student where sID = any (select sID from Apply where major = ?) and not sID = any (select sID from Apply where major = ?)";
            ps = conn.prepareStatement(stmt6);
            System.out.println("Enter a major1 name: ");
            major1 = scan.nextLine();
            System.out.println("Enter a major2 name: ");
            major2 = scan.nextLine();
            ps.clearParameters();
            ps.setString(1, major1);
            ps.setString(2, major2);
            rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt(1) + "\t" + rs.getString(2));
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();



            System.out.println("Query 7-------------------------------------------------------------");
            // Query 7을 실행: Select문 이용
            // 사용자에게 major, cName 값으로 CS, Stanford 입력 받음
            // 입력 받은 값을 넣어 Query를 처리하고
            // 결과는 적절한 Print문을 이용해 Display
            String major, cName;
            String stmt7 = "select sName, GPA from Student natural join Apply where major = ? and cName = ?";
            ps = conn.prepareStatement(stmt7);
            System.out.println("Enter a major name: ");
            major = scan.nextLine();
            System.out.println("Enter a cName name: ");
            cName = scan.nextLine();
            ps.clearParameters();
            ps.setString(1, major);
            ps.setString(2, cName);
            rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1) + "\t" + rs.getString(2));
            }

        } catch(SQLException ex)
        {
            throw ex;
        }
    }


}
