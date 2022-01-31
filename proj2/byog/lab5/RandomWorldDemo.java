package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;

import javax.sound.sampled.LineEvent;
import java.util.Random;

/**
 * Draws a world that contains RANDOM tiles.
 */
public class RandomWorldDemo {
    private static final int WIDTH = 29;
    private static final int HEIGHT = 30;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Fills the given 2D array of tiles with RANDOM tiles.
     * @param tiles
     */
    public static void fillWithRandomTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = randomTile();
            }
        }
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(7);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.FLOWER;
            case 3: return Tileset.TREE;
            case 4: return Tileset.FLOOR;
            case 5: return Tileset.GRASS;
            case 6: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }
    private static Integer endLine;

    private static void fillWithHexagonsInit(TETile[][] hexagonTiles, Integer Line) {
        endLine = Line;
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                hexagonTiles[x][y] = Tileset.NOTHING;
            }
        }
        // Set the initial spot.
        Integer initX = 4 * Line - 1;
        Integer initY = Line;
        fillWithHexagons(hexagonTiles, initX, initY);
    }
    
    private static void fillWithHexagons(TETile[][] hexagonTiles, Integer initX, Integer initY) {
        Integer totalLine = endLine * 3 - 2;
        if (initY + 2 * endLine > HEIGHT && ((initX < 2 * endLine - 1) || (initX + totalLine + 2 * endLine - 1 > WIDTH)))
            return;
        
        createHexagon(hexagonTiles, initX, initY, totalLine);
        // Set the next hexagon in recursion.
        
        if (hexagonTiles[initX - 1][initY] == Tileset.NOTHING && initX >= 2 * endLine - 1 && initY + 2 * endLine <= HEIGHT) {
            fillWithHexagons(hexagonTiles, initX - 2 * endLine + 1, initY + 3);
        }
        
        
         if (hexagonTiles[initX + totalLine][initY] == Tileset.NOTHING && initX + totalLine + 2 * endLine - 1 <= WIDTH - 1 && initY + 2 * endLine <= HEIGHT) {
            fillWithHexagons(hexagonTiles, initX + endLine * 2 - 1, initY + endLine);
        }
        
    }
    
    private static void createHexagon(TETile[][] hexagonTiles, Integer initX, Integer y, Integer lineNum) {
        Integer tmpNum = lineNum;
        Integer x = initX;
        TETile newSet = randomTile();
        while (tmpNum-- != 0) { // Set the middle.
            hexagonTiles[x][y] = newSet;
            hexagonTiles[x++][y - 1] = newSet;
        }
        createHexagon2(hexagonTiles,initX + 1, y + 1, lineNum - 2, false);  // Set the upline in recursion.
        createHexagon2(hexagonTiles, initX + 1, y -2,lineNum - 2, true);    // Set the downline in recursion.
    }
    
    private static void createHexagon2(TETile[][] hexagonTiles, Integer initX, Integer y, Integer lineNum, boolean upOrDown) {
        if (lineNum < endLine) return;
        Integer tmpNum = lineNum;
        Integer x = initX;
        TETile saveSet = hexagonTiles[x][y];
        if (upOrDown == false) {
            saveSet = hexagonTiles[x][y - 1];
        }
        else {
            saveSet = hexagonTiles[x][y + 1];
        }
        while (tmpNum-- != 0) {
            hexagonTiles[x++][y] = saveSet;
        }
        if (upOrDown == false)
            createHexagon2(hexagonTiles,initX + 1, y + 1, lineNum - 2, upOrDown);
        else
            createHexagon2(hexagonTiles,initX + 1, y - 1, lineNum - 2, upOrDown);
    }
    
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] hexagonTiles = new TETile[WIDTH][HEIGHT];
        
        fillWithHexagonsInit(hexagonTiles, 3);

        ter.renderFrame(hexagonTiles);
    }


}
