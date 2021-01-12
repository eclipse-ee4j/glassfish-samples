/*
 	Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
	
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
import java.util.logging.*;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.resource.ConnectionFactoryDefinition;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.*;
// import samples.connectors.mailconnector.ra.outbound.*;
import samples.connectors.mailconnector.api.*;
import samples.connectors.mailconnector.share.*;

/**
 */

@ConnectionFactoryDefinition(name = "java:comp/env/eis/MyConnectionFactory", 
  description = "Connection factory against mail server", 
  interfaceName = "samples.connectors.mailconnector.api.JavaMailConnectionFactory", 
  resourceAdapter = "#mailconnector", 
  minPoolSize = 2, 
  transactionSupport = TransactionSupportLevel.NoTransaction)

@WebServlet(name = "MailBrowserServlet", urlPatterns = { "/browse" })
public class MailBrowserServlet extends HttpServlet {

    @Resource(lookup="java:comp/env/eis/MyConnectionFactory")
    JavaMailConnectionFactory  connectionFactory ;

    public static final Logger logger = Logger.getLogger("samples.connectors.mailconnector.servlet");

    public void init() throws ServletException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Acquire request parameters we need
        String foldername = request.getParameter("folder");
        String servername = request.getParameter("server");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String protocol = request.getParameter("protocol");

        if (protocol == null || protocol.equals("") || foldername == null
                || foldername.equals("") || servername == null
                || servername.equals("") || username == null
                || username.equals("") || password == null
                || password.equals("")) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher(
                    "/mailbrowser.jsp");
            rd.forward(request, response);
            return;
        }

        // Save these values in the session object (would be used as default
        // values by JSP page)
        HttpSession session = request.getSession();
        session.setAttribute("folder", foldername);
        session.setAttribute("server", servername);
        session.setAttribute("username", username);
        session.setAttribute("password", password);
        session.setAttribute("protocol", protocol);

        // Prepare the beginning of our response

        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<html>");
        writer.println("<head>");
        String mailBox = username + "-" + foldername + "@" + servername;
        writer.println("<title>MailBox: " + mailBox + " </title>");
        writer.println("</head>");
        writer.println("<body bgcolor=\"white\">");

        writer.println("<strong>Mailbox: " + mailBox + " </strong><BR>");

        ConnectionSpecImpl connectionSpec = new ConnectionSpecImpl();
        connectionSpec.setUserName(username);
        connectionSpec.setPassword(password);
        connectionSpec.setServerName(servername);
        connectionSpec.setFolderName(foldername);
        connectionSpec.setProtocol(protocol);

        try(JavaMailConnection connection = connectionFactory.createConnection(connectionSpec)) {

            if (connection != null) {
                javax.mail.Message[] msgs = connection.getNewMessages();

                if (msgs != null) {
                    writer.println("<table border=\"1\""
                            + "summary=\"new messages at: " + mailBox
                            + "\"><CAPTION><EM>");
                    if (msgs.length > 1)
                        writer.println("There are " + msgs.length
                                + " new messages");
                    else
                        writer.println("There is only " + msgs.length
                                + " new message");

                    writer.println("</EM></CAPTION><tr><th>ID</th>"
                            + "<th><b>Sender</b></th><th><b>Subject</b>"
                            + "</th></tr>");
                    for (int i = 0; i < msgs.length; i++) {
                        InternetAddress[] from = (InternetAddress[]) msgs[i]
                                .getFrom();
                        String sender = "Unknown";
                        if (from.length > 0)
                            sender = from[0].toString();
                        writer.println("<tr><td>" + i + "</td><td>" + sender
                                + "</td><td>" + msgs[i].getSubject()
                                + "</td></tr>");
                    }
                    writer.println("</table>");
                    connection.close();
                } else
                    writer.println("<strong> No new messages available </strong>");
            } else
                writer.println("<strong> Can't not obtain a connection </strong>");
        } catch (Throwable t) {

            writer.println("<font color=\"red\">");
            writer.println("ERROR when attempting to open this mailbox: <B> "
                    + mailBox + "</B> <BR>" + t.getMessage());
            // writer.println("<pre>");
            // Uncomment next line to see the full stack trace
            // t.printStackTrace(writer);
            // writer.println("</pre>");
            writer.println("</font>");
            writer.println("<BR>Review the values you entered and try again.");
        }

        // Prepare the ending of our response
        writer.println("<br><br>");
        writer.println("<a href=\"mailbrowser.jsp\">Check for new messages</a><br>");
        writer.println("<a href=\"index.html\">Return to main page</a><br>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
