import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    private static final double BOUNDULLON = -122.2998046875;
    private static final double BOUNDULLAT = 37.892195547244356;
    private static final double BOUNDLRLON = -122.2119140625;
    private static final double BOUNDLRLAT = 37.82280243352756;
    private static final double WIDTH = BOUNDLRLON - BOUNDULLON;
    private static final double HEIGHT = BOUNDULLAT - BOUNDLRLAT;
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        // the query is below:
        // lrlon=-122.24053369025242, ullon=-122.24163047377972,
        // w=892.0, h=875.0,
        // ullat=37.87655856892288, lrlat=37.87548268822065
        String[][] render_grid;
        double raster_ul_lon;
        double raster_ul_lat;
        int depth = 0;
        double raster_lr_lon;
        double raster_lr_lat;
        // turn the flag to true
        boolean query_success = true;
        // TODO: how to compute the depth of images
        // compute the lonDPP of the query to select the depth.
        // 1. compute the lonDPP of the query
        double lonDPPOfQuery = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        // 2. compute the lonDPP of every depth
        double[] lonDPPForEveryDepth = new double[8];
        double lonDPPForDepth0 = 0.00034332275390625;
        for (int i = 0; i < lonDPPForEveryDepth.length; i++) {
            if (i == 0) {
                lonDPPForEveryDepth[i] = lonDPPForDepth0;
            }
            else {
                lonDPPForDepth0 /= 2;
                lonDPPForEveryDepth[i] = lonDPPForDepth0;
            }
        }
        // 3. compare one by one, if larger, then end it and choose
        // the current depth, else compare with next one, until
        // it be larger or the count number for depth reach 7.
        for (int i = 0; i < lonDPPForEveryDepth.length; i++) {
            if (lonDPPOfQuery >= lonDPPForEveryDepth[i] || i == 7) {
                depth = i;
                break;
            }
        }
        // depth = 7;
        // TODO: how to find the ul and lr image (which is the bounding box.)
        // find the bound
        /**
         * 1. (take the upper left one as an example)
         * compute the x and y of the position.
         * 2. compute the basic distance of the current depth.
         * 3. x (y) % distance = subscript of the corresponding image
         * while for lower right , it will be x (y) % distance + 1
        */
        double x_ul = params.get("ullon") - BOUNDULLON;
        double y_ul = BOUNDULLAT - params.get("ullat");
        double x_lr = params.get("lrlon") - BOUNDULLON;
        double y_lr = BOUNDULLAT - params.get("lrlat");
        double singleDistance_w = WIDTH / Math.pow(2, depth);
        double singleDistance_h = HEIGHT / Math.pow(2, depth);
        int subscript_ul_x = countSubscript(x_ul, singleDistance_w, (int) Math.pow(2, depth) - 1);
        int subscript_ul_y = countSubscript(y_ul, singleDistance_h, (int) Math.pow(2, depth) - 1);
        int subscript_lr_x = countSubscript(x_lr, singleDistance_w, (int) Math.pow(2, depth) - 1) + 1;
        int subscript_lr_y = countSubscript(y_lr, singleDistance_h, (int) Math.pow(2, depth) - 1) + 1;
        // System.out.println(subscript_ul_x + " " + subscript_ul_y);
        // System.out.println(subscript_ul_x + " " + subscript_ul_y);
        // System.out.println(subscript_lr_x + " " + subscript_lr_y);
        // 4. get the width and the height of the String.
        int width = subscript_lr_x - subscript_ul_x;
        int height = subscript_lr_y - subscript_ul_y;
        // 5. load the image to the String[][].
        render_grid = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                render_grid[i][j] = "d" + depth + "_x" + (j + subscript_ul_x) + "_y" + (i + subscript_ul_y) + ".png";
            }
        }
        /*
        System.out.println(render_grid[0][0].equals("d7_x84_y28.png"));
        System.out.println(render_grid[0][1].equals("d7_x85_y28.png"));
        System.out.println(render_grid[0][2].equals("d7_x86_y28.png"));
        System.out.println(render_grid[1][0].equals("d7_x84_y29.png"));
        System.out.println(render_grid[1][1].equals("d7_x85_y29.png"));
        System.out.println(render_grid[1][2].equals("d7_x86_y29.png"));
        System.out.println(render_grid[2][0].equals("d7_x84_y30.png"));
        System.out.println(render_grid[2][1].equals("d7_x85_y30.png"));
        System.out.println(render_grid[2][2].equals("d7_x86_y30.png"));
        */
        // 6. get the four spots.
        raster_ul_lon = BOUNDULLON + singleDistance_w * subscript_ul_x;
        raster_ul_lat = BOUNDULLAT - singleDistance_h * subscript_ul_y;
        raster_lr_lon = BOUNDULLON + singleDistance_w * subscript_lr_x;
        raster_lr_lat = BOUNDULLAT - singleDistance_h * subscript_lr_y;
        /*
        System.out.println(raster_ul_lon == -122.24212646484375);
        System.out.println(raster_lr_lon == -122.24006652832031);
        System.out.println(raster_lr_lat == 37.87538940251607);
        System.out.println(raster_ul_lat == 37.87701580361881);
        */
        // TODO: how to load the images to the map
        // Already done!
        // hard coding the correct result (deleted)
        System.out.println("depth = " + depth);
        for (int i = 0; i < render_grid.length; i++) {
            for (int j = 0; j <render_grid[0].length; j++) {
                System.out.println("render_grid = " + render_grid[i][j]);
            }
        }
        /*
        System.out.println("raster_ul_lon = " + raster_ul_lon);
        System.out.println("raster_ul_lat = " + raster_ul_lat);
        System.out.println("raster_lr_lon = " + raster_lr_lon);
        System.out.println("raster_lr_lat = " + raster_lr_lat);
        System.out.println("query_success = " + query_success);
        */
        results.put("render_grid", render_grid);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("depth", depth);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("query_success", query_success);
        return results;
    }
    
    private int countSubscript(double wholeDistance, double singleDistance, int bound) {
        // if < ul, return subscript 0
        if (wholeDistance < 0) {
            return 0;
        }
        
        double tmp = wholeDistance;
        int check = 0;
        while (tmp > singleDistance) {
            // if > lr bound, return bound
            if (check == bound) {
                break;
            }
            tmp -= singleDistance;
            check++;
        }
        return check;
    }
}
