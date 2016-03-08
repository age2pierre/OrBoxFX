package imageProcessing;

import org.opencv.core.Mat;

public interface ImageFilter {
	public Mat process(Mat inputIm) throws FilterExecutionException;
}
