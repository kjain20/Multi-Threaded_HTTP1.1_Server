package edu.upenn.cis.cis455.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Routes Util class
 */
public class RoutesUtil {

    public static List<String> convertRouteToList(String route) {
        String[] pathArray = route.split("/");
        List<String> path = new ArrayList<>();
        for (String p : pathArray) {
            if (p.length() > 0) {
                path.add(p);
            }
        }
        return path;
    }
}
