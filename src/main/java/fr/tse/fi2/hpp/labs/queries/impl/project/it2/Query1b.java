package fr.tse.fi2.hpp.labs.queries.impl.project.it2;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query1b extends AbstractQueryProcessor {

	private HashMap<String, CompteRoute> map = null;

	public LinkedList<CommonRoute> liste = null;

	public LinkedList<CompteRoute> tenBest = null;

	long last_time;
	long start_time;
	CommonRoute route = null;
	CompteRoute cr = null;

	Iterator<CompteRoute> it = null;
	int i;
	String line;

	String key;

	boolean sortRequiered;

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Query1b(QueryProcessorMeasure measure) {
		super(measure);

		map = new HashMap<String, CompteRoute>();

		liste = new LinkedList<CommonRoute>();

		tenBest = new LinkedList<CompteRoute>();

		grid600 = false;

		sortRequiered = false;
	}

	@Override
	protected void process(DebsRecord record) {
		start_time = System.nanoTime();

		last_time = record.getDropoff_datetime();

		route = new CommonRoute(convertRecordToRoute(record), last_time);

		if(isInGrid(route.getRoute())) {

			key = route.toString();
			if(map.containsKey(key)) {
				cr = map.get(key);
				cr.setLast_dropoff_time(last_time);
				cr.incr();

				if(tenBest.size() >= 10) {
					if(tenBest.get(9).getCounter() <= cr.getCounter()) {
						sortRequiered = true;
					}					
				} else {
					sortRequiered = true;
				}

			} else {
				cr = new CompteRoute(last_time, route.getRoute());
				map.put(key, cr);
				tenBest.add(cr);
				if(tenBest.size() >= 10) {
					if(tenBest.get(9).getCounter() <= cr.getCounter()) {
						sortRequiered = true;
					}					
				} else {
					sortRequiered = true;
				}
			}

			liste.add(route);
			
			if(!liste.isEmpty()) {
				while((last_time - liste.getFirst().getDropoffTime())/60000 > 30) {

					map.get(liste.getFirst().toString()).decr();

					if(isInTop10(liste.getFirst())) {
						sortRequiered = true;
					}

					liste.removeFirst();
				}
			}
			
			if(sortRequiered) {
				Collections.sort(tenBest);
				sortRequiered = false;
			}

		}

		line = sdfDate.format(new Date(record.getPickup_datetime())) + "," + sdfDate.format(new Date(last_time)) + ",";

		

		i = 0;
		for (CompteRoute truc : tenBest) {
			i++;
			line += truc.getCoord() + ",";
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

	// Vérifie que la route est dans la grille
	private boolean isInGrid(Route r) {
		return r.getDropoff().getX()<=300 && r.getDropoff().getY()<=300 && r.getPickup().getX()<=300 && r.getPickup().getY()<=300
				&& r.getDropoff().getX()>0 && r.getDropoff().getY()>0 && r.getPickup().getX()>0 && r.getPickup().getY()>0;
	}

	private boolean isInTop10(CommonRoute r) {
		for (i = 0; i<10; i++) {
			if(tenBest.get(i).getCoord().equals(r.toString())) return true;
		}
		return false;
	}
}
