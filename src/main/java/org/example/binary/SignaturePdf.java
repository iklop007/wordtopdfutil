package org.example.binary;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.CertificateInfo;

import java.io.FileOutputStream;

public class SignaturePdf {

    public static void main(String[] args) {
        try {
            // 读取现有的PDF文件
            PdfReader pdfReader = new PdfReader("path/to/original.pdf");
            // 创建PdfStamper对象以添加签名
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("path/to/signed_output.pdf"));

            // 加载新的签名证书和私钥
            // 这通常需要你使用KeyStore和PrivateKey
            // 这里的代码需要根据实际情况进行替换
            // 加载你的新签名内容
            byte[] newSignature = new byte[1024]; // 获取新的签名数据

            // 获取原始签名字典的位置
            int approvalSignature = 0; // 获取原始签名的位置

            // 创建新的PdfSignatureAppearance
            PdfSignatureAppearance signatureAppearance = pdfStamper.getSignatureAppearance();
            // 设置必要的参数
            signatureAppearance.setReason("Reason");
            signatureAppearance.setLocation("Location");
            signatureAppearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_FORM_FILLING);

            // 使用MakeSignature类来添加新的签名

//            MakeSignature.signDetached(new Signature(newSignature, certificate, privateKey, null, null, null, 0, MakeSignature.CryptoStandard.CMS),
//                    signatureAppearance, approvalSignature, null);

            // 关闭PdfStamper
            pdfStamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
