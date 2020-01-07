package pt.agora.videoclip;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import org.junit.Test;

public class VideoClipUtilsTest {

	@Test(expected=IllegalArgumentException.class)
	public void ThrowExceptionIfFilenameIsNull() throws IOException {
		VideoClipUtils.extractFirstFrames(null, ".");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ThrowExceptionIfFilenameIsEmpty() throws IOException {
		VideoClipUtils.extractFirstFrames(" ", ".");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ThrowExceptionIfDirectoryPathIsNull() throws IOException {
		VideoClipUtils.extractFirstFrames("big_buck_bunny_trailer.webm", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ThrowExceptionIfDirectoryPathIsEmpty() throws IOException {
		VideoClipUtils.extractFirstFrames("big_buck_bunny_trailer.webm", " ");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ThrowExceptionIfFramesIsLessThanZero() throws IOException {
		VideoClipUtils.extractFirstFrames("big_buck_bunny_trailer.webm", ".", -1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ThrowExceptionIfVideoFormatIsNull() throws IOException {
		VideoClipUtils.extractFirstFrames("big_buck_bunny_trailer.webm", ".", null);
	}
	
	@Test
	public void extractFilesIfEverythingIsOK() throws IOException {
		long before = Files.list(Paths.get(".")).count();
		
		VideoClipUtils.extractFirstFrames("big_buck_bunny_trailer.webm", ".");
		
		long after = Files.list(Paths.get(".")).count();
		
		assertEquals(after, before + 10);

		//clear folder
		IntStream
			.rangeClosed(1, 10)
			.forEach(n -> {
				try {
					Files.deleteIfExists(Paths.get("." + File.separator + "big_buck_bunny_trailer-" + n + ".jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}); 
	}

}
