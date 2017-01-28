package caas;

public class SimVehicle {
	
	private String id;
	private double xpos;
	private double ypos;
	
	public void setXpos(double value) {
		xpos = value;
	}
	
	public void setYpos(double value) {
		ypos = value;
	}
	
	public double getXpos() {
		return xpos;
	}
	
	public double getYpos() {
		return ypos;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}
}
