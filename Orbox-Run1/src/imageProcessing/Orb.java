package imageProcessing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Orb implements ImageFilter, Timeable {

	private long counter = 0;
	
	@Override
	public Mat process(Mat inputIm) throws FilterExecutionException {
		counter = System.currentTimeMillis();
		
		FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.ORB);
		DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		
		MatOfKeyPoint matKeyPoints1 = new MatOfKeyPoint();
		Mat descriptors1 = new Mat();
		
		featureDetector.detect(inputIm, matKeyPoints1);
		descriptorExtractor.compute(inputIm, matKeyPoints1, descriptors1);
		
		Mat img2 = Imgcodecs.imread("C:\\Users\\Antoine\\Desktop\\image\\pc_1_img.jpg", Imgcodecs.IMREAD_GRAYSCALE);
		Mat descriptors2 = new Mat();
		MatOfKeyPoint matKeyPoints2 = new MatOfKeyPoint();
		
		featureDetector.detect(img2, matKeyPoints2);
		descriptorExtractor.compute(img2, matKeyPoints2, descriptors2);
		
		//matcher should include 2 different image's descriptors
		MatOfDMatch  matches = new MatOfDMatch();             
		descriptorMatcher.match(descriptors1,descriptors2,matches);
		
		//MatOfDMatch  matches2 = new MatOfDMatch();      
		
		int DIST_LIMIT = 80;
		List<DMatch> matchesList = matches.toList();
		List<DMatch> matches_final= new ArrayList<DMatch>();
		for(int i=0; i<matchesList.size(); i++){
		   if(matchesList .get(i).distance <= DIST_LIMIT){
		       matches_final.add(matches.toList().get(i));
		   }
		}
		
		MatOfDMatch matches_final_mat = new MatOfDMatch();
		matches_final_mat.fromList(matches_final);
		
		//feature and connection colors
		Scalar RED = new Scalar(255,0,0);
		Scalar GREEN = new Scalar(0,255,0);
		
		//output image
		Mat outputImg = new Mat();
		MatOfByte drawnMatches = new MatOfByte();
		
		//this will draw all matches, works fine
		Features2d.drawMatches(inputIm, matKeyPoints1, img2, matKeyPoints2, matches_final_mat, 
		outputImg, GREEN, RED,  drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);
		
		counter = System.currentTimeMillis() - counter;
		return outputImg;
	}
	
	@Override
	public long getCounter() {
		return counter;
	}
}
