import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://eventplanner-srv.database.windows.net:1433;databaseName=EventPlannerDB;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net";
        String user = "sqladmin@eventplanner-srv";
        String password = "Topshop3?";
        
        try {
            System.out.println("🔍 Testing connection to Azure SQL Database...");
            System.out.println("URL: " + url);
            System.out.println("User: " + user);
            
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connection successful!");
            
            // Test a simple query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM INFORMATION_SCHEMA.TABLES");
            
            if (rs.next()) {
                int tableCount = rs.getInt("count");
                System.out.println("📊 Number of tables in database: " + tableCount);
            }
            
            // Check if EventM table exists
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM EventM");
            if (rs.next()) {
                int eventCount = rs.getInt("count");
                System.out.println("📅 Number of events in EventM table: " + eventCount);
            }
            
            conn.close();
            System.out.println("✅ Test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 