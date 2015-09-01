import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditarChamadoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		TratarLogin.Verificar(request, response);
		int ID = 0;
		String titulo = "";
		String conteudo = "";
		String mensagemErro="";
		boolean erro=false;
		if (request.getParameter("id") != null) {
			ID = Integer.parseInt(request.getParameter("id"));
			try {
				Class.forName("org.postgresql.Driver");
				try {
					Connection conexao = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/dbrlsystem", "postgres", "st97ch19");
					String sql;
					sql = "";
					sql += "select";
					sql += " public.chamados.titulo,";
					sql += " public.chamados.conteudo";
					sql += " from";
					sql += " public.chamados";
					sql += " where";
					sql += " public.chamados.id=?";
					PreparedStatement comando = conexao.prepareStatement(sql);
					comando.setInt(1, ID);
					ResultSet resultado = comando.executeQuery();
					while (resultado.next()) {
						titulo = resultado.getString("titulo");
						conteudo = resultado.getString("conteudo");
					}
					resultado.close();
					comando.close();
					conexao.close();
				} catch (SQLException err) {
					erro = true;
					mensagemErro = "Erro ao obter os dados do chamado: " + Integer.toString(ID) + " - " + err.getMessage();
				}
			} catch (ClassNotFoundException err) {
				erro = true;
				mensagemErro = "Erro ao carregar o driver de conexão jdbc: " + err.getMessage();
			}
		} else {
			erro = true;
			mensagemErro = "Código do chamado não informado!";
		}
		this.Listar(response, erro, mensagemErro, titulo, conteudo, ID);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		TratarLogin.Verificar(request, response);
		String titulo = "";
		String conteudo = "";
		int ID = 0;
		String mensagemErro="";
		boolean erro=false;
		if (request.getParameter("txtCodigo") != null) {
			titulo = request.getParameter("txtTitulo");
			conteudo = request.getParameter("txtConteudo");
			ID = Integer.parseInt(request.getParameter("txtCodigo"));
			if (request.getParameter("txtCodigo").equals("0")) {
				erro=true;
				mensagemErro = "Código do Chamado não informado!";
			} else if (titulo.trim().length() == 0) {
				erro=true;
				mensagemErro = "Preencha o título!";				
			} else if (conteudo.trim().length() == 0) {
				erro=true;
				mensagemErro = "Preencha o conteúdo!";
			} else {
				try {
					Class.forName("org.postgresql.Driver");
					try {
						Connection conexao = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/dbrlsystem", "postgres", "st97ch19");
						String sql;
						sql = "update public.chamados set titulo=?, conteudo=? where public.chamados.id=?;";
						PreparedStatement comando = conexao.prepareStatement(sql);
						comando.setString(1, titulo.trim().toUpperCase());
						comando.setString(2, conteudo.trim().toUpperCase());
						comando.setInt(3, ID);
						comando.execute();
						comando.close();
						conexao.close();
						erro=false;
					} catch (SQLException err) {
						erro = true;
						mensagemErro = "Erro ao alterar o chamado: " + err.getMessage();
					}
				} catch (ClassNotFoundException err) {
					erro = true;
					mensagemErro = "Erro ao carregar o driver de conexão jdbc: " + err.getMessage();
				}				
			}
		} else {
			erro=true;
			mensagemErro = "Código do Chamado não informado!";
		}
		this.Listar(response, erro, mensagemErro, titulo, conteudo, ID);
		if (erro == false) {
			try {
				response.sendRedirect("ListaChamados");
			} catch (IOException err) {
				
			}
		}
	}
	
	private void Listar(HttpServletResponse response, boolean erro, String mensagem, String titulo, String conteudo, int ID) {
		try {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Sistema de Chamados - Editar Chamado</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<b>Editar Chamado</b>");
			out.println("<hr/>");
			if (erro) {
				out.println("<span style'color:red'>" + mensagem + "</span><br>");
			}
			out.println("<form method='POST'>");
			out.println("<input type='hidden' name='txtCodigo' value='" + Integer.toString(ID) + "'>");
			out.println("Título:<br><input type='text' name='txtTitulo' value='" + titulo +"'><br><br>");
			out.println("Conteúdo:<br><textarea name='txtConteudo' rows=10 cols=40>" + conteudo + "</textarea><br><br>");
			out.println("<input type='submit' value='Salvar Chamado'>");
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