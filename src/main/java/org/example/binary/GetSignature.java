package org.example.binary;


import com.spire.pdf.*;
import com.spire.pdf.security.PdfSignature;
import com.spire.pdf.widget.PdfFormFieldWidgetCollection;
import com.spire.pdf.widget.PdfFormWidget;
import com.spire.pdf.widget.PdfSignatureFieldWidget;

import java.util.Date;

public class GetSignature {
    public static void main(String[] args) {
        //创建PdfDocument实例
        PdfDocument pdf = new PdfDocument();
        //加载含有签名的PDF文件
        pdf.loadFromFile("E:\\刘志成的离职签署材料 (1)\\ssc-sign-1641363917183-DZQ-LZ04 离职证明3.pdf");



        //获取域集合
        PdfFormWidget pdfFormWidget = (PdfFormWidget) pdf.getForm();
        PdfFormFieldWidgetCollection pdfFormFieldWidgetCollection = pdfFormWidget.getFieldsWidget();

        //遍历域
        for (int i = 0; i < pdfFormFieldWidgetCollection.getCount(); i++) {
            //判定是否为签名域
            if (pdfFormFieldWidgetCollection.get(i) instanceof PdfSignatureFieldWidget) {
                //获取签名域
                PdfSignatureFieldWidget signatureFieldWidget = (PdfSignatureFieldWidget) pdfFormFieldWidgetCollection.get(i);
                //获取签名
                PdfSignature signature = signatureFieldWidget.getSignature();
                String location = signature.getLocationInfo();
                String reason = signature.getReason();
//                signature.setDate(new Date());
                String data = signature.getDate().toString();

                String name = signature.getSignatureName();

                System.out.println("签名位置信息："+ location +"\n"+
                        "签名原因：" + reason +"\n"+
                        "签名日期："+ data +"\n"+
                        "签名人："+ name +"\n"+
                        "文档中的签名坐标：X = "+ signatureFieldWidget.getLocation().getX()+ "  Y = "+ signatureFieldWidget.getLocation().getY()
                );
            }
        }
    }
}
