package imageProcessing;

import org.opencv.core.Mat;

public class PicTabViewer implements ImageFilter {

	private static int unamedCounter = 0;
	
	private String nameTab = null;

	public PicTabViewer() {
		unamedCounter++;
	}
	
	public PicTabViewer(String arg) {
		this.nameTab = arg;
	}

	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		return inputIm;
	}
	
	public String getNameOfTab(){
		if (nameTab == null)
			return "Tab " + unamedCounter;
		else
			return nameTab;
	}

}
