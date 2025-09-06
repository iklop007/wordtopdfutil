package org.example.binary;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;

public class ResetSignature {
    public static void main(String[] args) {
        try {
            // 读取现有的PDF文件
            PdfReader pdfReader = new PdfReader("E:\\刘志成的离职签署材料 (1)\\ssc-sign-1641363917183-DZQ-LZ04 离职证明.pdf");
            // 创建一个PdfStamper对象来修改PDF
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("E:\\刘志成的离职签署材料 (1)\\ssc-sign-1641363917183-DZQ-LZ04 离职证明3.pdf"));
            // 重置所有字段的签名
            AcroFieldsSigDict.removeSigDict(pdfReader, pdfStamper);
            // 关闭PdfStamper
            pdfStamper.close();
            pdfReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
