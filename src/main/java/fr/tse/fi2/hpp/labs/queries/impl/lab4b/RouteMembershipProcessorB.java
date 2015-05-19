package fr.tse.fi2.hpp.labs.queries.impl.lab4b;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RouteMembershipProcessorB extends AbstractQueryProcessor {

	public ArrayList<DebsRecord> liste = null;
	public BitSet listBloom = null;
	
	public RouteMembershipProcessorB(QueryProcessorMeasure measure) {
		super(measure);
		
		listBloom = new BitSet(14378);
		//liste = new ArrayList<DebsRecord>();
	}
	
	@Override
	protected void process(DebsRecord record) {
		for(int i = 0; i < 10; i++) {
			String s = recordToString(record, "A"+i);
			s = SHA3Util.digest(s);
			BigInteger temp = new BigInteger(s, 16);
			int index = temp.mod(new BigInteger("14378")).intValue();
			listBloom.set(index, true);
		}
		
		//liste.add(record);
	}
	
	public Boolean searchRoute(DebsRecord r) {
		/*for (DebsRecord record : liste) {
			if(r.getDropoff_latitude() == record.getDropoff_latitude()
					&& r.getDropoff_longitude() == record.getDropoff_longitude()
					&& r.getPickup_latitude() == record.getPickup_latitude()
					&& r.getPickup_longitude() == record.getPickup_longitude()
					&& r.getHack_license().equals(record.getHack_license())) {
				System.out.println("Chemin trouvé");
				return true;
			}
		}
		System.out.println("Chemin non trouvé");
		return false;*/
		
		for(int i = 0; i < 10; i++) {
			String s = recordToString(r, "A"+i);
			s = SHA3Util.digest(s);
			BigInteger temp = new BigInteger(s, 16);
			int index = temp.mod(new BigInteger("14378")).intValue();
			if(!listBloom.get(index)) {
				return false;
			}
		}
		return true;
	}
	
	public String recordToString(DebsRecord r, String salt) {
		String s = r.getHack_license();
		
		s += r.getDropoff_latitude();
		s += r.getDropoff_longitude();
		s += r.getPickup_latitude();
		s += r.getPickup_longitude();
		s += salt;
		
		return s;
	}
}
