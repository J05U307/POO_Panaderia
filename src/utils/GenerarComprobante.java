package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import entity.Boleta;
import entity.Comprobante;
import entity.Factura;
import entity.Pedido;
import entity.DetalleVenta;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import repository.ComprobanteRepository;
import service.PedidoService;
import service.DetalleVentaService;

public class GenerarComprobante {

    private PedidoService pedidoService = new PedidoService();
    private ComprobanteRepository repo = new ComprobanteRepository();
    private DetalleVentaService detalleService = new DetalleVentaService();

    public String generarComprobantePDF(int pedidoId) {

        Pedido pedido = pedidoService.buscar(pedidoId);

        if (pedido == null) {
            return "ERROR: Pedido no encontrado";
        }

        Comprobante comprobante = repo.findByPedido(pedido);

        if (comprobante == null) {
            return "ERROR: El pedido no tiene comprobante generado";
        }

        try {
            return crearPDF(comprobante, pedido);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR al generar PDF: " + e.getMessage();
        }
    }

    private String crearPDF(Comprobante comp, Pedido pedido)
            throws DocumentException, IOException {

        File folder = new File("data/comprobantes/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String numeroFormateado = String.format("%05d", comp.getNumero());

        String filePath = "data/comprobantes/"
                + comp.getSerie() + "-" + numeroFormateado + ".pdf";

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();


        String fechaCompleta = comp.getFecha().toString();
        String fecha = fechaCompleta.substring(0, 10).replace("-", "/");
        String hora = fechaCompleta.substring(11, 19);


        Paragraph tituloEmpresa = new Paragraph("LA PASTENERÍA S.A.C.",
                new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        tituloEmpresa.setAlignment(Element.ALIGN_CENTER);

        Paragraph datosEmpresa = new Paragraph(
                "RUC: 2060163469\n"
                + "Dirección: Av. Larco con los Cisnes 1156 - Trujillo\n"
                + "Giros: Panadería - Postres - Bocaditos - Tortas\n",
                new Font(Font.FontFamily.HELVETICA, 10)
        );
        datosEmpresa.setAlignment(Element.ALIGN_CENTER);

        document.add(tituloEmpresa);
        document.add(datosEmpresa);


        String tipoDoc = (comp instanceof Boleta) ? "BOLETA" : "FACTURA";

        Paragraph titulo = new Paragraph(tipoDoc,
                new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        Paragraph serieNum = new Paragraph(
                comp.getSerie() + " - " + numeroFormateado,
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)
        );
        serieNum.setAlignment(Element.ALIGN_CENTER);
        document.add(serieNum);


        String nombreCliente = (pedido.getCliente() != null)
                ? pedido.getCliente().getNombre()
                : "Sin cliente asignado";

        document.add(new Paragraph("Cliente: " + nombreCliente));
        document.add(new Paragraph("Fecha: " + fecha));
        document.add(new Paragraph("Hora: " + hora));
        document.add(new Paragraph("Tipo de pago: " + comp.getPago().getNombre()));

        if (comp instanceof Boleta) {
            document.add(new Paragraph("DNI: " + ((Boleta) comp).getDni()));
        } else {
            Factura f = (Factura) comp;
            document.add(new Paragraph("RUC: " + f.getRuc()));
            document.add(new Paragraph("Razón Social: " + f.getRazonSocial()));
        }

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{4, 1, 2, 2});
        tabla.setSpacingBefore(10);
        tabla.setSpacingAfter(10);

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

        PdfPCell h1 = new PdfPCell(new Phrase("Producto", headerFont));
        PdfPCell h2 = new PdfPCell(new Phrase("Cant.", headerFont));
        PdfPCell h3 = new PdfPCell(new Phrase("P. Unit.", headerFont));
        PdfPCell h4 = new PdfPCell(new Phrase("Subtotal", headerFont));

        BaseColor gris = new BaseColor(200, 200, 200);

        h1.setBackgroundColor(gris);
        h2.setBackgroundColor(gris);
        h3.setBackgroundColor(gris);
        h4.setBackgroundColor(gris);

        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);

        tabla.addCell(h1);
        tabla.addCell(h2);
        tabla.addCell(h3);
        tabla.addCell(h4);

        List<DetalleVenta> detalles = detalleService.listarPorPedido(pedido.getId());

        for (DetalleVenta det : detalles) {
            tabla.addCell(det.getProducto().getNombre());
            tabla.addCell(String.valueOf(det.getCantidad()));
            tabla.addCell("S/. " + det.getProducto().getPrecio());
            tabla.addCell("S/. " + det.getSubtotal());
        }

        document.add(tabla);

        
        Paragraph total = new Paragraph(
                "\nTOTAL A PAGAR:   S/. " + pedido.getTotal(),
                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)
        );
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);
        
        Paragraph footer = new Paragraph(
                "Gracias por su compra.\nLa Pastenería S.A.C.",
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC)
        );
        footer.setAlignment(Element.ALIGN_CENTER);

        document.add(footer);
        document.close();

        return filePath;
    }   

}
