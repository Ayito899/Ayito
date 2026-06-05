import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/main")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login.html?error=session");
            return;
        }

        String username = (String) session.getAttribute("username");
        String[] data = StudentStore.getStudent(username);

        if (data == null) {
            session.invalidate();
            resp.sendRedirect("login.html?error=session");
            return;
        }

        String encoded = java.net.URLEncoder.encode(username, "UTF-8");
        resp.sendRedirect("main.html?user=" + encoded);
    }
}