package rs.pedjaapps.Linpack;

public class ResultsEntry {
	
	double mflops;
	String date;
	
	public ResultsEntry(double mflops, String date) {
		super();
		this.mflops = mflops;
		this.date = date;
	}
	
	public double getMflops() {
		return mflops;
	}
	public String getDate() {
		return date;
	}
	
}
