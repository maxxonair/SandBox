package animate;

import utils.Quaternion;
import utils.Vec3;

@SuppressWarnings("exports")
public class SequenceSet {
	
	public double time;
	public Vec3 position;
	public Quaternion attitude;

	public SequenceSet() {
		this.time=0;
		this.position = new Vec3(0,0,0);
		this.attitude = new Quaternion(1,0,0,0);
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
