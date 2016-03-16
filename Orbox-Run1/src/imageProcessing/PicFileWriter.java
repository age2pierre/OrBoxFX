package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


public class PicFileWriter implements ImageFilter {
	private String path;
	
	public PicFileWriter(String argPath) {
		this.path = argPath;
	}
	
	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		if (Imgcodecs.imwrite(path, inputIm))
			return inputIm;
		else
			throw new FilterExecutionException("Unable to save image to " + path + " , check extension.");
	}

}
