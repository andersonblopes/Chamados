

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ListarChamadosServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		HttpSession sessao = request.getSession();
		
		if(sessao.getAttribute("login") == null){
			response.sendRedirect("http://localhost:8080/Chamados/Login");
		}
		
		out.println("SEU IP = " + sessao.getAttribute("info"));
		out.println("<br><a href='http://localhost:8080/Chamados/Login?msg=logoff'>Sair</a>");
		try{
			Class.forName("com.mysql.jdbc.Driver");
			try {
				
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/chamados_rlsystem", "root", "");
				
				// Deleta o registro
				// Quando exisitir uma QUERY String ID
				if (request.getParameter("id") != null){
					int ID = Integer.parseInt(request.getParameter("id"));
					String SQLDelete = "DELETE FROM chamados WHERE id = ?";
					PreparedStatement pstm = conn.prepareStatement(SQLDelete);
					pstm.setInt(1, ID);
					pstm.execute();
				}
				
				String SQL = "SELECT * FROM chamados";
				
				Statement stm = conn.createStatement();
				
				ResultSet rs = stm.executeQuery(SQL);
				
				out.println("<table width='100%'>");
				
				out.println("<tr bgcolor='#c0c0c0'>");
				out.println("<td>ID</td>");
				out.println("<td>Titulo</td>");
				out.println("<td>Data</td>");
				out.println("<td>Editar</td>");
				out.println("<td>Apagar</td>");
				out.println("</tr>");
				while (rs.next()){
					out.println("<td>" + rs.getInt("id") + "</td>");
					out.println("<td>" + rs.getString("titulo") + "</td>");
					out.println("<td>" + rs.getDate("data") + "</td>");
					out.println("<td><a href='http://localhost:8080/Chamados/EditarChamado?id="+rs.getInt("id")+"'>[EDITAR]</a></td>");
					out.println("<td><a href='http://localhost:8080/Chamados/ListarChamados?id="+rs.getInt("id")+"'>[APAGAR]</a></td>");
					out.println("</tr>");
				}
				out.println("</table>");
				
				stm.close();
				conn.close();
				
			} catch (SQLException e) {
				out.println("Problema no banco de dados: " + e.getMessage());
			}
			
		} catch (ClassNotFoundException ex){
			out.println("Problema ao carregar o driver de conexão!");
		}
	}
}
