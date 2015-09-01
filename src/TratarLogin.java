import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TratarLogin {
	
	public static void Verificar (HttpServletRequest request, HttpServletResponse response) {
		HttpSession sessao = request.getSession();
		if (sessao.getAttribute("Ichamados_usuario") == null) {
			try {
				response.sendRedirect("Login?action=Login");
			} catch (IOException err) {
				
			}
		}
	}
	
	public static void Logoff (HttpServletRequest request, HttpServletResponse response) {
		HttpSession sessao = request.getSession();
		sessao.invalidate();
		try {
			response.sendRedirect("Index");
		} catch (IOException err) {
			
		}
	}
}