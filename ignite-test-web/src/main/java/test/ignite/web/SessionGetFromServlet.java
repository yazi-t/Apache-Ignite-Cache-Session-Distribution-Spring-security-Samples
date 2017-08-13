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
 * <p>Displays user send data which has been stored in session. Displays ip and port of
 * server which handled previous request and ip and port of current server instance.</p>
 *
 * @author Yasitha Thilakaratne
 * @since v-1.0.0
 */
public class SessionGetFromServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Object localIpPortPrevReqObj = session.getAttribute("local-ip-port");
        String localIpPortPrevReq = localIpPortPrevReqObj != null ? localIpPortPrevReqObj.toString() : "N/A";

        String localIpPortCurReq = req.getLocalAddr() + ':' + req.getLocalPort();

        Object valueObj = session.getAttribute("value");
        String value = valueObj != null ? valueObj.toString() : "N/A";

        String sessionId = session.getId();

        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<h1>Hello Servlet Distributed</h1>");
        out.println("<br>");
        out.println("<p>Processed server ip and port previous request: " + localIpPortPrevReq);
        out.println("<p>Processed server ip and port current request: " + localIpPortCurReq);
        out.println("<p>User session ID: " + sessionId + "</p>");
        out.println("<p>Session value: " + value + "</p>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
