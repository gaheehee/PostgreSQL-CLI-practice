import  java.io.* ;
import java.util.*;
import java.sql.*;

public class SqlTest3 {
    public static void main(String[] args) throws SQLException
    {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try
        {
            Scanner scan = new Scanner(System.in);
            System.out.println("SQL Programming Test");

            // JDBC를 이용해 PostgreSQL 서버 및 데이터베이스 연결
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/","postgres", "zxcvbnm147");

            st = conn.createStatement();

            System.out.println("Trigger test 1");
            // Trigger R2 생성
            st.execute("CREATE OR REPLACE FUNCTION trigger2() RETURNS TRIGGER AS $R2$ BEGIN delete from Apply where sID = Old.sID; return New; end; $R2$ LANGUAGE plpgsql;"
                    + "\n" + "create trigger R2 after delete on Student for each row execute procedure trigger2();");
            // Delete문 실행
            st.execute("delete from Student where GPA < 3.5;");
            // Query 1 실행하고 결과는 적절한 Print문을 이용해 Display
            String stmt1 = "select * from Student order by sID;";
            rs = st.executeQuery(stmt1);
            int i = 1;
            System.out.println("Query 1");
            System.out.println("\tsID\tsName\tGPA\tsizesHS");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3)+ "\t" + rs.getString(4));
                i++;
            }
            // Tuple print시 Tuple 번호도 함께 print
            // 예) cName state
            // ------------------------------------
            // 1 Stanford CA
            // 2 MIT MA

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();

            // Query 2 실행하고 결과는 적절한 Print문을 이용해 Display
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Trigger test 1” 참조)
            String stmt2 = "select * from Apply order by sID, cName, major;";
            rs = st.executeQuery(stmt2);
            i = 1;
            System.out.println("Query 2");
            System.out.println("\tsID\tcName\tmajor\tdicision");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" +rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3)+ "\t" + rs.getString(4));
                i++;
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();


            System.out.println("Trigger test 2");
            // Trigger R4 생성
            st.execute("create or replace function trigger4() returns trigger as $R4$" +
                    "begin IF exists(select * from College where cName = New.cName) THEN return null; ELSE return New; END IF; end;" +
                    "$R4$ language plpgsql;\n" +
                    "create trigger R4 before insert on College for each row execute procedure trigger4();");

            // Insert문 실행
            st.execute("insert into College values ('UCLA', 'CA', 20000);");
            st.execute("insert into College values ('MIT', 'hello', 10000);"); //---------------테이블 생성할 때, state를 char(2)로 하여 에러나서 char(5)로 바꿈.
            // Query 3 실행하고 결과는 적절한 Print문을 이용해 Display
            String stmt3 = " select * from College order by cName;";
            rs = st.executeQuery(stmt3);
            i = 1;
            System.out.println("Query 3");
            System.out.println("\tsID\tcName\tmajor\tdicision");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" +rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
                i++;
            }
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Trigger test 1” 참조)

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();

            System.out.println("View test 1");
            // View CSEE 생성
            st.execute("create view CSEE as select sID, cName, major from Apply where major = 'CS' or major = 'EE';");
            // Query 4 실행하고 결과는 적절한 Print문을 이용해 Display
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Trigger test 1” 참조)
            String stmt4 = " select * from CSEE order by sID, cName, major;";
            rs = st.executeQuery(stmt4);
            i = 1;
            System.out.println("Query 4");
            System.out.println("\tsID\tcName\tmajor");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" +rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
                i++;
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();

            System.out.println("View test 2");
            // Trigger CSEEinsert 생성
            st.execute("create or replace function triggerCSEE() returns trigger as $CSEEinsert$ " +
                    "begin IF New.major = 'CS' or New.major = 'EE' THEN insert into Apply values(New.sID, New.cName, New.major, null); return New;" +
                    "ELSE return null; END IF; end; $CSEEinsert$ language plpgsql;\n" +
                    "create trigger CSEEinsert instead of insert on CSEE for each row execute procedure triggerCSEE();");
            // Insert문 실행
            st.execute("insert into CSEE values(333,'UCLA', 'biology');");
            // Query 5 실행하고 결과는 적절한 Print문을 이용해 Display
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Trigger test 1” 참조)
            String stmt5 = " select * from CSEE order by sID, cName, major;";
            rs = st.executeQuery(stmt5);
            i = 1;
            System.out.println("Query 5");
            System.out.println("\tsID\tcName\tmajor");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
                i++;
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();

            // Query 6 실행하고 결과는 적절한 Print문을 이용해 Display
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Trigger test 1” 참조)
            String stmt6 = " select * from Apply order by sID, cName, major;";
            rs = st.executeQuery(stmt6);
            i = 1;
            System.out.println("Query 6");
            System.out.println("\tsID\tcName\tmajor\tdecicion");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4));
                i++;
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();

            System.out.println("View test 3");
            // Insert문 실행
            st.execute("insert into CSEE values(333,'UCLA','CS');");
            // Query 7 실행하고 결과는 적절한 Print문을 이용해 Display
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Trigger test 1” 참조)
            String stmt7 = " select * from CSEE order by sID, cName, major;";
            rs = st.executeQuery(stmt7);
            i = 1;
            System.out.println("Query 7");
            System.out.println("\tsID\tcName\tmajor");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
                i++;
            }

            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();

            // Query 8 실행하고 결과는 적절한 Print문을 이용해 Display
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Trigger test 1” 참조)
            String stmt8 = " select * from Apply order by sID, cName, major;";
            rs = st.executeQuery(stmt8);
            i = 1;
            System.out.println("Query 8");
            System.out.println("\tsID\tcName\tmajor\tdecicion");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4));
                i++;
            }

        } catch(SQLException ex)
        {
            throw ex;
        }
    }
}
