package fr.tse.fi2.hpp.labs.queries.impl.lab1;

import java.util.ArrayList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class StupidAveragePrice extends AbstractQueryProcessor {

	private ArrayList<Float> liste = null;
	
	public StupidAveragePrice(QueryProcessorMeasure measure) {
		super(measure);
		liste = new ArrayList<Float>();
	}

	@Override
	protected void process(DebsRecord record) {
		
		liste.add(record.getFare_amount());
		
		float sum = 0f;
		for (Float f : liste) {
			sum += f;
		}
		writeLine("current mean : " + (sum / liste.size()));
	}

}
