import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public Graphics2D g2;
    public int centerX = 250;
    public int centerY = 250;
    int FPS = 60;
    double rotationAngle = 0;
    Thread gameThread;
    KeyHandler keyH = new KeyHandler(this);
    Cursor cursor = new Cursor(this, keyH);
    Sound sound = new Sound();
    public UI ui = new UI(this);
    List<Wall> walls = new ArrayList<>();
    int wallSpawnTimer = 0;

    GamePanel() {
        this.setPreferredSize(new Dimension(500, 500));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        gameState = titleState;
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1_000_000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void spawnWalls() {
        wallSpawnTimer++;
        if (wallSpawnTimer > 100) {
            int emptySliceIndex = (int) (Math.random() * 6);
            walls.add(new Wall(500, 2, emptySliceIndex));
            wallSpawnTimer = 0;
        }
    }



    public void centerHexagon() {
        int hexRadius = 60;

        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            xPoints[i] = (int) (centerX + hexRadius * Math.cos(Math.toRadians(60 * i)));
            yPoints[i] = (int) (centerY + hexRadius * Math.sin(Math.toRadians(60 * i)));
        }

        g2.setPaint(Color.RED);
        for (int i = 0; i < 6; i++) {
            int targetX = (int) (centerX + 500 * Math.cos(Math.toRadians(60 * i)));
            int targetY = (int) (centerY + 500 * Math.sin(Math.toRadians(60 * i)));
            g2.drawLine(centerX, centerY, targetX, targetY);
        }

        g2.setPaint(Color.RED);
        Polygon hexagon = new Polygon(xPoints, yPoints, 6);
        g2.fillPolygon(hexagon);
    }

    public void update() {
        if (gameState == playState) {
            cursor.update();
            rotationAngle += 0.01;

            spawnWalls();
            for (int i = 0; i < walls.size(); i++) {
                Wall wall = walls.get(i);
                wall.update(rotationAngle);

                if (wall.distance <= 0) {
                    walls.remove(i);
                    i--;
                } else if (wall.checkCollision(cursor.angle, cursor.cursorRadius)) {
                    System.out.println("Collision detected!");
                }
            }
        }
    }

    public void setGameState(int newState) {
        if (gameState != newState) {
            gameState = newState;

            if (gameState == playState) {
                playMusic(0);
            } else if (gameState == pauseState) {
                pauseMusic();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;

        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            AffineTransform originalTransform = g2.getTransform();

            g2.translate(centerX, centerY);
            g2.rotate(rotationAngle);
            g2.translate(-centerX, -centerY);

            for (Wall wall : walls) {
                wall.draw(g2, centerX, centerY);
            }

            centerHexagon();
            cursor.draw(g2);

            g2.setTransform(originalTransform);
            ui.draw(g2);

            g2.dispose();
        }
    }



    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

    public void pauseMusic() {
        sound.pause();
    }
}
