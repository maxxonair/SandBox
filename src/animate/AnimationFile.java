package animate;

import java.util.ArrayList;
import java.util.List;

public class AnimationFile {
//------------------------------------------------------------------------------------
// Meta data	
//------------------------------------------------------------------------------------
public String FileName;
private double maxTime;
private double frequency;
//------------------------------------------------------------------------------------
// Content data	
//------------------------------------------------------------------------------------	
private List<SequenceSet> sequenceList;
	
	public AnimationFile() {
		sequenceList = new ArrayList<SequenceSet>();
		maxTime = 0 ;
		frequency = 0;
	}
	
	public AnimationFile(String FileName) {
		this.FileName = FileName;
		sequenceList = new ArrayList<SequenceSet>();
		maxTime = 0 ;
		frequency = 0;
	}
	
	public List<SequenceSet> getSequence() {
		return sequenceList;
	}
	
	public void setSequenceList(List<SequenceSet> sequenceList) {
		this.sequenceList = sequenceList;
	}

	public int getSequenceLength() {
		return sequenceList.size();		
	}

	public double getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(double maxTime) {
		this.maxTime = maxTime;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
	
}
