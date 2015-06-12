package fr.tse.fi2.hpp.labs.queries.impl.project.it2;

import fr.tse.fi2.hpp.labs.beans.Route;

public class CommonRoute {

	private long last_dropoff_time;
	
	private Route route;

	public CommonRoute(Route r, long dropoff_time) {
		route = new Route(r.getPickup(), r.getDropoff());
		last_dropoff_time = dropoff_time;
	}

	public long getDropoffTime() {return last_dropoff_time;}

	public Route getRoute() {
		return route;
	}
	
	public String toString() {
		return route.getPickup().getX() + "." + route.getPickup().getY() + "," + route.getDropoff().getX() + "." + route.getDropoff().getY();
	}
	
	public String getDropoffString() {
		return route.getDropoff().getX() + "." + route.getDropoff().getY();
	}
}
