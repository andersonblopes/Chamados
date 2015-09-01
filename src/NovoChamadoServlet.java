import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NovoChamadoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
       
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		TratarLogin.Verificar(request, response);
		this.Listar(response, false, "", "", "");
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		TratarLogin.Verificar(request, response);
		String titulo = request.getParameter("txtTitulo");
		String conteudo = request.getParameter("txtConteudo");
		String mensagemErro="";
		boolean erro=false;
		if (titulo.trim().length() == 0) {
			erro=true;
			mensagemErro = "Preencha o título!";				
		} else if (conteudo.trim().length() == 0) {
			erro=true;
			mensagemErro = "Preencha o conteúdo!";
		} else {
			erro=false;
			try {
				Class.forName("org.postgresql.Driver");
				try {
					Connection conexao = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/dbrlsystem", "postgres", "st97ch19");
					String sql;
					//sql = "insert into public.chamados (titulo, conteudo, data) values (?, ?, now());";
					sql = "insert into public.chamados (titulo, conteudo, data) values (?, ?, ?);";
					PreparedStatement comando = conexao.prepareStatement(sql);
					comando.setString(1, titulo.trim().toUpperCase());
					comando.setString(2, conteudo.trim().toUpperCase());
					Date datasql = new Date(new java.util.Date().getTime());
					comando.setDate(3, datasql);
					comando.execute();
					comando.close();
					conexao.close();
				} catch (SQLException err) {
					erro=true;
					mensagemErro = "Erro ao inserir o chamado: " + err.getMessage();
				}
			} catch (ClassNotFoundException err) {
				erro=true;
				mensagemErro = "Erro ao carregar o driver de conexão jdbc: " + err.getMessage();
			}				
		}
		if (erro == false) { 
			this.Listar(response, erro, "", "", "");
		} else {
			this.Listar(response, erro, mensagemErro, titulo, conteudo);
		}
	}
	
	private void Listar(HttpServletResponse response, boolean erro, String mensagem, String titulo, String conteudo) {
		try {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Sistema de Chamados - Novo Chamado</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<b>Novo Chamado</b>");
			out.println("<hr/>");
			if (erro) {
				out.println("<span style'color:red'>" + mensagem + "</span><br>");
			}
			out.println("<form method='POST'>");
			out.println("Título:<br><input type='text' name='txtTitulo' value='" + titulo +"'><br><br>");
			out.println("Conteúdo:<br><textarea name='txtConteudo' rows=10 cols=40>" + conteudo + "</textarea><br><br>");
			out.println("<input type='submit' value='Abrir Chamado'>");
			out.println("</form>");
			out.println("<br>");
			out.println("<a href='ListaChamados'>Listar Chamados</a>");
			out.println("<a href='Login?action=Logoff'>Sair</a>");
			out.println("</body>");
			out.println("</html>");
		} catch (IOException err) {
			
		}
	}
}