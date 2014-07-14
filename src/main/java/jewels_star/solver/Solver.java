package jewels_star.solver;

import jewels_star.ml.CannyEdgeDetector;

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
        Rectangle gameRect = new Rectangle(startingPoint.x, startingPoint.y, Constants.CELL_WIDTH*Constants.CELL_COLS, Constants.CELL_HEIGHT*Constants.CELL_ROWS);
//        Rectangle gameRect = new Rectangle(startingPoint.x, startingPoint.y, Constants.CELL_WIDTH, Constants.CELL_HEIGHT);
        return r.createScreenCapture(gameRect);
    }

    public static void main(String[] args) throws AWTException, IOException {
        Solver s = new Solver();
        System.out.println("Click the top left cell");

        for (int i=0; i<250; i++) {
            s.r.delay(500);
            s.solve();
        }
/*
        s.solve();
*/

        System.out.println("thanks. bye");
    }

    private void moveToCell(Cell c) {
        moveToCell(c.point.x, c.point.y);
    }

    private void moveToCell(int row, int col) {
        System.out.printf("Moving to [%d,%d]\n", row, col);
        r.mouseMove(startingPoint.x + Constants.CELL_WIDTH * (col + 1) - Constants.CELL_WIDTH / 2, startingPoint.y + Constants.CELL_HEIGHT * (row + 1) - Constants.CELL_HEIGHT / 2);
    }

    private void solve() throws IOException {
        saveScreenshot();

        final BufferedImage screenshot = screenshot();
        getEdges(screenshot);

        // Generate a grid from the screenshot
        Grid grid = new Grid(screenshot, 8);
        System.out.println(grid);
        // analyze the screenshot
        // calculate centers
        grid.classify();
        System.out.println("After classification");
        grid.printClasses();

        Cell[] cellsToSwap = grid.findMoves();
        if (cellsToSwap != null) {
            System.out.printf("Swapping [%d,%d] and [%d,%d]\n", cellsToSwap[0].point.x, cellsToSwap[0].point.y, cellsToSwap[1].point.x, cellsToSwap[1].point.y);
            swap(cellsToSwap[0], cellsToSwap[1]);
        } else {
            System.out.println("Found no cells to swap! :(");
        }
    }

    private void getEdges(BufferedImage screenshot) throws IOException {
        CannyEdgeDetector detector = new CannyEdgeDetector();
        detector.setLowThreshold(0.5f);
        detector.setHighThreshold(1f);
        detector.setSourceImage(screenshot());
        detector.process();
        BufferedImage edges = detector.getEdgesImage();

        File edgeSnap = new File("c:\\temp\\jewel-edge.png");
        ImageIO.write(edges, "png", edgeSnap);
        System.out.printf("Edge screenshot saved to %s\n", edgeSnap.getAbsolutePath());

    }

    private void swap(Cell c1, Cell c2) {
        moveToCell(c1);
        this.r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this.r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//        this.r.delay(1000);
        moveToCell(c2);
        this.r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this.r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
}
