package animate;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import utils.Vec3;
import worldWindow.WorldView;

public class Animation {

	private AnimationFile animationFile; 
	
	private WorldView worldView;
	
	private AnimationTimer animationTimer;
    private Timeline timeline;
	
	private boolean animation_stop;
	private boolean animation_running;
	private boolean animation_pause;
	
	private Integer frame;
	
	
	public Animation(WorldView worldView) {
		this.worldView = worldView;
		animation_stop = true;
		animation_running=false;
		animation_pause=false;
		
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
		
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	
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
		double fac =2;
		for(int ii=0;ii<100;ii++) {
			Vec3 position = new Vec3(0,ii*fac,0);
			SequenceSet seq = new SequenceSet(ii,position);
			sequenceList.add(new SequenceSet(ii,position));
		}
		testAnimationFile.setSequenceList(sequenceList);
		animationFile = testAnimationFile;
	}
	
}