package org.example.file;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {

    public enum ScaleMode {
        FIT,        // 保持宽高比，适应目标尺寸
        FILL,       // 填充目标尺寸，可能裁剪
        STRETCH     // 拉伸填充，不保持宽高比
    }

    /**
     * 从文件加载并缩放图片
     */
    public static BufferedImage loadAndResizeImage(File imageFile, int width, int height, ScaleMode mode) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);
            if (originalImage == null) return null;

            return resizeImage(originalImage, width, height, mode);

        } catch (IOException e) {
            System.err.println("无法加载图片: " + e.getMessage());
            return null;
        }
    }

    /**
     * 缩放图片
     */
    public static BufferedImage resizeImage(BufferedImage original, int width, int height, ScaleMode mode) {
        if (original == null) return null;

        int originalWidth = original.getWidth();
        int originalHeight = original.getHeight();

        int targetWidth = width;
        int targetHeight = height;

        // 根据模式计算目标尺寸
        switch (mode) {
            case FIT:
                double scale = Math.min(
                        (double) width / originalWidth,
                        (double) height / originalHeight
                );
                targetWidth = (int) (originalWidth * scale);
                targetHeight = (int) (originalHeight * scale);
                break;

            case FILL:
                double fillScale = Math.max(
                        (double) width / originalWidth,
                        (double) height / originalHeight
                );
                targetWidth = (int) (originalWidth * fillScale);
                targetHeight = (int) (originalHeight * fillScale);
                break;

            case STRETCH:
                // 直接使用输入的宽高
                break;
        }

        // 创建缩放后的图像
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight,
                original.getTransparency() == Transparency.OPAQUE ?
                        BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        setupHighQualityRendering(g2d);

        if (mode == ScaleMode.FILL) {
            // 对于FILL模式，需要居中裁剪
            int x = (targetWidth - width) / 2;
            int y = (targetHeight - height) / 2;
            g2d.drawImage(original, -x, -y, targetWidth, targetHeight, null);
        } else {
            g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        }

        g2d.dispose();

        if (mode == ScaleMode.FILL) {
            // 裁剪到目标尺寸
            return resized.getSubimage(0, 0, width, height);
        }

        return resized;
    }

    private static void setupHighQualityRendering(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }

    /**
     * 保存缩放后的图片
     */
    public static boolean saveResizedImage(File originalFile, File outputFile, int width, int height) {
        try {
            BufferedImage resizedImage = loadAndResizeImage(originalFile, width, height, ScaleMode.FIT);
            if (resizedImage != null) {
                String formatName = getFormatName(outputFile.getName());
                return ImageIO.write(resizedImage, formatName, outputFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getFormatName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg": case "jpeg": return "JPEG";
            case "png": return "PNG";
            case "gif": return "GIF";
            case "bmp": return "BMP";
            default: return "PNG";
        }
    }
}
