import  java.io.* ;
import java.util.*;
import java.sql.*;

public class SqlTest4 {
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

            System.out.println("Recursive test 1");
            // Recursive query 1 실행하고 결과는 적절한 Print문을 이용해 Display
            // Page 8의 Recursive query 1에 “order by a”절을 추가해 실행
            String stmt1 = "with recursive Ancestor(a,d) as (select parent as a, child as d from parentOf " +
                    "union select Ancestor.a, ParentOf.child as d from Ancestor, ParentOf where Ancestor.d = ParentOf.parent)" +
                    "select a from Ancestor where d = 'Frank' order by a";
            rs = st.executeQuery(stmt1);
            int i = 1;
            System.out.println(" \ta");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1) );
                i++;
            }
            // Tuple print시 Tuple 번호도 함께 print
            // 예) cName state
            // ------------------------------------
            // 1 Stanford CA
            // 2 MIT MA
            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();


            System.out.println("Recursive test 2");
            // Recursive query 2 실행하고 결과는 적절한 Print문을 이용해 Display
            // Page 12의 Recursive query 2 실행
            String stmt2 = "with recursive Superior as (select * from Manager union\n" +
                    "\tselect S.mid, M.eID from Superior S, Manager M where S.eID = M.mid) \n" +
                    "\tselect sum(salary) from Employee where ID in (select mgrID from Project where name = 'X' union\n" +
                    "\tselect eID from Project, Superior where Project.name = 'X' AND Project.mgrID = Superior.mid)";
            rs = st.executeQuery(stmt2);
            i = 1;
            System.out.println(" \tsum");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1) );
                i++;
            }
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Recursive test 1” 참조)
            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();


            System.out.println("Recursive test 3");
            // Recursive query 3 실행하고 결과는 적절한 Print문을 이용해 Display
            // Page 16의 Recursive query 3에 “order by total”절을 추가해 실행
            String stmt3 = "with recursive FromA(dest,total) as (select dest,cost as total from Flight where orig = 'A' union\n" +
                    "\tselect F.dest, cost+total as total from FromA FA, Flight F where FA.dest = F.orig)\n" +
                    "\tselect * from FromA order by total";
            rs = st.executeQuery(stmt3);
            i = 1;
            System.out.println(" \tdest\ttotal");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1) + "\t" + rs.getString(2) );
                i++;
            }
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Recursive test 1” 참조)
            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();


            System.out.println("Recursive test 4");
            // Recursive query 4 실행하고 결과는 적절한 Print문을 이용해 Display
            // Page 17의 Recursive query 4 실행
            String stmt4 = "with recursive FromA(dest,total) as (select dest,cost as total from Flight where orig = 'A' union\n" +
                    "\tselect F.dest, cost+total as total from FromA FA, Flight F where FA.dest = F.orig)\n" +
                    "\tselect min(total) from FromA where dest = 'B'";
            rs = st.executeQuery(stmt4);
            i = 1;
            System.out.println(" \tmin");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1));
                i++;
            }
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Recursive test 1” 참조)
            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();


            System.out.println("OLAP test 1");
            // OLAP query 1 실행하고 결과는 적절한 Print문을 이용해 Display
            // Page 16의 OLAP query 1에 “order by storeID, itermID, custID”절을 추가해 실행 (결과 Tuple 개수: 97)
            String stmt11 = "select storeID, itemID, custID, sum(price)\n" +
                    "from Sales\n" +
                    "group by cube(storeID, itemID, custID) order by storeID, itemID, custID;";
            rs = st.executeQuery(stmt11);
            i = 1;
            System.out.println(" \tstoreID\titemID\tcustID\tsum(price)");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1)+ "\t" + rs.getString(2)+ "\t" + rs.getString(3)+ "\t" + rs.getString(4));
                i++;
            }
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Recursive test 1” 참조)
            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();


            System.out.println("OLAP test 2");
            // OLAP query 2 실행하고 결과는 적절한 Print문을 이용해 Display
            // Page 27의 OLAP query 2에 “order by storeID, itermID, custID”절을 추가해 실행 (결과 Tuple 개수: 69)
            String stmt22 = "select storeID, itemID, custID, sum(price)\n" +
                    "from Sales F\n" +
                    "group by itemID, cube(storeID, custID) order by storeID, itemID, custID;";
            rs = st.executeQuery(stmt22);
            i = 1;
            System.out.println(" \tstoreID\titemID\tcustID\tsum(price)");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1)+ "\t" + rs.getString(2)+ "\t" + rs.getString(3)+ "\t" + rs.getString(4));
                i++;
            }
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Recursive test 1” 참조)
            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();


            System.out.println("OLAP test 3");
            // OLAP query 3 실행하고 결과는 적절한 Print문을 이용해 Display
            // Page 30의 OLAP query 3에 “order by storeID, itermID, custID”절을 추가해 실행 (결과 Tuple 개수: 53)
            String stmt33 = "select storeID, itemID, custID, sum(price)\n" +
                    "from Sales F\n" +
                    "group by rollup(storeID, itemID, custID) order by storeID, itemID, custID;";
            rs = st.executeQuery(stmt33);
            i = 1;
            System.out.println(" \tstoreID\titemID\tcustID\tsum(price)");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1)+ "\t" + rs.getString(2)+ "\t" + rs.getString(3)+ "\t" + rs.getString(4));
                i++;
            }
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Recursive test 1” 참조)
            System.out.println("Continue? (Enter 1 for continue)");
            scan.nextLine();


            System.out.println("OLAP test 4");
            // OLAP query 4 실행하고 결과는 적절한 Print문을 이용해 Display
            // Page 32의 OLAP query에 “order by state, county, city”절을 추가해 실행 (결과 Tuple 개수: 12)
            String stmt44 = "select state, county, city, sum(price)\n" +
                    "from Sales F, Store S\n" +
                    "where F.storeID = S.storeID\n" +
                    "group by rollup(state, county, city) order by state, county, city;";
            rs = st.executeQuery(stmt44);
            i = 1;
            System.out.println(" \tstate\tcounty\tcity\tsum(price)");
            System.out.println("----------------------------------------");
            while(rs.next()) {
                System.out.println(i + "\t" + rs.getString(1)+ "\t" + rs.getString(2)+ "\t" + rs.getString(3)+ "\t" + rs.getString(4));
                i++;
            }
            // Tuple print시 Tuple 번호도 함께 print (예시는 위 “Recursive test 1” 참조)

            } catch(SQLException ex)
            {
                throw ex;
            }
        }
}
