package animate;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import utils.Interpolator;
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
            	Vec3 position = findFrameData(animationTime, animationFile, animationFrame, true);
            	
            	worldView.moveModelTo(position.x, position.y, position.z);
            	worldView.setAnimationTime(animationTime);
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
			sequenceList.add(new SequenceSet(ii,position));
		}
		testAnimationFile.setSequenceList(sequenceList);
		animationFile = testAnimationFile;
	}
	
	@SuppressWarnings("unused")
	private Vec3 findFrameData(double time, AnimationFile animationFile, int animationFrame) {
		Vec3 position = new Vec3(0,0,0);
		List<SequenceSet> sequenceList = animationFile.getSequence();
		
		for(int i=animationFrame;i<animationFile.getSequenceLength();i++) {
			if ( Math.abs(sequenceList.get(i).time - time) >  Math.abs(sequenceList.get(i-1).time - time)) {
				this.animationFrame=i;
				return sequenceList.get(i-1).position ;
			}
		}		
		return position;
	}
	
	private Vec3 findFrameData(double time, AnimationFile animationFile, int animationFrame, boolean withInterpolation) {
		Vec3 position = new Vec3(0,0,0);
		double keyTest = 0 ;
		List<SequenceSet> sequenceList = animationFile.getSequence();
		
		for(int i=animationFrame;i<animationFile.getSequenceLength();i++) {
			
			 keyTest = sequenceList.get(i-1).time - time ;
			if ( Math.abs(sequenceList.get(i).time - time) >  Math.abs(keyTest) ){
				this.animationFrame=i;
				if ( keyTest > 0 ) {
					position.x = interpolate.linear(sequenceList.get(i-2).time      , sequenceList.get(i-1).time, 
													sequenceList.get(i-2).position.x, sequenceList.get(i-1).position.x, 
													time - sequenceList.get(i-2).time );
					position.y = interpolate.linear(sequenceList.get(i-2).time      , sequenceList.get(i-1).time, 
													sequenceList.get(i-2).position.y, sequenceList.get(i-1).position.y, 
													time - sequenceList.get(i-2).time );
					position.z = interpolate.linear(sequenceList.get(i-2).time      , sequenceList.get(i-1).time, 
													sequenceList.get(i-2).position.z, sequenceList.get(i-1).position.z, 
													time - sequenceList.get(i-2).time );
				} else if (keyTest == 0) {
					position = sequenceList.get(i-1).position;
				} else {
					position.x = interpolate.linear(sequenceList.get(i-1).time      , sequenceList.get(i).time, 
													sequenceList.get(i-1).position.x, sequenceList.get(i).position.x, 
													time - sequenceList.get(i-1).time );
					position.y = interpolate.linear(sequenceList.get(i-1).time      , sequenceList.get(i).time, 
													sequenceList.get(i-1).position.y, sequenceList.get(i).position.y, 
													time - sequenceList.get(i-1).time );
					position.z = interpolate.linear(sequenceList.get(i-1).time      , sequenceList.get(i).time, 
													sequenceList.get(i-1).position.z, sequenceList.get(i).position.z, 
													time - sequenceList.get(i-1).time );
					
				}
				
				return position;
			}
		}		
		return position;
	}
	
}