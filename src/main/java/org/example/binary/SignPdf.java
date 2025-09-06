package org.example.binary;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.security.MakeSignature;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SignPdf {
    public static void main(String[] args) {
        try {
            // 读取现有的PDF文件
            PdfReader pdfReader = new PdfReader("path_to_your_pdf_file.pdf");

            // 创建PdfStamper对象以添加签名
            PdfStamper pdfStamper = PdfStamper.createSignature(pdfReader, new FileOutputStream("path_to_output_file.pdf"), '\0');

            // 获取PdfSignatureAppearance对象
            PdfSignatureAppearance signatureAppearance = pdfStamper.getSignatureAppearance();
            signatureAppearance.setReason("Reason for signature replacement");
            signatureAppearance.setLocation("Location of signing");
            // 设置你的签名字段名
            String fieldName = "SignatureField";

            // 创建新的签名字典
            // 这里需要你使用KeyStore和相关密钥重新生成签名
            // 省略生成新签名的代码
            // byte[] newSignature = ; // 新生成的签名数据

            // 更新现有的签名字段
//            pdfStamper.close(new PdfStamper.CLOSE_FLAG_SIGNED_ONLY); // 关闭用于签名的PdfStamper
//            pdfReader = new PdfReader("path_to_output_file.pdf");
//            pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("path_to_output_file.pdf"));
//            MakeSignature.signDetached(new Signature(newSignature, pdfReader), fieldName, pdfStamper.getAcroFields(),
//                    new FileInputStream("path_to_certificate.pfx"), "password".toCharArray(), null, null, pdfStamper.getAcroFields());
//            pdfStamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
