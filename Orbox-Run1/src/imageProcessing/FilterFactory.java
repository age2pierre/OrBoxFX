package imageProcessing;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.ParsingScriptException;

public class FilterFactory {

	public ImageFilter create(String inst, String optionList, int lineNumber) throws ParsingScriptException {
		switch(inst.toUpperCase()){
		case "HUE" :
			if (optionList.matches("[\\w]*"))
				return new HSV(0);
			else
				throw new ParsingScriptException(lineNumber);
		case "SATURATION" :
			if (optionList.matches("[\\w]*"))
				return new HSV(1);
			else
				throw new ParsingScriptException(lineNumber);
		case "VALUE" :
			if (optionList.matches("[\\w]*"))
				return new HSV(2);
			else
				throw new ParsingScriptException(lineNumber);
		case "OPEN" :
			String argPath = parseOptionForPicFileReader(optionList);
			if (argPath != null)
				return new PicFileReader(argPath);
			else
				throw new ParsingScriptException(lineNumber);
		case "SHOW" :
			String argName = parseOptionForPicTabViewer(optionList);
			if(argName != null)
				return new PicTabViewer(argName);
			else
				return new PicTabViewer();
		case "GAUSSIANBLUR" :
			int kernelSize = parseOptionForGaussianBlur(optionList);
			if (kernelSize == -1)
				throw new ParsingScriptException(lineNumber);
			else
				return new GaussianBlur(kernelSize);
		case "OTSU" :
			if (optionList.matches("[\\w]*"))
				return new OtsuBinarization();
			else
				throw new ParsingScriptException(lineNumber);
		case "COLORMAP" :
			if (optionList.matches("[\\w]*"))
				return new ColorMap();
			else
				throw new ParsingScriptException(lineNumber);
		case "CALIBRATION" :
			String path = parseOptionForPicFileReader(optionList);
			if(path != null){
				Calibration calib = new Calibration();
				calib.openJson(path);
				return calib;
			}
			else				
				throw new ParsingScriptException(lineNumber);
		}
		throw new ParsingScriptException(lineNumber);
	}
	
	private String parseOptionForPicFileReader(String optList){
		Pattern pat = Pattern.compile("\\A\\s*([^\\s]+)\\s*\\Z");
		Matcher mat = pat.matcher(optList);
		
		if(mat.find()){
			String uri = mat.group(1);
			if(new File(uri).isFile())
				return uri;
		}
		return null;
	}
	
	private String parseOptionForPicTabViewer(String optList) {
		Pattern pat = Pattern.compile("\\s*((?:(?:-n)|(?:--name))\\s+(\\w+))\\s*");
		Matcher mat = pat.matcher(optList);
		
		if(mat.find()) {
			return mat.group(2);
		}
		return null;
	}
	
	private int parseOptionForGaussianBlur(String optList) {
		Pattern pat = Pattern.compile("\\s*((?:(?:-s)|(?:--size))\\s+(\\d+))\\s*");
		Matcher mat = pat.matcher(optList);
		
		if(mat.find()) {
			return Integer.parseInt(mat.group(2));
		}
		return -1;
	}
}
