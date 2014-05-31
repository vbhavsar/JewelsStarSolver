package jewels_star.solver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * User: vishal
 * Date: 5/31/14
 * Time: 12:36 PM
 */
public class Solver {

    private Robot r;
    private Point startingPoint;

    public Solver() throws AWTException, IOException {
        init();
    }

    private void init() throws IOException, AWTException {
        r = new Robot();
        startingPoint = MouseInfo.getPointerInfo().getLocation();
    }

    private void saveScreenshot() throws IOException {
        final BufferedImage screenCapture = screenshot();
        File gameSnap = new File("c:\\temp\\jewel.png");
        ImageIO.write(screenCapture, "png", gameSnap);
        System.out.printf("Screenshot saved to %s\n", gameSnap.getAbsolutePath());
    }

    private BufferedImage screenshot() {
        Rectangle gameRect = new Rectangle(startingPoint.x, startingPoint.y, Constants.CELL_WIDTH*Constants.CELL_COLS+Constants.BUFFER, Constants.CELL_HEIGHT*Constants.CELL_ROWS+Constants.BUFFER);
        return r.createScreenCapture(gameRect);
    }

    public static void main(String[] args) throws AWTException, IOException {
        Solver s = new Solver();
        System.out.println("Click the top left cell");
        s.r.delay(1000);
        s.saveScreenshot();
        s.solve();
        System.out.println("thanks. bye");
    }

    private void moveToCell(Cell c) {
        moveToCell(c.point.x, c.point.y);
    }

    private void moveToCell(int row, int col) {
        System.out.printf("Moving to [%d,%d]\n", row, col);
        r.mouseMove(startingPoint.x + Constants.CELL_WIDTH * (col + 1) - Constants.CELL_WIDTH / 2, startingPoint.y + Constants.CELL_HEIGHT * (row + 1) - Constants.CELL_HEIGHT / 2);
    }

    private void solve() {
        // analyze the board by taking a screenshot and processing it

        // move some cells around
        Cell c1 = new Cell(0, 6);
        Cell c2 = new Cell(1, 6);
        swap(c1, c2);

    }

    private void swap(Cell c1, Cell c2) {
        moveToCell(c1);
        this.r.mousePress(InputEvent.BUTTON3_MASK);
        this.r.delay(4000);
        moveToCell(c2);
        this.r.mouseRelease(InputEvent.BUTTON3_MASK);
    }
}
