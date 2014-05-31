package jewels_star.solver;

import java.awt.*;

/**
 * User: vishal
 * Date: 5/31/14
 * Time: 2:24 PM
 */
public class Cell {

    public Cell(int x, int y) {
        this.point = new Point(x, y);
    }

    public Cell(Point p) {
        this.point = p;
    }

    Point point;
}
