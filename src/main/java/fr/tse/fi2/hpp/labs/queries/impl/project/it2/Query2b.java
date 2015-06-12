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

public class Query2b extends AbstractQueryProcessor {

	private LinkedList<CommonRoute> listRecords15;
	private LinkedList<DebsRecord> listRecords30;

	private ArrayList<Cellule> top10Cell;
	private HashMap<String, Cellule> cellules = null;
	private HashMap<String, Taxi> listTaxis = null;

	private Cellule cellule = null;
	private CommonRoute cr = null;
	private CommonRoute route = null;

	private DebsRecord debs = null;
	private Taxi taxi = null;
	
	private boolean sortRequiered;

	private String coordCell;
	
	private long last_time;
	
	private int i;
	private String line;

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Query2b(QueryProcessorMeasure measure) {
		super(measure);
		grid600 = true;
		sortRequiered = false;
		
		listRecords15 = new LinkedList<CommonRoute>();
		listRecords30 = new LinkedList<DebsRecord>();
		top10Cell = new ArrayList<Cellule>(10000);
		cellules = new HashMap<String, Cellule>();
		listTaxis = new HashMap<String, Taxi>();
	}

	@Override
	protected void process(DebsRecord record) {
		long start_time = System.nanoTime();

		last_time = record.getDropoff_datetime();

		route = new CommonRoute(convertRecordToRoute(record), last_time);

		if(!listRecords15.isEmpty()) {
			while((last_time - listRecords15.getFirst().getDropoffTime())/60000 > 15) {
				cr = listRecords15.removeFirst();
				cellule = cellules.get(cr.getDropoffString());
				cellule.remove();
				cellule.calcMedianFare();
				
				if(isInTop10(cellule)) {
					sortRequiered = true;
				}
				
				if(top10Cell.size() >= 10) {
					if(top10Cell.get(9).getProfitability() <= cellule.getProfitability()) {
						sortRequiered = true;
					}					
				} else {
					sortRequiered = true;
				}
				
				if(listRecords15.isEmpty()) {
					break;
				}
			}
		}

		if(!listRecords30.isEmpty()) {
			while((last_time - listRecords30.getFirst().getDropoff_datetime())/60000 > 30) {
				debs = listRecords30.removeFirst();

				taxi = listTaxis.get(debs.getHack_license());
				if(debs.getDropoff_datetime() == taxi.getLastDropoff()) {
					taxi.setWithin30(false);
					cellule = cellules.get(taxi.getCoordDropoff());
					cellule.decrEmptyTaxis();
					if(top10Cell.size() >= 10) {
						if(top10Cell.get(9).getProfitability() <= cellule.getProfitability()) {
							sortRequiered = true;
						}					
					} else {
						sortRequiered = true;
					}
				}
				
				if(listRecords30.isEmpty()) {
					break;
				}
			}
		}

		if(isInGrid(route.getRoute())) {

			listRecords15.add(route);
			listRecords30.add(record);
			coordCell = route.getDropoffString();
			
			if(listTaxis.containsKey(record.getHack_license())) {
				taxi = listTaxis.get(record.getHack_license());
				if(taxi.isWithin30()) {
					cellules.get(taxi.getCoordDropoff()).decrEmptyTaxis();
				} 
				taxi.setCoordDropoff(coordCell);
				taxi.setLastDropoff(last_time);
			} else {
				listTaxis.put(record.getHack_license(), new Taxi(last_time, coordCell));
			}
			
			if(!cellules.containsKey(coordCell)) {
				cellule = new Cellule(coordCell);
				cellules.put(coordCell, cellule);
				top10Cell.add(cellule);
			}
			
			cellule = cellules.get(coordCell);
			cellule.incrEmptyTaxis();
			
			cellule.add(record.getFare_amount()+record.getTip_amount());
			cellule.calcMedianFare();
			
			if(isInTop10(cellule)) {
				sortRequiered = true;
			}
			
			if(top10Cell.size() >= 10) {
				if(top10Cell.get(9).getProfitability() <= cellule.getProfitability()) {
					sortRequiered = true;
				}					
			} else {
				sortRequiered = true;
			}
			
			if(sortRequiered) {
				Collections.sort(top10Cell);
				sortRequiered = false;
			}
			
			line = sdfDate.format(new Date(record.getPickup_datetime())) + "," + sdfDate.format(new Date(last_time)) + ",";

			i = 0;
			for (Cellule cell : top10Cell) {
				i++;
				line += cell.getId() + "," + cell.getNbEmptyTaxis() + "," + cell.getMedianFare() + "," + cell.getProfitability() + ",";
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
	}


	// Vérifie que la route est dans la grille
	private boolean isInGrid(Route r) {
		return r.getDropoff().getX()<=600 && r.getDropoff().getY()<=600 && r.getPickup().getX()<=600 && r.getPickup().getY()<=600
				&& r.getDropoff().getX()>0 && r.getDropoff().getY()>0 && r.getPickup().getX()>0 && r.getPickup().getY()>0;
	}
	
	// Vérifie que la cellule se trouve dans le top 10
	private boolean isInTop10(Cellule c) {
		for (i = 0; i<10; i++) {
			if(top10Cell.get(i).getId().equals(c.getId())) return true;
		}
		return false;
	}
}
