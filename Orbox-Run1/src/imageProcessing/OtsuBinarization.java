package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class OtsuBinarization implements ImageFilter {

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		Imgproc.threshold(inputIm, inputIm, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
		return inputIm;
	}

}
