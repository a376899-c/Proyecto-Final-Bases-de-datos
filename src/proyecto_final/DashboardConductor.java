/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyecto_final;
import java.awt.BorderLayout;

import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;

import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;

import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Point;

import java.util.HashSet;
import java.util.Set;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

import java.awt.Graphics2D;
import java.awt.Color;

import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.painter.CompoundPainter;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.PreparedStatement;


import java.sql.SQLException;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.text.SimpleDateFormat;



/**
 *
 * @author moral
 */
public class DashboardConductor extends javax.swing.JFrame {
    private JXMapViewer mapa;
    private Set<Waypoint> waypoints =
        new HashSet<>();
    private int contadorClicks = 0;
    private GeoPosition origen;
    private String matriculaUsuario;
    
   Connection con;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardConductor.class.getName());
    private GeoPosition destino;
    
    private boolean seleccionandoMapa = false;

    /**
     * Creates new form DashboardConductor
     * @param matriculaUsuario
     */
    public DashboardConductor(String matriculaUsuario) {
        initComponents();
        this.matriculaUsuario = matriculaUsuario;
        SpinnerDateModel modeloHora =
    new SpinnerDateModel();

    spinnerHora.setModel(modeloHora);

    JSpinner.DateEditor editor =
    new JSpinner.DateEditor(
        spinnerHora,
        "HH:mm");

    spinnerHora.setEditor(editor);
        
        txtOrigen.addActionListener(e -> {

    GeoPosition pos = obtenerCoordenadas(txtOrigen.getText());

    if (pos != null) {
        origen = pos;

        waypoints.add(new DefaultWaypoint(pos));

        WaypointPainter<Waypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(waypoints);

        mapa.setOverlayPainter(painter);

        mapa.setAddressLocation(pos);

        System.out.println("Origen marcado");
    }
});
        
        txtDestino.addActionListener(e -> {

    GeoPosition pos = obtenerCoordenadas(txtDestino.getText());

    if (pos != null) {
        destino = pos;

        waypoints.add(new DefaultWaypoint(pos));

        WaypointPainter<Waypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(waypoints);

        mapa.setOverlayPainter(painter);

        mapa.setAddressLocation(pos);

        System.out.println("Destino marcado");

        if (origen != null && destino != null) {
            dibujarRuta(); // tu línea recta
        }
    }
});
        
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

if(contadorClicks == 1){
    origen = posicion;
    String direccion =
        obtenerDireccion(
                posicion.getLatitude(),
                posicion.getLongitude());

txtOrigen.setText(direccion);
}

else if(contadorClicks == 2){
    destino = posicion;
    String direccion =
            obtenerDireccion(
                    posicion.getLatitude(),
                    posicion.getLongitude());

    txtDestino.setText(direccion);
    dibujarRuta();
}
else{

    contadorClicks = 1;

    waypoints.clear();

    txtOrigen.setText("");

    txtDestino.setText("");
    String direccion =
        obtenerDireccion(
                posicion.getLatitude(),
                posicion.getLongitude());

txtOrigen.setText(direccion);
}


        System.out.println(
                "Latitud: " + posicion.getLatitude());

        System.out.println(
                "Longitud: " + posicion.getLongitude());

        waypoints.add(
                new DefaultWaypoint(posicion));

        if(contadorClicks == 1){

    WaypointPainter<Waypoint> painter =
            new WaypointPainter<>();

    painter.setWaypoints(waypoints);

    mapa.setOverlayPainter(painter);

}

else if(contadorClicks == 2){

    dibujarRuta();
    

}

    }

});
}
    private void guardarViaje() {
    try {
        con = conexion.conectar();
        
        int idVehiculo = 0;

        String sqlVehiculo =
        "SELECT id_vehiculo FROM vehiculo WHERE matricula_conductor = ?";

        PreparedStatement psVehiculo =
                con.prepareStatement(sqlVehiculo);

        psVehiculo.setString(1, matriculaUsuario);

        java.sql.ResultSet rs =
                psVehiculo.executeQuery();

        if (rs.next()) {

            idVehiculo = rs.getInt("id_vehiculo");

        } else {

            System.out.println("El conductor no tiene vehículo registrado");
            return;
        }
        
        String sql =
        "INSERT INTO viaje " +
        "(matricula_conductor, id_vehiculo, origen, destino, " +
        "lat_origen, lon_origen, lat_destino, lon_destino, estado, fecha, hora_salida) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setString(1, matriculaUsuario);
        
        ps.setInt(2, idVehiculo);

        ps.setString(3, txtOrigen.getText());
        ps.setString(4, txtDestino.getText());

        ps.setDouble(5, origen.getLatitude());
        ps.setDouble(6, origen.getLongitude());

        ps.setDouble(7, destino.getLatitude());
        ps.setDouble(8, destino.getLongitude());

        ps.setString(9, "Disponible");
        
        java.sql.Date fechaSQL =
        new java.sql.Date(
        dateViaje.getDate().getTime());

        ps.setDate(10, fechaSQL);
        
        SimpleDateFormat sdf =
        new SimpleDateFormat("hh:mm a");

        String hora =
        sdf.format(spinnerHora.getValue());

        ps.setString(11, hora);
        
        ps.executeUpdate();

        System.out.println("Viaje publicado");

    } catch (SQLException e) {

        e.printStackTrace();

    }
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

        while((linea = br.readLine()) != null){

            respuesta.append(linea);

        }

        br.close();

        String json = respuesta.toString();
        System.out.println(json);
        int inicio =
                json.indexOf("\"display_name\":\"")
                + 16;

        int fin =
                json.indexOf("\",", inicio);

        String direccion =
        json.substring(inicio, fin);

        String partes[] =
        direccion.split(",");

        if(partes.length >= 2){

        return partes[0] + "," + partes[1];

}

return direccion;
    }

    catch(IOException e){
        e.printStackTrace();
        return "Lugar desconocido";

    }

}
   private void dibujarRuta() {

    Painter<JXMapViewer> linea = (g, map, w, h) -> {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.RED);
        g2.setStroke(new java.awt.BasicStroke(3));

        Point2D p1 = map.getTileFactory().geoToPixel(origen, map.getZoom());
        Point2D p2 = map.getTileFactory().geoToPixel(destino, map.getZoom());

        Rectangle rect = map.getViewportBounds();

        int x1 = (int) p1.getX() - rect.x;
        int y1 = (int) p1.getY() - rect.y;

        int x2 = (int) p2.getX() - rect.x;
        int y2 = (int) p2.getY() - rect.y;

        g2.drawLine(x1, y1, x2, y2);

        g2.dispose();
    };

    WaypointPainter<Waypoint> painter = new WaypointPainter<>();
    painter.setWaypoints(waypoints);

    CompoundPainter<JXMapViewer> cp = new CompoundPainter<>(painter, linea);

    mapa.setOverlayPainter(cp);
}
   
   private GeoPosition obtenerCoordenadas(String lugar) {

    try {
        String urlTexto =
                "https://nominatim.openstreetmap.org/search?format=jsonv2&q="
                + lugar.replace(" ", "%20");

        URL url = new URL(urlTexto);

        HttpURLConnection conexion =
                (HttpURLConnection) url.openConnection();

        conexion.setRequestMethod("GET");
        conexion.setRequestProperty("User-Agent", "UniRideApp/1.0");

        BufferedReader br =
                new BufferedReader(new InputStreamReader(conexion.getInputStream()));

        String linea;
        StringBuilder respuesta = new StringBuilder();

        while ((linea = br.readLine()) != null) {
            respuesta.append(linea);
        }

        br.close();

        String json = respuesta.toString();

        // sacar lat y lon (forma simple)
        int latIndex = json.indexOf("\"lat\":\"") + 7;
        int latEnd = json.indexOf("\"", latIndex);

        int lonIndex = json.indexOf("\"lon\":\"") + 7;
        int lonEnd = json.indexOf("\"", lonIndex);

        double lat = Double.parseDouble(json.substring(latIndex, latEnd));
        double lon = Double.parseDouble(json.substring(lonIndex, lonEnd));

        return new GeoPosition(lat, lon);

    } catch (Exception e) {
        e.printStackTrace();
        return null;
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

        panelMenu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtOrigen = new javax.swing.JTextField();
        txtDestino = new javax.swing.JTextField();
        btnMapa = new javax.swing.JButton();
        btnPublicar = new javax.swing.JButton();
        spinnerHora = new javax.swing.JSpinner();
        btnVerSolicitud = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        panelMapa = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelMenu.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(26, 86, 219));
        jLabel1.setText("📍 Origen:");
        jLabel1.setAutoscrolls(true);

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(26, 86, 219));
        jLabel2.setText("🏁  Destino:");

        txtOrigen.setForeground(new java.awt.Color(220, 220, 235));
        txtOrigen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(60, 60, 90)));
        txtOrigen.addActionListener(this::txtOrigenActionPerformed);

        txtDestino.setForeground(new java.awt.Color(220, 220, 235));
        txtDestino.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(60, 60, 90)));

        btnMapa.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        btnMapa.setForeground(new java.awt.Color(180, 180, 210));
        btnMapa.setText("Seleccionar en mapa");
        btnMapa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80, 80, 120)));
        btnMapa.addActionListener(this::btnMapaActionPerformed);

        btnPublicar.setBackground(new java.awt.Color(26, 86, 219));
        btnPublicar.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        btnPublicar.setForeground(new java.awt.Color(255, 255, 255));
        btnPublicar.setText("➤ Publicar viaje");
        btnPublicar.setBorderPainted(false);
        btnPublicar.setFocusPainted(false);
        btnPublicar.addActionListener(this::btnPublicarActionPerformed);

        spinnerHora.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        spinnerHora.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(60, 60, 90)));
        spinnerHora.setOpaque(true);

        btnVerSolicitud.setForeground(new java.awt.Color(160, 160, 190));
        btnVerSolicitud.setText("📋 Ver solicitudes");
        btnVerSolicitud.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80, 80, 120)));
        btnVerSolicitud.addActionListener(this::btnVerSolicitudActionPerformed);

        jPanel1.setBackground(new java.awt.Color(26, 86, 219));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("PUBLICAR VIAJE");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(26, 86, 219));
        jLabel4.setText("Asientos Disponibles:");

        jLabel5.setFont(new java.awt.Font("Segoe UI Emoji", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(26, 86, 219));
        jLabel5.setText("🗺️ Mapa: ");

        javax.swing.GroupLayout panelMenuLayout = new javax.swing.GroupLayout(panelMenu);
        panelMenu.setLayout(panelMenuLayout);
        panelMenuLayout.setHorizontalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMenuLayout.createSequentialGroup()
                        .addComponent(btnPublicar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelMenuLayout.createSequentialGroup()
                        .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinnerHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnMapa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMenuLayout.createSequentialGroup()
                .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelMenuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnVerSolicitud, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMenuLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMenuLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 52, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelMenuLayout.setVerticalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMapa)
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addGap(12, 12, 12)
                .addComponent(spinnerHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPublicar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnVerSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );

        panelMapa.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelMapa, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(panelMapa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrigenActionPerformed

    private void btnMapaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMapaActionPerformed
    seleccionandoMapa = true;
    contadorClicks = 0;
    waypoints.clear();
    
    mapa.repaint();

    System.out.println("Modo selección activado");

        // TODO add your handling code here:
    }//GEN-LAST:event_btnMapaActionPerformed

    private void btnPublicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPublicarActionPerformed

        if (origen == null || destino == null) {

        System.out.println("Selecciona origen y destino");
        return;
    }

    guardarViaje();

    System.out.println("Viaje guardado correctamente");

        // TODO add your handling code here:
    }//GEN-LAST:event_btnPublicarActionPerformed

    private void btnVerSolicitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerSolicitudActionPerformed
      
   SolicitudesConductor form =
            new SolicitudesConductor(
                    matriculaUsuario
            );

    form.setVisible(true);
        
// TODO add your handling code here:
    }//GEN-LAST:event_btnVerSolicitudActionPerformed

   /**
 * @param args the command line arguments
 */
public static void main(String args[]) {

    try {
        for (javax.swing.UIManager.LookAndFeelInfo info :
                javax.swing.UIManager.getInstalledLookAndFeels()) {

            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }

    } catch (ReflectiveOperationException |
             javax.swing.UnsupportedLookAndFeelException ex) {

        logger.log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(() -> {

        new DashboardConductor("1521").setVisible(true);

    });
}

      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMapa;
    private javax.swing.JButton btnPublicar;
    private javax.swing.JToggleButton btnVerSolicitud;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelMapa;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JSpinner spinnerHora;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtOrigen;
    // End of variables declaration//GEN-END:variables
}
