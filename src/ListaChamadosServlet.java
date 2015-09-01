import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListaChamadosServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		TratarLogin.Verificar(request, response);
		if (request.getParameter("op") != null) {
			if (request.getParameter("id") != null) {
				String Operacao = request.getParameter("op");
				int ID = Integer.parseInt(request.getParameter("id"));
				try {
					Class.forName("org.postgresql.Driver");
					try {
						Connection conexao = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/dbrlsystem", "postgres", "st97ch19");
						String SQL;
						if (Operacao.equals("E")) {
							SQL = "delete from public.chamados where public.chamados.id=?";
							PreparedStatement comando = conexao.prepareStatement(SQL);
							comando.setInt(1, ID);
							comando.execute();
							comando.close();
							conexao.close();
							this.Listar(response, "");
						} else {
							conexao.close();
							response.sendRedirect("EditarChamado?id=" + Integer.toString(ID));
						}
					} catch (SQLException err) {
						this.Listar(response, "Erro ao acessar o banco de dados: " + err.getMessage());
					}
				} catch (ClassNotFoundException err) {
					this.Listar(response, "Erro ao carregar o driver de conexão jdbc: " + err.getMessage());
				}
			} else {
				this.Listar(response, "id não informado!");
			}
		} else {
			this.Listar(response, "");
		}
	}
	
	private void Listar(HttpServletResponse response,  String erro) {
		try {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Sistema de Chamados - Listagem de Chamados</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<b>Listagem de Chamados</b>");
			out.println("<hr/>");
			if (erro.trim().length() > 0) {
				out.println("<span style'color:red'>" + erro + "</span><br>");
			}
			out.println("<table width=100% cellpadding=2 cellspacing=2>");
			out.println("<tr>");
			out.println("<td width=1% nowrap><b>Código</b></td>");
			out.println("<td width=1% nowrap><b>Data</b></td>");
			out.println("<td width=1% nowrap><b>Título</b></td>");
			out.println("<td width=1% nowrap><b>Ações</b></td>");
			out.println("<td width=96% nowrap>&nbsp</td>");
			out.println("</tr>");
			try {
				Class.forName("org.postgresql.Driver");
				try {
					Connection conexao = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/dbrlsystem", "postgres", "st97ch19");
					String SQL;
					SQL = "";
					SQL += "select";
					SQL += " public.chamados.id as codigo,";
					SQL += " public.formatar_datahora(cast(public.chamados.data as text)) as data,";
					SQL += " public.chamados.titulo";
					SQL += " from";
					SQL += " public.chamados";
					SQL += " order by";
					SQL += " public.chamados.id asc";
					Statement comando = conexao.createStatement();
					ResultSet resultado = comando.executeQuery(SQL);
					while (resultado.next()) {
						out.println("<tr>");
						out.println("<td nowrap>" + resultado.getInt("codigo") + "</td>");
						out.println("<td nowrap>" + resultado.getString("data") + "</td>");
						out.println("<td nowrap>" + resultado.getString("titulo") + "</td>");
						out.println("<td nowrap>");
						out.println("<a href='ListaChamados?op=A&id=" + resultado.getInt("codigo") + "'>Alterar</a>");
						out.println("&nbsp&nbsp");
						out.println("<a href='ListaChamados?op=E&id=" + resultado.getInt("codigo") + "'>Excluir</a>");
						out.println("</td>");
						out.println("<td nowrap>&nbsp</td>");
						out.println("</tr>");
					}
					resultado.close();
					comando.close();
					conexao.close();
				} catch (SQLException err) {
					out.println("Erro ao listar os chamados: " + err.getMessage());
				}
			} catch (ClassNotFoundException err) {
				out.println("Erro ao carregar o driver de conexão jdbc: " + err.getMessage());
			}
			out.println("</table>");
			out.println("<br>");
			out.println("<br/>");
			out.println("<a href='NovoChamado'>Novo Chamado</a>");
			out.println("<a href='ListaChamados'>Listar Chamados</a>");
			out.println("<a href='Login?action=Logoff'>Sair</a>");
			out.println("</body>");
			out.println("</html>");
		} catch (IOException err) {
			
		}
	}
}