package se.otaino2.breakoutgame;

public interface BreakoutBoardCallback {
    public void onGameChanged(int nbrOfLivesLeft);
    public void onGameFinished();
}
