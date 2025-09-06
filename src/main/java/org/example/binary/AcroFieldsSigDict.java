package org.example.binary;


import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.util.Date;

public class AcroFieldsSigDict {
    // 删除PDF中所有字段的签名字典
    public static void removeSigDict(PdfReader pdfReader, PdfStamper pdfStamper) throws Exception {
        AcroFields fields = pdfStamper.getAcroFields();
        for (String name : fields.getSignatureNames()) {
            fields.setField("date",new Date().toString());
        }
    }
}
