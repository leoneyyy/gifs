package com.projekt;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;

public class GifSequenceWriter implements AutoCloseable {
    protected ImageWriter gifWriter;
    protected ImageWriteParam imageWriteParam;
    protected IIOMetadata imageMetaData;

    /**
     * @param outputStream Der Output-Stream (z.B. FileOutputStream)
     * @param imageType Typ des Bildes (z.B. BufferedImage.TYPE_INT_RGB)
     * @param timeBetweenFramesMS Zeit zwischen Bildern in Millisekunden
     * @param loopContinuously Ob das GIF loopen soll (true) oder nur einmal abspielt (false)
     */
    public GifSequenceWriter(ImageOutputStream outputStream, int imageType, int timeBetweenFramesMS, boolean loopContinuously) throws IOException {
        gifWriter = getWriter();
        imageWriteParam = gifWriter.getDefaultWriteParam();
        ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);

        imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);

        String metaFormatName = imageMetaData.getNativeMetadataFormatName();

        IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);

        configureRootMetadata(root, timeBetweenFramesMS, loopContinuously);

        imageMetaData.setFromTree(metaFormatName, root);

        gifWriter.setOutput(outputStream);
        gifWriter.prepareWriteSequence(null);
    }

    private void configureRootMetadata(IIOMetadataNode root, int timeBetweenFramesMS, boolean loopContinuously) {
        String graphicControlExtensionNode = "GraphicControlExtension";
        String appExtensionsNode = "ApplicationExtensions";
        String appExtensionNode = "ApplicationExtension";
        String commentExtensionsNode = "CommentExtensions";

        IIOMetadataNode graphicsControlExtensionNode = getNode(root, graphicControlExtensionNode);
        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(timeBetweenFramesMS / 10)); // Einheit ist 1/100 Sekunde
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

        IIOMetadataNode appExtensionsNodeNode = getNode(root, appExtensionsNode);
        IIOMetadataNode appExtensionNodeNode = new IIOMetadataNode(appExtensionNode);

        appExtensionNodeNode.setAttribute("applicationID", "NETSCAPE");
        appExtensionNodeNode.setAttribute("authenticationCode", "2.0");

        int loop = loopContinuously ? 0 : 1;
        appExtensionNodeNode.setUserObject(new byte[]{0x1, (byte) (loop & 0xFF), (byte) ((loop >> 8) & 0xFF)});
        appExtensionsNodeNode.appendChild(appExtensionNodeNode);
    }

    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) {
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) {
                return ((IIOMetadataNode) rootNode.item(i));
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return (node);
    }

    public void writeToSequence(BufferedImage img) throws IOException {
        gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam);
    }

    private static ImageWriter getWriter() throws IIOException {
        Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
        if (!iter.hasNext()) {
            throw new IIOException("No GIF Image Writers Exist");
        } else {
            return iter.next();
        }
    }

    public void close() throws IOException {
        gifWriter.endWriteSequence();
    }
}