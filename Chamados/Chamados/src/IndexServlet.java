import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IndexServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res){
		try {
			PrintWriter out = res.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Sistema de Chamados</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Sistema de Chamados</h1>");
			out.println("<hr/>");
			out.println("<a href='http://localhost:8080/Chamados/NovoChamado'>Novo Chamado</a>");
			out.println("<br>");
			out.println("<a href='http://localhost:8080/Chamados/ListarChamados'>Listar Chamados</a>");
			out.println("<br>");
			out.println("<a href='/Logoff'>Sair</a>");
			out.println("</body>");
			out.println("</html>");
		} catch (IOException e) {
			
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res){
		
	}
}