package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class HoughCircles implements ImageFilter, Timeable {
	
	private long counter = 0;
	
	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		counter = System.currentTimeMillis();
		
		Mat circles  = new Mat();
		Imgproc.HoughCircles(inputIm, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 200, 200, 100, 100, 800);
		
		for (int x = 0; x < circles.cols(); x++) 
        {
                double vCircle[]=circles.get(0,x);

                Point center =new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
                int radius = (int)Math.round(vCircle[2]);
                // draw the circle center
                Imgproc.circle(inputIm, center, 3,new Scalar(0,0,0), -1, 8, 0 );
                // draw the circle outline
                Imgproc.circle( inputIm, center, radius, new Scalar(0,0, 0), 3, 8, 0 );

        }
		counter = System.currentTimeMillis() - counter;
		return inputIm;
	}
	
	@Override
	public long getCounter() {
		return counter;
	}
}