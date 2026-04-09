package org.example.word;

//import com.jacob.activeX.ActiveXComponent;
//import com.jacob.com.ComThread;
//import com.jacob.com.Dispatch;
//import com.jacob.com.Variant;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class Word2PdfJacobUtil {

    private static final Logger log = LoggerFactory.getLogger(Word2PdfJacobUtil.class);
    /* 转PDF格式值 */
    private static final int wdFormatPDF = 17;
    /**
     * Word文档转换
     *
     * @param inputFile
     * @param pdfFile
     */
//    public static boolean word2PDF(String inputFile, String pdfFile) {
//        ComThread.InitMTA(true);
//        long start = System.currentTimeMillis();
//        ActiveXComponent app = null;
//        Dispatch doc = null;
//        try {
//            app = new ActiveXComponent("Word.Application");// 创建一个word对象
//            app.setProperty("Visible", new Variant(false)); // 不可见打开word
//            app.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
//            Dispatch docs = app.getProperty("Documents").toDispatch();// 获取文挡属性
//            System.out.println("打开文档 >>> " + inputFile);
//            // Object[]第三个参数是表示“是否只读方式打开”
//            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
//            doc = Dispatch.call(docs, "Open", inputFile, false, true).toDispatch();
//            System.out.println("转换文档 [" + inputFile + "] >>> [" + pdfFile + "]");
//            // 调用Document对象的SaveAs方法，将文档保存为pdf格式
//            // word保存为pdf格式宏，值为17
//            Dispatch.call(doc, "SaveAs", pdfFile, wdFormatPDF);// word保存为pdf格式宏，值为17
//            long end = System.currentTimeMillis();
//            System.out.println("用时：" + (end - start) + "ms.");
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("========Error:文档转换失败：" + e.getMessage());
//        } finally {
//            Dispatch.call(doc, "Close", false);
//            System.out.println("关闭文档");
//            if (app != null)
//                app.invoke("Quit", new Variant[] {});
//            // 如果没有这句话,winword.exe进程将不会关闭
//            ComThread.Release();
//            ComThread.quitMainSTA();
//        }
//        return false;
//    }
//

    /**
     * 通过documents4j 实现word转pdf
     *
     * @param sourcePath 源文件地址 如 /root/example.doc
     * @param targetPath 目标文件地址 如 /root/example.pdf
     */
    public static void documents4jWordToPdf(String sourcePath, String targetPath) {
        File inputWord = new File(sourcePath);
        File outputFile = new File(targetPath);
        try  {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream)
                    .as(DocumentType.DOCX)
                    .to(outputStream)
                    .as(DocumentType.PDF).execute();
            outputStream.close();
            System.out.println("成功");
        } catch (Exception e) {
            log.error("[documents4J] word转pdf失败:{}", e.toString());
//            System.err.println("[documents4J] word转pdf失败:{}");
        }
    }
    public static void main(String[] arg){
        String docPath = "C:\\Users\\qlzcj\\Desktop\\刘志成-简历\\刘志成-简历-new.docx";
        String pdfPath = "C:\\Users\\qlzcj\\Desktop\\刘志成-简历\\刘志成-简历-new.pdf";
        Word2PdfJacobUtil.documents4jWordToPdf(docPath, pdfPath);
        System.exit(0);
//        System.out.println(res);
    }
}
