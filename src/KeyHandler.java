import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean leftPressed, rightPressed;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (gp.gameState == gp.runsState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.scrollOffset = Math.max(0, gp.ui.scrollOffset - 20);
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.scrollOffset += 20;
            }
            gp.repaint();
        }


        if (code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            if (gp.gameState != gp.titleState && gp.gameState != gp.gameOverState) {
                if (gp.gameState == gp.playState) {
                    gp.setGameState(gp.pauseState);
                } else if (gp.gameState == gp.pauseState){
                    gp.setGameState(gp.playState);
                } else if (gp.gameState == gp.runsState){
                    gp.gameState = gp.titleState;
                } else if (gp.gameState == gp.settingState) {
                    gp.removeAll();
                    gp.revalidate();
                    gp.repaint();
                    gp.gameState = gp.titleState;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = false;

        }
    }
}
