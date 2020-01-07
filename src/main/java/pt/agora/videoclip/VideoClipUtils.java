package pt.agora.videoclip;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

/**
 * Extract first frames from a videoclip
 * filename -> name of videoclip file
 * directoryPath -> path of videoclip
 * frames -> number of first frames to extract
 * videoformat -> type of video format
 */
public class VideoClipUtils {
	
	private static final int DEFAULT_NUMBER_OF_FRAMES = 10;
	private static final String IMAGE_TYPE = "jpeg";
    private static final String IMAGE_EXTENSION = ".jpg";
    
    public enum VideoFormat {
    	WEBM("webm"),
    	MP4("mp4");
    	
    	private String format;
    	
    	private VideoFormat(String format) {
			this.format = format;
		}
    	
    	public String getFormat() {
			return format;
		}
    }

    public static void extractFirstFrames(String filename, String directoryPath) throws IOException{
    	extractFirstFrames(filename, directoryPath, DEFAULT_NUMBER_OF_FRAMES, VideoFormat.WEBM);
    }
        
    public static void extractFirstFrames(String filename, String directoryPath, int frames) throws IOException{
    	extractFirstFrames(filename, directoryPath, frames, VideoFormat.WEBM);
    }
    
    public static void extractFirstFrames(String filename, String directoryPath, VideoFormat videoFormat) throws IOException{
    	extractFirstFrames(filename, directoryPath, DEFAULT_NUMBER_OF_FRAMES, videoFormat);
    }
    
    public static void extractFirstFrames(String filename, String directoryPath, int frames, VideoFormat videoFormat) throws IOException{
    	if(filename == null || filename.trim().isEmpty())
            throw new IllegalArgumentException(filename + " doesn't exist or is null");

    	if(directoryPath == null || directoryPath.trim().isEmpty())
            throw new IllegalArgumentException(directoryPath + " doesn't exist or is null");

    	File file = new File(directoryPath, filename);
    	if(!file.exists())
            throw new IllegalArgumentException(file.getPath() + " doesn't exist");

    	if(frames <= 0)
    		throw new IllegalArgumentException("frames cannot be less than 1");
    	
    	if(frames >= Integer.MAX_VALUE)
    		throw new IllegalArgumentException("frames cannot be greater than " + Integer.MAX_VALUE);
    	
    	if(videoFormat == null)
    		throw new IllegalArgumentException("videoFormat cannot be null");
    	
		try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file)) {

			grabber.setFormat(videoFormat.getFormat());
			grabber.start();

			IntStream
				.rangeClosed(1, DEFAULT_NUMBER_OF_FRAMES)
				.forEach(n -> saveImage(n, file, grabber));

		} catch (IOException e) {
			throw e;
		}
	}

	private static void saveImage(int n, File file, FFmpegFrameGrabber grabber) {
		final Java2DFrameConverter converter = new Java2DFrameConverter();
		
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			Frame frame = grabber.grabImage();

			BufferedImage grabbedImage = converter.convert(frame);

			String nameOfFile = file.getName().substring(0, file.getName().lastIndexOf("."));
			File newFile = new File(file.getParent() + File.separator + nameOfFile + "-" + n + IMAGE_EXTENSION);
			
			ImageIO.write(grabbedImage, IMAGE_TYPE, newFile);

		} catch (IOException e) {
			Logger.getLogger(VideoClipUtils.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}
