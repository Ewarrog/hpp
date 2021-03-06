package fr.tse.fi2.hpp.labs.queries.impl.lab2;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class IncrementalAverage_lab2 extends AbstractQueryProcessor {

	private int nb = 0;
	private float sum = 0;

	public IncrementalAverage_lab2(QueryProcessorMeasure measure) {
		super(measure);
	}

	protected void process(DebsRecord record) {
		nb++;
		sum += record.getFare_amount();
		writeLine("current mean : " + (sum / nb));
	}

}