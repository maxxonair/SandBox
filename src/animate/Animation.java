package animate;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import utils.Interpolator;
import utils.Quaternion;
import utils.Vec3;
import worldWindow.WorldView;

public class Animation {

	private AnimationFile animationFile; 
	private int animationFrame;
	private Interpolator interpolate = new Interpolator();
	
	@SuppressWarnings("unused")
	private WorldView worldView;
	
	private AnimationTimer animationTimer;
    private Timeline timeline;
	
	private boolean animation_stop;
	private boolean animation_running;
	private boolean animation_pause;
	
	//private Integer frame;
	private long startTime;
	private boolean timeFlag=false;
	
	private double animationTime;
	
	
	public Animation(WorldView worldView) {
		this.worldView = worldView;
		animation_stop = true;
		animation_running=false;
		animation_pause=false;
		animationFrame = 1;
		animationTime=0;
		
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
		
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	animationTime = 0;
            	if ( !timeFlag ) {
            		startTime = now;
            		timeFlag =true;
            	} else {
            		animationTime = (now - startTime)*0.000000001;
            	}
            	try {
            		
            		// Generate interpolated state
	            	SequenceSet set = interpolateData(animationTime, animationFile, animationFrame);
	            	//SequenceSet set = returnData(animationTime, animationFile, animationFrame);
	            	// Update model position
	            	worldView.moveModelTo(set.position.x, set.position.y, set.position.z);
	            	// Update model attitude
	            	worldView.roateModelTo(set.attitude);
	            	// Update model HUD info
	            	worldView.setAnimationTime(animationTime);
	            	worldView.updateModelAttitude(set.attitude);
	            	
            	} catch (Exception exp ) {
            		
            	}
            }
        };
	}
	
	public void start() {
		animationTimer.start();
		animation_stop = false;
		animation_running=true;
		animation_pause=false;
	}
	
	public void stop() {
		animationTimer.stop();
		animation_stop = true;
		animation_running=false;
		animation_pause=false;
		timeFlag = false;
		animationFrame = 1;
	}
	
	public void pause() {
		
	}

	public AnimationFile getAnimationFile() {
		return animationFile;
	}

	public void setAnimationFile(AnimationFile animationFile) {
		this.animationFile = animationFile;
	}

	public boolean isAnimation_stop() {
		return animation_stop;
	}

	public boolean isAnimation_running() {
		return animation_running;
	}

	public boolean isAnimation_pause() {
		return animation_pause;
	}
	
	public void setTestAnimationFile() {
		// Test Animation File for functional testing 
		AnimationFile testAnimationFile = new AnimationFile("TestAnimation");
		List<SequenceSet> sequenceList = new ArrayList<SequenceSet>();
		double fac =-7;
		for(int ii=0;ii<60;ii++) {
			Vec3 position = new Vec3(ii*ii*fac,ii*ii*fac,ii*fac);
			Quaternion attitude = new Quaternion( 1 , ii*0.01 , 0 , 0 );
			//attitude.normalize();
			sequenceList.add(new SequenceSet(ii,position, attitude));
		}
		testAnimationFile.setSequenceList(sequenceList);
		animationFile = testAnimationFile;
	}
	
	@SuppressWarnings("unused")
	private SequenceSet returnData(double time, AnimationFile animationFile, int animationFrame) {
		SequenceSet sequenceSet = new SequenceSet();
		
		List<SequenceSet> sequenceList = animationFile.getSequence();
		
		for(int i=animationFrame;i<animationFile.getSequenceLength();i++) {
			if ( Math.abs(sequenceList.get(i).time - time) >  Math.abs(sequenceList.get(i-1).time - time)) {
				this.animationFrame=i;
				sequenceSet = sequenceList.get(i-1);
				sequenceSet.time = time;
				return sequenceSet ;
			}
		}		
		return sequenceSet;
	}
	
	private SequenceSet interpolateData(double time, AnimationFile animationFile, int animationFrame) {
		SequenceSet sequenceSet = new SequenceSet();
		
		sequenceSet.time = time;
		
		double keyTest = 0 ;
		List<SequenceSet> sequenceList = animationFile.getSequence();
		
		for(int i=animationFrame;i<animationFile.getSequenceLength();i++) {
			
			 keyTest = sequenceList.get(i-1).time - time ;
			if ( Math.abs(sequenceList.get(i).time - time) >  Math.abs(keyTest) ){
				this.animationFrame=i;
				if ( keyTest > 0 ) {
					// Interpolate Position:
					sequenceSet.position.x = interpolate.linear(sequenceList.get(i-2).time      , sequenceList.get(i-1).time, 
													sequenceList.get(i-2).position.x, sequenceList.get(i-1).position.x, 
													time - sequenceList.get(i-2).time );
					sequenceSet.position.y = interpolate.linear(sequenceList.get(i-2).time      , sequenceList.get(i-1).time, 
													sequenceList.get(i-2).position.y, sequenceList.get(i-1).position.y, 
													time - sequenceList.get(i-2).time );
					sequenceSet.position.z = interpolate.linear(sequenceList.get(i-2).time      , sequenceList.get(i-1).time, 
													sequenceList.get(i-2).position.z, sequenceList.get(i-1).position.z, 
													time - sequenceList.get(i-2).time );
					// Interpolate Attitude
					sequenceSet.attitude.w = interpolate.linear(sequenceList.get(i-2).time      , sequenceList.get(i-1).time, 
													sequenceList.get(i-2).attitude.w, sequenceList.get(i-1).attitude.w, 
													time - sequenceList.get(i-2).time );
					sequenceSet.attitude.x = interpolate.linear(sequenceList.get(i-2).time      , sequenceList.get(i-1).time, 
													sequenceList.get(i-2).attitude.x, sequenceList.get(i-1).attitude.x, 
													time - sequenceList.get(i-2).time );
					sequenceSet.attitude.y = interpolate.linear(sequenceList.get(i-2).time      , sequenceList.get(i-1).time, 
													sequenceList.get(i-2).attitude.y, sequenceList.get(i-1).attitude.y, 
													time - sequenceList.get(i-2).time );
					sequenceSet.attitude.z = interpolate.linear(sequenceList.get(i-2).time      , sequenceList.get(i-1).time, 
													sequenceList.get(i-2).attitude.z, sequenceList.get(i-1).attitude.z, 
													time - sequenceList.get(i-2).time );
				} else if (keyTest == 0) {
					// Position from file 
					sequenceSet.position = sequenceList.get(i-1).position;
					sequenceSet.attitude = sequenceList.get(i-1).attitude;
					
				} else {
					// Interpolate Position: 
					sequenceSet.position.x = interpolate.linear(sequenceList.get(i-1).time      , sequenceList.get(i).time, 
													sequenceList.get(i-1).position.x, sequenceList.get(i).position.x, 
													time - sequenceList.get(i-1).time );
					sequenceSet.position.y = interpolate.linear(sequenceList.get(i-1).time      , sequenceList.get(i).time, 
													sequenceList.get(i-1).position.y, sequenceList.get(i).position.y, 
													time - sequenceList.get(i-1).time );
					sequenceSet.position.z = interpolate.linear(sequenceList.get(i-1).time      , sequenceList.get(i).time, 
													sequenceList.get(i-1).position.z, sequenceList.get(i).position.z, 
													time - sequenceList.get(i-1).time );	
					// Interpolate Attitude: 
					sequenceSet.attitude.w = interpolate.linear(sequenceList.get(i-1).time      , sequenceList.get(i).time, 
													sequenceList.get(i-1).attitude.w, sequenceList.get(i).attitude.w, 
													time - sequenceList.get(i-1).time );
					sequenceSet.attitude.x = interpolate.linear(sequenceList.get(i-1).time      , sequenceList.get(i).time, 
													sequenceList.get(i-1).attitude.x, sequenceList.get(i).attitude.x, 
													time - sequenceList.get(i-1).time );
					sequenceSet.attitude.y = interpolate.linear(sequenceList.get(i-1).time      , sequenceList.get(i).time, 
													sequenceList.get(i-1).attitude.y, sequenceList.get(i).attitude.y, 
													time - sequenceList.get(i-1).time );
					sequenceSet.attitude.z = interpolate.linear(sequenceList.get(i-1).time      , sequenceList.get(i).time, 
													sequenceList.get(i-1).attitude.z, sequenceList.get(i).attitude.z, 
													time - sequenceList.get(i-1).time );
				}
				
				return sequenceSet;
			}
		}		
		return sequenceSet;
	}
	
}