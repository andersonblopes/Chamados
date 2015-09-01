import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public String login_name="";
       
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("action").equals("Login")) {
			Cookie[] getck = request.getCookies();
			if (getck != null){
				for (Cookie cookie : getck){
					if (cookie.getName().equals("login_name")){
						this.login_name=cookie.getValue();
					}
				}
			}
			this.Listar(request, response, false, "", "", "");
		} else if (request.getParameter("action").equals("Logoff")) {
			TratarLogin.Logoff(request, response);
		} else {
			this.Listar(request, response, true, "Ação não definida", "", "");
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String login = request.getParameter("txtLogin");
		String senha = request.getParameter("txtSenha");
		String mensagemErro="";
		boolean erro=false;
		if (login.trim().length() == 0) {
			erro=true;
			mensagemErro = "Preencha o login!";				
		} else if (senha.trim().length() == 0) {
			erro=true;
			mensagemErro = "Preencha a senha!";
		} else {
			try {
				Class.forName("org.postgresql.Driver");
				try {
					Connection conexao = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/dbrlsystem", "postgres", "st97ch19");
					String sql;
					sql = "select public.usuarios.id as codusuario from public.usuarios where public.usuarios.login=? and public.usuarios.senha=md5(?)";
					PreparedStatement comando = conexao.prepareStatement(sql);
					comando.setString(1, login.trim());
					comando.setString(2, senha.trim());
					ResultSet resultado = comando.executeQuery();
					if (resultado.next()) {
						HttpSession sessao = request.getSession();
						sessao.setAttribute("Ichamados_usuario", resultado.getInt("codusuario"));
						sessao.setAttribute("Ichamados_informacoes", request.getRemoteAddr());
						Cookie setck = new Cookie("login_name", login.trim());
						setck.setMaxAge(36000); // 10 minutos. Para apagar basta colocar o valor 0. 
						response.addCookie(setck);
						resultado.close();
						comando.close();
						conexao.close();
						response.sendRedirect("Index");
					} else {
						erro=true;
						resultado.close();
						comando.close();
						conexao.close();
						mensagemErro = "Acesso inválido!";
					}
				} catch (SQLException err) {
					erro=true;
					mensagemErro = "Erro ao consultar: " + err.getMessage();
				}
			} catch (ClassNotFoundException err) {
				erro=true;
				mensagemErro = "Erro ao carregar o driver de conexão jdbc: " + err.getMessage();
			}				
		}
		if (erro == false) { 
			this.Listar(request, response, erro, "", "", "");
		} else {
			this.Listar(request, response, erro, mensagemErro, login, senha);
		}
	}
	
	private void Listar(HttpServletRequest request, HttpServletResponse response, boolean erro, String mensagem, String login, String senha) {
		try {
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Sistema de Chamados - Login</title>");
			out.println("</head>");
			out.println("<body>");
			if (request.getParameter("action").isEmpty()) {
				out.println("<span style'color:red'>" + mensagem + "</span><br>");
			} else {
				if (erro) {
					out.println("<span style'color:red'>" + mensagem + "</span><br>");
				}
				out.println("<b>Acesso ao Sistema</b>");
				out.println("<hr/>");
				out.println("<form method='POST'>");
				out.println("Login:<br><input type='text' name='txtLogin' value='");
				if (login.equals(""))
					out.println(this.login_name);
				else
					out.println(login);
				out.println("'><br><br>");
				out.println("Senha:<br><input type='password' name='txtSenha' value='" + senha +"'><br><br>");
				out.println("<input type='submit' value='Acessar'>");
				out.println("</form>");
			}
			out.println("</body>");
			out.println("</html>");
		} catch (IOException err) {
			
		}
	}
}