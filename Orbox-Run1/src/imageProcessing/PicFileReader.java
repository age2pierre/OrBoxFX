package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


public class PicFileReader implements ImageFilter {
	
	private String path;
	
	public PicFileReader(String filePath) {
		this.path = filePath;
	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		Mat out = Imgcodecs.imread(path, Imgcodecs.IMREAD_ANYCOLOR);
		if(out != null)
			return out;
		else
			throw new FilterExecutionException("The path [ " + path + " ] is not a valid picture file." );
	}

}
