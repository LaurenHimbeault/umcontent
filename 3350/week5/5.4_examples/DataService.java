
// Logic Layer Service Manager
public class DataService {
    private MySQLDatabase database;

    public DataService() {
        this.database = new MySQLDatabase();
    }

    public void performDataOperation() {
        database.connect();
        database.backup(); // tightly coupled to backup support
        System.out.println("Performing data operation...");
        database.disconnect();
    }

    // presentation layer
    public static void main(String[] args) {
        DataService ds = new DataService();
        ds.performDataOperation();
    }
}


// Persistant Layer Class
class MySQLDatabase {
    public void connect() {
        System.out.println("Connecting to MySQL...");
    }

    public void disconnect() {
        System.out.println("Disconnecting MySQL...");
    }

    public void backup() {
        System.out.println("Backing up MySQL...");
    }

    public void restore() {
        System.out.println("Restoring MySQL...");
    }
}