package animate;

import utils.Quaternion;
import utils.Vec3;

public class SequenceSet {
	
	public double time;
	public Vec3 position;
	public Quaternion attitude;

	public SequenceSet() {
		
	}
	
	public SequenceSet(double time, Vec3 position) {
		this.time=time;
		this.position=position;
	}
	
	public SequenceSet(double time, Vec3 position, Quaternion attitude) {
		this.time=time;
		this.position=position;
		this.attitude=attitude;
	}
}
