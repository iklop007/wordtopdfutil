package org.example.pdf;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;

public class ConvertPdfToWord {
    public static void main(String[] args) {
        //创建一个 PdfDocument 对象
        PdfDocument doc = new PdfDocument();

        //加载 PDF 文件
        doc.loadFromFile("C:\\Users\\qlzcj\\Desktop\\农行刘志成.pdf");

        //将PDF转换为Doc格式文件并保存  E:\农行刘志成\农行刘志成.doc
        doc.saveToFile("C:\\Users\\qlzcj\\Desktop\\农行刘志成.doc", FileFormat.DOC);

        //将PDF转换为Docx格式文件并保存
        doc.saveToFile("C:\\Users\\qlzcj\\Desktop\\农行刘志成.docx", FileFormat.DOCX);
        doc.close();


        // C:\Users\qlzcj\Desktop\刘志成-简历-new.docx

    }
}
