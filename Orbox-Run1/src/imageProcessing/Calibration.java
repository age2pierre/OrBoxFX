package imageProcessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Calibration implements ImageFilter {

	private Mat intrinsic = null;
	private Mat distCoeffs = null;


	protected boolean openJson(String path) {
		try {
			StringBuffer txt = new StringBuffer("");
			Files.lines(Paths.get(path)).forEach(s -> txt.append(s));
			JsonParser parser = new JsonParser();
			JsonObject jsonObj = parser.parse(txt.toString()).getAsJsonObject();
			JsonObject intrinsicJson = jsonObj.get("intrisic").getAsJsonObject();
			JsonObject distCoeffsJson = jsonObj.get("distCoeffs").getAsJsonObject();
			intrinsic = Utilitary.matFromJson(intrinsicJson);
			distCoeffs = Utilitary.matFromJson(distCoeffsJson);
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		if(intrinsic == null || distCoeffs == null)
			throw new FilterExecutionException("The Calibration filter as not been initialize.");
		Mat undistored = new Mat();
		Imgproc.undistort(inputIm, undistored, intrinsic, distCoeffs);
		return undistored;
	}

}
