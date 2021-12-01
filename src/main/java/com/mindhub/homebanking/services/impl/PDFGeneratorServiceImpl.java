package com.mindhub.homebanking.services.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.PDFGeneratorService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class PDFGeneratorServiceImpl implements PDFGeneratorService {

        @Autowired
        private AccountService accountService;

        @Autowired
        private ClientService clientService;

        @Autowired
        private PDFGeneratorService pdfGeneratorService;

        @Autowired
        private TransactionService transactionService;

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.WHITE);
        cell.setPadding(10);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(18,194,176);
        //font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Fecha de alta", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Cuenta Nº", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Saldo", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, List<AccountDTO> listAccounts) {
        PdfPCell cellDos = new PdfPCell();
        for (AccountDTO acc : listAccounts) {
            table.addCell(String.valueOf(acc.getId()));
            table.addCell(String.valueOf(acc.getCreationDate()));
            table.addCell(acc.getNumber());
            table.addCell(String.valueOf(acc.getBalance()));
        }
    }

    @Override
    public void exportAccount(HttpServletResponse response, List<AccountDTO> listAccounts, Client client) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = new Font(Font.HELVETICA, 22, Font.BOLD, new Color(18,194,176));//negrita
        //fontTitle.setColor(18,194,176);

        Font fontMuted = new Font(Font.HELVETICA, 14, Font.NORMAL, new Color(155,155,155));//gris
        //fontMuted.setColor(155,155,155);
        Font fontReg =  new Font(Font.HELVETICA, 14);

        Paragraph title = new Paragraph("Comprobante de transferencia", fontTitle);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(18,194,176);

        Image logo = Image.getInstance("src/main/resources/static/web/assets/hb_logo.png");// Image logo = Image.getInstance("http://localhost:8080/web/assets/hb_logo.png");
        logo.scaleAbsolute(233,41);
        logo.setAlignment(Paragraph.ALIGN_CENTER);

        Paragraph paragraph0 = new Paragraph(" ", font);
        Paragraph p = new Paragraph("Resumen de cuentas", fontTitle);

        Paragraph p2 = new Paragraph("Nombre y apellido ", fontMuted);
        Paragraph p3 = new Paragraph(client.getFirstName() + " " + client.getLastName(), fontReg);

        Paragraph p4 = new Paragraph("Email ", fontMuted);
        Paragraph p5 = new Paragraph(client.getEmail(), fontReg);


        document.add(logo);
        document.add(paragraph0);
        document.add(p);
        document.add(Chunk.NEWLINE);
        document.add(p2);
        document.add(p3);
        document.add(p4);
        document.add(p5);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.0f, 3.0f, 2.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table, listAccounts);

        document.add(table);
        document.close();

    }

    @Override
    public void exportTransfer(HttpServletResponse response, Transaction transaction, Account account, Client client) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Image logo = Image.getInstance("src/main/resources/static/web/assets/hb_logo.png");// Image logo = Image.getInstance("http://localhost:8080/web/assets/hb_logo.png");
        logo.scaleAbsolute(233,41);

        // Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD); //otra manera de generar una fuente
        //fontTitle.setSize(16);
        Image y = Image.getInstance("src/main/resources/static/web/assets/pdfHoja2.png");
        y.scaleAbsolute(60,207);
        // y.scalePercent(14);
        y.setAbsolutePosition(30,280);
        Chunk space = new Chunk("                ");

        Font fontTitle = new Font(Font.HELVETICA, 22, Font.BOLD, new Color(18,194,176));//negrita
        //fontTitle.setColor(18,194,176);

        Font fontAmount =  new Font(Font.HELVETICA, 28);

        Font fontBold = new Font(Font.HELVETICA, 14, Font.BOLD);//negrita
        Font fontMuted = new Font(Font.HELVETICA, 14, Font.NORMAL, new Color(155,155,155));//gris
        //fontMuted.setColor(155,155,155);
        Font fontReg =  new Font(Font.HELVETICA, 14);

        Paragraph paragraph0 = new Paragraph(" ");
        Paragraph title = new Paragraph("Comprobante de transacción", fontTitle);
        //title.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);

        Paragraph p1 = new Paragraph("Fecha: "+ String.valueOf(transaction.getDate()), fontBold);

        Paragraph debit = new Paragraph("Monto debitado", fontMuted);
        Paragraph credit = new Paragraph("Monto acreditado", fontMuted);


        Paragraph amount = new Paragraph("$"+String.valueOf(transaction.getAmount()), fontAmount);

        Paragraph p2 = new Paragraph(space+"Nombre y apellido ", fontMuted);
        Paragraph p3 = new Paragraph(space+client.getFirstName() + " " + client.getLastName(), fontReg);

        Paragraph p4 = new Paragraph(space+"Tipo de transacción ", fontMuted);
        //Paragraph p5 = new Paragraph(String.valueOf(transaction.getType()), fontReg);

        Paragraph p6 = new Paragraph(space+"Cuenta debitada ", fontMuted);
        Paragraph p7 = new Paragraph(space+"Cuenta acreditada ", fontMuted);

        Paragraph p8 = new Paragraph(space+String.valueOf(account.getNumber()), fontReg);

        Paragraph p9 = new Paragraph(space+"Descripción ", fontMuted);
        Paragraph p10 = new Paragraph(space+transaction.getDescription(), fontReg);


        document.add(logo);
        document.add(y);
        document.add((paragraph0));
        document.add(title);
        document.add(Chunk.NEWLINE);
        document.add(p1);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        if(transaction.getType() == TransactionType.DEBITO){
            document.add(debit);
        } else {
            document.add(credit);
        }
        document.add(amount);
        document.add(Chunk.NEWLINE);
        document.add(p2);
        document.add(p3);
        document.add(Chunk.NEWLINE);
        document.add(p4);
        if(transaction.getType() == TransactionType.DEBITO){
            document.add(new Paragraph(space+"DEBIN", fontReg));
        } else {
            document.add(new Paragraph(space+"CREDIN", fontReg));
        }
        document.add(Chunk.NEWLINE);
        if(transaction.getType() == TransactionType.DEBITO){
            document.add((p6));
        } else {
            document.add((p7));
        }
        document.add(p8);
        document.add(Chunk.NEWLINE);
        document.add(p9);
        document.add(p10);


        document.close();
    }

    @Override
    public void exportTransaction(HttpServletResponse response, Client client, String amount, String description, String originNumberAccount, String destinyNumberAccount) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Image logo = Image.getInstance("src/main/resources/static/web/assets/hb_logo.png");// Image logo = Image.getInstance("http://localhost:8080/web/assets/hb_logo.png");
        logo.scaleAbsolute(233,41);

        Image y = Image.getInstance("src/main/resources/static/web/assets/pdfHoja.png");
        y.scaleAbsolute(60,272);
       // y.scalePercent(14);
        y.setAbsolutePosition(30,215);

        // Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD); //otra manera de generar una fuente
        //fontTitle.setSize(16);
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy - HH:mm");
        String currentDateTime = dateFormatter.format(new Date());

        Font fontTitle = new Font(Font.HELVETICA, 22, Font.BOLD, new Color(18,194,176));//negrita
        //fontTitle.setColor(18,194,176);

        Font fontAmount =  new Font(Font.HELVETICA, 28);

        Font fontBold = new Font(Font.HELVETICA, 14, Font.BOLD);//negrita
        Font fontMuted = new Font(Font.HELVETICA, 14, Font.NORMAL, new Color(155,155,155));//gris
        //fontMuted.setColor(155,155,155);
        Font fontReg =  new Font(Font.HELVETICA, 14);

        Paragraph paragraph0 = new Paragraph(" ");
        Paragraph title = new Paragraph("Comprobante de transferencia", fontTitle);
        //title.setAlignment(Paragraph.ALIGN_CENTER);
        //Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);

        Paragraph p1 = new Paragraph("Fecha: " + currentDateTime, fontBold);
        Paragraph debit = new Paragraph("Monto: ", fontMuted);
        Paragraph amountT = new Paragraph("$"+ amount, fontAmount);

        Chunk space = new Chunk("                ");
       // Paragraph amount = new Paragraph("$"+String.valueOf(transaction.getAmount()), fontAmount);
        Paragraph p2 = new Paragraph(space+"Nombre y apellido ", fontMuted);
        Paragraph p3 = new Paragraph(space+client.getFirstName() + " " + client.getLastName(), fontReg);

        Paragraph p4 = new Paragraph(space+"Tipo de transacción ", fontMuted);
        Paragraph p5 = new Paragraph(space+"DEBIN", fontReg);

        Paragraph p6 = new Paragraph(space+"Cuenta debitada ", fontMuted);
        Paragraph p7 = new Paragraph(space+originNumberAccount, fontReg);

        Paragraph p8 = new Paragraph(space+"Descripción ", fontMuted);
        Paragraph p9 = new Paragraph(space+description, fontReg);

        Paragraph p10 = new Paragraph(space+"Cuenta de destino", fontMuted);
        Paragraph p11 = new Paragraph(space+destinyNumberAccount, fontReg);


        document.add(logo);
        document.add(y);
        document.add((paragraph0));
        document.add(title);
        document.add(Chunk.NEWLINE);
        document.add(p1);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(debit);
        document.add(amountT);
        document.add(Chunk.NEWLINE);
        document.add(p2);
        document.add(p3);
        document.add(Chunk.NEWLINE);
        document.add(p4);
        document.add(p5);
        document.add(Chunk.NEWLINE);
        document.add(p6);
        document.add(p7);
        document.add(Chunk.NEWLINE);
        document.add(p8);
        document.add(p9);
        document.add(Chunk.NEWLINE);
        document.add(p10);
        document.add(p11);

        document.close();
    }

    //list of transaccion between dates

    private void writeTableHeaderTransactions(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.WHITE);
        cell.setPadding(10);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(18,194,176);
        //font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Fecha", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tipo", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Descripción", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Monto", font));
        table.addCell(cell);
    }

    private void writeTableDataTransactions(PdfPTable table, List<Transaction> listTransactions) {
        PdfPCell cellDos = new PdfPCell();
        for (Transaction transfer : listTransactions) {
            table.addCell(String.valueOf(transfer.getId()));
            table.addCell(String.valueOf(transfer.getDate()));
            table.addCell(String.valueOf(transfer.getType()));
            table.addCell(transfer.getDescription());
            table.addCell(String.valueOf(transfer.getAmount()));

        }
    }

    @Override
    public void exportListTransactions(HttpServletResponse response, List<Transaction> listTransactions, Client client, LocalDateTime from, LocalDateTime to, Account account) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());


        document.open();
        Font fontTitle = new Font(Font.HELVETICA, 22, Font.BOLD, new Color(18,194,176));//negrita

        Font fontMuted = new Font(Font.HELVETICA, 14, Font.NORMAL, new Color(155,155,155));//gris
        Font fontReg =  new Font(Font.HELVETICA, 14);

        Paragraph title = new Paragraph("Comprobante de transferencia", fontTitle);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(18,194,176);

        Image logo = Image.getInstance("src/main/resources/static/web/assets/hb_logo.png");// Image logo = Image.getInstance("http://localhost:8080/web/assets/hb_logo.png");
        logo.scaleAbsolute(233,41);
        logo.setAlignment(Paragraph.ALIGN_CENTER);

        Paragraph paragraph0 = new Paragraph(" ", font);
        Paragraph p = new Paragraph("Lista de transacciones", fontTitle);

        Paragraph p2 = new Paragraph("Nombre y apellido ", fontMuted);
        Paragraph p3 = new Paragraph(client.getFirstName() + " " + client.getLastName(), fontReg);

        Paragraph p4 = new Paragraph("Email ", fontMuted);
        Paragraph p5 = new Paragraph(client.getEmail(), fontReg);


        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateStringFrom = from.format(dateFormat);
        String dateStringTo = to.format(dateFormat);

        Paragraph p10 = new Paragraph("Fecha: ", fontMuted);
        Paragraph p6 = new Paragraph("Desde: " + dateStringFrom + "  -  Hasta: "+ dateStringTo, fontReg);


        Paragraph p11 = new Paragraph("Nº de cuenta ", fontMuted);
        Paragraph p12 = new Paragraph(account.getNumber(), fontReg);





        document.add(logo);
        document.add(paragraph0);
        document.add(p);
        document.add(Chunk.NEWLINE);
        document.add(p2);
        document.add(p3);
        document.add(p4);
        document.add(p5);
        document.add(p11);
        document.add(p12);
        document.add(p10);
        document.add(p6);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.0f, 2.5f, 1.5f, 3.5f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeaderTransactions(table);
        writeTableDataTransactions(table, listTransactions);

        document.add(table);
        document.close();
    }





   /*
    public void export(HttpServletResponse accountExtract) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, accountExtract.getOutputStream());

        graph Pagare1=new Paragraph();

        //AGREGAR TEXTO NEGRITA EN LA MISMA LINEA.
        Font font1 = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);//negrita
        Chunk texto1 = new Chunk("Tu texto parte 1", font1);
        Chunk texto2 = new Chunk("Tu texto 2", new Font(Font.TIMES_ROMAN, 10, Font.ITALIC));

        Pagare1.add(text1);
        Pagare1.add(text2);


        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("This is a title", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);

        Paragraph paragraph2 = new Paragraph("This is a paragraph", fontParagraph);
        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);


        document.add(paragraph);
        document.add((paragraph2));
        document.close();

    }*/

}
