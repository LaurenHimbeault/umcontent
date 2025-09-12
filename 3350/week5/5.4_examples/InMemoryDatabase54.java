// db interface
interface Database {
    void connect();
    void disconnect();
    void backup();        
    void restore();       
}

// DB type 2
class MySQLDatabase implements Database {
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

// DB type 1
class InMemoryDatabase implements Database {
    public void connect() {
        System.out.println("Connecting to In-Memory DB...");
    }

    public void disconnect() {
        System.out.println("Disconnecting In-Memory DB...");
    }

    public void backup() {
        throw new UnsupportedOperationException("In-memory DB can't be backed up.");
    }

    public void restore() {
        throw new UnsupportedOperationException("In-memory DB can't be restored.");
    }
}

// Logic Layer Service Manager
class DataService {
    private Database database;

    public DataService(Database database) {
        this.database = database;
    }

    public void performDataOperation() {
        database.connect();
        database.backup(); // tightly coupled to backup support
        System.out.println("Performing data operation...");
        database.disconnect();
    }
}

/*
// production main
public class InMemoryDatabase54 {
    public static void main(String[] args) {
        DataService ds = new DataService(new MySQLDatabase());
        ds.performDataOperation();
    }
}

// this code wouldnt run as is ps
class TestDataService {
    @Test
    public void testPerformDataOperations() {
        // arrange
        DataService ds = new DataService(new InMemoryDatabase());

        // act
        ds.performDataOperation();

        // assert
        // data put in database
        // connection closed through disconnect
    }
}
*/













