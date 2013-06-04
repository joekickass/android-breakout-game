package se.otaino2.breakoutgame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Very simple break board game.
 * 
 * Inspiration was taken from the LunarLander example from the Android SDK as well as
 * http://www.mysecretroom.com/www/programming-and-software/android-game-loops
 * 
 * @author otaino-2
 * 
 */
public class MainActivity extends Activity implements BreakoutBoardCallback {

    private static final String TAG = "MainActivity";
    private BreakoutBoardView board;
    private TextView lives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lives = (TextView) findViewById(R.id.lives);
        board = (BreakoutBoardView) findViewById(R.id.gameboard);
        board.setCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_reset:
            Log.d(TAG, "Resetting game");
            board.getThread().reset();
            break;
        }
        return false;
    }

    @Override
    public void onGameChanged(final int nbrOfLivesLeft) {
        // Make sure we run on UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String label = String.format(getResources().getString(R.string.lives), nbrOfLivesLeft);
                lives.setText(label);
            }
        });
    }

    @Override
    public void onGameFinished() {
        // do nothing (for now)
    }
}
