package Controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Conexion.Conexion;
import DAO.Rol;
import DAO.Usuario;
import DTO.UsuarioJpaController;
import Utilidad.Util_Rol;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login.do")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String user = request.getParameter("user");
        String pass = request.getParameter("pass");

        Conexion con = Conexion.getConexion();
        UsuarioJpaController reg = new UsuarioJpaController(con.getBd());
        List<Usuario> us=reg.findUsuarioEntities();
        Util_Rol ur=new Util_Rol();      
      Usuario buscado= ur.BuscarUser(us, user);    
     
   
       
        try {
        	List<Rol> rol=buscado.getRolList();
        	
        	for(Rol rbus: rol) {
                
                if(rbus.getDescripcion().equals("Administrador")&&buscado.getClave().equals(pass)){
                	 HttpSession misession = request.getSession(true);
                     misession.setAttribute("usuario", buscado.getNombre());
                     misession.setAttribute("email", buscado.getUsuario());

                     response.sendRedirect("index_admin.jsp");
                } else if(rbus.getDescripcion().equals("Auxiliar")&&buscado.getClave().equals(pass)){
               	 HttpSession misession = request.getSession(true);
                 misession.setAttribute("usuario", buscado.getNombre());
                 misession.setAttribute("email", buscado.getUsuario());

                 response.sendRedirect("index_auxiliar.jsp");
            } 
            }
 
            
                String tipo = "NoExiste";
                    request.setAttribute("nombre", tipo);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
            

//        
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String user = request.getParameter("user");
        String pass = request.getParameter("pass");

        Conexion con = Conexion.getConexion();
        UsuarioJpaController reg = new UsuarioJpaController(con.getBd());
        List<Usuario> us=reg.findUsuarioEntities();
        Util_Rol ur=new Util_Rol();      
      Usuario buscado= ur.BuscarUser(us, user);    
     
   
       
        try {
        	List<Rol> rol=buscado.getRolList();
        	
        	for(Rol rbus: rol) {
                
                if(rbus.getDescripcion().equals("Administrador")&&buscado.getClave().equals(pass)){
                	 HttpSession misession = request.getSession(true);
                     misession.setAttribute("usuario", buscado.getNombre());
                     misession.setAttribute("email", buscado.getUsuario());

                     response.sendRedirect("index_admin.jsp");
                } else if(rbus.getDescripcion().equals("Auxiliar")&&buscado.getClave().equals(pass)){
               	 HttpSession misession = request.getSession(true);
                 misession.setAttribute("usuario", buscado.getNombre());
                 misession.setAttribute("email", buscado.getUsuario());

                 response.sendRedirect("index_auxiliar.jsp");
            } 
            }
 
            
                String tipo = "NoExiste";
                    request.setAttribute("nombre", tipo);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
            

//        
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            
        }
	}

}
