package takePicture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

//import com.phidgets.Phidget;

public class TakePicture extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 894189184386535190L;
	BufferedImage image;
	
	public static void saveImage(BufferedImage img) {        
        try {
            File outputfile = new File("new.png");
            ImageIO.write(img, "png", outputfile);
        } catch (Exception e) {
            System.out.println("error during save");
        }
    }
	
	public TakePicture(BufferedImage img) {
        image = img;
    } 
	
	public BufferedImage givePicture(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//System.loadLibrary(Core.VERSION);
		TakePicture t = new TakePicture();
        VideoCapture camera = new VideoCapture(0);

        Mat frame = new Mat();
        camera.read(frame); 

        if(!camera.isOpened()){
            System.out.println("Error during opening");
        }
        else {                  
            while(true){        

                if (camera.read(frame)){

					BufferedImage image = t.MatToBufferedImage(frame);

                    //t.window(image, "Original Image", 0, 0);

                    //t.window(t.grayscale(image), "Processed Image", 40, 60);
                    
                    saveImage(image);

                    break;
                }
            }   
        }
        camera.release();
        
		return image;
	}
	
	public TakePicture() {
		// TODO Auto-generated constructor stub
		
	}

	public BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }

	
	
}