package fr.tse.fi2.hpp.labs.queries.impl.project.it2;

import java.util.Collections;
import java.util.LinkedList;

public class Cellule implements Comparable<Cellule>{
	
	private String id;
	private int nbEmptyTaxis;
	private float medianFare;
	private LinkedList<Float> records;

	
	public Cellule(String id) {
		this.id = id;
		nbEmptyTaxis = 0;
		records = new LinkedList<Float>();
	}
	
	/**
	 * Ajoute un départ de taxi à la zone et donc un fair
	 * 
	 * @param rex
	 */
	public void add(float rex) {
		if(rex > 0) {
			records.add(rex);
		}
	}
	
	public void remove() {
		records.removeFirst();
	}
	
	public void incrEmptyTaxis() {
		nbEmptyTaxis++;
	}
	
	public void decrEmptyTaxis() {
		nbEmptyTaxis--;
	}
	
	public float getMedianFare() {
		return medianFare;
	}
	
	public void calcMedianFare() {
		LinkedList<Float> copy = (LinkedList<Float>) records.clone();
		Collections.sort(copy);
		if(copy.size()>0) {
			medianFare = copy.get((int)(copy.size()/2));
		} else {
			medianFare = 0;
		}
	}

	public int getNbEmptyTaxis() {
		return nbEmptyTaxis;
	}

	public void setNbEmptyTaxis(int nbTaxisVides) {
		this.nbEmptyTaxis = nbTaxisVides;
	}

	public LinkedList<Float> getRec() {
		return records;
	}

	public void setRec(LinkedList<Float> recs) {
		this.records = recs;
	}
	
	public float getProfitability() {
		if(nbEmptyTaxis != 0 && medianFare != 0) {
			return medianFare/nbEmptyTaxis;
		} else {
			return 0;
		}
		
	}
	
	public String getId() {
		return id;
	}

	@Override
	public int compareTo(Cellule o) {
		if(o.getProfitability() > this.getProfitability()) {
			return 1;
		} else if(o.getProfitability() < this.getProfitability()) {
			return -1;
		} else {
			if(o.getMedianFare() > this.medianFare) {
				return 1;
			} else if(o.getMedianFare() < this.medianFare) {
				return -1;
			} else {
				return 0;
			}
		}
	}

}
