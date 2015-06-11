package fr.tse.fi2.hpp.labs.queries.impl.project.it1;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query1a extends AbstractQueryProcessor {

	public LinkedList<DebsRecord> liste = null;
	public LinkedList<CommonRoute> tenBest = null;
	
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Query1a(QueryProcessorMeasure measure) {
		super(measure);
		liste = new LinkedList<DebsRecord>();
		tenBest = new LinkedList<CommonRoute>();
	}

	@Override
	protected void process(DebsRecord record) {
		long start_time = System.nanoTime();
		liste.add(record);
		tenBest.clear();
		
		if(!liste.isEmpty()) {
			while((record.getDropoff_datetime() - liste.getFirst().getDropoff_datetime())/60000 > 30) {
				liste.removeFirst();
			}
		}

		for (DebsRecord debsRecord : liste) {
			Route r = convertRecordToRoute(debsRecord);
			boolean exists = false;
			for (CommonRoute commonRoute : tenBest) {
				if(commonRoute.exists(r)) {
					exists = true;
					break;
				}
			}
			if(!exists && r.getDropoff().getX()<300 && r.getDropoff().getY()<300 && r.getPickup().getX()<300 && r.getPickup().getY()<300) {
				tenBest.add(new CommonRoute(r, debsRecord.getDropoff_datetime()));
			}
		}
		Collections.sort(tenBest);

		String line = sdfDate.format(new Date(record.getPickup_datetime())) + ", " + sdfDate.format(new Date(record.getDropoff_datetime())) + " ; ";
		CommonRoute cr;
		for (int i = 0; i < 10; i++) {
			
			
			try {
				cr = tenBest.get(i);
				line += cr.getCount() + "," + cr.getPickup().getX() + "." + cr.getPickup().getY() + "," + cr.getDropoff().getX() + "." + cr.getDropoff().getY() + " ; ";
			} catch (Exception e) {
				cr = null;
				line += "NULL ; ";
			}
			
		}
		line += (System.nanoTime() - start_time);
		writeLine(line);

	}

}
