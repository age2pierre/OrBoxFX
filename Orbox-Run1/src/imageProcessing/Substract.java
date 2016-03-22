package imageProcessing;

import org.opencv.core.Core;
import org.opencv.core.Mat;


public class Substract implements ImageFilter, Timeable {
	
	private long counter = 0;
	private PicFileReader reader;
	
	public Substract(String path) {
		reader = new PicFileReader(path);
	}

	@Override
	public long getCounter() {
		return counter;
	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		Mat substractor = reader.process(null);
		counter = System.currentTimeMillis();
		Core.absdiff(inputIm, substractor, inputIm);
		counter -= System.currentTimeMillis();
		return inputIm;
	}

}
