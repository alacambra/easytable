package org.vandeseer.integrationtest.custom;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.drawing.cell.TextCellDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class CustomCellDrawer {

    private static final TextCellDrawer CUSTOM_DRAWER = new TextCellDrawer() {
        @Override
        protected void drawText(String text, PDFont font, int fontSize, Color color, float x, float y, PDPageContentStream contentStream)
                        throws IOException {
            System.out.println("My custom drawer is called :-)");
            super.drawText(text.toUpperCase(), font, fontSize, color, x, y, contentStream);
        }
    };

    public static void main(String[] args) throws IOException {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            TableDrawer.builder()
                    .contentStream(contentStream)
                    .table(createSimpleTable())
                    .startX(50)
                    .startY(page.getMediaBox().getHeight() - 50)
                    .build()
                    .draw();

        }

        document.save("target/customCellDrawerNoLombok.pdf");
        document.close();
    }

    private static Table createSimpleTable() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(8)
                .font(HELVETICA);

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().drawer(CUSTOM_DRAWER).borderWidth(1).text("One").build())
                .add(TextCell.builder().borderWidth(1).text("Two").build())
                .add(TextCell.builder().borderWidth(1).text("Three").build())
                .add(TextCell.builder().borderWidth(1).text("Four").build())
                .build());

        return tableBuilder.build();
    }

}
