package fr.tse.fi2.hpp.labs.queries.impl.project.it1;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Tets extends AbstractQueryProcessor{

	public Tets(QueryProcessorMeasure measure) {
		super(measure);
		
	}

	@Override
	protected void process(DebsRecord record) {
		DebsRecord rex = new DebsRecord("03B0493FEB9C714754477C4B816B7B73", "00B7691D86D96AEBD21DD9E138F90840",
				(long)0.0, (long)0.0, (long)0.0, (float)0.0, (float)(-74.913585+0.005986),(float)(41.474937-0.004491556),(float)-73.802734,(float)40.705162,
				"a", (float)0.0, (float)0.0, (float)0.0, (float)0.0, (float)0.0, (float)0.0, false);
		
		Route r = convertRecordToRoute(rex);
		System.out.println(r.getPickup().getX() + "." + r.getPickup().getY());
	}

}
