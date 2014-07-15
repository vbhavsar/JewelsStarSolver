import jewels_star.solver.Cell;
import jewels_star.solver.Grid;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static jewels_star.solver.Constants.CELL_COLS;
import static jewels_star.solver.Constants.CELL_ROWS;
import static junit.framework.Assert.assertNotNull;

/**
 * User: vishal
 * Date: 7/14/14
 * Time: 10:45 PM
 */
public class SolverTest {

    private static final String SAMPLE_GRID_FILE_PATH = "resources/samples/grid.txt";

    @Test
    public void findMovesTest() throws FileNotFoundException {

        final Cell[][] cells = new Cell[CELL_ROWS][CELL_COLS];
        final Scanner sc = new Scanner(new File(SAMPLE_GRID_FILE_PATH));

        for (int r=0; r<CELL_ROWS; r++) {
            for (int c=0; c<CELL_COLS; c++) {
                cells[r][c] = new Cell(r, c);
                cells[r][c].setClazz(sc.nextInt());
            }
        }

        final Grid grid = new Grid(cells);
        final Cell[] moves = grid.findMoves();

        assertNotNull("Moves should not be null", moves);
    }
}
