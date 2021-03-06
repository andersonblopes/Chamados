<%@page import="java.sql.SQLException" %>
<%@page import="java.sql.Connection" %>
<%@page import="java.sql.DriverManager" %>
<%@page import="java.sql.PreparedStatement" %>
<%@page import="java.sql.ResultSet" %>
<%@page import="br.com.delos.classes.Utils" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Sistema de Chamados - Listagem de Usu�rios</title>
	</head>
<body>
	<table width=100% cellpadding=2 cellspacing=2>
		<tr>
			<td width=1% nowrap><b>C�digo</b></td>
			<td width=1% nowrap><b>Usu�rio</b></td>
			<td width=98% nowrap></td>
		</tr>
		<%
		
		Utils mensagem = new Utils();
		out.println(mensagem.Retorno("Valor da mensagem"));
		
		try {
			Class.forName("org.postgresql.Driver");
			try {
				Connection conexao = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/dbrlsystem", "postgres", "st97ch19");
				String sql;
				sql = "";
				sql += "select";
				sql += " public.usuarios.id as codigo,";
				sql += " public.usuarios.login as login";
				sql += " from";
				sql += " public.usuarios";
				sql += " where";
				sql += " 1=?";
				sql += " order by";
				sql += " public.usuarios.login asc";
				PreparedStatement comando = conexao.prepareStatement(sql);
				comando.setInt(1, 1);
				ResultSet resultado = comando.executeQuery();
				while (resultado.next()) {
		%>
		<tr>
			<td nowrap><%= resultado.getString("codigo") %></td>
			<td nowrap><%= resultado.getString("login") %></td>
			<td nowrap></td>
		</tr>
		<%
				}
				resultado.close();
				comando.close();
				conexao.close();
			} catch (SQLException err) {
				out.println("Erro ao listar os chamados: " + err.getMessage());
			}
		} catch (ClassNotFoundException err) {
			out.println("Erro ao carregar o driver de conex�o jdbc: " + err.getMessage());
		}
		%>
	</table>
</body>
</html>