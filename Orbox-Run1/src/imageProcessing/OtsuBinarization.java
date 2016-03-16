package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class OtsuBinarization implements ImageFilter, Timeable {

	
	private long counter = 0;
	
	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		counter = System.currentTimeMillis();
		
		Imgproc.threshold(inputIm, inputIm, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
		
		counter = System.currentTimeMillis() - counter;
		
		return inputIm;
	}

	@Override
	public long getCounter() {
		return counter;
	}

}
