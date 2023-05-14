package com.este.promptmaker;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageManipulator {

    // still a work in progress
    public BufferedImage resizeImage(File file) throws IOException {
        BufferedImage bi = ImageIO.read(new File(file.getCanonicalPath()));
        if (!(bi == null)) {
            bi = simpleResizeImage(bi, 1024);
            return bi;
        }
        return null;
    }

    private BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) {
        return Scalr.resize(originalImage, targetWidth);
    }
}
