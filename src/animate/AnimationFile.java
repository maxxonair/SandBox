package animate;

import java.util.ArrayList;
import java.util.List;

public class AnimationFile {
//------------------------------------------------------------------------------------
// Meta data	
//------------------------------------------------------------------------------------
public String FileName;
//------------------------------------------------------------------------------------
// Content data	
//------------------------------------------------------------------------------------	
private List<SequenceSet> sequenceList;
	
	public AnimationFile() {
		sequenceList = new ArrayList<SequenceSet>();
	}
	
	public AnimationFile(String FileName) {
		this.FileName = FileName;
		sequenceList = new ArrayList<SequenceSet>();
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
	
}
