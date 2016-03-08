package application;


@SuppressWarnings("serial")
public class ParsingScriptException extends Exception {
	private int lineNumberFaulty;
	
	
	public ParsingScriptException(int lineNumber) {
		this.lineNumberFaulty = lineNumber;
	}
	
	public int getLineNumberFaulty() {
		return this.lineNumberFaulty;
	}

}
