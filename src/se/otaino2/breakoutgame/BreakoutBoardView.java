package se.otaino2.breakoutgame;

import java.util.ArrayList;
import java.util.List;

import se.otaino2.breakoutgame.model.Background;
import se.otaino2.breakoutgame.model.Block;
import se.otaino2.breakoutgame.model.Dot;
import se.otaino2.breakoutgame.model.Entity;
import se.otaino2.breakoutgame.model.Paddle;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BreakoutBoardView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "BreakoutBoardView";
    /** The thread that actually draws the animation */
    private BreakoutBoardThread thread;

    public BreakoutBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // SurfaceView must have focus to get touch events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Surface created, starting new thread...");
        thread = new BreakoutBoardThread(holder, getContext());
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "Surface changed, resetting game...");
        thread.setSurfaceSize(width, height);
        thread.reset();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface destroyed, trying to shut down thread...");
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // Do nothing
            }
        }
        Log.d(TAG, "Thread's dead, baby. Thread's dead.");
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus)
            thread.reset();
    }

    public BreakoutBoardThread getThread() {
        return thread;
    }

    class BreakoutBoardThread extends Thread {

        private static final String TAG = "BreakoutBoardThread";
        private static final double NBR_OF_BLOCKS = 6;
        private SurfaceHolder surfaceHolder;
        private Context context;
        private boolean running;
        private int state;
        private int canvasWidth;
        private int canvasHeight;
        private Background background;
        private List<Entity> gameEntities;
        private long lastTime;

        public BreakoutBoardThread(SurfaceHolder surfaceHolder, Context context) {
            this.surfaceHolder = surfaceHolder;
            this.context = context;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public void reset() {
            createEntities(canvasWidth, canvasHeight);
        }

        @Override
        public void run() {
            while (running) {
                Canvas c = null;
                try {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        updatePhysics();
                        // NOTE: In newer versions of Android (4+), it seems SurfaceHolder.lockCanvas() may return null whenever
                        // SurfaceHolder.Callback.surfaceDestroyed() has been invoked. In earlier versions, a canvas was always
                        // returned until SurfaceHolder.Callback.surfaceDestroyed() was FINISHED. See bug report:
                        // https://code.google.com/p/android/issues/detail?id=38658
                        if (c != null) {
                            doDraw(c);
                        }
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public void setSurfaceSize(int width, int height) {
            synchronized (surfaceHolder) {
                canvasWidth = width;
                canvasHeight = height;
                createEntities(width, height);
                lastTime = System.currentTimeMillis() + 100;
            }
        }

        private void createEntities(int width, int height) {
            background = new Background(width, height);
            gameEntities = new ArrayList<Entity>();
            double blockWidth = width / (1.1 * NBR_OF_BLOCKS + 0.1);
            double blockHeight = 0.1 * blockWidth;
            for (int i = 0; i < NBR_OF_BLOCKS; i++) {
                double blockX = i * (blockWidth + blockHeight) + blockHeight;
                double blockY = 2 * blockHeight;
                Block b = new Block((int) blockX, (int) blockY, (int) blockWidth, (int) blockHeight);
                gameEntities.add(b);
            }

            double dotSide = blockHeight / 2;
            double dotX = width / 2;
            double dotY = height / 2;
            Dot d = new Dot((int) dotX, (int) dotY, (int) dotSide, (int) dotSide);
            gameEntities.add(d);

            double paddleWidth = blockWidth * 2;
            double paddleHeight = blockHeight * 2;
            double paddleX = (width - paddleWidth) / 2;
            double paddleY = height - paddleHeight;
            Paddle p = new Paddle((int) paddleX, (int) paddleY, (int) paddleWidth, (int) paddleHeight);
            gameEntities.add(p);
        }

        // Update game entities for next iteration
        private void updatePhysics() {
            long now = System.currentTimeMillis();

            // Make sure we don't update physics unnecessary often
            if (lastTime > now)
                return;

            double elapsed = (now - lastTime) / 1000.0;

            lastTime = now;
        }

        // Draws game entities on canvas. Must be run in
        private void doDraw(Canvas c) {
            renderBackground(c);
            renderEntities(c);
        }

        // Render the board background.
        private void renderBackground(Canvas c) {
            c.drawRect(background.getRect(), background.getPaint());
        }

        private void renderEntities(Canvas c) {
            for (Entity e : gameEntities) {
                c.drawRect(e.getRect(), e.getPaint());
            }
        }
    }
}
