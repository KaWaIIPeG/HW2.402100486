import java.awt.*;

public class Wall {
    double startAngle; // Angle at the start of the wall
    double endAngle;   // Angle at the end of the wall
    double distance;   // Distance from the center
    double speed;      // Speed at which the wall moves toward the center
    int sliceIndex;    // Slice to which the wall belongs
    int thickness = 50; // Make the wall wide enough to fill its slice completely

    public Wall(int sliceIndex, double distance, double speed) {
        this.sliceIndex = sliceIndex; // Assign the wall to a slice
        this.distance = distance;
        this.speed = speed;

        // Calculate start and end angles based on slice
        double sliceStart = sliceIndex * Math.PI / 3; // Starting angle of the slice
        double sliceEnd = sliceStart + Math.PI / 3;  // Ending angle of the slice

        // Make the wall fit within its slice
        startAngle = sliceStart + (Math.PI / 50) + 1;  // Slight offset for visibility
        endAngle = sliceEnd - (Math.PI / 50) - 1;     // Reduce overlap with neighboring slices
    }

    public void update(double rotationAngle) {
        distance -= speed; // Move the wall closer to the center
    }

    public void draw(Graphics2D g2, int centerX, int centerY) {
        g2.setColor(Color.WHITE);

        // Calculate polygon points for the wall
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

    public boolean checkCollision(double playerAngle, int playerRadius) {
        return distance <= playerRadius + 10 && distance >= playerRadius - 10 &&
                playerAngle >= startAngle && playerAngle <= endAngle;
    }
}