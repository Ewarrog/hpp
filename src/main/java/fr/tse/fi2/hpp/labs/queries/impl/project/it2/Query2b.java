package fr.tse.fi2.hpp.labs.queries.impl.project.it2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.project.it1.Cellule;

public class Query2b extends AbstractQueryProcessor {

	private LinkedList<DebsRecord> listRecords15;
	private LinkedList<DebsRecord> listRecords30;
	// grille des cellules
	private ArrayList<Cellule> greed;

	private LinkedList<Cellule> top10Cell;
	private HashSet<Integer> indexes;

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Query2b(QueryProcessorMeasure measure) {
		super(measure);
		grid600 = true;
		listRecords15 = new LinkedList<DebsRecord>();
		listRecords30 = new LinkedList<DebsRecord>();
		top10Cell = new LinkedList<Cellule>();
		indexes = new HashSet<Integer>();

		greed = new ArrayList<Cellule>(360000);
		for(int i=0; i<360000; i++) {
			greed.add(new Cellule(i));
		}
	}

	@Override
	protected void process(DebsRecord record) {
		long start_time = System.nanoTime();

		top10Cell.clear();



		if(!listRecords15.isEmpty()) {
			while((record.getDropoff_datetime() - listRecords15.getFirst().getDropoff_datetime())/60000 > 15) {
				Route r = convertRecordToRoute(listRecords15.removeFirst());
				
				greed.get((r.getDropoff().getX()-1)+600*(r.getDropoff().getY()-1)).remove();
				greed.get((r.getDropoff().getX()-1)+600*(r.getDropoff().getY()-1)).calcMedianFare();

			}
		}

		if(!listRecords30.isEmpty()) {
			while((record.getDropoff_datetime() - listRecords30.getFirst().getDropoff_datetime())/60000 > 30) {
				Route r = convertRecordToRoute(listRecords30.removeFirst());

				greed.get((r.getDropoff().getX()-1)+600*(r.getDropoff().getY()-1)).decrEmptyTaxis();

			}
			for (int i=0; i < listRecords30.size(); i++) {
				if(record.getHack_license().equals(listRecords30.get(i).getHack_license())) {
					listRecords30.remove(i);
					break;
				}
			}
		}

		Route r = convertRecordToRoute(record);

		if(r.getDropoff().getX()<=600 && r.getDropoff().getY()<=600 && r.getPickup().getX()<=600 && r.getPickup().getY()<=600
				&& r.getDropoff().getX()>0 && r.getDropoff().getY()>0 && r.getPickup().getX()>0 && r.getPickup().getY()>0) {

			int ind = (r.getDropoff().getX()-1)+600*(r.getDropoff().getY()-1);
			indexes.add(ind);
			greed.get(ind).incrEmptyTaxis();
			greed.get(ind).add(record.getFare_amount()+record.getTip_amount());
			greed.get(ind).calcMedianFare();

			listRecords15.add(record);
			listRecords30.add(record);

			Cellule cell = null;

			for (int index : indexes) {
				cell = greed.get(index);
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

}
