public class User implements ScoreUpdatable {
    private String name;
    private int score;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void addScore(int points) {
        this.score += points;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getName() {
        return name;
    }
}
