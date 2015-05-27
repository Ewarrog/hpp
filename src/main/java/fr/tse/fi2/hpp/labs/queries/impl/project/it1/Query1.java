package fr.tse.fi2.hpp.labs.queries.impl.project.it1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query1 extends AbstractQueryProcessor {

	public LinkedList<DebsRecord> liste = null;
	public ArrayList<CommonRoute> tenBest = null;
	
	public Query1(QueryProcessorMeasure measure) {
		super(measure);
		liste = new LinkedList<DebsRecord>();
		tenBest = new ArrayList<CommonRoute>();
	}

	@Override
	protected void process(DebsRecord record) {
		liste.add(record);
		
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
			if(!exists && tenBest.size() < 10) {
				tenBest.add(new CommonRoute(r));
			}
		}
		Collections.sort(tenBest);
		String line = "" + new Date(record.getDropoff_datetime()) + " ; ";
		for (CommonRoute cr : tenBest) {
			line += cr.getCount() + "," + cr.getPickup().getX() + "." + cr.getPickup().getY() + "," + cr.getDropoff().getX() + "." + cr.getDropoff().getY() + " ; ";
		}
		writeLine(line);

	}

}
