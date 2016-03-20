package application;


@SuppressWarnings("serial")
public class ParsingScriptException extends Exception {
	
	public ParsingScriptException(String msg) {
		super("Error while parsing script : " + msg);
	}
	
	public ParsingScriptException(String msg, int lineNmber) {
		super("Error found line " + lineNmber + " while parsing script : " + msg);
	}

}
