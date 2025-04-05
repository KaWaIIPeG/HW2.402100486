import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;

public class UI {
    Font font;
    GamePanel gp;
    Graphics2D g2;
    double playTime;
    public int scrollOffset = 0;
    DecimalFormat dFormat = new DecimalFormat("#0.00");
    public JTextField textField;
    public JButton button1,button2,button3,button4,button5;
    public JRadioButton musicOn,musicOff,saveOn,saveOff;
    public ButtonGroup saveGroup,musicGroup;
    public boolean musicEnabled = true;
    public boolean saveEnabled = true;
    String BestFormattedTime = "0";
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
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
        if (gp.gameState == gp.gameOverState){
            drawEndScreen();
        }
        if (gp.gameState == gp.nameState){
            drawNameScreen();
        }
        if (gp.gameState == gp.runsState){
            drawRunsScreen();
        }
        if (gp.gameState == gp.settingState){
            drawSettingScreen();
        }
        if (gp.gameState == gp.playState){
            playTime += (double)1/60;
            g2.setColor(Color.yellow);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString("Time:" +dFormat.format(playTime),0,30);
            g2.drawString("Best Run: "+ BestFormattedTime , 0,80);
        }
    }
    private void drawSettingScreen() {
        gp.setLayout(null);

        musicOn = new JRadioButton("Music: ON");
        musicOn.setFont(new Font("Arial", Font.BOLD, 16));
        musicOn.setBackground(Color.BLACK);
        musicOn.setForeground(Color.WHITE);
        musicOn.setFocusable(false);
        musicOn.setBounds(50, 50, 200, 30);

        musicOff = new JRadioButton("Music: OFF");
        musicOff.setFont(new Font("Arial", Font.BOLD, 16));
        musicOff.setBackground(Color.BLACK);
        musicOff.setForeground(Color.WHITE);
        musicOff.setFocusable(false);
        musicOff.setBounds(50, 90, 200, 30);

        if (musicEnabled) {
            musicOn.setSelected(true);
        } else {
            musicOff.setSelected(true);
        }

        musicOn.addActionListener(e -> this.musicEnabled = true);
        musicOff.addActionListener(e -> this.musicEnabled = false);

        musicGroup = new ButtonGroup();
        musicGroup.add(musicOn);
        musicGroup.add(musicOff);

        saveOn = new JRadioButton("Save Runs: ON");
        saveOn.setFont(new Font("Arial", Font.BOLD, 16));
        saveOn.setBackground(Color.BLACK);
        saveOn.setForeground(Color.WHITE);
        saveOn.setFocusable(false);
        saveOn.setBounds(50, 150, 200, 30);

        saveOff = new JRadioButton("Save Runs: OFF");
        saveOff.setFont(new Font("Arial", Font.BOLD, 16));
        saveOff.setBackground(Color.BLACK);
        saveOff.setForeground(Color.WHITE);
        saveOff.setFocusable(false);
        saveOff.setBounds(50, 190, 200, 30);

        if (saveEnabled) {
            saveOn.setSelected(true);
        } else {
            saveOff.setSelected(true);
        }

        saveOn.addActionListener(e -> this.saveEnabled = true);
        saveOff.addActionListener(e -> this.saveEnabled = false);

        saveGroup = new ButtonGroup();
        saveGroup.add(saveOn);
        saveGroup.add(saveOff);

        gp.add(musicOn);
        gp.add(musicOff);
        gp.add(saveOn);
        gp.add(saveOff);

        JLabel instructionLabel = new JLabel("Press 'ESC' to go back.");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        instructionLabel.setForeground(Color.GRAY);
        instructionLabel.setBounds(12, gp.getHeight() - 40, 300, 20);
        gp.add(instructionLabel);

        gp.revalidate();
        gp.repaint();
    }
    public void drawRunsScreen() {
        try {
            ObjectMapper objMapper = new ObjectMapper();
            File file = new File("C:\\Users\\GS\\HW2.402100486\\src\\players.json");

            if (!file.exists() || file.length() == 0) {
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.setColor(Color.RED);
                g2.drawString("No player data found.", 100, 100);
                return;
            }

            List<Runs> players = objMapper.readValue(file, new TypeReference<List<Runs>>() {});

            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2.setColor(Color.WHITE);

            int frameWidth = gp.getWidth();
            int yPosition = 20 - scrollOffset;
            FontMetrics metrics = g2.getFontMetrics();
            int lineHeight = metrics.getHeight();

            for (Runs run : players) {
                String formattedTime = dFormat.format(run.getTime());
                String playerData = "Name: " + run.getName() + ", Time: " + formattedTime + ", Date: " + run.getDate();
                List<String> lines = wrapText(playerData, metrics, frameWidth - 20);
                for (String line : lines) {
                    if (yPosition > 0 && yPosition < gp.getHeight()) {
                        g2.drawString(line, 10, yPosition);
                    }
                    yPosition += lineHeight;
                }
                yPosition += 10;
            }

            g2.setFont(new Font("Arial", Font.ITALIC, 14));
            g2.setColor(Color.GRAY);
            g2.drawString("Use W/S keys to scroll or 'ESC' to go back.", 12, gp.getHeight() - 10);

        } catch (Exception e) {
            e.printStackTrace();
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(Color.RED);
            g2.drawString("An error occurred while displaying player data.", 100, 100);
        }
    }

    private List<String> wrapText(String text, FontMetrics metrics, int maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            if (metrics.stringWidth(currentLine.toString() + word) > maxWidth) {
                lines.add(currentLine.toString().trim());
                currentLine = new StringBuilder(word + " ");
            } else {
                currentLine.append(word).append(" ");
            }
        }

        if (!currentLine.toString().isEmpty()) {
            lines.add(currentLine.toString().trim());
        }
        return lines;
    }
    private void drawNameScreen() {
        if (textField == null) {
            gp.setLayout(new BoxLayout(gp, BoxLayout.Y_AXIS));

            JLabel nameLabel = new JLabel("Please enter your name:");
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            textField = new JTextField();
            textField.setPreferredSize(new Dimension(250, 40));
            textField.setForeground(Color.RED);
            textField.setBackground(Color.GREEN);
            textField.setMaximumSize(new Dimension(250, 40));
            textField.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel errorLabel = new JLabel("");
            errorLabel.setFont(new Font("Arial", Font.BOLD, 14));
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            button1 = new JButton();
            button1.setAlignmentX(Component.CENTER_ALIGNMENT);
            button1.setText("Start");
            button1.setFocusable(false);
            button1.addActionListener(e -> {
                String playerName = textField.getText().trim();

                if (playerName.isEmpty()) {
                    errorLabel.setText("The Name Cannot Be Empty.");
                } else {
                    gp.run.setName(playerName);

                    gp.gameState = gp.playState;
                    if (musicEnabled){
                        gp.playMusic(0);
                    }
                    gp.remove(nameLabel);
                    gp.remove(textField);
                    gp.remove(button1);
                    gp.remove(errorLabel);
                    textField = null;
                    gp.requestFocus();
                }

                gp.revalidate();
                gp.repaint();
            });

            gp.removeAll();
            gp.add(Box.createVerticalGlue());
            gp.add(nameLabel);
            gp.add(Box.createVerticalStrut(10));
            gp.add(textField);
            gp.add(Box.createVerticalStrut(10));
            gp.add(errorLabel);
            gp.add(Box.createVerticalStrut(20));
            gp.add(button1);
            gp.add(Box.createVerticalGlue());
            textField.requestFocusInWindow();

            gp.revalidate();
            gp.repaint();
        }
        g2.setFont(new Font("Arial", Font.ITALIC, 14));
        g2.setColor(Color.GRAY);
        g2.drawString("Use A/D keys to move the cursor and use 'ESC' anytime to pause the game.", 12, gp.getHeight() - 10);
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

        button2 = new JButton();
        button2.setBounds(gp.centerX - 80,gp.centerY - 100,150,40);
        button2.setText("New Game");
        button2.setFocusable(false);
        gp.add(button2);
        button2.addActionListener(e -> {
            gp.gameState = gp.nameState;
            gp.removeAll();
            gp.revalidate();
            gp.repaint();
        });

        button3 = new JButton();
        button3.setBounds(gp.centerX - 80,gp.centerY - 50,150,40);
        button3.setText("Previous Runs");
        button3.setFocusable(false);
        gp.add(button3);
        button3.addActionListener(e -> {
            gp.gameState = gp.runsState;
            gp.removeAll();
            gp.revalidate();
            gp.repaint();
        });

        button4 = new JButton();
        button4.setBounds(gp.centerX - 80,gp.centerY ,150,40);
        button4.setText("Settings");
        button4.setFocusable(false);
        gp.add(button4);
        button4.addActionListener(e -> {
            gp.gameState = gp.settingState;
            gp.removeAll();
            gp.revalidate();
            gp.repaint();
        });

        button5 = new JButton();
        button5.setBounds(gp.centerX - 80,gp.centerY + 50,150,40);
        button5.setText("Quit");
        button5.setFocusable(false);
        gp.add(button5);
        button5.addActionListener(e -> System.exit(0));

        try {
            ObjectMapper objMapper = new ObjectMapper();
            File file = new File("C:\\Users\\GS\\HW2.402100486\\src\\players.json");

            if (!file.exists() || file.length() == 0) {
                text = "Best run: 0";
                g2.setFont(g2.getFont().deriveFont(Font.BOLD,18));
                g2.setColor(Color.GRAY);
                g2.drawString(text,gp.centerX - 220,gp.centerY + 135);
                return;
            }
            List<Runs> players = objMapper.readValue(file, new TypeReference<List<Runs>>() {});

            Runs bestPlayer = null;
            for (Runs player : players) {
                if (bestPlayer == null || player.getTime() > bestPlayer.getTime()) {
                    bestPlayer = player;
                }
            }

            if (bestPlayer != null) {
                BestFormattedTime = dFormat.format(bestPlayer.getTime());
                text = "Best run: " + BestFormattedTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(Color.RED);
            g2.drawString("An error occurred while displaying the best time.", 100, 100);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,18));
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