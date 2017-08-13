package test.ignite.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>This application demonstrates usage of Apache Ignite for session distribution.
 * Application has been developed using <i>javax.servlet</i> technology. Session has been
 * distributed through out multiple nodes using ignite.</p>
 *
 * <p>Start point Servlet to the web application. Responses with simple web page
 * and puts served server ip and port to the user session.
 * Web page contains input field to obtain user input.</p>
 *
 * @author Yasitha Thilakaratne
 * @since v-1.0.0
 */
public class SampleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        HttpSession session = req.getSession();

        String localIpPortCurReq = req.getLocalAddr() + ':' + req.getLocalPort();

        String sessionId = session.getId();

        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<h1>Hello Servlet Distributed</h1>");
        out.println("<br>");
        out.println("<p>Processed server ip and port current request: " + localIpPortCurReq);
        out.println("<p>User session ID: " + sessionId + "</p>");
        out.println("<form action='/ignite-test/set-session' method='post'>");
        out.println("<p>Enter value to be added to session:</p>");
        out.println("<input type='text' name='text-val'/>");
        out.println("<input type='submit' value='Set to session'/>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

        session.setAttribute("local-ip-port", localIpPortCurReq);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
