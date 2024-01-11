package com.aditya.gstbillingapp.Model;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aditya.gstbillingapp.Config.AppConfig;
import com.aditya.gstbillingapp.Helper.DayHelper;
import com.aditya.gstbillingapp.Helper.NumberToWorld;
import com.aditya.gstbillingapp.R;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PdfView {
    public String generatePdf(Context context, String path, List<MyProducts> productsList,
                            User user,String type)
    {
        File file = new File(Environment.getExternalStorageDirectory(), AppConfig.folderName);
        File newFile = new File(file,AppConfig.Bills);
        if(!newFile.exists())
        {
            newFile.mkdir();
        }
        File filePath = new File(newFile,path);
        try {
            Cell p15 = null;
            Cell p16 = null;
            Cell p13 = null;
            Cell p14 = null;
            PdfWriter pdfWriter = new PdfWriter(filePath);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            document.setFontSize(10f);

            Paragraph title = new Paragraph(""+type)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(15)
                    .setBold();

            document.add(title);

            float[] columnWidths = {1, 3, 1};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mconstrlogo);
            Image image = new Image(ImageDataFactory.create(imgByteArray(bitmap)));
            image.setHeight(70);
            image.setWidth(100);
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            image.setMargins(5,5,5,5);

            Bitmap qrbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.qrcodemconstr);
            Image qrimage = new Image(ImageDataFactory.create(imgByteArray(qrbitmap)));
            qrimage.setHeight(100);
            qrimage.setWidth(100);
            qrimage.setHorizontalAlignment(HorizontalAlignment.CENTER);
            qrimage.setMargins(5,5,5,5);

            Paragraph companyInfo = new Paragraph()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedLeading(15);

            Text boldText = new Text("MOTTIRAM CONSTRUCTION\n")
                    .setBold()
                    .setFontSize(14);

            Text regularText = new Text("38 a panchadeep bunglow near vishwakarma garden,\n")
                    .setFontSize(10);

            companyInfo.add(boldText);
            companyInfo.add(regularText);
            companyInfo.add("Mahalaxminagar hirawadi road, Nashik, 490878 \n");
            companyInfo.add("GST :- 27CGKPD2158L1Z6\n");
            companyInfo.add("Phone: 091687 53111");

            Cell companyInfoCell = new Cell().add(companyInfo);
            companyInfoCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            companyInfoCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
            companyInfoCell.setBorderBottom(Border.NO_BORDER);
            companyInfoCell.setWidth(UnitValue.createPercentValue(100));

            Cell emptyImageCell = new Cell().add(new Paragraph(""));
            emptyImageCell.setBorderBottom(Border.NO_BORDER);

            table.addCell(new Cell().add(image).setBorderBottom(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(companyInfoCell);
            table.addCell(new Cell().add(qrimage).setBorderBottom(Border.NO_BORDER));
            document.add(table);

            //customer Details
            Paragraph customerLeading = new Paragraph("Customer Details")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold();

            columnWidths = new float[] { 1, 1, 1, 1 };
            table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            Cell customerDetails = new Cell(1,4).add(customerLeading);

            Cell custName = new Cell(1, 1).add(new Paragraph("Customer Name:").setBold());
            Cell cvalueName = new Cell(1, 3).add(new Paragraph(""+user.getCustemerName()));

            Cell custPhone = new Cell().add(new Paragraph("Customer Phone:").setBold());
            Cell cvaluePhone = new Cell().add(new Paragraph(""+user.getCustemerPhone()));

            Cell custGstNo = new Cell().add(new Paragraph("Customer GST No:").setBold());
            Cell cvalueGstNo = new Cell().add(new Paragraph(""+user.getCustemerGstNo()));

            Cell custEmail = new Cell(1, 1).add(new Paragraph("Customer Email:").setBold());
            Cell cvalueEmail = new Cell(1, 1).add(new Paragraph(""+user.getCustemerEmail()));
            if(type.toLowerCase().equals("quotation")){
                cvalueEmail = new Cell(1,3).add(new Paragraph(""+user.getCustemerEmail()));
            }

            Cell custAddress = new Cell(1, 1).add(new Paragraph("Customer Address:").setBold());
            Cell cvalueAddress = new Cell(1, 3).add(new Paragraph(""+user.getCustemerAddress()));

            p13 = new Cell(1, 1).add(new Paragraph("Invoice No.:").setBold());
            p14 = new Cell(1, 1).add(new Paragraph(""+System.currentTimeMillis()));
            p15 = new Cell(1, 1).add(
                    new Paragraph("Date: ").setBold());
            p16 = new Cell(1, 1).add(
                    new Paragraph(getCurrentDate()));

            Calendar calendar = Calendar.getInstance();
            Cell weekOfDay = new Cell(1, 1).add(new Paragraph("Day: ").setBold());
            Cell weekOfDayValue = new Cell(1, 1).add(new Paragraph(new DayHelper().getDayOfWeekString(calendar)));

            table.addCell(customerDetails);
            table.addCell(custName);
            table.addCell(cvalueName);
            table.addCell(custPhone);
            table.addCell(cvaluePhone);
            table.addCell(custGstNo);
            table.addCell(cvalueGstNo);
            table.addCell(custEmail);
            table.addCell(cvalueEmail);
            if(!type.toLowerCase().equals("quotation"))
            {
                table.addCell(p13);
                table.addCell(p14);
            }
            table.addCell(p15);
            table.addCell(p16);
            table.addCell(weekOfDay);
            table.addCell(weekOfDayValue);


            table.addCell(custAddress);
            table.addCell(cvalueAddress);



            document.add(table);
            document.add(new Paragraph("\n"));

            Table productTable = new Table(new float[] { 1, 1, 1, 1, 1, 1 });
            productTable.setWidth(UnitValue.createPercentValue(100));

            //customer Details
            Paragraph billingInfo = new Paragraph("Bill Details")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold();

            Cell billingDetails = new Cell(1,6).add(billingInfo);

            productTable.addHeaderCell(billingDetails);

            productTable.addHeaderCell(new Cell(1, 1).add(new Paragraph("Sr. No")
                    .setFontSize(10).setBold()));
            productTable.addHeaderCell(new Cell(1, 1).add(new Paragraph("Product Name").setFontSize(10).setBold()));
            productTable.addHeaderCell(new Cell(1, 1).add(new Paragraph("Product Qtn.").setFontSize(10).setBold()));
            productTable.addHeaderCell(new Cell(1, 1).add(new Paragraph("HSN/SAN no.").setFontSize(10).setBold()));
            productTable.addHeaderCell(new Cell(1, 1).add(new Paragraph("Rate").setFontSize(10).setBold()));
            productTable.addHeaderCell(new Cell(1, 1)
                    .add(new Paragraph("Rate \n(Incl. Of all taxes)")
                    .setFontSize(10).setBold()));
            Cell srno = null;
            Cell product = null;
            Cell quat = null;
            Cell hsasan = null;
            Cell cgst = null;
            Cell sgst = null;
            Cell totalwithgst = null;
            Cell totalwithoutgst = null;
            int totalPriceWithoutGst = 0, totalPriceWithGst = 0;
            for(int i = 0; i < productsList.size(); i++)
            {
                int sr = i + 1;
                int priceWithQunt = 0, priceWithQuntWithGst = 0;
                MyProducts products = productsList.get(i);
                int mPrice = Integer.valueOf(products.getProductPrice());
                int mQuantity = Integer.valueOf(products.getProductQuantity());
                priceWithQunt = mPrice * mQuantity;
                priceWithQuntWithGst = ((priceWithQunt * 18) / 100)+ priceWithQunt;

                totalPriceWithoutGst  = totalPriceWithoutGst + priceWithQunt;
                totalPriceWithGst = totalPriceWithGst + priceWithQuntWithGst;

                srno = new Cell(1, 1).add(new Paragraph(""+sr)).setWidth(10f);
                product = new Cell(1, 1).add(new Paragraph(products.getProductName()));
                quat = new Cell(1, 1).add(new Paragraph(products.getProductQuantity()));
                hsasan = new Cell(1, 1).add(new Paragraph(""+products.getProductSANno()));
                totalwithgst = new Cell(1, 1).add(new Paragraph(""+priceWithQunt+"Rs"));
                totalwithoutgst = new Cell(1, 1).add(new Paragraph(""+priceWithQuntWithGst+"Rs"));

                productTable.addCell(srno);
                productTable.addCell(product);
                productTable.addCell(quat);
                productTable.addCell(hsasan);
                productTable.addCell(totalwithgst);
                productTable.addCell(totalwithoutgst);

            }

            document.add(productTable);
            document.add(new Paragraph("\n"));

            Table totalTable = new Table(new float[] { 100f,100f,130f,100f});
            totalTable.setWidth(UnitValue.createPercentValue(100));
            totalTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            Cell p1 = new Cell(1, 1).add(new Paragraph("Account No.:").setBold());
            Cell p2 = new Cell(1, 1).add(new Paragraph(AppConfig.account_number));
            Cell totalPrice = new Cell(1, 1).add(
                    new Paragraph("Non Taxable Amount: ").setBold());
            Cell totalPriceValue = new Cell(1, 1).add(
                    new Paragraph(totalPriceWithoutGst+"Rs"));

            Cell p5 = new Cell(1, 1).add(new Paragraph("IFSC: ").setBold());
            Cell p6 = new Cell(1, 1).add(new Paragraph(AppConfig.account_ifsc));
            Cell totalPriceWithGstCell = new Cell(1, 1).add(
                    new Paragraph("Taxable Amount: ").setBold());
            Cell totalPriceWithGstCellValue = new Cell(1, 1).add(
                    new Paragraph(totalPriceWithGst+"Rs"));

            Cell p9 = new Cell(1, 1).add(new Paragraph("Branch:- ").setBold());
            Cell p10 = new Cell(1, 1).add(new Paragraph(AppConfig.account_branch));

            Cell totalGst = new Cell(1, 1).add(
                    new Paragraph("Total Tax: ").setBold());
            Cell totalGstValue = new Cell(1, 1).add(
                    new Paragraph((totalPriceWithGst-totalPriceWithoutGst)+"Rs"));


            Cell totalAmount = new Cell(1, 1).add(
                    new Paragraph("Total Payble Amount: ").setBold());
            Cell totalAmountValue = new Cell(1, 1).add(
                    new Paragraph(totalPriceWithGst+"Rs"));

            NumberToWorld converter = new NumberToWorld();
            String numberInWords = converter.asWords(totalPriceWithGst);
            Cell date = new Cell(1, 1).add(new Paragraph("Payble Amount\nin Words:").setBold());
            Cell dateValue = new Cell(1, 1).add(new Paragraph(numberInWords));

            totalTable.addCell(p1);
            totalTable.addCell(p2);
            totalTable.addCell(totalPrice);
            totalTable.addCell(totalPriceValue);

            totalTable.addCell(p5);
            totalTable.addCell(p6);
            totalTable.addCell(totalPriceWithGstCell);
            totalTable.addCell(totalPriceWithGstCellValue);

            totalTable.addCell(p9);
            totalTable.addCell(p10);;
            totalTable.addCell(totalGst);
            totalTable.addCell(totalGstValue);



            totalTable.addCell(totalAmount);
            totalTable.addCell(totalAmountValue);

//            totalTable.addCell(p15);
//            totalTable.addCell(p16);
            totalTable.addCell(date);
            totalTable.addCell(dateValue);

// Repeat the cells as needed for multiple rows
            document.add(totalTable);

            LineSeparator separator = new LineSeparator(new SolidLine());
            document.add(separator);

            Table authorizedSignature = new Table(new float[] {1});
            authorizedSignature.setWidth(UnitValue.createPercentValue(100));

            Paragraph declaration = new Paragraph("Declearation:-\n" +
                    "1. We hereby declare that the goods and services mentioned in this invoice/bill have" +
                    "been supplied as per our customer's requirements, and all applicable taxes including" +
                    "GST have been charged as per the GST Law in India\n"+
                    "2. We declare that this invoice shows the actual price of the " +
                    "goods described and that all particulars are true and " +
                    "correct.\n" +
                    "3.In this bill cgst 9% and sgst 9% total gst are 18% added"
            )
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginLeft(10f)
                    .setFontSize(7f)
                    .setMarginTop(2);
            authorizedSignature.addCell(new Cell().add(declaration));

            Paragraph signature = new Paragraph("MOTIRAM CONSTRUCTION\n\nAuthorized Signature")
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20);
            authorizedSignature.addCell(new Cell().add(signature));

            document.add(authorizedSignature);

            LineSeparator separator1 = new LineSeparator(new SolidLine());
            document.add(separator1);

            Paragraph endText = new Paragraph("This bill generated by Computer")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(endText);
            document.flush();
            document.close();
            Toast.makeText(context, "Bill Generated!", Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e)
        {
            getProgressBar(context).dismiss();
            Log.d("not found", "generatePdf: "+e.getMessage());
        }
        return filePath.getAbsolutePath();
    }

    private byte[] imgByteArray(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bArray = stream.toByteArray();
            stream.close();
            return bArray;
        }
        catch (Exception e)
        {
            Log.d("byte image", e.getMessage());
        }
        return null;
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    public static  AlertDialog getProgressBar(Context ctx) {
        ProgressBar pb = new ProgressBar(ctx);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(pb);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        return dialog;
    }

}