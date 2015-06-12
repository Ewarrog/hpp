package fr.tse.fi2.hpp.labs.queries.impl.project.it2;

public class Taxi {
	private boolean within30;
	private long lastDropoff;
	private String coordDropoff;
	
	public Taxi(long lastDropoff, String coordDropoff) {
		this.within30 = true;
		this.lastDropoff = lastDropoff;
		this.coordDropoff = coordDropoff;
	}

	public boolean isWithin30() {
		return within30;
	}

	public void setWithin30(boolean within30) {
		this.within30 = within30;
	}

	public long getLastDropoff() {
		return lastDropoff;
	}

	public void setLastDropoff(long lastDropoff) {
		this.lastDropoff = lastDropoff;
	}

	public String getCoordDropoff() {
		return coordDropoff;
	}

	public void setCoordDropoff(String coordDropoff) {
		this.coordDropoff = coordDropoff;
	}
}
