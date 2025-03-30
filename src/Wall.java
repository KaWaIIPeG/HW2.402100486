import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Wall {
    double distance;
    double speed;
    int thickness = 50;
    public int centerX = 250;
    public int centerY = 250;
    int wallSpawnTimer = 0;
    GamePanel gp;
    int x1Start;
    int y1Start;
    int x2Start;
    int y2Start;

    int x1End;
    int y1End;
    int x2End;
    int y2End;
    List<Integer> wallSlices;

    public Wall(GamePanel gp){
        this.gp = gp;
    }

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
    public Wall(double distance, double speed, int emptySliceIndex1, int emptySliceIndex2, int emptySliceIndex3) {
        this.distance = distance;
        this.speed = speed;

        if (emptySliceIndex2 >= 6) {
            emptySliceIndex2 -= 6;
        }
        if (emptySliceIndex3 >= 6) {
            emptySliceIndex3 -= 6;
        }

        wallSlices = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (i != emptySliceIndex1 && i != emptySliceIndex2 && i != emptySliceIndex3) {
                wallSlices.add(i);
            }
        }
    }
    public Wall(double distance, double speed, int emptySliceIndex1, int emptySliceIndex2) {
        this.distance = distance;
        this.speed = speed;

        if (emptySliceIndex2 >= 6) {
            emptySliceIndex2 -= 6;
        }

        wallSlices = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (i != emptySliceIndex1 && i != emptySliceIndex2) {
                wallSlices.add(i);
            }
        }
    }

    public void spawnWalls(double speed) {
        this.speed = speed;
        wallSpawnTimer++;

        if (wallSpawnTimer > 100) {
            int thePattern = (int) (Math.random() * 6);

            if (thePattern == 0 || thePattern == 1 || thePattern == 2) {
                int emptySliceIndex = (int) (Math.random() * 6);
                gp.walls.add(new Wall(500, speed, emptySliceIndex));
            }
            else if (thePattern == 3) {

                int emptySliceIndex1 = (int) (Math.random() * 6);
                int emptySliceIndex2 = (emptySliceIndex1 + 2) % 6;
                int emptySliceIndex3 = (emptySliceIndex1 + 4) % 6;
                gp.walls.add(new Wall(500, speed, emptySliceIndex1, emptySliceIndex2, emptySliceIndex3));
            }
            else if (thePattern == 4) {

                int emptySliceIndex1 = (int) (Math.random() * 6);
                int emptySliceIndex2 = (emptySliceIndex1 + 3) % 6;
                gp.walls.add(new Wall(500, speed, emptySliceIndex1, emptySliceIndex2));
            }
            else if (thePattern == 5) {

                int emptySliceIndex1 = (int) (Math.random() * 6);
                int emptySliceIndex2 = (emptySliceIndex1 + 2) % 6;
                gp.walls.add(new Wall(500, speed, emptySliceIndex1, emptySliceIndex2));
            }
            wallSpawnTimer = 0;
        }
    }

    public void update(double rotationAngle) {
        distance -= speed;
    }

    public void draw(Graphics2D g2, int centerX, int centerY) {
        g2.setColor(Color.WHITE);

            for (int sliceIndex : wallSlices) {

            polygonNum(centerX,centerY,sliceIndex);

            Polygon wallPolygon = new Polygon(
                    new int[]{x1Start, x2Start, x2End, x1End},
                    new int[]{y1Start, y2Start, y2End, y1End},
                    4
            );

            g2.fillPolygon(wallPolygon);
        }
    }

    public boolean checkCollision(Polygon cursorPolygon) {
        for (int sliceIndex : wallSlices) {
            Polygon wallPolygon = getWallPolygon(sliceIndex);

            if (wallPolygon.intersects(cursorPolygon.getBounds2D())) {
                return true;
            }
        }
        return false;
    }
    public Polygon getWallPolygon(int sliceIndex) {

        polygonNum(this.centerX,this.centerY,sliceIndex);

        return new Polygon(
                new int[]{x1Start, x2Start, x2End, x1End},
                new int[]{y1Start, y2Start, y2End, y1End},
                4
        );
    }
    public void polygonNum(int centerX, int centerY,int sliceIndex){
        double startAngle = sliceIndex * Math.PI / 3;
        double endAngle = startAngle + Math.PI / 3;

        x1Start = (int) (centerX + distance * Math.cos(startAngle));
        y1Start = (int) (centerY + distance * Math.sin(startAngle));
        x2Start = (int) (centerX + distance * Math.cos(endAngle));
        y2Start = (int) (centerY + distance * Math.sin(endAngle));

        x1End = (int) (centerX + (distance + thickness) * Math.cos(startAngle));
        y1End = (int) (centerY + (distance + thickness) * Math.sin(startAngle));
        x2End = (int) (centerX + (distance + thickness) * Math.cos(endAngle));
        y2End = (int) (centerY + (distance + thickness) * Math.sin(endAngle));
    }
}