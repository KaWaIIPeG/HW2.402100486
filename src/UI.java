import java.awt.*;
import java.text.DecimalFormat;

public class UI {
    Font font;
    GamePanel gp;
    Graphics2D g2;
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");
    public int commandNum = 0;
    public UI(GamePanel gp){

        this.gp = gp;
        font = new Font("Arial", Font.BOLD, 36);
    }
    public void draw(Graphics2D g2){

        this.g2 = g2;
        g2.setFont(font);

        if (gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        if (gp.gameState == gp.playState){
            playTime += (double)1/60;
            g2.setColor(Color.yellow);
            g2.drawString("Time:" +dFormat.format(playTime),0,30);
        }
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
        if (gp.gameState == gp.gameOverState){
            drawEndScreen();
        }
    }
    private void drawTitleScreen() {

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC,50));
        String text = "Super Hexagon";

        g2.setColor(Color.DARK_GRAY);
        g2.drawString(text,gp.centerX - 187,gp.centerY - 172);

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC,50));
        text = "Super Hexagon";

        g2.setColor(Color.RED);
        g2.drawString(text,gp.centerX - 185,gp.centerY - 175);

        text = "Created By : Arshian flh";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,12));
        g2.setColor(Color.YELLOW);
        g2.drawString(text,gp.centerX - 220,gp.centerY + 200);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,30));
        text = "New Game";
        g2.setColor(Color.WHITE);
        g2.drawString(text,gp.centerX - 80,gp.centerY - 70);
        if (commandNum == 0){
            g2.drawString(">" , gp.centerX - 110,gp.centerY - 70);
        }

        text = "Previous Runs";
        g2.drawString(text,gp.centerX - 80,gp.centerY - 30);
        if (commandNum == 1){
            g2.drawString(">" , gp.centerX - 110,gp.centerY - 30);
        }

        text = "Settings";
        g2.drawString(text,gp.centerX - 80,gp.centerY + 10);
        if (commandNum == 2){
            g2.drawString(">" , gp.centerX - 110,gp.centerY + 10);
        }

        text = "Quit";
        g2.drawString(text,gp.centerX - 80,gp.centerY + 50);
        if (commandNum == 3){
            g2.drawString(">" , gp.centerX - 110,gp.centerY + 50);
        }

        text = "Best run:";
        g2.setColor(Color.GRAY);
        g2.drawString(text,gp.centerX - 220,gp.centerY + 135);

    }

    private void drawPauseScreen() {
        centerMessage("Paused",Color.YELLOW);
    }
    private void drawEndScreen() {
        centerMessage("Game Over",Color.RED);
        g2.drawString("Time:" +dFormat.format(playTime),gp.centerX - 87,gp.centerY + 50);
    }

    private void centerMessage(String text,Color color){
        Font font = new Font("Arial", Font.BOLD, 36);
        g2.setFont(font);
        g2.setPaint(color);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int x = gp.centerX - (textWidth / 2);
        int y = gp.centerY + (textHeight / 4);
        g2.drawString(text, x, y);
    }
}