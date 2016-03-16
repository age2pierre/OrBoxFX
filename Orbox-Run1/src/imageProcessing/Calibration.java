package imageProcessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Calibration implements ImageFilter, Timeable {

	private Mat intrinsic = null;
	private Mat distCoeffs = null;

	private long counter = 0;


	protected boolean openJson(String path) {
		try {
			long currentCounter = System.currentTimeMillis();
			StringBuffer txt = new StringBuffer("");
			Files.lines(Paths.get(path)).forEach(s -> txt.append(s));
			JsonParser parser = new JsonParser();
			JsonObject jsonObj = parser.parse(txt.toString()).getAsJsonObject();
			JsonObject intrinsicJson = jsonObj.get("intrisic").getAsJsonObject();
			JsonObject distCoeffsJson = jsonObj.get("distCoeffs").getAsJsonObject();
			intrinsic = Utilitary.matFromJson(intrinsicJson);
			distCoeffs = Utilitary.matFromJson(distCoeffsJson);
			currentCounter = System.currentTimeMillis() - currentCounter;
			counter += currentCounter;
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		if (intrinsic == null || distCoeffs == null)
			throw new FilterExecutionException("The Calibration filter as not been initialize.");
		long currentCounter = System.currentTimeMillis();
		Imgproc.undistort(inputIm, inputIm, intrinsic, distCoeffs);
		currentCounter = System.currentTimeMillis() - currentCounter;
		counter += currentCounter;
		return inputIm;
	}

	@Override
	public long getCounter() {
		return counter;
	}

}
