package org.example.binary;


import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSeedSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ResetSignatureDate {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

//        try (PDDocument document = PDDocument.load(new File("path/to/document.pdf"))) {
//            List<SignatureInterface> signatureInterfaces = document.getSignatureInterfaces();
//
//            for (SignatureInterface signatureInterface : signatureInterfaces) {
//                if (signatureInterface instanceof PDSeedSignature) {
//                    PDSeedSignature seedSignature = (PDSeedSignature) signatureInterface;
//                    X509Certificate certificate = seedSignature.getSigner().getCertificate();
//
//                    // 设置新的时间戳
//                    Calendar newTime = Calendar.getInstance();
//                    newTime.setTime(new Date()); // 使用当前时间
//                    seedSignature.setSignDate(newTime);
//
//                    // 重新签名
//                    seedSignature.sign(certificate, null);
//
//                    System.out.println("数字签名的时间已重置。");
//                }
//            }
//
//            // 保存更改
//            document.save("path/to/output_document.pdf");
//        } finally {
//
//        }
    }
}
