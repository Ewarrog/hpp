package fr.tse.fi2.hpp.labs.queries.impl.project.it1;

import java.util.ArrayList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query2 extends AbstractQueryProcessor {

	ArrayList<DebsRecord> listRecords;
	
	public Query2(QueryProcessorMeasure measure) {
		super(measure);
		listRecords = new ArrayList<DebsRecord>();
	}

	@Override
	protected void process(DebsRecord record) {
		boolean trouve = false;
		for (DebsRecord debsRecord : listRecords) {
			if(debsRecord.getHack_license().equals(record.getHack_license())) {
				debsRecord = record;
				trouve = true;
				break;
			}
		}
		if(!trouve) {
			listRecords.add(record);
		}
	}

}
