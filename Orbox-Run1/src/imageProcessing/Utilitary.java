package imageProcessing;

import java.io.ByteArrayInputStream;

import org.apache.commons.codec.binary.Base64;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.scene.image.Image;
import net.semanticmetadata.lire.utils.SerializationUtils;

public class Utilitary {

	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
	 * 
	 * @param frame
	 *            the {@link Mat} representing the current frame
	 * @return the {@link Image} to show
	 */
	
	public static Image mat2Image(Mat frame) {
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer
		Imgcodecs.imencode(".jpg", frame, buffer);
		// build and return an Image created from the image encoded in the
		// buffer
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
	
	/**
	 * Take an OpenCV Mat object and return a string formated as an JSON object for seraliazation
	 * @param mat
	 * @return
	 */
	public static String matToJson(Mat mat) {
		JsonObject obj = new JsonObject();

		if (mat.isContinuous()) {
			int cols = mat.cols();
			int rows = mat.rows();
			int elemSize = (int) mat.elemSize();
			int type = mat.type();

			obj.addProperty("rows", rows);
			obj.addProperty("cols", cols);
			obj.addProperty("type", type);

			// We cannot set binary data to a json object, so:
			// Encoding data byte array to Base64.
			String dataString;

			if (type == CvType.CV_32S || type == CvType.CV_32SC2 || type == CvType.CV_32SC3 || type == CvType.CV_16S) {
				int[] data = new int[cols * rows * elemSize];
				mat.get(0, 0, data);
				dataString = new String(Base64.encodeBase64(SerializationUtils.toByteArray(data)));
			}
			else if (type == CvType.CV_32F || type == CvType.CV_32FC2) {
				float[] data = new float[cols * rows * elemSize];
				mat.get(0, 0, data);
				dataString = new String(Base64.encodeBase64(SerializationUtils.toByteArray(data)));
			}
			else if (type == CvType.CV_64F || type == CvType.CV_64FC2) {
				double[] data = new double[cols * rows * elemSize];
				mat.get(0, 0, data);
				dataString = new String(Base64.encodeBase64(SerializationUtils.toByteArray(data)));
			}
			else if (type == CvType.CV_8U) {
				byte[] data = new byte[cols * rows * elemSize];
				mat.get(0, 0, data);
				dataString = new String(Base64.encodeBase64(data));
			}
			else {

				throw new UnsupportedOperationException("unknown type");
			}
			obj.addProperty("data", dataString);

			Gson gson = new Gson();
			String json = gson.toJson(obj);

			return json;
		}
		else {
			System.out.println("Mat not continuous.");
		}
		return "{}";
	}

	/**
	 * 
	 * @param json the string containing a serialized opencv mat
	 * @return the mat object corresponding
	 */
	public static Mat matFromJson(String json) {

		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(json).getAsJsonObject();

		int rows = jsonObject.get("rows").getAsInt();
		int cols = jsonObject.get("cols").getAsInt();
		int type = jsonObject.get("type").getAsInt();

		Mat mat = new Mat(rows, cols, type);

		String dataString = jsonObject.get("data").getAsString();
		if (type == CvType.CV_32S || type == CvType.CV_32SC2 || type == CvType.CV_32SC3 || type == CvType.CV_16S) {
			int[] data = SerializationUtils.toIntArray(Base64.decodeBase64(dataString.getBytes()));
			mat.put(0, 0, data);
		}
		else if (type == CvType.CV_32F || type == CvType.CV_32FC2) {
			float[] data = SerializationUtils.toFloatArray(Base64.decodeBase64(dataString.getBytes()));
			mat.put(0, 0, data);
		}
		else if (type == CvType.CV_64F || type == CvType.CV_64FC2) {
			double[] data = SerializationUtils.toDoubleArray(Base64.decodeBase64(dataString.getBytes()));
			mat.put(0, 0, data);
		}
		else if (type == CvType.CV_8U) {
			byte[] data = Base64.decodeBase64(dataString.getBytes());
			mat.put(0, 0, data);
		}
		else {

			throw new UnsupportedOperationException("unknown type");
		}
		return mat;
	}

	/**
	 * 
	 * @param jsonObject containing a serialized opencv mat
	 * @return the corresponding 
	 */
	public static Mat matFromJson(JsonObject jsonObject) {

		int rows = jsonObject.get("rows").getAsInt();
		int cols = jsonObject.get("cols").getAsInt();
		int type = jsonObject.get("type").getAsInt();

		Mat mat = new Mat(rows, cols, type);

		String dataString = jsonObject.get("data").getAsString();
		if (type == CvType.CV_32S || type == CvType.CV_32SC2 || type == CvType.CV_32SC3 || type == CvType.CV_16S) {
			int[] data = SerializationUtils.toIntArray(Base64.decodeBase64(dataString.getBytes()));
			mat.put(0, 0, data);
		}
		else if (type == CvType.CV_32F || type == CvType.CV_32FC2) {
			float[] data = SerializationUtils.toFloatArray(Base64.decodeBase64(dataString.getBytes()));
			mat.put(0, 0, data);
		}
		else if (type == CvType.CV_64F || type == CvType.CV_64FC2) {
			double[] data = SerializationUtils.toDoubleArray(Base64.decodeBase64(dataString.getBytes()));
			mat.put(0, 0, data);
		}
		else if (type == CvType.CV_8U) {
			byte[] data = Base64.decodeBase64(dataString.getBytes());
			mat.put(0, 0, data);
		}
		else {

			throw new UnsupportedOperationException("unknown type");
		}
		return mat;
	}
}
