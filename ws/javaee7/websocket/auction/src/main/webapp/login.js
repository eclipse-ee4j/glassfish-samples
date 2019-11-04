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

var wsUri = getRootUri() + "/websocket-auction/auction";
var output;
var debug = false;
var websocket;
var separator = ":";
var id = 0;

function getRootUri() {
    return "ws://" + (document.location.hostname == "" ? "localhost" : document.location.hostname) + ":" +
        (document.location.port == "" ? "8080" : document.location.port);
}

function init() {
    output = document.getElementById("output");
    websocket = new WebSocket(wsUri);
    websocket.onopen = function (evt) {
        login();
    };
    websocket.onmessage = function (evt) {
        handleResponse(evt)
    };
    websocket.onerror = function (evt) {
        onError(evt)
    };
}

function login() {
}

function doLogin() {
    var myStr = "lreq" + separator + id + separator + document.getElementById("loginID").value;
    websocket.send(myStr);
    window.setTimeout('to_select()', 10);
}

function to_select() {
    var link = "select.html?name=" + document.getElementById("loginID").value;
    window.location = link;
}


function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(message) {
    if (debug) {
        var pre = document.createElement("p");
        pre.style.wordWrap = "break-word";
        pre.innerHTML = message;
        output.appendChild(pre);
    }
}

window.addEventListener("load", init, false);
