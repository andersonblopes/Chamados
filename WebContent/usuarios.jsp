<%@page import="java.sql.SQLException" %>
<%@page import="java.sql.Connection" %>
<%@page import="java.sql.DriverManager" %>
<%@page import="java.sql.PreparedStatement" %>
<%@page language='java' contentType='text/html; charset=ISO-8859-1' pageEncoding='ISO-8859-1'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>
		<title>Sistema de Chamados - Novo Usuários</title>
	</head>
	<body>
		<b>Novo Usuário</b>
		<hr/>
		<%
		if (request.getParameter("msg") != null) {
			if (request.getParameter("msg").trim().equals("OK")) {
				out.println("<span style'color:blue'>Usuário cadastrado com sucesso!</span><br>");
			} else {
				out.println("<span style'color:red'>" + request.getParameter("msg").trim() + "</span><br>");
			}
		}
		%>
		<form method='POST' action='Usuarios'>
			Login:<br><input type='text' name='txtLogin' value=''><br><br>
			Senha:<br><input type='password' name='txtSenha' value=''><br><br>
			<input type='submit' value='Cadastrar'>
		</form>
	</body>
</html>