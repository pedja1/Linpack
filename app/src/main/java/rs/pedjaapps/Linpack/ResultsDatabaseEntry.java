package rs.pedjaapps.Linpack;

public class ResultsDatabaseEntry {

	int id;
	double mflops;
	double nres;
	double time;
	double precision;
	String date;
	
	
	
	public ResultsDatabaseEntry() {
		super();
		
	}

	public ResultsDatabaseEntry(int id, double mflops, double nres,
			double time, double precision, String date) {
		super();
		this.id = id;
		this.mflops = mflops;
		this.nres = nres;
		this.time = time;
		this.precision = precision;
		this.date = date;
	}

	public ResultsDatabaseEntry(double mflops, double nres, double time,
			double precision, String date) {
		super();
		this.mflops = mflops;
		this.nres = nres;
		this.time = time;
		this.precision = precision;
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getMflops() {
		return mflops;
	}
	public void setMflops(double mflops) {
		this.mflops = mflops;
	}
	public double getNres() {
		return nres;
	}
	public void setNres(double nres) {
		this.nres = nres;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getPrecision() {
		return precision;
	}
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
