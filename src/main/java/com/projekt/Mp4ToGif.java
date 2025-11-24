package com.projekt;

import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Mp4ToGif {



    public static void convertMp4ToGif(File source, File destination) throws Exception {

        FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(source));


        try (ImageOutputStream output = new FileImageOutputStream(destination)) {


            GifSequenceWriter writer = new GifSequenceWriter(output, BufferedImage.TYPE_INT_RGB, 100, true);

            Picture picture;
            int frameCount = 0;


            while (null != (picture = grab.getNativeFrame())) {
                BufferedImage frame = AWTUtil.toBufferedImage(picture);


                BufferedImage smallFrame = resizeImage(frame, 480);

                writer.writeToSequence(frame);
                frameCount++;

                if (frameCount % 2 == 0) {
                    System.out.print(".");
                }
            }


            writer.close();
            System.out.println("\nAnzahl Frames verarbeitet: " + frameCount);
        }
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) {

        int targetHeight = (int) (originalImage.getHeight() * ((double) targetWidth / originalImage.getWidth()));

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();


        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();

        return resizedImage;
    }
}

