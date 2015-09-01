
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		
		// Exemplo de Apagar Cookie
		Cookie[] ck2 = request.getCookies();
		
		if( ck2 != null){
			for(Cookie cookie : ck2){
				if (cookie.getName().equals("login_name")){
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
		// Exemplo de Apagar Cookie
		
		try {
			PrintWriter out = response.getWriter();
			
			// Sair
			if (request.getParameter("msg") != null){
				if (request.getParameter("msg").equals("logoff")){
					HttpSession sessao = request.getSession();
					sessao.invalidate();
					out.println("<span style='color: red'>Deslogado com sucesso.!</span>");
				}
			}
			
			
			if (request.getParameter("msg") != null){
				if (request.getParameter("msg").equals("error")){
					out.println("<span style='color: red'>Login e/ou senhas incorreto!</span>");
				}
			}
			
			String login_name = "";
			
			Cookie[] ck = request.getCookies();
			
			if( ck != null){
				for(Cookie cookie : ck){
					if (cookie.getName().equals("login_name")){
						login_name = cookie.getValue();
					}
				}
			}
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Login</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Preencha seu login e senha</h1>");
			out.println("<hr/>");
			out.println("<form method='POST'>");
			out.println("Login:<br> <input type='text' name='txtLogin' value='"+ login_name +"'>");
			out.println("<br>");
			out.println("Senha:<br> <input type='password' name='txtSenha'>");
			out.println("<br>");
			out.println("<input type='submit' value='Logar'>");
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

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		String login = request.getParameter("txtLogin");
		String senha = request.getParameter("txtSenha");
		
		Cookie ck = new Cookie("login_name", login);
		ck.setMaxAge(60*60*24*30);
		response.addCookie(ck);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			try {
				Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost/chamados_rlsystem", "root", "");

				String SQL = "SELECT * FROM usuarios WHERE login = ? and senha = ?";
				
				PreparedStatement pstm = conn.prepareStatement(SQL);

				pstm.setString(1, login);
				pstm.setString(2, senha);

				ResultSet rs = pstm.executeQuery();

				if (rs.next()){
					pstm.close();
					conn.close();
					HttpSession sessao = request.getSession();
					sessao.setAttribute("login", login);
					sessao.setAttribute("info", request.getRemoteAddr());
					response.sendRedirect("http://localhost:8080/Chamados/ListarChamados");
				} else {
					pstm.close();
					conn.close();
					response.sendRedirect("http://localhost:8080/Chamados/Login?msg=error");
				}
				
				

			} catch (SQLException e) {
				out.println("Problema no banco de dados: " + e.getMessage());
			}

		} catch (ClassNotFoundException ex) {
			out.println("Problema ao carregar o driver de conexão!");
		}

	}

}
