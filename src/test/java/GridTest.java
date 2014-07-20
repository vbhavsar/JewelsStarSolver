import jewels_star.util.MathUtils;
import org.junit.Test;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User: vishal
 * Date: 7/1/14
 * Time: 7:12 AM
 */
public class GridTest {

    /**
     * Confirm that the samples being collected
     * are accurate
     */
    @Test
    public void cellSamplesTest() {

        final Point center = new Point(50, 50);
        final double hyp = 25d;
        final Set<String> offsetSet = new HashSet<String>();
        for (int degree = 0; degree < 360; degree+=5) {
            final Point offset = MathUtils.getOffset(center, degree, hyp);
            offsetSet.add(String.format("%d:%d",offset.x, offset.y));
        }

        drawGrid(offsetSet);
    }

    private void drawGrid(Set<String> points) {

        final StringBuilder builder = new StringBuilder();

        for (int r=0; r<100; r++) {
//            builder.append("[");
            for (int c=0; c<100; c++) {

                final String rcStr = String.format("%d:%d", r, c);
                if (points.contains(rcStr)) {
                    builder.append("+");
                } else if (r == 50 && c == 50) {
                    builder.append("=");
                } else {
                    builder.append(" ");
                }

            }
            builder.append("\n");
        }
        System.out.println(builder.toString());
    }
}
