package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opencv.core.Mat;

import imageProcessing.FilterExecutionException;
import imageProcessing.FilterFactory;
import imageProcessing.ImageFilter;
import imageProcessing.PicTabViewer;
import imageProcessing.Timeable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller {

	@FXML
	private TextArea scriptArea, consoleArea;
	@FXML
	private TabPane tabPane;

	private FilterFactory filterFactory = new FilterFactory();
	private LinkedList<ImageFilter> filterQueue;

	@FXML
	private void run(ActionEvent event) {
		// initialization
		consoleArea.setText("");
		filterQueue = new LinkedList<>();
		try {
			this.parseScript();

			Mat currentMat = null;
			long counter = 0;

			while (!filterQueue.isEmpty()) {
				ImageFilter currentFilter = filterQueue.poll();

				currentMat = currentFilter.process(currentMat);

				if (currentFilter instanceof PicTabViewer) {
					if (currentMat != null)
						tabPane.getTabs().add(new ViewerTab(((PicTabViewer) currentFilter).getNameOfTab(), currentMat));
					else throw new FilterExecutionException("There is no image to show!");
				}

				if (currentFilter instanceof Timeable)
					counter += ((Timeable) currentFilter).getCounter();
			}
			consoleArea.setText(consoleArea.getText() + "The current script executed in : " + counter + " ms.\n");
		}
		catch (FilterExecutionException e) {
			e.printStackTrace();
			consoleArea.setText(consoleArea.getText() + e.getMessage() + '\n');
		}
		catch (ParsingScriptException e) {
			e.printStackTrace();
			consoleArea.setText(consoleArea.getText() + e.getMessage() + '\n');
		}
	}

	/**
	 * Open a file chooser dialog.
	 * The selected file is display inside the scriptArea and the consoleArea is
	 * empty.
	 * If no file has been selected, there is no side effect.
	 * 
	 * @param event
	 */
	@FXML
	private void openScript(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().addAll(new ExtensionFilter("Orbox Script Files", "*.obs"),
				new ExtensionFilter("Text Files", "*.txt"), new ExtensionFilter("All files", "*.*"));
		File inputFile = chooser.showOpenDialog(tabPane.getScene().getWindow());
		if (inputFile != null) {
			StringBuffer txt = new StringBuffer("");
			try {
				Files.lines(inputFile.toPath()).forEach(s -> txt.append(s + '\n'));
				scriptArea.setText(txt.toString());
				consoleArea.setText("");
			}
			catch (IOException e) {
				consoleArea.setText(
						consoleArea.getText() + "Sorry, cannot open the script file [" + inputFile.getPath() + "] !\n");
				e.printStackTrace();
			}

		}
	}

	/**
	 * Opens a file chooser dialog box
	 * if a file has been selected the content of the script area is written to
	 * the file
	 * 
	 * @param event
	 */
	@FXML
	private void saveScript(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().addAll(new ExtensionFilter("Orbox Script Files", "*.obs"),
				new ExtensionFilter("Text Files", "*.txt"), new ExtensionFilter("All files", "*.*"));
		File outputFile = chooser.showSaveDialog(tabPane.getScene().getWindow());
		if (outputFile != null) {
			try {
				BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath());
				writer.write(scriptArea.getText());
				writer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
				consoleArea.setText(consoleArea.getText() + "Sorry, could not save the script at ["
						+ outputFile.getPath() + "] !\n");
			}
		}
	}

	private void parseScript() throws ParsingScriptException  {
		Pattern linePattern = Pattern.compile("(?m-)^(.*)$");
		Matcher lineMatcher = linePattern.matcher(scriptArea.getText());
		int lineNumber = 0;

		while (lineMatcher.find()) {
			String line = lineMatcher.group(1);
			try {
				filterQueue.add(filterFactory.create(line));
			}
			catch (ParsingScriptException e) {
				throw new ParsingScriptException(e.getMessage(), lineNumber);
			}
			lineNumber++;
		}

	}

}
