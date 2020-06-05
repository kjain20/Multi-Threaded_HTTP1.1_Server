package edu.upenn.cis.cis455.m2.server.interfaces;

import edu.upenn.cis.cis455.handlers.Route;

public class Routable {
    Route route;
    String route_path;

    public Routable(Route route, String route_path)
    {
        this.route = route;
        this.route_path = route_path;
    }

    public Route getRoute() {
        return this.route;
    }

    public String getRoute_path()
    {
        return this.route_path;
    }
}
