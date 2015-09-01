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
		if (request.getParameter("txtLogin") != null) {
			String mensagemErro = "";
			String login = request.getParameter("txtLogin");
			String senha = request.getParameter("txtSenha");
			if (login.trim().length() == 0) {
				mensagemErro = "Preencha o Login!";				
			} else if (senha.trim().length() == 0) {
				mensagemErro = "Preencha a Senha!";
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
						response.sendRedirect("Index");
					} catch (SQLException err) {
						mensagemErro = "Erro ao inserir o usuário: " + err.getMessage();
					}
				} catch (ClassNotFoundException err) {
					mensagemErro = "Erro ao carregar o driver de conexão jdbc: " + err.getMessage();
				}				
			}
			if (mensagemErro.trim().length() > 0)
				out.println("<span style'color:red'>" + mensagemErro + "</span><br>");
		}
		%>
		<form method='POST' action='cadastrar_usuarios.jsp'>
			Login:<br><input type='text' name='txtLogin' value=''><br><br>
			Senha:<br><input type='password' name='txtSenha' value=''><br><br>
			<input type='submit' value='Cadastrar'>
		</form>
	</body>
</html>