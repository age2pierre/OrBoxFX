package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;


public class Dilatation implements ImageFilter, Timeable {

	private long counter = 0;
	private int iteration;

	public Dilatation(int argIteration) {
		this.iteration = argIteration;
	}

	@Override
	public long getCounter() {
		return counter;
	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		counter = System.currentTimeMillis();
		Imgproc.dilate(inputIm, inputIm, new Mat(), new Point(-1, -1), iteration);
		counter -= System.currentTimeMillis();
		return inputIm;
	}

}
