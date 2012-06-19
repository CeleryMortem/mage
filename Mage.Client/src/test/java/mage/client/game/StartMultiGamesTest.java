package mage.client.game;

import javax.swing.SwingUtilities;
import mage.client.MageFrame;
import mage.client.components.MageComponents;
import mage.client.components.MageUI;
import org.apache.log4j.Logger;
import org.junit.Ignore;

/**
 * @author ayratn
 */
@Ignore
public class StartMultiGamesTest {

    private final static Logger logger = Logger.getLogger(StartMultiGamesTest.class);

    /**
     * Amount of games to be started from this test.
     */
    private final static Integer GAME_START_COUNT = 10;

    private MageFrame frame = null;
    private Object sync = new Object();
    private MageUI ui;

    public static void main(String[] argv) throws Exception {
        new StartMultiGamesTest().startMultiGames();
    }

    //@Test
    public void testEmpty() {

    }

    public void startMultiGames() throws Exception {
        for (int i = 0; i < GAME_START_COUNT; i++) {
            logger.info("Starting game");
            startGame();
        }
    }

    private void startGame() throws Exception {
        frame = null;
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                logger.fatal(null, e);
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                synchronized (sync) {
                    frame = new MageFrame();
                    frame.setVisible(true);
                    sync.notify();
                }
            }
        });
        synchronized (sync) {
            if (frame == null) {
                sync.wait();
            }
            ui = MageFrame.getUI();
            ui.doClick(MageComponents.TABLES_MENU_BUTTON);
            ui.doClick(MageComponents.NEW_GAME_BUTTON);
            ui.doClick(MageComponents.NEW_TABLE_OK_BUTTON, 500);
            ui.doClick(MageComponents.TABLE_WAITING_START_BUTTON);
        }

        sleep(3000);
        frame.setVisible(false);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
