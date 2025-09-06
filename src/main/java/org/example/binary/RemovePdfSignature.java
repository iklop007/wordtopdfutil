package org.example.binary;


import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.FileOutputStream;
import java.util.List;

public class RemovePdfSignature {
    public static void main(String[] args) {
        try {
            PdfReader pdfReader = new PdfReader("E:\\刘志成的离职签署材料 (1)\\ssc-sign-1641363917183-DZQ-LZ04 离职证明.pdf");
            FileOutputStream fos = new FileOutputStream("E:\\刘志成的离职签署材料 (1)\\ssc-sign-1641363917183-DZQ-LZ04 离职证明1.pdf");
            PdfStamper pdfStamper = new PdfStamper(pdfReader, fos);

            AcroFields fields = pdfStamper.getAcroFields();
            System.out.println(pdfStamper.getSignatureAppearance().getLocation());
            System.out.println(pdfStamper.getSignatureAppearance().getContact());
            System.out.println(pdfStamper.getSignatureAppearance().getReason());
            System.out.println(pdfStamper.getSignatureAppearance().getFieldName());
            System.out.println(pdfStamper.getSignatureAppearance().getNewSigName());
            List<String> names = fields.getSignatureNames();
            System.out.println("--------------------------------------------");
            for (String name : names) {
                fields.setField(name,"");
            }
            System.out.println("--------------------------------------------");
            pdfStamper.close();
            pdfReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
