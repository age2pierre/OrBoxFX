package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class ColorMap implements ImageFilter {


	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		Mat outputIm = new Mat();
		Imgproc.applyColorMap(inputIm, outputIm, Imgproc.COLORMAP_JET);
		return outputIm;
	}

}
