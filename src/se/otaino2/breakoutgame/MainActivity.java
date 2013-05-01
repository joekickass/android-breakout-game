package se.otaino2.breakoutgame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Very simple break board game.
 * 
 * Inspiration was taken from the LunarLander example from the Android SDK as well as
 * http://www.mysecretroom.com/www/programming-and-software/android-game-loops
 * 
 * @author otaino-2
 * 
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private BreakoutBoardView board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board = (BreakoutBoardView) findViewById(R.id.gameboard);
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
}
