package imageProcessing;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ColorHistogram implements ImageFilter, Timeable{
	
	private long counter = 0;
	@Override
	public long getCounter() {
		return counter;
	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		counter = System.currentTimeMillis();
		
		Mat inputHSV = new Mat();
		Imgproc.cvtColor(inputIm, inputHSV, Imgproc.COLOR_BGR2HSV);
		
		ArrayList<Mat> HSV = new ArrayList<>();
		Core.split( inputHSV, HSV );
		
		MatOfInt histSize = new MatOfInt(256);
		final MatOfFloat histRange = new MatOfFloat(0f, 256f);
		
		Mat h_hist = new Mat(); 
		Imgproc.calcHist(HSV, new MatOfInt(0), new Mat(), h_hist, histSize, histRange);

		int hist_h = 512;
	    int hist_w = 512;
	    long bin_w = Math.round((double) hist_w / 256);
		
		Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC1);
	    histImage.setTo(new Scalar(0,0,0));
		
		Core.normalize(h_hist, h_hist, 3, histImage.rows(), Core.NORM_MINMAX);
		
	    for (int i = 1; i < 256; i++) {
	        
	    	Point p1 = new Point(bin_w * (i - 1), hist_h- Math.round(h_hist.get(i - 1, 0)[0]));
	        Point p2 = new Point(bin_w * (i), hist_h - Math.round(h_hist.get(i, 0)[0]));
	        Imgproc.line(histImage, p1, p2, new Scalar(255, 255, 255), 2, 8, 0);
	        
	    }
		
		counter = System.currentTimeMillis() - counter;
		return histImage;
	}

}
