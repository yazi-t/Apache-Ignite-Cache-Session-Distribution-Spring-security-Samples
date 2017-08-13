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
 * <p>Captures posted user input from previous web page's form. Displays ip and port of
 * server which handled previous request and ip and port of current server instance.
 * Puts ip port of server and user send data into the session.</p>
 *
 * @author Yasitha Thilakaratne
 * @since v-1.0.0
 */
public class SessionSetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Object localIpPortPrevReqObj = session.getAttribute("local-ip-port");
        String localIpPortPrevReq = localIpPortPrevReqObj != null ? localIpPortPrevReqObj.toString() : "N/A";

        String localIpPortCurReq = req.getLocalAddr() + ':' + req.getLocalPort();

        String sessionId = session.getId();

        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<h1>Hello Servlet Distributed</h1>");
        out.println("<br>");
        out.println("<p>Processed server ip and port previous request: " + localIpPortPrevReq);
        out.println("<p>Processed server ip and port current request: " + localIpPortCurReq);
        out.println("<p>User session ID: " + sessionId + "</p>");
        out.println("<p>Entered value has been saved in the session. To view saved value click on this link...</p>");
        out.println("<a href='/ignite-test/get-session'>Get session value</a>");
        out.println("</body>");
        out.println("</html>");

        String valueReceived = req.getParameter("text-val");
        session.setAttribute("value", valueReceived);

        session.setAttribute("local-ip-port", localIpPortCurReq);
    }
}
