package utils;

public class Interpolator {

	
	public Interpolator() {
		
	}
	
	public double linear(double Xstart, double Xend, double Ystart, double Yend, double xRel) {
		return Ystart + xRel * (Yend - Ystart) / (Xend - Xstart);
	}
}
