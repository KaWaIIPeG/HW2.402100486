import java.awt.*;

public class UI {
    Font font;
    GamePanel gp;
    Graphics2D g2;
    public boolean messageOn = false;
    public String message = "";
    public UI(GamePanel gp){
        this.gp = gp;

        font = new Font("Arial", Font.BOLD, 36);
    }
    public void showMessage(String text){

        message = text;
        messageOn = true;
    }
    public void draw(Graphics2D g2){

        this.g2 = g2;
        g2.setFont(font);
        g2.setColor(Color.GREEN);

        if (gp.gameState == gp.playState){

        } else if (gp.gameState == gp.pauseState) {
            drawPauseScreen(g2);
        }
    }
    private void drawPauseScreen(Graphics2D g2) {
        String text = "PAUSED";
        Font font = new Font("Arial", Font.BOLD, 36);
        g2.setFont(font);
        g2.setPaint(Color.YELLOW);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int x = gp.centerX - (textWidth / 2);
        int y = gp.centerY + (textHeight / 4);
        g2.drawString(text, x, y);
    }
}
