package utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DecimalFormat;

public class Formats {
	
	public static DecimalFormat decform00 		  = new DecimalFormat("#");
    public static DecimalFormat decform01 		  = new DecimalFormat("#.#");
    
    public static DecimalFormat decform03 		  = new DecimalFormat("#.###");
    
    public static String readFile(File file, Charset charset) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), charset);
    }
}
