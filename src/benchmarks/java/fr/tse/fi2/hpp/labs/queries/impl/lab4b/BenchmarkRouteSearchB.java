package fr.tse.fi2.hpp.labs.queries.impl.lab4b;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.LoadFirstDispatcher;
import fr.tse.fi2.hpp.labs.main.MainNonStreaming;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

@State(Scope.Benchmark)
@Fork(1)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class BenchmarkRouteSearchB {

	final static Logger logger = LoggerFactory
			.getLogger(MainNonStreaming.class);

	RouteMembershipProcessorB pross = null;

	@Setup
	public void init() {
		// Init query time measure
		QueryProcessorMeasure measure = new QueryProcessorMeasure();
		// Init dispatcher and load everything
		LoadFirstDispatcher dispatch = new LoadFirstDispatcher(
				"src/main/resources/data/1000Records.csv");
		logger.info("Finished parsing");
		// Query processors
		List<AbstractQueryProcessor> processors = new ArrayList<>();

		// Add you query processor here
		// processors.add(new StupidAveragePrice_lab3(measure));
		pross = new RouteMembershipProcessorB(measure);
		processors.add(pross);

		// Register query processors
		for (AbstractQueryProcessor queryProcessor : processors) {
			dispatch.registerQueryProcessor(queryProcessor);
		}
		// Initialize the latch with the number of query processors
		CountDownLatch latch = new CountDownLatch(processors.size());
		// Set the latch for every processor
		for (AbstractQueryProcessor queryProcessor : processors) {
			queryProcessor.setLatch(latch);
		}
		for (AbstractQueryProcessor queryProcessor : processors) {
			Thread t = new Thread(queryProcessor);
			t.setName("QP" + queryProcessor.getId());
			t.start();
		}
		// Start everything dispatcher first, not as a thread
		dispatch.run();
		logger.info("Finished Dispatching");
		// Wait for the latch
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("Error while waiting for the program to end", e);
		}
		// Output measure and ratio per query processor
		measure.setProcessedRecords(dispatch.getRecords());
		measure.outputMeasure();
	}

	@Benchmark
	public int benchmarkingbloom() {

		System.out.println("call bench");
		DebsRecord r = new DebsRecord("DC94C90BDE77EE687F8BB379A349C674",
				"952CF2A2975BFD2A7B9A341A885BE7CE", (long) 0.0, (long) 0.0,
				(long) 0.0, (float) 0.0, (float) -73.955582, (float) 40.772488,
				(float) -73.949554, (float) 40.773617, "a", (float) 0.0,
				(float) 0.0, (float) 0.0, (float) 0.0, (float) 0.0,
				(float) 0.0, false);

		Boolean ok = pross.searchRoute(r);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ok) { logger.info("chemin trouvé"); return 1; }
		logger.info("chemin non trouvé");
		
		
		/*
		 * i++;
		 * 
		 * if(ok) { logger.info("chemin trouvé "+i); return 1; }
		 * logger.info("chemin non trouvé");
		 */
		return 0;
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(
				BenchmarkRouteSearchB.class.getSimpleName()).build();

		new Runner(opt).run();
	}

}
