package Utilidad;


import java.sql.Date;
import java.util.List;

import Conexion.Conexion;
import DAO.*;
import DTO.*;

public class Registrar_administrador {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	   	Date nacimiento = new Date(119, 6, 25);
        Usuario u= new Usuario();
      u.setNombre("Yeison");
      u.setClave("12345");
      u.setEmail("yeison@admin.com");
      u.setFechanacimiento(nacimiento);
      u.setUsuario("admin1");        
       Conexion con = Conexion.getConexion();
       UsuarioJpaController reg = new UsuarioJpaController(con.getBd());
        reg.create(u);
        //registro de rol
        Rol r= new Rol();
        r.setDescripcion("Administrador");
        RolJpaController rol=new RolJpaController(con.getBd());
        rol.create(r);
        //registro del usuario y el rol
        List<Rol> lr=u.getRolList();
        lr.add(r);
      
        
     
       System.err.println("registrado");
       
       
       
	}
	
	

}
