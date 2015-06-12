package fr.tse.fi2.hpp.labs.queries.impl.project.it2;

import fr.tse.fi2.hpp.labs.beans.Route;

/**
 * Classe permettant de compter le nombre de fois qu'une route a été utilisée.
 * Elle contient également le dropoff_time de la dernière occurence et l'ordre d'apparition de cette route.
 * 
 * @author Aurelien & Samed
 *
 */
public class CompteRoute implements Comparable<CompteRoute> {

	private int counter;
	
	private long last_dropoff_time;
	
	private String coord;
	
	private static long ordre_global = 0;
	
	private long ordre;
	
	public CompteRoute(long last_time, Route r) {
		counter = 1;
		ordre = ordre_global++;
		coord = coordFromRoute(r);
		last_dropoff_time = last_time;
	}

	private String coordFromRoute(Route r) {		
		return r.getPickup().getX() + "." + r.getPickup().getY() + "," + r.getDropoff().getX() + "." + r.getDropoff().getY();
	}
	
	public void incr() {
		counter++;
	}
	
	public void decr() {
		counter--;
	}
	
	@Override
	public int compareTo(CompteRoute cr) {
		if(cr.getCounter() > counter) {
			return 1;
		} else if (cr.getCounter() < counter) {
			return -1;
		} else {
			if(cr.getLast_dropoff_time() > last_dropoff_time) {
				return 1;
			} else if (cr.getLast_dropoff_time() < last_dropoff_time) {
				return -1;
			} else {
				if(cr.getOrdre() > ordre) {
					return 1;
				} else if(cr.getOrdre() < ordre) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}
	
	public String getCoord() {
		return coord;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public long getLast_dropoff_time() {
		return last_dropoff_time;
	}
	
	public long getOrdre() {
		return ordre;
	}
	
	public void setLast_dropoff_time(long last_dropoff_time) {
		this.last_dropoff_time = last_dropoff_time;
	}
}
