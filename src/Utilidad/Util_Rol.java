package Utilidad;

import java.util.List;

import DAO.Usuario;

public class Util_Rol {
	
	public static void main(String[] args) {
		
		
		
	}
	
	public Usuario BuscarUser(List<Usuario> u,String usuario) {
		List<Usuario> user=u;
		Usuario t=new Usuario();
       
        for(Usuario p: user) {
            
            if(p.getUsuario().equals(usuario)){
            	t=p;
            	 return t;
            } 
        }
        
        return t;
      
}
    

}
