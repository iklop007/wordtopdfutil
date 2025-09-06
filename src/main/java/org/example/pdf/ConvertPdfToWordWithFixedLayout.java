package org.example.pdf;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;

public class ConvertPdfToWordWithFixedLayout {
    public static void main(String[] args) {
        //创建一个 PdfDocument 对象
        PdfDocument doc = new PdfDocument();

        //加载 PDF 文件
        doc.loadFromFile("C:\\Users\\qlzcj\\Desktop\\刘志成-简历.pdf");
        // D:\刘志成的离职签署材料\ssc-sign-1641363917183-DZQ-LZ04 离职证明.pdf
        //将PDF转换为Doc格式文件并保存
//        doc.saveToFile("D:\\刘志成的离职签署材料\\离职证明.doc", FileFormat.DOC);
        // D:\刘志成的离职签署材料\离职证明.docx
        //将PDF转换为Docx格式文件并保存
        doc.saveToFile("C:\\Users\\qlzcj\\Desktop\\刘志成-简历.docx", FileFormat.DOCX);
        doc.close();

    }
}
