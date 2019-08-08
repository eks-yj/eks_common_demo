package com.eks.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;

public class ClipboardUtils {
    private static Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    public static void setClipboardText(String contentString) {
        Transferable transferable = new StringSelection(contentString);
        CLIPBOARD.setContents(transferable, null);
    }
    public static String getTextFromClipboard() throws IOException, UnsupportedFlavorException {
        Transferable transferable = CLIPBOARD.getContents(null);
        if (transferable == null || !transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            throw new RuntimeException("Clipboard content is not String.");
        }
        return (String) transferable.getTransferData(DataFlavor.stringFlavor);
    }
    public static void setClipboardImage(String imageFilePathString) throws IOException {
        Image image = ImageIO.read(new File(imageFilePathString));
        setClipboardImage(image);
    }
    public static void setClipboardImage(final Image image){
        Transferable transferable = new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { DataFlavor.imageFlavor };
            }
            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }
            @Override
            public Object getTransferData(DataFlavor flavor)throws UnsupportedFlavorException {
                if (!isDataFlavorSupported(flavor)) {
                    throw new UnsupportedFlavorException(flavor);
                }
                return image;
            }
        };
        CLIPBOARD.setContents(transferable,null);
    }
}