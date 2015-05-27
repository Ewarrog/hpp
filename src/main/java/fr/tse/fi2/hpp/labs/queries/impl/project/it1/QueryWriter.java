package fr.tse.fi2.hpp.labs.queries.impl.project.it1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class QueryWriter implements Runnable {

	/**
	 * Writer to write the output of the queries
	 */
	private BufferedWriter outputWriter;

	private int id;

	private BlockingQueue<String> writeQueue;

	public QueryWriter(BlockingQueue<String> q, int id) {

		super();

		this.id = id;

		writeQueue = q;

		// Initialize writer
		try {
			outputWriter = new BufferedWriter(new FileWriter(new File(
					"result/query" + this.id + ".txt")));
		} catch (IOException e) {
			System.out.println("Cannot open output file for " + this.id);
			System.exit(-1);
		}
	}

	/**
	 * Poison pill has been received, close output
	 */
	public void finish() {
		// Close writer
		try {
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {
			System.out.println("Cannot property close the output file for query "
					+ id);
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				String line = writeQueue.take();
				if(line.equals("KILL YOURSELF!"))
				{
					break;
				}
				outputWriter.write(line);
				outputWriter.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		finish();
	}

}
