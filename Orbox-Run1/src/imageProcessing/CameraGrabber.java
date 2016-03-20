package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class CameraGrabber implements ImageFilter, Timeable {
	
	private long counter = 0;
	private VideoCapture capture = new VideoCapture();
	private Mat frame = new Mat();

	@Override
	public long getCounter() {
		return counter;
	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		counter = System.currentTimeMillis();
		capture.open(0);
		if(capture.isOpened()) {
			//capture.set(Videoio.CAP_PROP_FORMAT, Videoio.CAP_MODE_BGR);
			capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 1280);
			capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 720);
			capture.read(frame);
			counter -= System.currentTimeMillis();
			capture.release();
			return frame;
		}
		else
			throw new FilterExecutionException("Could not open the camera flux!");
	}

}
