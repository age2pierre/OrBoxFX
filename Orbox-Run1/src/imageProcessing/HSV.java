package imageProcessing;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class HSV implements ImageFilter, Timeable {

	private int channel;
	private long counter = 0;

	protected HSV(int channel) {
		this.channel = channel;
	}

	@Override
	public Mat process(Mat inputIm) {
		counter = System.currentTimeMillis();
		
		Mat inputHSV = new Mat();
		inputHSV.create(inputIm.size(), CvType.CV_8U);
		Imgproc.cvtColor(inputIm, inputHSV, Imgproc.COLOR_BGR2HSV);

		ArrayList<Mat> inputHSVChannels = new ArrayList<>();
		Core.split(inputHSV, inputHSVChannels);

		counter = System.currentTimeMillis() - counter;
		
		return inputHSVChannels.get(channel);
	}

	@Override
	public long getCounter() {
		return counter;
	}

}
