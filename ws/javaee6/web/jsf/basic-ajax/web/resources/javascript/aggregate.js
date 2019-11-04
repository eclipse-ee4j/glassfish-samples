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

var increment = 1000; //  1 sec timeout, only to show results.  Should be more like 50ms
var token;
function aggregate(target, element) {
    window.clearTimeout(token);
    addStatusMessage("cleared request, requeued");
    var send = function send() {
        jsf.ajax.request(element, null, {render: target});
    };
    token = window.setTimeout(send, increment);
}

function addStatusMessage(message) {
    var statusElement = document.getElementById("status");
    var status = statusElement.value;
    var now = new Date();
    var timestamp = now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds();
    status = timestamp + ' ' + message + '\n' + status;
    statusElement.value = status;
}

jsf.ajax.addOnEvent(function(data) {
    if (data.status === "begin") {
        addStatusMessage("request sent");
    }
});
