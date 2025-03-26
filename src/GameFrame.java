import javax.swing.*;

public class GameFrame extends JFrame {

    GamePanel gamePanel;
    GameFrame(){

        gamePanel = new GamePanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Super Hexagon");
        this.add(gamePanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        gamePanel.startGameThread();

    }
}
