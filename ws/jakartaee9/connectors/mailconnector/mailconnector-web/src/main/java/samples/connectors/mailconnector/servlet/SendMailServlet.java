/*
 	Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
	
	This program and the accompanying materials are made available under the
	terms of the Eclipse Public License v. 2.0, which is available at
	http://www.eclipse.org/legal/epl-2.0.
	
	This Source Code may also be made available under the following Secondary
	Licenses when the conditions for such availability set forth in the
	Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
	version 2 with the GNU Classpath Exception, which is available at
	https://www.gnu.org/software/classpath/license.html.
	
	SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
*/

package samples.connectors.mailconnector.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.annotation.Resource;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.MailSessionDefinition;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Example servlet that sends a mail message via a JNDI resource.
 * @author Craig McClanahan
 */
@MailSessionDefinition(
        name = "java:app/env/TheMailSession",
        storeProtocol = "IMAP", 
        transportProtocol = "SMTP", 
        host = "localhost", 
        user = "joe", 
        password = "joe", 
        from = "daphu", 
        properties = {
                "mail.imap.class=com.sun.mail.imap.IMAPStore",
                "mail.smtp.class=com.sun.mail.smtp.SMTPTransport" 
        }
)
@WebServlet(name = "SendMailServlet", urlPatterns = { "/mail" })
public class SendMailServlet extends HttpServlet {

    public static final Logger logger = Logger.getLogger("samples.connectors.mailconnector.servlet");
    
    @Resource(lookup="java:app/env/TheMailSession")
    Session session;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Acquire request parameters we need
        String from = request.getParameter("mailfrom");
        String to = request.getParameter("mailto");
        String subject = request.getParameter("mailsubject");
        String content = request.getParameter("mailcontent");
        if ((from == null) || (to == null) || (subject == null)
                || (content == null)) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher(
                    "/sendmail.jsp");
            rd.forward(request, response);
            return;
        }

        // Save these values in the session object (would be used as default
        // values by JSP page)
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("mailfrom", from);
        httpSession.setAttribute("mailto", to);
        httpSession.setAttribute("mailsubject", subject);

        // Prepare the beginning of our response
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>GlassFish v3 : "
                + "Connector 1.6 Examples: Sending Results</title>");
        writer.println("</head>");
        writer.println("<body bgcolor=\"white\">");

        try {

            // Prepare our mail message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress dests[] = new InternetAddress[] { new InternetAddress(
                    to) };
            message.setRecipients(Message.RecipientType.TO, dests);
            message.setSubject(subject);
            message.setContent(content, "text/plain");

            // Send our mail message
            Transport.send(message);

            // Report success
            writer.println("<strong>Message successfully sent!</strong>");

        } catch (Throwable t) {
            logger.log(
                    Level.SEVERE,
                    "SendMailServlet failed to lookup mail session resource: java:comp/env/TheMailSession ",
                    t);
            writer.println("<font color=\"red\">");
            writer.println("ENCOUNTERED EXCEPTION:  " + t);
            writer.println("<pre>");
            t.printStackTrace(writer);
            writer.println("</pre>");
            writer.println("</font>");

        }

        // Prepare the ending of our response
        writer.println("<br><br>");
        writer.println("<a href=\"sendmail.jsp\">Create a new message</a><br>");
        writer.println("<a href=\"index.html\">Return to main page</a><br>");
        writer.println("</body>");
        writer.println("</html>");

    }

}
