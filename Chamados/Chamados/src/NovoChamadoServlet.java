

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NovoChamadoServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Novo Chamado</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Preencha as informações do chamado</h1>");
			out.println("<hr/>");
			out.println("<form method='POST'>");
			out.println("Título:<br> <input type='text' name='txtTitulo'>");
			out.println("<br>");
			out.println("Conteúdo:<br> <textarea name='txtConteudo' rows='10' cols='40'></textarea>");
			out.println("<br>");
			out.println("<input type='submit' value='Abrir Chamado'>");
			out.println("</form>");
			out.println("<br>");
			out.println("<a href='http://localhost:8080/Chamados/ListarChamados'>Listar Chamados</a>");
			out.println("<br>");
			out.println("<a href='http://localhost:8080/Chamados/Logoff'>Sair</a>");
			out.println("</body>");
			out.println("</html>");
		} catch (IOException e) {
			
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			PrintWriter out = response.getWriter();
			
			String titulo = request.getParameter("txtTitulo");
			String conteudo = request.getParameter("txtConteudo");
			
			if (titulo.trim().length() < 4){
				out.println("Preencha o campo titulo");
			} else if (conteudo.trim().length() < 4){
				out.println("Preencha o campo conteudo");
			} else {
				try{
					Class.forName("com.mysql.jdbc.Driver");
					
					//String SQL = "INSERT INTO chamados (titulo, conteudo) VALUES (";
					///SQL += " '"+ titulo +"', '"+ conteudo +"') ";
					String SQL = "INSERT INTO chamados (titulo, conteudo, data) VALUES (?, ?, ?)";
					try {
						Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/chamados_rlsystem", "root", "");
						
						PreparedStatement pstm = conn.prepareStatement(SQL);
						
						Date dataSQL = new Date(new java.util.Date().getTime());
						
						//SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
						// dt.format(data);
					
						pstm.setString(1, titulo);
						pstm.setString(2, conteudo);
						pstm.setDate(3, dataSQL);
						
						pstm.execute();
						
						pstm.close();
						
						conn.close();
						
						response.sendRedirect("http://localhost:8080/Chamados/ListarChamados");
						
					} catch (SQLException e) {
						out.println("Problema no banco de dados: " + e.getMessage());
					}
					
				} catch (ClassNotFoundException ex){
					out.println("Problema ao carregar o driver de conexão!");
				}
			}
	}
}
