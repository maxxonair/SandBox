package interfaces;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import animate.SequenceSet;
import utils.Vec3;


public class LoadFromFile {
	
	public LoadFromFile() {
		
	}

	public List<SequenceSet> loadTextData(File file, double dimensionFactor) {
		List<SequenceSet> sequenceList = new ArrayList<SequenceSet>();
		
		// Read all data from file: 
		FileInputStream fstream = null;
		try{ 
			fstream = new FileInputStream(file);} catch(IOException eIO) { System.out.println(eIO);}
			          DataInputStream in = new DataInputStream(fstream);
			          BufferedReader br = new BufferedReader(new InputStreamReader(in));
			          String strLine;
			try {
					while ((strLine = br.readLine()) != null )   {
						Object[] tokens = strLine.split(" ");
						SequenceSet sequenceSet = new SequenceSet();
						sequenceSet.time = Double.parseDouble((String) tokens[0]);
						sequenceSet.position.x = Double.parseDouble((String) tokens[41]);
						sequenceSet.position.y = Double.parseDouble((String) tokens[42]);
						sequenceSet.position.z = Double.parseDouble((String) tokens[43]);
						
						sequenceList.add(sequenceSet);
					}
			fstream.close();
		    in.close();
			br.close();
		
		} catch (NullPointerException | IOException eNPE) { 
			  System.out.println("Read text data, Nullpointerexception");
		}catch(IllegalArgumentException eIAE) {
		  System.out.println("Read text data, illegal argument error");
		}
		 sequenceList = postProcess(sequenceList, dimensionFactor);
		 return sequenceList;
	}

	private List<SequenceSet> postProcess(List<SequenceSet> sequenceList, double factor){
		Vec3 referencePosition = sequenceList.get( sequenceList.size() - 1 ).position;
		for(int i=0;i<sequenceList.size();i++) {
			Vec3 position = sequenceList.get(i).position;
			sequenceList.get(i).position.x = (position.x - referencePosition.x) * factor;
			sequenceList.get(i).position.y = (position.y - referencePosition.y) * factor;
			sequenceList.get(i).position.z = (position.z - referencePosition.z) * factor;
			
			System.out.println(i+" "+
							   sequenceList.get(i).position.x+" "+
							   sequenceList.get(i).position.y+" "+
							   sequenceList.get(i).position.z
							);
		}
		return sequenceList;
	}

}