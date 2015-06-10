package fr.tse.fi2.hpp.labs.queries.impl.project.it2;

import java.util.ArrayList;
import java.util.Collections;

public class Cellule {
	
	private int id;
	private int nbEmptyTaxis;
	private float medianFare;
	private ArrayList<Float> records;

	public Cellule(int id) {
		this.id = id;
		nbEmptyTaxis = 0;
		setRec(new ArrayList<Float>());
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

	public ArrayList<Float> getRec() {
		return records;
	}

	public void setRec(ArrayList<Float> recs) {
		this.records = recs;
	}
	
	public float getProfitability() {
		return medianFare/nbEmptyTaxis;
	}
	
	public int getId() {
		return id;
	}

}
