package daos;

import basededatos.Singleton;
import modelos.Coleccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColeccionDAO {

		//
    public boolean insertarColeccion(Coleccion c) {
        /**
 	     * 06/05/2025
 	     * Programador: Javi
 	     * v1.0 inserta una coleccion a la lista de colecciones  
 	     * @param c
 	     * Salida: boolean
 	     */
        String sql="INSERT INTO colecciones(nombre,idUsuario) VALUES(?,?)";
        try(Connection conn=Singleton.getInstance().getConnection();
            PreparedStatement p=conn.prepareStatement(sql)) {
            p.setString(1,c.getNombre());
            p.setInt(2,c.getIdUsuario());
            return p.executeUpdate()>0;
        }catch(SQLException e){
            System.err.println("Error insertarColeccion: "+e.getMessage());
            return false;
        }
    }//public boolean insertarColeccion(Coleccion c)
    
    
    public Coleccion obtenerPorId(int idColeccion) {
        /**
 	     * 06/05/2025
 	     * Programador: Javi
 	     * v1.0 obtiene una coleccion por id 
 	     * @param idColeccion
 	     * Salida: Coleccion

 	     */
        String sql="SELECT * FROM colecciones WHERE idColeccion=?";
        try(Connection conn=Singleton.getInstance().getConnection();
            PreparedStatement p=conn.prepareStatement(sql)) {
            p.setInt(1,idColeccion);
            ResultSet rs=p.executeQuery();
            if(rs.next()) return new Coleccion(rs.getInt("idColeccion"),rs.getString("nombre"),rs.getInt("idUsuario"));
        }catch(SQLException e){
            System.err.println("Error obtenerPorId Coleccion: "+e.getMessage());
        }
        return null;
    }//public Coleccion obtenerPorId(int idColeccion)

    public List<Coleccion> obtenerPorUsuario(int idUsuario) {
        /**
 	     * 06/05/2025
 	     * Programador: Javi
 	     * v1.0 obtiene la lista de colecciones correspondientes a un usuario 
 	     * @param idUsuario
 	     * Salida: lista
 	     */
    	
        String sql="SELECT * FROM colecciones WHERE idUsuario=?";
        List<Coleccion> lista=new ArrayList<>();
        try(Connection conn=Singleton.getInstance().getConnection();
            PreparedStatement p=conn.prepareStatement(sql)) {
            p.setInt(1,idUsuario);
            ResultSet rs=p.executeQuery();
            while(rs.next()) lista.add(new Coleccion(rs.getInt("idColeccion"),rs.getString("nombre"),rs.getInt("idUsuario")));
        }catch(SQLException e){
            System.err.println("Error obtenerPorUsuario Coleccion: "+e.getMessage());
        }
        return lista;
    }//public List<Coleccion> obtenerPorUsuario(int idUsuario)
    
    //a
    public boolean actualizarColeccion(Coleccion c) {
        /**
 	     * 06/05/2025
 	     * Programador: Javi
 	     * v1.0 actualiza la coleccion 
 	     * @param c
 	     * Salida: boolean
 	     */
        String sql="UPDATE colecciones SET nombre=? WHERE idColeccion=?";
        try(Connection conn=Singleton.getInstance().getConnection();
            PreparedStatement p=conn.prepareStatement(sql)) {
            p.setString(1,c.getNombre());
            p.setInt(2,c.getIdColeccion());
            return p.executeUpdate()>0;
        }catch(SQLException e){
            System.err.println("Error actualizarColeccion: "+e.getMessage());
            return false;
        }
    }//public boolean actualizarColeccion(Coleccion c)
    
    
    public List<String> obtenerNombresPorUsuario(int idUsuario) {
        /**
 	     * 06/05/2025
 	     * Programador: Javi
 	     * v1.0 obtiene nombres de coleccion por id de usuario 
 	     * @param idUsuario
 	     * Salida: lista
 	     */
        String sql = "SELECT nombre FROM colecciones WHERE idUsuario = ?";
        List<String> nombres = new ArrayList<>();
        try (Connection c = Singleton.getInstance().getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idUsuario);
            ResultSet rs = p.executeQuery();
            while (rs.next()) nombres.add(rs.getString("nombre"));

        } catch (SQLException e) {
            System.err.println("Error nombres coleccion: "+e.getMessage());
        }
        return nombres;
    }
    
    public int obtenerIdPorNombre(String nombre, int idUsuario) {
        /**
 	     * 06/05/2025
 	     * Programador: Javi
 	     * v1.0 obtiene id de coleccion por nombre de coleccion e id de usuario
 	     * @param idUsuario
 	     * @param nombre 
 	     * Salida: lista
 	     */
        String sql = "SELECT idColeccion FROM colecciones WHERE nombre=? AND idUsuario=?";
        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, nombre);
            p.setInt(2, idUsuario);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                return rs.getInt("idColeccion");
            }

        } catch (SQLException e) {
            System.err.println("Error obtenerIdPorNombre: " + e.getMessage());
        }

        return -1; 
    }
    
    //
    public List<Integer> obtenerIdsPorUsuario(int idUsuario) {
    	
        /**
 	     * 06/05/2025
 	     * Programador: Javi
 	     * v1.0 obtieen los ids de las colecciones por usuario
 	     * @param idUsuario
 	     * Salida: lista
 	     */
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT idColeccion FROM colecciones WHERE idUsuario = ?";

        try (Connection conn = Singleton.getInstance().getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setInt(1, idUsuario);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                ids.add(rs.getInt("idColeccion"));
            }

        } catch (SQLException e) {
            System.err.println("Error obtenerIdsPorUsuario: " + e.getMessage());
        }

        return ids;
    }// public List<Integer> obtenerIdsPorUsuario(int idUsuario) 

}
