package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


public class GaussianBlur implements ImageFilter, Timeable {
	
	private int size;
	private long counter = 0;
	
	public GaussianBlur(int kernelSize) {
		this.size = kernelSize;
	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		counter = System.currentTimeMillis();
		Imgproc.blur(inputIm, inputIm, new Size(size, size));
		counter = System.currentTimeMillis() - counter;
		return inputIm;
	}
	
	@Override
	public long getCounter() {
		return counter;
	}

}
