import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserScoreManagerTest {

    // imagine if these methods did LOTS of work in reality, 
    // this allows us to clear the test of the overhead
    // we will find better solutions to this later too
    static class StubLogger implements Logger {
        public void log(String message) { System.out.println("StubLogger log() called"); }
    }

    static class StubNotifier implements Notifier {
        public void sendNotification(String userName, String message) { 
            System.out.println("StubNotifier has fakey notified the user " + 
                userName + " with the message: " + message); 
        }
    }

    static class StubRepo implements ScoreRepository {
        public int fetchBaseScore(String userName) {
            return 20; // predictable stub
        }
    }

    static class FakeUser implements ScoreUpdatable {
        int score = 0;
        public void addScore(int points) { score += points; }
        public int getScore() { return score; }
        public String getName() { return "FakeUser"; }
    }

    @Test
    public void testScoreUpdateOnly() {
        // arrange
        var logger = new StubLogger();
        var notifier = new StubNotifier();
        var repo = new StubRepo();
        var user = new FakeUser();
        var manager = new UserScoreManager(logger, notifier, repo);

        // act
        manager.processUser(user);

        // assert
        assertEquals(25, user.getScore()); // 20 base + 5 bonus
    }
}
