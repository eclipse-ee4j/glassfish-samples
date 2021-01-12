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
package org.glassfish.executor;

import java.util.Map;
import java.util.concurrent.Future;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedTask;
import javax.enterprise.concurrent.ManagedTaskListener;

/**
 * @author Arun Gupta
 */
public class MyTaskWithListener implements Runnable, ManagedTask, ManagedTaskListener {
    
    private int id;

    public MyTaskWithListener() {
    }

    public MyTaskWithListener(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.printf("%d: running");
    }

    @Override
    public void taskSubmitted(Future<?> future, ManagedExecutorService mes, Object task) {
        System.out.printf("%d: submitted", id);
    }

    @Override
    public void taskAborted(Future<?> future, ManagedExecutorService mes, Object task, Throwable thrwbl) {
        System.out.printf("%d: aborted", id);
    }

    @Override
    public void taskDone(Future<?> future, ManagedExecutorService mes, Object task, Throwable thrwbl) {
        System.out.printf("%d: done", id);
    }

    @Override
    public void taskStarting(Future<?> future, ManagedExecutorService mes, Object task) {
        System.out.printf("%d: starting", id);
    }

    @Override
    public ManagedTaskListener getManagedTaskListener() {
        return this;
    }

    @Override
    public Map<String, String> getExecutionProperties() {
        return null;
    }
    
}
