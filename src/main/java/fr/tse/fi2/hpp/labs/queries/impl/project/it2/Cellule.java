package fr.tse.fi2.hpp.labs.queries.impl.project.it2;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Classe représentant une case de la grille.
 * Contient les coordonnées de la case, le nombre de taxis vides sur cette case depuis moins de 30 minutes, 
 * la liste des fares et tips des 15 dernières minutes et la médiane de ces fares.
 * Permet le calcul de la productivité de cette case.
 * 
 * @author Aurelien & Samed
 *
 */
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
	 * Ajoute un fare à la liste des fares
	 * 
	 * @param rex fare et tip à ajouter à la liste
	 */
	public void add(float rex) {
		if(rex > 0) {
			records.add(rex);
		}
	}
	
	/**
	 * Supprime le fare le plus ancien
	 */
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
	
	/**
	 * Calcule le fare médian.
	 * Pour cela, on trie une copie de la liste afin de récupérer la valeur médiane.
	 * On trie une copie afin de conserver l'ordre d'ancienneté de la liste d'origine.
	 */
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
	
	/**
	 * Calcule et renvoie la profitabilité de la case.
	 * @return
	 * 		Profitabilité de la case.
	 */
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
