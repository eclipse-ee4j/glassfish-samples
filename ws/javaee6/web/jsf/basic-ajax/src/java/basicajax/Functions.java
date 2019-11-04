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

package basicajax;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * EL Functions.
 */
public class Functions {

    private static final Logger LOGGER = Logger.getLogger(Functions.class.getName());


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>
     * Write the file content to the current ResponseWriter.
     * </p>
     *
     * @param ctx the <code>FacesContext</code> for the current request
     * @param file the file to display
     */
    public static void writeSource(FacesContext ctx, String file) {

        // PENDING - add logic to colorize key words/XML elements?
        // PENDING - add logic to strip licence header

        ExternalContext extCtx = ctx.getExternalContext();
        BufferedReader r =
              new BufferedReader(
                    new InputStreamReader(extCtx.getResourceAsStream(file)));
        StringWriter w = new StringWriter();
        PrintWriter pw = new PrintWriter(w);

        try {
            int lineNumber = 1;
            for (String s = r.readLine(); s != null; s = r.readLine()) {
                pw.format("%3s", Integer.toString(lineNumber++));
                pw.write(": ");
                pw.write(s);
                pw.write('\n');
            }
            ctx.getResponseWriter().writeText(w.toString(), null);
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           ioe.toString(),
                           ioe);
            }
        }

    }
}
