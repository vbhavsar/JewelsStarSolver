package ml;

import jewels_star.solver.Grid;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * User: vishal
 * Date: 7/4/14
 * Time: 10:49 AM
 */
public class ClusteringEffectiveness {

    private final static String JEWEL_IMG_PATH = "resources/samples/jewel.png";

    @Test
    public void clusterImage() throws IOException {

        final BufferedImage image = ImageIO.read(new File(JEWEL_IMG_PATH));
        Grid grid = new Grid(image, 0);

        grid.classify();

        grid.printClasses();

//        KMeans.classifyBestFit(grid.getCells(), 6);

//        Assert.assertEquals(7, classifiedMap.keySet().size());
    }
}
