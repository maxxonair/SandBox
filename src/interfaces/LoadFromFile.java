package interfaces;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import animate.AnimationFile;
import animate.SequenceSet;
import utils.Vec3;


public class LoadFromFile {
	
	private String SPACE_DELIMITER = " ";
	
	public LoadFromFile() {
		
	}

	public AnimationFile loadTextData(File file, double dimensionFactor) throws NumberFormatException, IOException {
		
		AnimationFile animationFile = new AnimationFile();
		List<SequenceSet> sequenceList = new ArrayList<SequenceSet>();
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in 		= new DataInputStream(fstream);
		BufferedReader br 		= new BufferedReader(new InputStreamReader(in));
		String strLine;

		while ( (strLine = br.readLine() ) != null )   {
			
			SequenceSet sequenceSet = new SequenceSet();
			Object[] tokens = strLine.split(SPACE_DELIMITER);
			sequenceSet.time 		= Double.parseDouble( (String) tokens[0]  );
			sequenceSet.position.x 	= Double.parseDouble( (String) tokens[41] );
			sequenceSet.position.y 	= Double.parseDouble( (String) tokens[42] );
			sequenceSet.position.z 	= Double.parseDouble( (String) tokens[43] );
			sequenceList.add(sequenceSet);
			
		}
		try {
		fstream.close();
	    in.close();
		br.close();
		} catch (Exception exp ) {
			System.out.println("Closing File reader failed.");
		}
		animationFile.setSequenceList(sequenceList);
		try {
			animationFile = postProcess(animationFile, dimensionFactor);
		} catch (Exception exp2) {
			System.out.println("Post-process animation file failed.");
		}
		 
		return animationFile;
	}

	private AnimationFile postProcess(AnimationFile animationFile, double factor){
		List<SequenceSet> sequenceList = animationFile.getSequence();
		Vec3 referencePosition = sequenceList.get( sequenceList.size() - 1 ).position;
		animationFile.setMaxTime(sequenceList.get(sequenceList.size() - 1).time);
		for(int i=0;i<sequenceList.size();i++) {
			
			Vec3 position = sequenceList.get(i).position;
			sequenceList.get(i).position.x = (position.x - referencePosition.x) * factor;
			sequenceList.get(i).position.y = (position.y - referencePosition.y) * factor;
			sequenceList.get(i).position.z = (position.z - referencePosition.z) * factor;
		}		
		return animationFile;
	}

}