import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsuariosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mensagem = "";
		if (request.getParameter("txtLogin") != null) {
			String login = request.getParameter("txtLogin");
			String senha = request.getParameter("txtSenha");
			if (login.trim().length() == 0) {
				mensagem = "Preencha o Login!";				
			} else if (senha.trim().length() == 0) {
				mensagem = "Preencha a Senha!";
			} else {
				try {
					Class.forName("org.postgresql.Driver");
					try {
						Connection conexao = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/dbrlsystem", "postgres", "st97ch19");
						String sql;
						sql = "insert into public.usuarios (login, senha) values (?, md5(?));";
						PreparedStatement comando = conexao.prepareStatement(sql);
						comando.setString(1, request.getParameter("txtLogin").trim());
						comando.setString(2, request.getParameter("txtSenha").trim());
						comando.execute();
						comando.close();
						conexao.close();
						mensagem = "OK";
					} catch (SQLException err) {
						mensagem = "Erro ao inserir o usuário: " + err.getMessage();
					}
				} catch (ClassNotFoundException err) {
					mensagem = "Erro ao carregar o driver de conexão jdbc: " + err.getMessage();
				}				
			}
		}
		response.sendRedirect("usuarios.jsp?msg=" + mensagem);
	}
}