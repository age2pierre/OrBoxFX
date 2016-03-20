package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;


public class Erosion implements ImageFilter, Timeable {
	
	private long counter = 0;

	@Override
	public long getCounter() {
		return counter;
	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		Imgproc.erode(inputIm, inputIm, new Mat(), new Point(-1,-1),1);
		return inputIm;
	}

}
