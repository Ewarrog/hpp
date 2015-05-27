package fr.tse.fi2.hpp.labs.queries.impl.project.it1;

import java.util.ArrayList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;

public class Cellule {
	
	private int nbTaxisVides;
	private ArrayList<DebsRecord> records;

	public Cellule() {
		setNbTaxisVides(0);
		setRec(new ArrayList<Float>());
	}
	
	public void add(DebsRecord rex) {
		if(rex.getFare_amount() + rex.getTip_amount() > 0) {
			records.add(rex);
		}
	}
	
	public float getMedianFare() {
		return fares.get(fares.size()/2);
	}

	public int getNbTaxisVides() {
		return nbTaxisVides;
	}

	public void setNbTaxisVides(int nbTaxisVides) {
		this.nbTaxisVides = nbTaxisVides;
	}

	public ArrayList<DebsRecord> getRec() {
		return records;
	}

	public void setRec(ArrayList<DebsRecord> recs) {
		this.records = recs;
	}
	
	

}
