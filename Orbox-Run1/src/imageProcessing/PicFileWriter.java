package imageProcessing;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


public class PicFileWriter implements ImageFilter {
	private String path;
	
	public PicFileWriter(String argPath) {
		this.path = argPath;
	}
	
	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		Mat picToSave = new Mat();
		inputIm.copyTo(picToSave);
		
		if(picToSave.channels() == 1) {
			if( path.endsWith(".png") || path.endsWith(".jp2"))
				picToSave.convertTo(picToSave, CvType.CV_16UC1);
			else
				picToSave.convertTo(picToSave, CvType.CV_8UC1);
		}
		else {
			picToSave.convertTo(picToSave, CvType.CV_8UC3);
		}
		
		if(!Imgcodecs.imwrite(path, picToSave))
			throw new FilterExecutionException("Unable to save image to " + path + " , check extension.");
		
		return inputIm;
	}

}
