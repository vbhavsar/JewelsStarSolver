package util;

import jewels_star.util.MathUtils;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

/**
 * User: vishal
 * Date: 7/4/14
 * Time: 7:34 PM
 */
public class MathUtilsTest {

    @Test
    public void getOffsetTest() {

        final Point center = new Point(50, 50);
        final double magnitude = 25d;

        final Point offset1 = MathUtils.getOffset(center, 0, magnitude);
        assertEquals(75, offset1.x);
        assertEquals(50, offset1.y);

        final Point offset2 = MathUtils.getOffset(center, 90, magnitude);
        assertEquals(50, offset2.x);
        assertEquals(25, offset2.y);

        final Point offset3 = MathUtils.getOffset(center, 180, magnitude);
        assertEquals(25, offset3.x);
        assertEquals(50, offset3.y);

        final Point offset4 = MathUtils.getOffset(center, 270, magnitude);
        assertEquals(50, offset4.x);
        assertEquals(75, offset4.y);

    }

}
