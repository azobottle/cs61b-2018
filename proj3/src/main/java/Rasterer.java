import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private double MLonDPP;
    private double MlatDPP;

    public Rasterer() {
        MLonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / 256;
        MlatDPP = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / 256;
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double h = params.get("h"), w = params.get("w"), lrlat = params.get("lrlat"),
                lrlon = params.get("lrlon"),
                ullat = params.get("ullat"), ullon = params.get("ullon");
        int dep = calDepth((lrlon - ullon) / w);
        results.put("depth", dep);
        double a_LonDPP = MLonDPP * Math.pow(0.5, dep), a_LatDPP = MlatDPP * Math.pow(0.5, dep);
        int ul_x = -1, ul_y = -1, lr_x = -1, lr_y = -1, len = (int) Math.pow(2, dep);
        for (int i = 0; i < len; i++) {
            if ((MapServer.ROOT_ULLON + a_LonDPP * 256 * i) <= ullon &&
                    ullon < (MapServer.ROOT_ULLON + a_LonDPP * 256 * (i + 1))) {
                results.put("raster_ul_lon", MapServer.ROOT_ULLON + a_LonDPP * 256 * i);
                ul_x = i;
            }
            if ((MapServer.ROOT_ULLON + a_LonDPP * 256 * i) < lrlon
                    && lrlon <= (MapServer.ROOT_ULLON + a_LonDPP * 256 * (i + 1))) {
                results.put("raster_lr_lon", MapServer.ROOT_ULLON + a_LonDPP * 256 * (i + 1));
                lr_x = i;
                break;
            }
        }
        if (ullon < MapServer.ROOT_ULLON) {
            results.put("raster_ul_lon", MapServer.ROOT_ULLON);
            ul_x = 0;
        }
        if (lrlon > MapServer.ROOT_LRLON) {
            results.put("raster_lr_lon", MapServer.ROOT_LRLON);
            lr_x = len - 1;
        }
        for (int i = 0; i < len; i++) {
            if ((MapServer.ROOT_ULLAT - a_LatDPP * 256 * i) >= ullat &&
                    ullat > (MapServer.ROOT_ULLAT - a_LatDPP * 256 * (i + 1))) {
                ul_y = i;
                results.put("raster_ul_lat", MapServer.ROOT_ULLAT - a_LatDPP * 256 * i);
            }
            if ((MapServer.ROOT_ULLAT - a_LatDPP * 256 * i) > lrlat &&
                    lrlat >= (MapServer.ROOT_ULLAT - a_LatDPP * 256 * (i + 1))) {
                lr_y = i;
                results.put("raster_lr_lat", MapServer.ROOT_ULLAT - a_LatDPP * 256 * (i + 1));
                break;
            }
        }
        if (ullat > MapServer.ROOT_ULLAT) {
            results.put("raster_ul_lat", MapServer.ROOT_ULLAT);
            ul_y = 0;
        }
        if (lrlat < MapServer.ROOT_LRLAT) {
            results.put("raster_lr_lat", MapServer.ROOT_LRLAT);
            lr_y = len - 1;
        }
        if (ul_x < 0 || ul_y < 0 || lr_x < 0 || lr_y < 0) {
            results.put("query_success", false);
        } else {
            results.put("query_success", true);
        }
        String[][] a = new String[lr_y - ul_y + 1][lr_x - ul_x + 1];
        for (int i = ul_y; i <= lr_y; i++) {
            for (int j = ul_x; j <= lr_x; j++) {
                a[i - ul_y][j - ul_x] = "d" + dep + "_x" + j + "_y" + i + ".png";
            }
        }
        results.put("render_grid", a);
        return results;
    }
    private int calDepth(double LDPP) {
        int dep = 0;
        while (dep < 7) {
            if (MLonDPP * Math.pow(0.5, dep) <= LDPP) {
                break;
            }
            dep++;
        }
        return dep;
    }
}