/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;
import DAO.*;
import DTO.UsuarioJpaController;

public class Prueba {
    
    public static void main(String[] args) throws Exception {
          Usuario user = new Usuario();
        user.setNombre("---g2-2qw");
        
        Conexion con = Conexion.getConexion();
        UsuarioJpaController reg = new UsuarioJpaController(con.getBd());
         reg.create(user);
        System.out.println("registrado" + user.getNombre());
        
    }
    
}
