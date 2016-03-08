package application;

import org.opencv.core.Mat;

import imageProcessing.Utilitary;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;

public class ViewerTab extends Tab {
	
	private ScrollPane scrollPane;
	private ImageView imageView;
	
	public ViewerTab(String name, Mat img) {
		this.setText(name);
		imageView = new ImageView(Utilitary.mat2Image(img));
		scrollPane = new ScrollPane(imageView);	
		this.setContent(scrollPane);
	}
}
