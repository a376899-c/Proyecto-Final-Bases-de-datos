/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyecto_final;
import java.awt.BorderLayout;
//mapa
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;

import org.jxmapviewer.viewer.DefaultTileFactory;

import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;

import javax.swing.event.MouseInputListener;
//puntos
import java.awt.Point;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.HashSet;
import java.util.Set;

import org.jxmapviewer.viewer.GeoPosition;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
//coordenadas
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

import java.util.ArrayList;

    
/**
 *
 * @author moral
 */
public class DashboardPasajero extends javax.swing.JFrame {
    //variables globales
    boolean mensajeMostrado = false;
     Connection con;
     ArrayList<Integer> idsViajes = new ArrayList<>();
     ArrayList<Double> latViajes = new ArrayList<>();
     ArrayList<Double> lonViajes = new ArrayList<>();
     int matriculaPasajero;
    private JXMapViewer mapa;
    
    private Set<Waypoint> waypoints =
        new HashSet<>();

    private int contadorClicks = 0;

    private GeoPosition origen;

    private GeoPosition destino;

    private boolean seleccionandoMapa = false;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardPasajero.class.getName());

    /**
     * Creates new form SolViaje
     */
    public DashboardPasajero() {
        initComponents();
        
        mapa = new JXMapViewer();

    OSMTileFactoryInfo info =
            new OSMTileFactoryInfo();

    DefaultTileFactory factory =
            new DefaultTileFactory(info);

    mapa.setTileFactory(factory);

    GeoPosition chihuahua =
            new GeoPosition(28.6353, -106.0889);

    mapa.setAddressLocation(chihuahua);

    mapa.setZoom(7);

    MouseInputListener mm =
            new PanMouseInputListener(mapa);

    mapa.addMouseListener(mm);

    mapa.addMouseMotionListener(mm);

    mapa.addMouseWheelListener(
            new ZoomMouseWheelListenerCenter(mapa));

    mapa.addMouseListener(
            new CenterMapListener(mapa));

    mapa.addKeyListener(
            new PanKeyListener(mapa));

    panelMapa.setLayout(new BorderLayout());

    panelMapa.add(mapa, BorderLayout.CENTER);

    panelMapa.revalidate();

    panelMapa.repaint();
    
    mapa.addMouseListener(new MouseAdapter() {

    @Override
    public void mouseClicked(MouseEvent e) {

        if (!seleccionandoMapa) {
            return;
        }

        Point punto = e.getPoint();

        GeoPosition posicion =
                mapa.convertPointToGeoPosition(punto);

        contadorClicks++;

        if (contadorClicks == 1) {

            origen = posicion;

            String direccion =
            obtenerDireccion(
        posicion.getLatitude(),
        posicion.getLongitude());

        txtOrigen.setText(direccion);

        } 
        
        else if (contadorClicks == 2) {

            destino = posicion;

            String direccion =
        obtenerDireccion(
        posicion.getLatitude(),
        posicion.getLongitude());

        txtDestino.setText(direccion);
        }

        waypoints.add(
                new DefaultWaypoint(posicion));

        WaypointPainter<Waypoint> painter =
                new WaypointPainter<>();

        painter.setWaypoints(waypoints);

        mapa.setOverlayPainter(painter);

        System.out.println(
                "Latitud: "
                + posicion.getLatitude());

        System.out.println(
                "Longitud: "
                + posicion.getLongitude());
    }
});    
}
   public DashboardPasajero(int matricula) {

    this();

    this.matriculaPasajero = matricula;

    revisarEstadoSolicitud();

    javax.swing.Timer t =
    new javax.swing.Timer(
    5000,
    e -> revisarEstadoSolicitud()
    );

    t.start();

}
    
    private String obtenerDireccion(double lat, double lon) {

    try {

        String urlTexto =
            "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat="
            + lat
            + "&lon="
            + lon;

        URL url = new URL(urlTexto);

        HttpURLConnection conexion =
            (HttpURLConnection) url.openConnection();

        conexion.setRequestMethod("GET");

        conexion.setRequestProperty(
            "User-Agent",
            "UniRideApp/1.0");

        conexion.setRequestProperty(
            "Accept-Language",
            "es");

        BufferedReader br =
            new BufferedReader(
                new InputStreamReader(
                    conexion.getInputStream()));

        String linea;

        StringBuilder respuesta =
            new StringBuilder();

        while ((linea = br.readLine()) != null) {

            respuesta.append(linea);
        }

        br.close();

        String json = respuesta.toString();

        int inicio =
            json.indexOf("\"display_name\":\"") + 16;

        int fin =
            json.indexOf("\",", inicio);

        String direccion =
            json.substring(inicio, fin);

        String partes[] = direccion.split(",");

        if (partes.length >= 2) {

            return partes[0] + "," + partes[1];
        }

        return direccion;

    } catch (IOException e) {

        e.printStackTrace();

        return "Lugar desconocido";
    }
    }

        
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        panelMenu = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtOrigen = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDestino = new javax.swing.JTextField();
        btnMapa = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaViajes = new javax.swing.JTable();
        btnSolicitar = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelMapa = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelMenu.setBackground(new java.awt.Color(240, 244, 255));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(26, 86, 219));
        jLabel2.setText("📍 Origen:");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(199, 212, 240)));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(26, 86, 219));
        jLabel3.setText("🏁 Destino:");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(199, 212, 240)));

        txtDestino.addActionListener(this::txtDestinoActionPerformed);

        btnMapa.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        btnMapa.setForeground(new java.awt.Color(107, 114, 128));
        btnMapa.setText("🗺️ Seleccionar en mapa");
        btnMapa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(199, 212, 240)));
        btnMapa.setBorderPainted(false);
        btnMapa.setFocusPainted(false);
        btnMapa.addActionListener(this::btnMapaActionPerformed);

        btnBuscar.setBackground(new java.awt.Color(26, 86, 219));
        btnBuscar.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("🔍 Buscar viajes");
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        jScrollPane2.setBackground(new java.awt.Color(240, 244, 255));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(199, 212, 240)));

        tablaViajes.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        tablaViajes.setForeground(new java.awt.Color(30, 30, 60));
        tablaViajes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Conductor", "Origen", "Destino", "Hora", "Fecha"
            }
        ));
        tablaViajes.setGridColor(new java.awt.Color(199, 212, 240));
        tablaViajes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaViajesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaViajes);

        jScrollPane2.setViewportView(jScrollPane1);

        btnSolicitar.setForeground(new java.awt.Color(107, 114, 120));
        btnSolicitar.setText("Solicitar Viaje");
        btnSolicitar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(199, 212, 240)));
        btnSolicitar.addActionListener(this::btnSolicitarActionPerformed);

        lblEstado.setFont(new java.awt.Font("Segoe UI Emoji", 2, 12)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(156, 163, 175));
        lblEstado.setText("⏳ Esperando solicitud...");

        jPanel1.setBackground(new java.awt.Color(26, 86, 219));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(" 🔍 BUSCAR VIAJE");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel1)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelMenuLayout = new javax.swing.GroupLayout(panelMenu);
        panelMenu.setLayout(panelMenuLayout);
        panelMenuLayout.setHorizontalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMapa, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelMenuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelMenuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addGroup(panelMenuLayout.createSequentialGroup()
                        .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMenuLayout.createSequentialGroup()
                                .addGap(170, 170, 170)
                                .addComponent(btnSolicitar))
                            .addGroup(panelMenuLayout.createSequentialGroup()
                                .addGap(137, 137, 137)
                                .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelMenuLayout.setVerticalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(btnMapa, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSolicitar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelMapa.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelMapa, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelMapa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDestinoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
    DefaultTableModel model =
            (DefaultTableModel) tablaViajes.getModel();

    model.setRowCount(0);

    idsViajes.clear();
    latViajes.clear();
    lonViajes.clear();

    try {

        con = conexion.conectar();

        String sql =
        "SELECT v.id_viaje, " +
        "c.nombre AS nombre_conductor, " +
        "v.origen, v.destino, v.hora_salida, v.fecha, " +
        "v.lat_origen, v.lon_origen " +
        "FROM viaje v " +
        "JOIN conductor c ON v.matricula_conductor = c.matricula " +
        "WHERE v.estado='Disponible'";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        //  TEXTO
        String textoOrigen = txtOrigen.getText().trim().toLowerCase();
        String textoDestino = txtDestino.getText().trim().toLowerCase();

        boolean usarTexto = !textoOrigen.isEmpty() || !textoDestino.isEmpty();

        // MAPA
        boolean usarMapa = (origen != null);

        double latPasajero = 0;
        double lonPasajero = 0;

        if (usarMapa) {
            latPasajero = origen.getLatitude();
            lonPasajero = origen.getLongitude();
        }

        while (rs.next()) {
            
            
            
            String origenBD = rs.getString("origen").toLowerCase();
            String destinoBD = rs.getString("destino").toLowerCase();

            //  FILTRO TEXTO (flexible)
            boolean matchTexto = true;

            if (usarTexto) {
                matchTexto =
                        origenBD.contains(textoOrigen) ||
                        destinoBD.contains(textoDestino);
            }

            //  FILTRO MAPA (km reales)
            boolean matchMapa = true;

            if (usarMapa) {

                double latViaje = rs.getDouble("lat_origen");
                double lonViaje = rs.getDouble("lon_origen");

                double distancia = distanciaKm(
                        latPasajero, lonPasajero,
                        latViaje, lonViaje
                );

                matchMapa = (distancia <= 2.0); //  radio 
            }

            //  FILTRO FINAL
           if (matchTexto && matchMapa) {

    idsViajes.add(
    rs.getInt("id_viaje")
    );

    latViajes.add(
    rs.getDouble("lat_origen")
    );

    lonViajes.add(
    rs.getDouble("lon_origen")
    );

    model.addRow(new Object[]{

        rs.getString("nombre_conductor"),
        rs.getString("origen"),
        rs.getString("destino"),
        rs.getString("hora_salida"),
        rs.getString("fecha")
    });
}
        }

        rs.close();
        ps.close();
        con.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }


        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnMapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMapaActionPerformed
    seleccionandoMapa = true;

    contadorClicks = 0;

    waypoints.clear();

    mapa.repaint();

    System.out.println("Modo selección activado");

        // TODO add your handling code here:
    }//GEN-LAST:event_btnMapaActionPerformed

    private void btnSolicitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSolicitarActionPerformed
    int fila = tablaViajes.getSelectedRow();

    if (fila == -1) {

        JOptionPane.showMessageDialog(
                null,
                "Selecciona un viaje primero"
        );

        return;
    }
    int idViajeSeleccionado = idsViajes.get(fila);

    System.out.println("ID viaje: " + idViajeSeleccionado);
    
    
    System.out.println(
        "Pasajero actual: "
        + matriculaPasajero
);

    String conductor =
        tablaViajes.getValueAt(fila,0).toString();

String origenViaje =
        tablaViajes.getValueAt(fila,1).toString();

String destinoViaje =
        tablaViajes.getValueAt(fila,2).toString();


// cálculo de distancia
double latViaje =
latViajes.get(fila);

double lonViaje =
lonViajes.get(fila);

double distancia =
distanciaKm(
origen.getLatitude(),
origen.getLongitude(),
latViaje,
lonViaje
);


// costo estimado
double costo =
20 + (distancia*5);


// ventana de confirmación
int opcion =
JOptionPane.showConfirmDialog(
null,

"Conductor: " + conductor +

"\nOrigen: " + origenViaje +

"\nDestino: " + destinoViaje +

"\nDistancia aproximada: " +
String.format("%.2f",distancia)
+ " km" +

"\nCosto estimado: $" +
String.format("%.2f",costo)

+

"\n\n¿Solicitar este viaje?",

"Resumen del viaje",

JOptionPane.YES_NO_OPTION
);

    if(opcion == JOptionPane.YES_OPTION){

    try{

        con = conexion.conectar();

        String sql =
        "INSERT INTO solicitud_viaje(" +
        "id_viaje, " +
        "matricula_pasajero, " +
        "punto_recogida, " +
        "lat_recogida, " +
        "lon_recogida" +
        ") VALUES (?,?,?,?,?)";

       PreparedStatement ps =
        con.prepareStatement(
        sql,
        PreparedStatement.RETURN_GENERATED_KEYS
        );

        ps.setInt(1,idViajeSeleccionado);

        ps.setInt(2,matriculaPasajero);

        ps.setString(3,txtOrigen.getText());

        ps.setDouble(4,origen.getLatitude());

        ps.setDouble(5,origen.getLongitude() );

        ps.executeUpdate();
        
        String sqlId=

"SELECT MAX(id_solicitud) " +
"FROM solicitud_viaje";

PreparedStatement psId=
con.prepareStatement(sqlId);

ResultSet rsId=
psId.executeQuery();

int idSolicitud=0;

if(rsId.next()){

    idSolicitud=
    rsId.getInt(1);

}

rsId.close();

psId.close();

        String sqlPago=

        "INSERT INTO pago(" +
        "id_solicitud," +
        "costo_estimado," +
        "fecha_pago" +
        ") VALUES(?,?,SYSDATE)";
        
        PreparedStatement psPago=
        con.prepareStatement(sqlPago);
        
        psPago.setInt(1,idSolicitud);

        psPago.setDouble(2,costo);
        
        psPago.executeUpdate();
          psPago.close();  

        JOptionPane.showMessageDialog(
                null,
                "Solicitud enviada correctamente"
        );
        
        

        ps.close();
        con.close();

    }
    catch(SQLException e){

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                null,
                "Error: " + e.getMessage()
        );
    }

}
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSolicitarActionPerformed

    private void tablaViajesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaViajesMouseClicked
    if(evt.getClickCount()==2){

        btnSolicitar.doClick();

    }

        // TODO add your handling code here:
    }//GEN-LAST:event_tablaViajesMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new DashboardPasajero().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnMapa;
    private javax.swing.JButton btnSolicitar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JPanel panelMapa;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JTable tablaViajes;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtOrigen;
    // End of variables declaration//GEN-END:variables

   private double distanciaKm(double lat1, double lon1, double lat2, double lon2) {

    double R = 6371; // radio de la Tierra en km

    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    double a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) *
            Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
}
   private void revisarEstadoSolicitud(){

    try{

        con = conexion.conectar();

        String sql=
        "SELECT estado " +
        "FROM solicitud_viaje " +
        "WHERE matricula_pasajero=? " +
        "ORDER BY id_solicitud DESC";

        PreparedStatement ps=
        con.prepareStatement(sql);

        ps.setInt(
                1,
                matriculaPasajero
        );

        ResultSet rs=
        ps.executeQuery();

       if(rs.next()){

    String estado=
    rs.getString("estado");

    lblEstado.setText(
    "Estado: " + estado
    );

    if(estado.equals("Aceptada")
&& !mensajeMostrado){

    mensajeMostrado=true;

    JOptionPane.showMessageDialog(
    null,
    "Tu viaje fue aceptado.\n\n" +
    "Dirígete puntual al punto de recogida."
    );

    ViajeEnCursoPasajero form = new ViajeEnCursoPasajero(
    matriculaPasajero
    );

    form.setVisible(true);

    this.dispose();



    }
    
    

}

        rs.close();
        ps.close();
        con.close();

    }

    catch(SQLException e){

        e.printStackTrace();

}

}

}