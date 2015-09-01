<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="teste.Teste"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" errorPage="erro.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cadastro de Usuários</title>
</head>
<body>
	<table>

		<tr>
			<td>ID</td>
			<td>Login</td>
		</tr>
		<%
				Teste t = new Teste();
				out.println(t.testar());
		
				Class.forName("com.mysql.jdbc.Driver");

				String SQL = "SELECT * FROM usuarios";


					Connection conn = DriverManager.getConnection(
							"jdbc:mysql://localhost/chamados_rlsystem", "root",
							"");

					Statement stm = conn.prepareStatement(SQL);

					ResultSet rs = stm.executeQuery(SQL);
					while (rs.next()) {
		%>
		<tr>
			<td><%=rs.getString("id")%></td>
			<td><%=rs.getString("login")%></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>