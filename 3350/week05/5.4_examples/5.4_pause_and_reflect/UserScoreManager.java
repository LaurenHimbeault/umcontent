public class UserScoreManager {
    private final Logger logger;
    private final Notifier notifier;
    private final ScoreRepository scoreRepo;

    public UserScoreManager(Logger logger, Notifier notifier, ScoreRepository scoreRepo) {
        this.logger = logger;
        this.notifier = notifier;
        this.scoreRepo = scoreRepo;
    }

    public void processUser(ScoreUpdatable user) {
        logger.log("Processing user " + user.getName());
        int baseScore = scoreRepo.fetchBaseScore(user.getName());
        user.addScore(baseScore + 5); // add some bonus logic
        notifier.sendNotification(user.getName(), "Your score has been updated!");
    }
}
