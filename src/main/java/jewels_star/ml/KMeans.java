package jewels_star.ml;

import jewels_star.solver.Cell;
import jewels_star.solver.Grid;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import java.util.*;

import static jewels_star.solver.Constants.*;

/**
 * User: vishal
 * Date: 6/29/14
 * Time: 3:02 PM
 */
public class KMeans {

    private final Grid grid;

    public KMeans(final Grid grid) {
        this.grid = grid;
    }

    public static Map<String, Integer> classify(Cell[][] cells, int k) {

        final Collection<Instance> data = new ArrayList<Instance>();

        for (int row=0; row<cells.length; row++) {
            for (int col=0; col<cells[row].length; col++) {
                final Cell cell = cells[row][col];
                final int[] rgb = cell.getRgb();

                DenseInstance inst = new DenseInstance(3);
                inst.put(R, Double.valueOf(rgb[0]));
                inst.put(G, Double.valueOf(rgb[1]));
                inst.put(B, Double.valueOf(rgb[2]));

                data.add(inst);

                // Add samples
                if (cell.getSamples() != null) {

                    for (int[] sample : cell.getSamples()) {
                        final DenseInstance sampleInstance = new DenseInstance(3);
                        sampleInstance.put(R, Double.valueOf(sample[0]));
                        sampleInstance.put(G, Double.valueOf(sample[1]));
                        sampleInstance.put(B, Double.valueOf(sample[2]));

                        data.add(sampleInstance);
                    }
                }
            }
        }

        net.sf.javaml.clustering.KMeans kmeans = new net.sf.javaml.clustering.KMeans(k, 100);
        Dataset dataset = new DefaultDataset(data);
        final Dataset[] clusters = kmeans.cluster(dataset);

        Map<String, Integer> rgbToClassMap = new HashMap<String, Integer>();

        for (int icluster = 0; icluster < clusters.length; icluster++) {
            final Iterator<Instance> iterator = clusters[icluster].iterator();
            while(iterator.hasNext()) {
                final Instance inst = iterator.next();
                int r = inst.get(R).intValue();
                int g = inst.get(G).intValue();
                int b = inst.get(B).intValue();

                final String rgb = Cell.getRGB(r, g, b);
                rgbToClassMap.put(rgb, icluster);
            }
        }

        return rgbToClassMap;
    }

    public static Map<String, Integer> classifyBestFit(Cell[][] cells, int minK) {
        return null;
    }
}
