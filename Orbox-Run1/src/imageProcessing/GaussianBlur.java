package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


public class GaussianBlur implements ImageFilter {
	
	private int size;
	
	public GaussianBlur(int kernelSize) {
		this.size = kernelSize;
	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		Imgproc.blur(inputIm, inputIm, new Size(size, size));
		return inputIm;
	}

}
