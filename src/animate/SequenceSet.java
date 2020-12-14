package animate;

import utils.Vec3;

public class SequenceSet {
	
	public double time;
	public Vec3 position;

	public SequenceSet() {
		
	}
	
	public SequenceSet(double time, Vec3 position) {
		this.time=time;
		this.position=position;
	}
}
