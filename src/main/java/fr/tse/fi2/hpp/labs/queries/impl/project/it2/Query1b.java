package fr.tse.fi2.hpp.labs.queries.impl.project.it2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

/**
 * Réalisation de la query 1.
 * Les routes sont stockées dans une hashmap ce qui permet d'assurer l'unicité des routes
 * ainsi que de compter le nombre de fois qu'elles sont empruntées. Cette HashMap a pour clé un string contenant
 * les coordonnées de départ et d'arrivée des routes.
 * On a une liste qui sert de fenêtre de 30 min afin de ne conserver que les records
 * les plus récents. A chaque nouveau record, on parcours le début de la liste et on supprime
 * le premier élément de la liste jusqu'à ce que tous les records datant de plus de 30min soient supprimés.
 * Toutes les routes sont stockées dans une table qui sera triée afin de récupérer les 10 routes les plus fréquentes.
 * Cette table n'est triée que lorsqu'il y a un changement d'ordre dans les 10 premières routes. Cela permet de réduire
 * considérablement la fréquence des tris de la table.
 * L'utilisation d'un ArrayList au lieu d'une LinkedList permet d'accélérer le tri.
 * 
 * @author Aurelien & Samed
 *
 */ 
public class Query1b extends AbstractQueryProcessor {

	private HashMap<String, CompteRoute> map = null;

	public LinkedList<CommonRoute> liste = null;

	public ArrayList<CompteRoute> tenBest = null;

	long last_time;
	long start_time;
	CommonRoute route = null;
	CompteRoute cr = null;

	int i;
	String line;

	String key;

	boolean sortRequiered;

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Query1b(QueryProcessorMeasure measure) {
		super(measure);

		map = new HashMap<String, CompteRoute>();

		liste = new LinkedList<CommonRoute>();

		tenBest = new ArrayList<CompteRoute>(10000);

		// boolean permettant de sélectionner la taille de la grille : si true => grille de 600*600 sinon => 300*300
		grid600 = false;

		sortRequiered = false;
	}

	@Override
	protected void process(DebsRecord record) {
		start_time = System.nanoTime();

		last_time = record.getDropoff_datetime();

		route = new CommonRoute(convertRecordToRoute(record), last_time);

		if(isInGrid(route.getRoute())) {

			// Ajout de la route et incrémentation du compteur correspondant si elle existe déjà
			key = route.toString();
			if(map.containsKey(key)) {
				cr = map.get(key);
				cr.setLast_dropoff_time(last_time);
				cr.incr();
			} else {
				cr = new CompteRoute(last_time, route.getRoute());
				map.put(key, cr);
				tenBest.add(cr);
			}

			
			if(tenBest.size() >= 10) {
				if(tenBest.get(9).getCounter() <= cr.getCounter()) {
					for(i = 0; i<9; i++) {
						if(tenBest.get(i).getCounter() < tenBest.get(i+1).getCounter()) {
							sortRequiered = true;
						}
					}
				}
			} else {
				sortRequiered = true;
			}
			
			liste.add(route);
			
			// Calcul de la fenêtre de 30 minutes
			if(!liste.isEmpty()) {
				while((last_time - liste.getFirst().getDropoffTime())/60000 > 30) {

					map.get(liste.getFirst().toString()).decr();

					if(isInTop10(liste.getFirst())) {
						sortRequiered = true;
					}

					liste.removeFirst();
					
					if(liste.isEmpty()) {
						break;
					}
				}
			}
			
			// tri du tableau lorsque c'est nécessaire
			if(sortRequiered) {
				Collections.sort(tenBest);
				sortRequiered = false;
			}

		}

		
		// Ecriture du résultat
		line = sdfDate.format(new Date(record.getPickup_datetime())) + "," + sdfDate.format(new Date(last_time)) + ",";

		i = 0;
		for (CompteRoute cptRoute : tenBest) {
			i++;
			line += cptRoute.getCoord() + ",";
			if(i>=10) {
				break;
			}
		}
		while(i<10) {
			line += "NULL,";
			i++;
		}
		line += (System.nanoTime() - start_time);
		writeLine(line);

	}

	/**
	 *  Vérifie que la route est dans la grille
	 * @param r route à vérifier
	 * @return
	 * 		true si la route est dans la grille de 300*300
	 * 		false sinon
	 */
	private boolean isInGrid(Route r) {
		return r.getDropoff().getX()<=300 && r.getDropoff().getY()<=300 && r.getPickup().getX()<=300 && r.getPickup().getY()<=300
				&& r.getDropoff().getX()>0 && r.getDropoff().getY()>0 && r.getPickup().getX()>0 && r.getPickup().getY()>0;
	}

	/**
	 *  Vérifie que la route se trouve dans le top 10
	 * @param r route à vérifier
	 * @return 
	 * 		true si cette route est dans les 10 meilleures
	 * 		false si elle ne s'y trouve pas
	 */
	private boolean isInTop10(CommonRoute r) {
		for (i = 0; i<10; i++) {
			if(tenBest.get(i).getCoord().equals(r.toString())) return true;
		}
		return false;
	}
}
