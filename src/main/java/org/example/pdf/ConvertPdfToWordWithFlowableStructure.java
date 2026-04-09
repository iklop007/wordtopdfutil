package org.example.pdf;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;

public class ConvertPdfToWordWithFlowableStructure {
    public static void main(String[] args) {

        //创建一个 PdfDocument 对象
        PdfDocument doc = new PdfDocument();

        //加载 PDF 文件
        doc.loadFromFile("C:\\Users\\qlzcj\\Desktop\\刘志成-简历\\刘志成-简历.pdf");

        //将 PDF 转换为流动形态的Word
        doc.getConvertOptions().setConvertToWordUsingFlow(true);

        //将PDF转换为Doc格式文件并保存
        doc.saveToFile("C:\\Users\\qlzcj\\Desktop\\刘志成-简历\\刘志成-简历.doc", FileFormat.DOC);

        //将PDF转换为Docx格式文件并保存
        doc.saveToFile("C:\\Users\\qlzcj\\Desktop\\刘志成-简历\\刘志成-简历..docx", FileFormat.DOCX);
        doc.close();
    }
}
