package com.este.promptmaker;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageManipulator {

    public BufferedImage resizeImage(File file) throws IOException {
        BufferedImage bi = ImageIO.read(new File(file.getAbsolutePath()));
        BufferedImage copy = bi;

        if (!(bi == null)) {
            int height = bi.getHeight();
            int width = bi.getWidth();

            if (height < 512 || width < 512) {
                if (height > width) {
                    bi = resizeBasedOnWidth(bi, 512);
                } else {
                    bi = resizeBasedOnHeight(bi, 512);
                }
            } else if (height > 1024 || width > 1024) {
                if (height > width) {
                    bi = resizeBasedOnHeight(bi, 1024);
                } else {
                    bi = resizeBasedOnWidth(bi, 1024);
                }
            }

            if (bi.getHeight() > 1024 || bi.getWidth() > 1024 || bi.getHeight() < 512 || bi.getWidth() < 512) {
                if (height < 1024 || width < 1024) {
                    bi = resizeAndKeepAspectRatio(copy, 512);
                } else {
                    bi = resizeAndKeepAspectRatio(copy, 1024);
                }
            }

            return bi;
        }
        return null;
    }

    private BufferedImage resizeBasedOnWidth(BufferedImage bi, int size) {
        return Scalr.resize(bi, Scalr.Mode.FIT_TO_WIDTH, size);
    }

    private BufferedImage resizeBasedOnHeight(BufferedImage bi, int size) {
        return Scalr.resize(bi, Scalr.Mode.FIT_TO_HEIGHT, size);
    }

    private BufferedImage resizeAndKeepAspectRatio(BufferedImage bi, int size) {
        if (bi.getHeight() > bi.getWidth()) {
            bi = resizeBasedOnHeight(bi, size);
        } else {
            bi = resizeBasedOnWidth(bi, size);
        }

        int centerX = 0;
        int centerY = 0;

        if (bi.getHeight() > bi.getWidth()) {
            centerX = (size - bi.getWidth()) / 2;
        } else {
            centerY = (size - bi.getHeight()) / 2;
        }

        BufferedImage newImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(bi, centerX, centerY, bi.getWidth(), bi.getHeight(), null);
        graphics.dispose();

        return newImage;
    }
}
