
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Usuario extends HttpServlet {

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		if (request.getParameter("txtLogin") != null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				String SQL = "INSERT INTO usuarios (login, senha) VALUES (?, ?)";

				try {
					Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost/chamados_rlsystem", "root",
							"");

					PreparedStatement pstm = conn.prepareStatement(SQL);

					pstm.setString(1, request.getParameter("txtLogin"));
					pstm.setString(2, request.getParameter("txtSenha"));

					pstm.execute();

					pstm.close();

					conn.close();

					response.sendRedirect("http://localhost:8080/Chamados/cadastro_usuario2.jsp?msg=sucesso");

				} catch (SQLException e) {
					out.println("Problema no banco de dados: " + e.getMessage());
				}

			} catch (ClassNotFoundException ex) {
				out.println("Problema ao carregar o driver de conexão!");
			}

		}
	}

}
