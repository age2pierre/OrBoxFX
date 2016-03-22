package imageProcessing;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.ParsingScriptException;

/**
 * 
 * @author Robi
 * 
 *         This class is used to create {@link ImageFilter} in a safe way by
 *         parsing a string;
 * 
 *         The common syntax is : INSTRUCTION [-options] [args...]
 * 
 *         HUE
 *         SATURATION
 *         VALUE
 *         OPEN [an absolute path to a *.jpg | *.png | *.jp2 | *.bmp]
 *         GAUSSIANBLUR [--size|-s] [POSITIVE INT]
 *         OTSU
 *         COLORMAP
 *         CALIBRATION [an absolute path to a *.json]
 *         SNAPSHOT
 *         SAVE [an absolute path to a *.jpg | *.png | *.jp2 | *.bmp]
 *         SUBSTRACT [an absolute path to a *.jpg | *.png | *.jp2 | *.bmp]
 *         ERODE [--iteration|-i] [POSITIVE INT]
 *         DILATE [--iteration|-i] [POSITIVE INT]
 *
 *
 */
public class FilterFactory {


	public ImageFilter create(String toParse) throws ParsingScriptException {
		String inst = null;
		String optionList = null;

		Pattern linePattern = Pattern.compile("\\s*(\\w+)(.*)");
		Matcher lineMatcher = linePattern.matcher(toParse);

		if (lineMatcher.find()) {
			inst = lineMatcher.group(1);
			optionList = lineMatcher.group(2);
		}

		switch (inst.toUpperCase()) {
		case "HUE":
			if (optionList.matches("[\\w]*"))
				return new HSV(0);
			else throw new ParsingScriptException("HUE filter doesn't accept option");
		case "SATURATION":
			if (optionList.matches("[\\w]*"))
				return new HSV(1);
			else throw new ParsingScriptException("SATURATION filter doesn't accept option");
		case "VALUE":
			if (optionList.matches("[\\w]*"))
				return new HSV(2);
			else throw new ParsingScriptException("VALUE filter doesn't accept option");
		case "OPEN":
			String argPath = parseOptionForPicFileReader(optionList);
			if (argPath != null)
				return new PicFileReader(argPath);
			else throw new ParsingScriptException("OPEN expects an absolute path pointing to picture file");
		case "SHOW":
			String argName = parseOptionForPicTabViewer(optionList);
			if (argName != null)
				return new PicTabViewer(argName);
			else return new PicTabViewer();
		case "GAUSSIANBLUR":
			int kernelSize = parseOptionForGaussianBlur(optionList);
			if (kernelSize == -1)
				throw new ParsingScriptException("GAUSSIANBLUR expects --size|-s [POSITIVE INT]");
			else return new GaussianBlur(kernelSize);
		case "OTSU":
			if (optionList.matches("[\\w]*"))
				return new OtsuBinarization();
			else throw new ParsingScriptException("OTSU filter doesn't accept option");
		case "COLORMAP":
			if (optionList.matches("[\\w]*"))
				return new ColorMap();
			else throw new ParsingScriptException("COLORMAP filter doesn't accept option");
		case "CALIBRATION":
			String path = parseOptionForPicFileReader(optionList);
			if (path != null) {
				Calibration calib = new Calibration();
				calib.openJson(path);
				return calib;
			}
			else throw new ParsingScriptException("CALIBRATION expects an absolute path pointing to json file");
		case "SNAPSHOT":
			if (optionList.matches("[\\w]*"))
				return new CameraGrabber();
			else throw new ParsingScriptException("SNAPSHOT filter doesn't accept option");
		case "SAVE":
			String pathOutput = parseOptionForPicFileWriter(optionList);
			if (pathOutput != null)
				return new PicFileWriter(pathOutput);
			else throw new ParsingScriptException("SAVE expects an absolute path pointing to picture file");
		case "SUBSTRACT":
			String pathToSubstract = parseOptionForPicFileReader(optionList);
			if (pathToSubstract != null)
				return new Substract(pathToSubstract);
			else throw new ParsingScriptException("SUBSTRACT expects an absolute path pointing to picture file");
		case "ERODE" :
			int argIteration = parseOptionForErosion(optionList);
			if (argIteration == -1)
				throw new ParsingScriptException("ERODE expects --iteration|-i [POSITIVE INT]");
			else return new Erosion(argIteration);
		case "DILATE" :
			int argIte = parseOptionForErosion(optionList);
			if (argIte == -1)
				throw new ParsingScriptException("DILATE expects --iteration|-i [POSITIVE INT]");
			else return new Dilatation(argIte);
			
		}
		throw new ParsingScriptException("The parser expects one valid instruction per line");
	}

	private String parseOptionForPicFileReader(String optList) {
		Pattern pat = Pattern.compile("\\A\\s*([^\\s]+)\\s*\\Z");
		Matcher mat = pat.matcher(optList);

		if (mat.find()) {
			String uri = mat.group(1);
			if (new File(uri).isFile())
				return uri;
		}
		return null;
	}

	private String parseOptionForPicFileWriter(String optList) {
		Pattern pat = Pattern.compile("\\A\\s*([^\\s]+((\\.jpg)|(\\.jpeg)|(\\.png)|(\\.jp2)|(\\.bmp)))\\s*\\Z");
		Matcher mat = pat.matcher(optList);

		if (mat.find()) {
			return mat.group(1);
		}
		return null;
	}

	private String parseOptionForPicTabViewer(String optList) {
		Pattern pat = Pattern.compile("\\s*((?:(?:-n)|(?:--name))\\s+(\\w+))\\s*");
		Matcher mat = pat.matcher(optList);

		if (mat.find()) {
			return mat.group(2);
		}
		return null;
	}

	private int parseOptionForGaussianBlur(String optList) {
		Pattern pat = Pattern.compile("\\s*((?:(?:-s)|(?:--size))\\s+(\\d+))\\s*");
		Matcher mat = pat.matcher(optList);

		if (mat.find()) {
			return Integer.parseInt(mat.group(2));
		}
		return -1;
	}
	
	private int parseOptionForErosion(String optList) {
		Pattern pat = Pattern.compile("\\s*((?:(?:-i)|(?:--iteration))\\s+(\\d+))\\s*");
		Matcher mat = pat.matcher(optList);

		if (mat.find()) {
			return Integer.parseInt(mat.group(2));
		}
		return -1;
	}
}
