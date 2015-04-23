package fr.tse.fi2.hpp.labs.queries.impl;

import java.util.concurrent.BlockingQueue;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

/**
 * Example query that outputs the number of processed events
 * 
 * @author Julien
 * 
 */
public class SimpleQuerySumEvent extends AbstractQueryProcessor {

	// Total of events
	int sum;

	public SimpleQuerySumEvent(QueryProcessorMeasure measure, BlockingQueue<String> q) {
		super(measure,q);
		sum = 0;
	}

	@Override
	protected void process(DebsRecord record) {
		//writeLine("" + sum++);

	}

}
