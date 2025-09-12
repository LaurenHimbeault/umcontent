public interface Logger {
    void log(String message);
}

 interface Notifier {
    void sendNotification(String userName, String message);
}

 interface ScoreRepository {
    int fetchBaseScore(String userName);
}

 interface ScoreUpdatable {
    void addScore(int points);
    int getScore();
    String getName();
}
