import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // If already logged in, redirect to main
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            resp.sendRedirect("main");
            return;
        }
        resp.sendRedirect("login.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null ||
                username.trim().isEmpty() || password.trim().isEmpty()) {
            resp.sendRedirect("login.html?error=missing");
            return;
        }

        if (StudentStore.authenticate(username.trim(), password.trim())) {
            HttpSession session = req.getSession(true);
            session.setAttribute("username", username.trim());
            session.setMaxInactiveInterval(30 * 60); // 30 min
            resp.sendRedirect("main");
        } else {
            resp.sendRedirect("login.html?error=invalid");
        }
    }
}