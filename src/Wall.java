import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Wall {
    double distance;
    double speed;
    int thickness = 50;
    List<Integer> wallSlices;

    public Wall(double distance, double speed, int emptySliceIndex) {
        this.distance = distance;
        this.speed = speed;

        wallSlices = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (i != emptySliceIndex) {
                wallSlices.add(i);
            }
        }
    }

    public void update(double rotationAngle) {
        distance -= speed;
    }

    public void draw(Graphics2D g2, int centerX, int centerY) {
        g2.setColor(Color.WHITE);

        // Draw walls in specified slices
        for (int sliceIndex : wallSlices) {
            double startAngle = sliceIndex * Math.PI / 3;
            double endAngle = startAngle + Math.PI / 3;

            int x1Start = (int) (centerX + distance * Math.cos(startAngle));
            int y1Start = (int) (centerY + distance * Math.sin(startAngle));
            int x2Start = (int) (centerX + distance * Math.cos(endAngle));
            int y2Start = (int) (centerY + distance * Math.sin(endAngle));

            int x1End = (int) (centerX + (distance + thickness) * Math.cos(startAngle));
            int y1End = (int) (centerY + (distance + thickness) * Math.sin(startAngle));
            int x2End = (int) (centerX + (distance + thickness) * Math.cos(endAngle));
            int y2End = (int) (centerY + (distance + thickness) * Math.sin(endAngle));

            Polygon wallPolygon = new Polygon(
                    new int[]{x1Start, x2Start, x2End, x1End},
                    new int[]{y1Start, y2Start, y2End, y1End},
                    4
            );

            g2.fillPolygon(wallPolygon);
        }
    }

    public boolean checkCollision(double playerAngle, int playerRadius) {
        for (int sliceIndex : wallSlices) {
            double startAngle = sliceIndex * Math.PI / 3 + Math.PI / 12;
            double endAngle = startAngle + Math.PI / 3 - Math.PI / 12;

            if (distance <= playerRadius + 10 && distance >= playerRadius - 10 &&
                    playerAngle >= startAngle && playerAngle <= endAngle) {
                return true;
            }
        }
        return false;
    }
}