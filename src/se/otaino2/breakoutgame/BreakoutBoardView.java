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

    /** The thread that actually draws the animation */
    private BreakoutBoardThread thread;

    public BreakoutBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        thread = new BreakoutBoardThread(holder, context);

        // SurfaceView must have focus to get touch events
        setFocusable(true);
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Start thread
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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
    }
    
    public Thread getThread() {
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

        public BreakoutBoardThread(SurfaceHolder surfaceHolder, Context context) {
            this.surfaceHolder = surfaceHolder;
            this.context = context;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
        
        @Override
        public void run() {
            while (running) {
                Canvas c = null;
                try {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        updatePhysics();
                        doDraw(c);
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
            }
        }

        private void createEntities(int width, int height) {
            background = new Background(width, height);
            gameEntities = new ArrayList<Entity>();
            double blockWidth = width / ( 1.1 * NBR_OF_BLOCKS + 0.1);
            double blockHeight = 0.1 * blockWidth;
            for (int i = 0; i < NBR_OF_BLOCKS; i++) {
                double blockX = i * (blockWidth + blockHeight) + blockHeight;
                double blockY = 2 * blockHeight;
                Block b = new Block((int)blockX, (int)blockY, (int)blockWidth, (int)blockHeight);
                gameEntities.add(b);
            }
            
            double dotSide = blockHeight/2;
            double dotX = width/2;
            double dotY = height/2;
            Dot d = new Dot((int)dotX, (int)dotY, (int)dotSide, (int)dotSide);
            gameEntities.add(d);
            
            double paddleWidth = blockWidth * 2;
            double paddleHeight = blockHeight * 2;
            double paddleX = (width - paddleWidth) / 2;
            double paddleY = height - paddleHeight;
            Paddle p = new Paddle((int)paddleX, (int)paddleY, (int)paddleWidth, (int)paddleHeight);
            gameEntities.add(p);
        }

        // Update game entities for next iteration 
        private void updatePhysics() {
            
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
