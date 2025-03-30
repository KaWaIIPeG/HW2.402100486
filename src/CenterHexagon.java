import java.awt.*;

public class CenterHexagon {

    GamePanel gp;
    public CenterHexagon(GamePanel gp) {
        this.gp = gp;

        int hexRadius = 60;

        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            xPoints[i] = (int) (gp.centerX + hexRadius * Math.cos(Math.toRadians(60 * i)));
            yPoints[i] = (int) (gp.centerY + hexRadius * Math.sin(Math.toRadians(60 * i)));
        }

        gp.g2.setPaint(new Color(90,72,159));
        for (int i = 0; i < 6; i++) {
            int targetX = (int) (gp.centerX + 500 * Math.cos(Math.toRadians(60 * i)));
            int targetY = (int) (gp.centerY + 500 * Math.sin(Math.toRadians(60 * i)));
            gp.g2.drawLine(gp.centerX,gp.centerY, targetX, targetY);
        }

        Polygon hexagon = new Polygon(xPoints, yPoints, 6);
        gp.g2.fillPolygon(hexagon);
    }
}
