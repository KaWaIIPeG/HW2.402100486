import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int gameOverState = 3;
    public Graphics2D g2;
    public int centerX = 250;
    public int centerY = 250;
    int FPS = 60;
    int counter = 0;
    double rotationAngle = 0.01;
    public int speedCounter = 0;
    Thread gameThread;
    KeyHandler keyH = new KeyHandler(this);
    Cursor cursor = new Cursor(this, keyH);
    Sound sound = new Sound();
    public UI ui = new UI(this);
    public Wall w = new Wall(this);
    List<Wall> walls = new ArrayList<>();

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

    public void update() {
        if (gameState == playState) {
            cursor.update(0.05 + (speedCounter / 60) * 0.00045);

            rotationAngle += 0.01 + (speedCounter / 60) * 0.0004;
            speedCounter++;

            if (speedCounter % 60 == 0) {
                for (Wall wall : walls) {
                    wall.speed += 0.07;
                }
            }

            w.spawnWalls(2 + (speedCounter / 60) * 0.07);

            if (speedCounter > 3600) {
                speedCounter = 3600;
            }

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = walls.get(i);
                wall.update(rotationAngle);

                if (wall.distance <= 0) {
                    walls.remove(i);
                    i--;
                }
            }

            handleCollision();

        } else if (gameState == gameOverState) {
            counter++;
            if (counter > 120) {
                gameState = titleState;
                w.wallSpawnTimer = 0;
                rotationAngle = 0.01;
                ui.playTime = 0;
                speedCounter = 0;
                counter = 0;
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;

        if (gameState == titleState) {
            ui.draw(g2);

        } else if (gameState == gameOverState) {
            ui.draw(g2);

        } else {
            AffineTransform originalTransform = g2.getTransform();

            g2.translate(centerX, centerY);
            g2.rotate(rotationAngle);
            g2.translate(-centerX, -centerY);

            for (Wall wall : walls) {
                wall.draw(g2, centerX, centerY);
            }

            new CenterHexagon(this);
            cursor.draw(g2);

            g2.setTransform(originalTransform);
            ui.draw(g2);
        }
        g2.dispose();
    }

    public static Color getRandomColor() {

        List<Color> colors = new ArrayList<>();
        colors.add(new Color(77,137,99));
        colors.add(new Color(225,179,120));
        colors.add(new Color(224,204,151));
        colors.add(new Color(236,121,154));
        colors.add(new Color(238,50,51));
        colors.add(new Color(168,74,92));
        colors.add(new Color(255,106,0));
        colors.add(new Color(94,119,3));
        colors.add(new Color(135, 206, 250));
        colors.add(new Color(255, 105, 180));
        colors.add(new Color(152, 80, 60));

        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));
    }

    public void handleCollision() {
        Polygon cursorPolygon = cursor.getBoundingPolygon();

        for (Wall wall : walls) {
            if (wall.checkCollision(cursorPolygon)) {
                if (gameState != gameOverState) {
                    sound.stop();
                    gameState = gameOverState;
                    walls.clear();
                    counter = 0;
                }
                break;
            }
        }
    }
    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    public void setGameState(int newState) {
        if (gameState != newState) {
            gameState = newState;

            if (gameState == playState) {
//                playMusic(0);
            } else if (gameState == pauseState) {
                sound.pause();
            }
        }
    }
}
