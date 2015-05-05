package fr.tse.fi2.hpp.labs.queries.impl.lab4;

import java.util.ArrayList;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RouteMembershipProcessor extends AbstractQueryProcessor {

	public ArrayList<DebsRecord> liste = null;
	
	public RouteMembershipProcessor(QueryProcessorMeasure measure) {
		super(measure);
		
		liste = new ArrayList<DebsRecord>();
	}
	
	@Override
	protected void process(DebsRecord record) {
		liste.add(record);
	}
	
	public Boolean searchRoute(DebsRecord r) {
		for (DebsRecord record : liste) {
			if(r.getDropoff_latitude() == record.getDropoff_latitude()
					&& r.getDropoff_longitude() == record.getDropoff_longitude()
					&& r.getPickup_latitude() == record.getPickup_latitude()
					&& r.getPickup_longitude() == record.getPickup_longitude()) {
				return true;
			}
		}
		return false;
	}
}
