package jewels_star.util;

import java.awt.*;

/**
 * User: vishal
 * Date: 7/4/14
 * Time: 7:02 PM
 */
public class MathUtils {

    public static Point getOffset(final Point center, final double degrees, final double hypotenuse) {

        final double rad = Math.toRadians(degrees);
        final double x_offset = Math.cos(rad) * hypotenuse;
        final double y_offset = Math.sin(rad) * hypotenuse;

        final Point offset = new Point();

        offset.y = (int) (center.y - y_offset);
        offset.x = (int) (center.x + x_offset);

        return offset;
    }
}
