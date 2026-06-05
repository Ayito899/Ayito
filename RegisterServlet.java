import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            resp.sendRedirect("main");
            return;
        }
        resp.sendRedirect("register.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        String email    = req.getParameter("email");
        String course   = req.getParameter("course");
        String year     = req.getParameter("year");

        // Basic validation
        if (username == null || password == null || fullName == null ||
                email == null || course == null || year == null ||
                username.trim().isEmpty() || password.trim().isEmpty() ||
                fullName.trim().isEmpty() || email.trim().isEmpty()) {
            resp.sendRedirect("register.html?error=missing");
            return;
        }

        if (password.trim().length() < 6) {
            resp.sendRedirect("register.html?error=shortpass");
            return;
        }

        boolean ok = StudentStore.register(
                username.trim(), password.trim(),
                fullName.trim(), email.trim(),
                course.trim(), year.trim());

        if (ok) {
            resp.sendRedirect("login.html?msg=registered");
        } else {
            resp.sendRedirect("register.html?error=exists");
        }
    }
}