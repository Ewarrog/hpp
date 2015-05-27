package fr.tse.fi2.hpp.labs.queries.impl.project.it1;

import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.Route;

public class CommonRoute extends Route implements Comparable<CommonRoute>{

	private int count;
	
	public CommonRoute(GridPoint pickup, GridPoint dropoff) {
		super(pickup, dropoff);
		count = 1;
	}

	public CommonRoute(Route r) {
		super(r.getPickup(), r.getDropoff());
		count = 1;
	}
	
	public CommonRoute() {
		
	}
	
	public int getCount() {return count;}
	
	public boolean exists(Route r) {
		if (r.getDropoff().getX() == this.getDropoff().getX() 
				&& r.getDropoff().getY() == this.getDropoff().getY()
				&& r.getPickup().getX() == this.getPickup().getX()
				&& r.getPickup().getY() == this.getPickup().getY()) {
			count++;
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(CommonRoute r) {
		return r.count - this.count;
	}

}
