import java.awt.*;

public class Cursor {
    GamePanel gp;
    KeyHandler keyH;
    int cursorRadius = 70;
    public double angle = 0;
    public double Speed = 0.07;
    int baseSize = 10;
    int height = 10;
    int[] xPoints;
    int[] yPoints;
    Cursor(GamePanel gp , KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
    }
    public void draw(Graphics2D g2) {

        cursorNum();
        g2.setColor(Color.GREEN);
        g2.fillPolygon(xPoints, yPoints, 3);
    }
    public void update(){
        if (keyH.leftPressed){
            angle -= Speed;
        }
        else if (keyH.rightPressed){
            angle += Speed;
        }
    }

    public Polygon getBoundingPolygon() {

        cursorNum();
        return new Polygon(xPoints, yPoints, 3);
    }
    public void cursorNum() {
        int centerX = (int) (gp.centerX + cursorRadius * Math.cos(angle));
        int centerY = (int) (gp.centerY + cursorRadius * Math.sin(angle));

        xPoints = new int[]{
                centerX,
                centerX - baseSize / 2,
                centerX + baseSize / 2
        };
        yPoints = new int[]{
                centerY - height / 2,
                centerY + height / 2,
                centerY + height / 2
        };
    }
}
