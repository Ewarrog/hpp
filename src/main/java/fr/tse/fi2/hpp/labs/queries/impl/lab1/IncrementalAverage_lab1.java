package fr.tse.fi2.hpp.labs.queries.impl.lab1;

import java.util.concurrent.BlockingQueue;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class IncrementalAverage_lab1 extends AbstractQueryProcessor {

	private int nb = 0;
	private float sum = 0;

	public IncrementalAverage_lab1(QueryProcessorMeasure measure, BlockingQueue<String> q) {
		super(measure,q);
	}

	@Override
	protected void process(DebsRecord record) {
		nb++;
		sum += record.getFare_amount();
		writeLine("current mean : " + (sum / nb));
	}

}