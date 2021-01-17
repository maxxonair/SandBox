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
	private String SEMICOLON_DELIMITER = ";";
	private String COMMA_DELIMITER = ",";
	
	private String delimiter;
	
	public LoadFromFile() {
		delimiter = SPACE_DELIMITER;
	}

	public AnimationFile loadCsvData(File file, double dimensionFactor) throws NumberFormatException, IOException {
		
		AnimationFile animationFile = new AnimationFile();
		List<SequenceSet> sequenceList = new ArrayList<SequenceSet>();
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in 		= new DataInputStream(fstream);
		BufferedReader br 		= new BufferedReader(new InputStreamReader(in));
		String strLine;

		while ( (strLine = br.readLine() ) != null )   {
			
			SequenceSet sequenceSet = new SequenceSet();
			Object[] tokens = strLine.split(delimiter);
			sequenceSet.time 		= Double.parseDouble( (String) tokens[0]  );
			
			sequenceSet.position.x 	= Double.parseDouble( (String) tokens[41] );
			sequenceSet.position.y 	= Double.parseDouble( (String) tokens[42] );
			sequenceSet.position.z 	= Double.parseDouble( (String) tokens[43] );
			
			sequenceSet.attitude.w  = Double.parseDouble( (String) tokens[47] );
			sequenceSet.attitude.x  = Double.parseDouble( (String) tokens[48] );
			sequenceSet.attitude.y  = Double.parseDouble( (String) tokens[49] );
			sequenceSet.attitude.z  = Double.parseDouble( (String) tokens[50] );
			
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
	
	public AnimationFile loadCsvData(File file, boolean doPostProcess, double dimensionFactor) throws NumberFormatException, IOException {
		
		AnimationFile animationFile = new AnimationFile();
		List<SequenceSet> sequenceList = new ArrayList<SequenceSet>();
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in 		= new DataInputStream(fstream);
		BufferedReader br 		= new BufferedReader(new InputStreamReader(in));
		String strLine;

		while ( (strLine = br.readLine() ) != null )   {
			
			SequenceSet sequenceSet = new SequenceSet();
			Object[] tokens = strLine.split(delimiter);
			sequenceSet.time 		= Double.parseDouble( (String) tokens[0]  );
			
			sequenceSet.position.x 	= Double.parseDouble( (String) tokens[1] );
			sequenceSet.position.y 	= Double.parseDouble( (String) tokens[2] );
			sequenceSet.position.z 	= Double.parseDouble( (String) tokens[3] );
			
			sequenceSet.attitude.w  = Double.parseDouble( (String) tokens[4] );
			sequenceSet.attitude.x  = Double.parseDouble( (String) tokens[5] );
			sequenceSet.attitude.y  = Double.parseDouble( (String) tokens[6] );
			sequenceSet.attitude.z  = Double.parseDouble( (String) tokens[7] );
			
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
		
		if (doPostProcess) { 
			try {
				animationFile = postProcess(animationFile, dimensionFactor);
			} catch (Exception exp2) {
				System.out.println("Post-process animation file failed.");
			}
		} 
		 
		return animationFile;
	}

	private AnimationFile postProcess(AnimationFile animationFile, double factor){
		List<SequenceSet> sequenceList = animationFile.getSequence();
		Vec3 referencePosition = sequenceList.get( sequenceList.size() - 1 ).position;
		animationFile.setMaxTime(sequenceList.get(sequenceList.size() - 1).time);
		if ( sequenceList.size() > 1) {
			double dt = animationFile.getSequence().get(1).time - animationFile.getSequence().get(0).time;
			animationFile.setFrequency(1/dt);
		}
		for(int i=0;i<sequenceList.size();i++) {			
			Vec3 position = sequenceList.get(i).position;
			sequenceList.get(i).position.x = (position.x - referencePosition.x) * factor;
			sequenceList.get(i).position.y = (position.y - referencePosition.y) * factor;
			sequenceList.get(i).position.z = (position.z - referencePosition.z) * factor;
		}		
		return animationFile;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getSPACE_DELIMITER() {
		return SPACE_DELIMITER;
	}

	public String getSEMICOLON_DELIMITER() {
		return SEMICOLON_DELIMITER;
	}

	public String getCOMMA_DELIMITER() {
		return COMMA_DELIMITER;
	}

}