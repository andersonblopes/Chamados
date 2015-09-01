import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IndexServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		TratarLogin.Verificar(request, response);
		try {
			PrintWriter out = response.getWriter();
			HttpSession sessao = request.getSession();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Sistema de Chamados</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<b>Sistema de Chamados</b>");
			out.println("<hr/>");
			out.println("<br/>");
			out.println("<a href='NovoChamado'>Novo Chamado</a>");
			out.println("<a href='ListaChamados'>Listar Chamados</a>");
			out.println("<a href='Login?action=Logoff'>Sair</a>");
			out.println("<br><br>");
			out.println(sessao.getAttribute("Ichamados_informacoes"));
			out.println("</body>");
			out.println("</html>");
		} catch (IOException err) {
			
		}
	}
}