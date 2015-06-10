package fr.tse.fi2.hpp.labs.queries.impl.project.it2;

import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.Route;

public class CommonRoute extends Route implements Comparable<CommonRoute>{

	private int count;
	private long last_dropoff_time;
	
	public CommonRoute(GridPoint pickup, GridPoint dropoff) {
		super(pickup, dropoff);
		count = 1;
	}

	public CommonRoute(Route r, long dropoff_time) {
		super(r.getPickup(), r.getDropoff());
		last_dropoff_time = dropoff_time;
		count = 1;
	}
	
	public CommonRoute() {
		
	}
	
	public int getCount() {return count;}
	
	public long getDropoffTime() {return last_dropoff_time;}
	
	public void setDropoffTime(long dropoff_time) {last_dropoff_time = dropoff_time;}
	
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
		if(r.count - this.count == 0) {
			return (int)(r.last_dropoff_time - this.last_dropoff_time);
		} else {
			return r.count - this.count;
		}
	}

}
