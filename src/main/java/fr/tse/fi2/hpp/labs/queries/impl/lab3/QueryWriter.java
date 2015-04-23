package fr.tse.fi2.hpp.labs.queries.impl.lab3;

import java.util.concurrent.BlockingQueue;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class QueryWriter extends AbstractQueryProcessor {

	public QueryWriter(QueryProcessorMeasure measure, BlockingQueue<String> q) {
		super(measure, q);
	}

	@Override
	protected void process(DebsRecord record) {
		try {
			String line = writeQueue.take();
			writeLine(line);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
