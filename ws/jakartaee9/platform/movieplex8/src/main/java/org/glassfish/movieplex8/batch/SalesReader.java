/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.movieplex8.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.batch.api.chunk.AbstractItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;

/**
 * @author Arun Gupta
 */
@Dependent
@Named
public class SalesReader extends AbstractItemReader {

    private BufferedReader reader;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        reader = new BufferedReader(
                new InputStreamReader(
                Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("META-INF/sales.csv")));
    }

    @Override
    public String readItem() {
        String string = null;
        try {
            string = reader.readLine();
            System.out.println("SalesReader.readItem: " + string);
        } catch (IOException ex) {
            Logger.getLogger(SalesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return string;
    }
}
