package fr.tse.fi2.hpp.labs.queries.impl.lab2;

import java.util.concurrent.BlockingQueue;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

@State(Scope.Thread)
public class IncrementalAverage_lab2 extends AbstractQueryProcessor {

	private int nb = 0;
	private float sum = 0;

	public IncrementalAverage_lab2(QueryProcessorMeasure measure, BlockingQueue<String> q) {
		super(measure,q);
	}

	@Override
	@Benchmark
	@Measurement(iterations=2)
	@Warmup(iterations=2)
	@Fork(1)
	protected void process(DebsRecord record) {
		nb++;
		sum += record.getFare_amount();
		writeLine("current mean : " + (sum / nb));
	}

}