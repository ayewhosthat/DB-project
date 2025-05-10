import java.sql.*;

class Program {
    public static void main (String[] args) {

        try {Class.forName("com.ibm.db2.jcc.DB2Driver");}
        catch (Exception cnfe) { System.out.println("Class not found"); }
    }
}