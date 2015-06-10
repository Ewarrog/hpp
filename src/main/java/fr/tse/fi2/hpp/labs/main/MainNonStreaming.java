package fr.tse.fi2.hpp.labs.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.LoadFirstDispatcher;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.project.it1.Query1a;
import fr.tse.fi2.hpp.labs.queries.impl.project.it1.Query2a;

/**
 * Main class of the program. Register your new queries here
 * 
 * Design choice: no thread pool to show the students explicit
 * {@link CountDownLatch} based synchronization.
 * 
 * @author Julien
 * 
 */
public class MainNonStreaming {

	final static Logger logger = LoggerFactory
			.getLogger(MainNonStreaming.class);

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Init query time measure
		QueryProcessorMeasure measure = new QueryProcessorMeasure();
		// Init dispatcher and load everything
		LoadFirstDispatcher dispatch = new LoadFirstDispatcher(
				"src/main/resources/data/100k.csv");
		logger.info("Finished parsing");
		// Query processors
		List<AbstractQueryProcessor> processors = new ArrayList<>();
		
		// Add you query processor here
		//processors.add(new StupidAveragePrice_lab3(measure));
		//RouteMembershipProcessorB pross = new RouteMembershipProcessorB(measure);
		processors.add(new Query2a(measure));

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
		
		/*for(int i=0; i<14378; i++) {
			if(pross.listBloom.get(i) == true) {
				System.out.println("1");
			} else {
				System.out.println("0");
			}
		}*/
		
		//03B0493FEB9C714754477C4B816B7B73,00B7691D86D96AEBD21DD9E138F90840,2013-01-01 00:04:00,2013-01-01 00:06:00,120,0.00,-73.802734,40.705162,-73.802734,40.705162,CRD,3.00,0.50,0.50,0.45,0.00,4.45
		
		//DebsRecord r = new DebsRecord("a", "q", (long)0.0, (long)0.0, (long)0.0, (float)0.0, (float)-73.955582,(float)40.772488,(float)-73.949554,(float)40.773617, "a", (float)0.0, (float)0.0, (float)0.0, (float)0.0, (float)0.0, (float)0.0, false);
		
		/*DebsRecord r = new DebsRecord("03B0493FEB9C714754477C4B816B7B73", "00B7691D86D96AEBD21DD9E138F90840", (long)0.0, (long)0.0, (long)0.0, (float)0.0, (float)-73.802734,(float)40.705162,(float)-73.802734,(float)40.705162, "a", (float)0.0, (float)0.0, (float)0.0, (float)0.0, (float)0.0, (float)0.0, false);
		if(pross.searchRoute(r)) {
			System.out.println("Chemin trouvé");
		} else {
			System.out.println("Chemin non trouvé");
		}*/

	}

}
