package com.bootdo.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.lowagie.text.pdf.PdfReader;

public class PDFToIMGUtil {

    private static Logger logger = Logger.getLogger(PDFToIMGUtil.class);

    public static void main(String[] args) {
        pdf2Png("D:\\document\\pdf\\FZ_T 10004-2018.pdf", "D:\\document\\pdf", 300);
    }

    /***
     * PDF文件转PNG图片，全部页数
     *
     * @param PdfFilePath pdf完整路径
     * @param dpi dpi越大转换后越清晰，相对转换速度越慢
     * @return
     */
    public static String pdf2Png(String PdfFilePath, String imgFolderPath, int dpi) {
        File file = new File(PdfFilePath);
        String descPath = null;
        PDDocument pdDocument;
        try {
            int dot = file.getName().lastIndexOf('.');
            String imagePDFName = file.getName().substring(0, dot); // 获取图片文件名
            imgFolderPath = file.getParent();;
            pdDocument = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            /* dpi越大转换后越清晰，相对转换速度越慢 */
            PdfReader reader = new PdfReader(PdfFilePath);
            int pages = reader.getNumberOfPages();
            StringBuffer imgFilePath = null;
            for (int i = 0; i < 1; i++) {
                String imgFilePathPrefix = imgFolderPath + File.separator + imagePDFName;
                imgFilePath = new StringBuffer();
                imgFilePath.append(imgFilePathPrefix);
                imgFilePath.append(".png");
                File dstFile = new File(imgFilePath.toString());
                descPath =  dstFile.getAbsolutePath();
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                ImageIO.write(image, "png", dstFile);
            }
            logger.info("PDF文档转PNG图片成功！");

        } catch (IOException e) {
            logger.error("PDF文档转PNG图片的异常："+e.getMessage());
        }
        return descPath;
    }
}
