package jewels_star.solver;

import jewels_star.ml.KMeans;
import jewels_star.util.MathUtils;
import org.springframework.util.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.util.Map;

import static jewels_star.solver.Constants.*;

/**
 * User: vishal
 * Date: 6/25/14
 * Time: 6:40 AM
 */
public class Grid {

    private final BufferedImage image;
    private Cell[][] cells;

    /**
     * Number of numSamples for a cell
     */
    private int numSamples = 0;

    public Grid(BufferedImage image, int numSamples) {
        this.numSamples = numSamples;
        this.image = image;
        cells = new Cell[CELL_ROWS][CELL_COLS];
        init();
    }

    /**
     * Purely for test purposes
     * @param cells
     */
    public Grid(Cell[][] cells) {
        this.cells = cells;
        this.image = null;
    }

    public void init() {

        for (int row=0; row<Constants.CELL_ROWS; row++) {
            for (int col=0; col<Constants.CELL_COLS; col++) {

                Rectangle rect = new Rectangle(col*CELL_WIDTH, row*CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
                final Raster raster = this.image.getData(rect);

                final SampleModel sampleModel = raster.getSampleModel();
                Cell cell = new Cell(row ,col);
                cell.setSampleModel(sampleModel);

                final double[] pixel = new double[raster.getNumBands()];
                final int center_x = (col * CELL_WIDTH) + CELL_WIDTH / 2;
                final int center_y = (row*CELL_HEIGHT) + CELL_HEIGHT / 2;
                raster.getPixel(center_x, center_y, pixel);

                int[] rgb = new int[3];
                rgb[0] = new Double(pixel[0]).intValue();
                rgb[1] = new Double(pixel[1]).intValue();
                rgb[2] = new Double(pixel[2]).intValue();

                cell.setRGB(rgb);

                cells[row][col] = cell;

                // Collect samples
                if (numSamples > 0) {
                    int[][] samples = new int[numSamples][3];
                    final int degrees = (360 / (numSamples));

                    final Point center = new Point(center_x, center_y);

                    final double width = CELL_WIDTH/4; // 4 because we want half the distance between center and edge
                    final double height = CELL_HEIGHT/4; // see comment above

                    double magnitude = 15; // Math.sqrt( width*width+height*height  );

                    for (int i=0; i<numSamples; i++) {

                        // Get rgb of the ith sample
                        final Point offset = MathUtils.getOffset(center, i * degrees, magnitude);
                        final int offset_x = offset.x;
                        final int offset_y = offset.y;
                        raster.getPixel(offset_x, offset_y, pixel);

                        samples[i][0] = new Double(pixel[0]).intValue();
                        samples[i][1] = new Double(pixel[1]).intValue();
                        samples[i][2] = new Double(pixel[2]).intValue();

                    }

                    cells[row][col].setSamples(samples);

                }
            }
        }
    }

    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        for (int i=0; i<Constants.CELL_ROWS; i++) {
            builder.append("[");
            for (int j=0; j<Constants.CELL_COLS; j++) {
                final int[] rgb = this.cells[i][j].getRgb();
                builder.append(this.cells[i][j].getRgbHex());
                builder.append(", ");
            }
            builder.setLength(builder.length() - 2);
            builder.append("]\n");
        }
        return builder.toString();
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    public void classify() {
        final KMeans kmeans = new KMeans(this);
        kmeans.classify(8);
        final Map<String,Integer> rgbToClassMap = kmeans.getRgbToClassMap();

        for (int row=0; row<Constants.CELL_ROWS; row++) {
            for (int col=0; col<Constants.CELL_COLS; col++) {

                final String rgbHex = cells[row][col].getRgbHex();
                final Integer rgbClass = rgbToClassMap.get(rgbHex);

                Assert.notNull(rgbClass, String.format("%s does not have a corresponding class!", rgbHex));

                cells[row][col].setClazz(rgbClass);

                cells[row][col].determineClazz(rgbToClassMap);
            }
        }
    }

    public Cell[] findMoves() {

        final Cell[] verticalMoves, horizontalMoves;

        // introduce randomness
        if ((int)(Math.random()*10)%2==0) {
            return (verticalMoves = findVerticalMoves()) == null ? findHorizontalMoves() : verticalMoves;
        } else {
            return (horizontalMoves = findHorizontalMoves()) == null ? findVerticalMoves() : horizontalMoves;
        }
    }

    private Cell[] findHorizontalMoves() {
        for (int row=0; row< Constants.CELL_ROWS-2; row++) {
            for (int col=0; col<Constants.CELL_COLS; col++) {

                Cell c1 = cells[row][col];
                Cell c2 = cells[row+1][col];
                Cell c3 = cells[row+2][col];

                if (c2.getClazz() == c3.getClazz()) {

                    Cell[] neighbors = new Cell[4];
                    neighbors[0] = getCell(c1, Direction.NORTH);
                    neighbors[1] = getCell(c1, Direction.SOUTH);
                    neighbors[2] = getCell(c1, Direction.EAST);
                    neighbors[3] = getCell(c1, Direction.WEST);

                    for (int i=0; i<4; i++) {
                        if (neighbors[i] != null && neighbors[i] != c2 && neighbors[i] != c3 && neighbors[i].getClazz() == c2.getClazz()) {
                            return new Cell[]{neighbors[i], c1};
                        }
                    }
                }

                if (c1.getClazz() == c3.getClazz()) {
                    Cell[] neighbors = new Cell[4];
                    neighbors[0] = getCell(c2, Direction.NORTH);
                    neighbors[1] = getCell(c2, Direction.SOUTH);
                    neighbors[2] = getCell(c2, Direction.EAST);
                    neighbors[3] = getCell(c2, Direction.WEST);

                    for (int i=0; i<4; i++) {
                        if (neighbors[i] != null && neighbors[i] != c1 && neighbors[i] != c3 && neighbors[i].getClazz() == c1.getClazz()) {
                            return new Cell[]{neighbors[i], c2};
                        }
                    }
                }

                if (c1.getClazz() == c2.getClazz()) {
                    Cell[] neighbors = new Cell[4];
                    neighbors[0] = getCell(c3, Direction.NORTH);
                    neighbors[1] = getCell(c3, Direction.SOUTH);
                    neighbors[2] = getCell(c3, Direction.EAST);
                    neighbors[3] = getCell(c3, Direction.WEST);

                    for (int i=0; i<4; i++) {
                        if (neighbors[i] != null && neighbors[i] != c1 && neighbors[i] != c2 && neighbors[i].getClazz() == c2.getClazz()) {
                            return new Cell[]{neighbors[i], c3};
                        }
                    }
                }
            }
        }

        return null;
    }

    private Cell[] findVerticalMoves() {
        for (int row=0; row< Constants.CELL_ROWS; row++) {
            for (int col=0; col<Constants.CELL_COLS-2; col++) {

                Cell c1 = cells[row][col];
                Cell c2 = cells[row][col+1];
                Cell c3 = cells[row][col+2];

                if (c2.getClazz() == c3.getClazz()) {

                    Cell[] neighbors = new Cell[4];
                    neighbors[0] = getCell(c1, Direction.NORTH);
                    neighbors[1] = getCell(c1, Direction.SOUTH);
                    neighbors[2] = getCell(c1, Direction.EAST);
                    neighbors[3] = getCell(c1, Direction.WEST);

                    for (int i=0; i<4; i++) {
                        if (neighbors[i] != null && neighbors[i] != c2 && neighbors[i] != c3 && neighbors[i].getClazz() == c2.getClazz()) {
                            return new Cell[]{neighbors[i], c1};
                        }
                    }
                }

                if (c1.getClazz() == c3.getClazz()) {
                    Cell[] neighbors = new Cell[4];
                    neighbors[0] = getCell(c2, Direction.NORTH);
                    neighbors[1] = getCell(c2, Direction.SOUTH);
                    neighbors[2] = getCell(c2, Direction.EAST);
                    neighbors[3] = getCell(c2, Direction.WEST);

                    for (int i=0; i<4; i++) {
                        if (neighbors[i] != null && neighbors[i] != c1 && neighbors[i] != c3 && neighbors[i].getClazz() == c1.getClazz()) {
                            return new Cell[]{neighbors[i], c2};
                        }
                    }
                }

                if (c1.getClazz() == c2.getClazz()) {
                    Cell[] neighbors = new Cell[4];
                    neighbors[0] = getCell(c3, Direction.NORTH);
                    neighbors[1] = getCell(c3, Direction.SOUTH);
                    neighbors[2] = getCell(c3, Direction.EAST);
                    neighbors[3] = getCell(c3, Direction.WEST);

                    for (int i=0; i<4; i++) {
                        if (neighbors[i] != null && neighbors[i] != c1 && neighbors[i] != c2 && neighbors[i].getClazz() == c2.getClazz()) {
                            return new Cell[]{neighbors[i], c3};
                        }
                    }
                }
            }
        }

        return null;
    }

    private Cell getCell(Cell cell, Direction direction) {
        final Point point = cell.point;
        int x, y;
        switch(direction) {
            case NORTH:
                x = point.x-1;
                if (x>=0) {
                    return this.cells[x][point.y];
                }
                break;
            case SOUTH:
                x = point.x+1;
                if (x<CELL_ROWS) {
                    return this.cells[x][point.y];
                }
                break;
            case WEST:
                y = point.y-1;
                if (y>=0) {
                    return this.cells[point.x][y];
                }
                break;
            case EAST:
                y = point.y+1;
                if (y<CELL_COLS) {
                    return this.cells[point.x][y];
                }
                break;
        }
        return null;
    }

    public void printClasses() {

        for (int row=0; row<Constants.CELL_ROWS; row++) {
            final StringBuilder builder = new StringBuilder();
            builder.append("[");
            for (int col=0; col<Constants.CELL_COLS; col++) {
                builder.append(String.format("%d ", cells[row][col].getClazz()));
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append("]\n");
            System.out.print(builder);
        }

    }
}
