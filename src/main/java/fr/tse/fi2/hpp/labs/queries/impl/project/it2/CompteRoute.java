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
	
	private double last_dropoff_time;
	
	private String coord;
	
	private static int ordre_global = 0;
	
	private int ordre;
	
	public CompteRoute(double last_time, Route r) {
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
		if(counter == cr.getCounter()) {
			if(last_dropoff_time == cr.getLast_dropoff_time()) {
				return cr.getOrdre() - ordre;
			} else {
				return (int)(cr.getLast_dropoff_time() - last_dropoff_time);
			}
		} else {
			return cr.counter - counter;
		}
	}
	
	public String getCoord() {
		return coord;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public double getLast_dropoff_time() {
		return last_dropoff_time;
	}
	
	public int getOrdre() {
		return ordre;
	}
	
	public void setLast_dropoff_time(double last_dropoff_time) {
		this.last_dropoff_time = last_dropoff_time;
	}
}
