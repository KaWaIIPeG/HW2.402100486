import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    public static int gameState;
    public static int playState = 1;
    public static int pauseState = 2;
    public Graphics2D g2;
    public int centerX = 250;
    public int centerY = 250;
    int FPS = 60;
    Thread gameThread;
    KeyHandler keyH = new KeyHandler();
    Cursor cursor = new Cursor(this,keyH);
    GamePanel(){
        this.setPreferredSize(new Dimension(500,500));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {

        double drawInterval = 1_000_000_000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null){

            update();

            repaint();

            try {

                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1_000_000;

                if (remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void centerHexagon(){

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int hexRadius = 60;

        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            xPoints[i] = (int) (centerX + hexRadius * Math.cos(Math.toRadians(60 * i)));
            yPoints[i] = (int) (centerY + hexRadius * Math.sin(Math.toRadians(60 * i)));
        }

        g2.setPaint(Color.red);
        for (int i = 0; i < 6; i++) {
            int targetX = (int) (centerX + 500 * Math.cos(Math.toRadians(60 * i)));
            int targetY = (int) (centerY + 500 * Math.sin(Math.toRadians(60 * i)));
            g2.drawLine(centerX, centerY, targetX, targetY);
        }

        Polygon hexagon = new Polygon(xPoints,yPoints,6);
        g2.setPaint(Color.MAGENTA);
        g2.fillPolygon(hexagon);
    }

    public void update(){
        cursor.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g2 = (Graphics2D) g;

        centerHexagon();

        cursor.draw(g2);

        g2.dispose();
    }
}
