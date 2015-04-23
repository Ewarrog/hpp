package fr.tse.fi2.hpp.labs.queries.impl.lab3;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class StupidAveragePrice_lab3 extends AbstractQueryProcessor {

	private ArrayList<Float> liste = null;
	
	public StupidAveragePrice_lab3(QueryProcessorMeasure measure,
			BlockingQueue<String> q) {
		
		super(measure, q);
		liste = new ArrayList<Float>();
		
	}

	@Override
	protected void process(DebsRecord record) {
		
		liste.add(record.getFare_amount());
		
		float sum = 0f;
		for (Float f : liste) {
			sum += f;
		}
		try {
			writeQueue.put("current mean : " + (sum / liste.size()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
	}

}
