package fr.tse.fi2.hpp.labs.queries.impl.project.it1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query2a extends AbstractQueryProcessor {

	private LinkedList<DebsRecord> listRecords15;
	private LinkedList<DebsRecord> listRecords30;
	// grille des cellules
	private ArrayList<Cellule> greed;
	
	private LinkedList<Cellule> top10Cell;
	
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Query2a(QueryProcessorMeasure measure) {
		super(measure);
		grid600 = true;
		listRecords15 = new LinkedList<DebsRecord>();
		listRecords30 = new LinkedList<DebsRecord>();
		top10Cell = new LinkedList<Cellule>();
		greed = new ArrayList<Cellule>(360000);
	}

	@Override
	protected void process(DebsRecord record) {
		long start_time = System.nanoTime();
		
		greed.clear();
		
		for(int i=0; i<360000; i++) {
			greed.add(new Cellule(i));
		}
		
		top10Cell.clear();
		
		listRecords15.add(record);
		
		if(!listRecords15.isEmpty()) {
			while((record.getDropoff_datetime() - listRecords15.getFirst().getDropoff_datetime())/60000 > 15) {
				listRecords15.removeFirst();
			}
		}
		
		if(!listRecords30.isEmpty()) {
			while((record.getDropoff_datetime() - listRecords30.getFirst().getDropoff_datetime())/60000 > 30) {
				listRecords30.removeFirst();
			}
			for (int i=0; i < listRecords30.size(); i++) {
				if(record.getHack_license().equals(listRecords30.get(i).getHack_license())) {
					listRecords30.remove(i);
					break;
				}
			}
		}
		listRecords30.add(record);

		
		for (DebsRecord debsRecord : listRecords15) {
			Route r = convertRecordToRoute(debsRecord);

			if(r.getDropoff().getX()<600 && r.getDropoff().getY()<600 && r.getPickup().getX()<600 && r.getPickup().getY()<600) {
				greed.get((r.getPickup().getX()-1)+600*(r.getPickup().getY()-1)).add(debsRecord.getFare_amount()+debsRecord.getTip_amount());
			}
		}
		
		for (DebsRecord debsRecord : listRecords30) {
			Route r = convertRecordToRoute(debsRecord);

			if(r.getDropoff().getX()<=600 && r.getDropoff().getY()<=600 && r.getPickup().getX()<=600 && r.getPickup().getY()<=600
					&& r.getDropoff().getX()>0 && r.getDropoff().getY()>0 && r.getPickup().getX()>0 && r.getPickup().getY()>0) {
				greed.get((r.getDropoff().getX()-1)+600*(r.getDropoff().getY()-1)).incrEmptyTaxis();
			}
		}
		
		for (Cellule cell : greed) {
			cell.calcMedianFare();
			
			if(!top10Cell.isEmpty() && top10Cell.size()>0) {
				int i = 0;
				while((i < top10Cell.size()) && (cell.getProfitability() < top10Cell.get(i).getProfitability()) && (cell.getProfitability() > 0)) {
					i++;
				}
				if(i < 10 && cell.getProfitability() > 0 && cell.getNbEmptyTaxis() > 0) {
					top10Cell.add(i, cell);
					if(top10Cell.size() >= 10) {
						top10Cell.removeLast();
					}
				}
			} else {
				if(cell.getMedianFare()>0 && cell.getNbEmptyTaxis()>0 && cell.getProfitability()>0) {
					top10Cell.add(cell);
				}
			}
		}
		
		String line = sdfDate.format(new Date(record.getPickup_datetime())) + ", " + sdfDate.format(new Date(record.getDropoff_datetime())) + " ; ";
		for (int i = 0; i < 10; i++) {
			
			Cellule cell;
			try {
				cell = top10Cell.get(i);
				float prof = cell.getProfitability();
				if(prof>0) {
					line += (cell.getId()%600+1) + "." + (cell.getId()/600+1) + ", " + cell.getNbEmptyTaxis() + ", " + cell.getMedianFare() + ", " + prof + " ; ";
				}
			} catch (Exception e) {
				cell = null;
				line += "NULL ; ";
			}
		}

		line += (System.nanoTime() - start_time);
		writeLine(line);
	}

}
