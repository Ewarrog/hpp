package fr.tse.fi2.hpp.labs.queries.impl.project.it1;

import java.util.Collections;
import java.util.LinkedList;

public class Cellule {
	
	private int id;
	private int nbEmptyTaxis;
	private float medianFare;
	private LinkedList<Float> records;

	public Cellule(int id) {
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
		Collections.sort(records);
		if(records.size()>0) {
			medianFare = records.get((int)(records.size()/2));
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
		return medianFare/nbEmptyTaxis;
	}
	
	public int getId() {
		return id;
	}

}
