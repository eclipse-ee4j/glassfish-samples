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

var wsUri = getRootUri() + "/websocket-auction/auction";
var output;
var debug = false;
var websocket;
var separator = ":";
var id = 0;
var name = "";

function getRootUri() {
    return "ws://" + (document.location.hostname == "" ? "localhost" : document.location.hostname) + ":" +
        (document.location.port == "" ? "8080" : document.location.port);
}

function init() {
    output = document.getElementById("output");
    name = getParam("name");

    writeToScreen("init name: " + name);
    websocket = new WebSocket(wsUri);
    websocket.onopen = function (evt) {
        getData();
    };
    websocket.onmessage = function (evt) {
        handleResponse(evt)
    };
    websocket.onerror = function (evt) {
        onError(evt)
    };
}

function getData() {
    var myStr = "xreq" + separator + id + separator + "selectList";
    websocket.send(myStr);
}

function handleResponse(evt) {
    var mString = evt.data.toString();
    writeToScreen(evt.data);
    if (mString.search("xres") == 0) {
        var message = mString.substring(4, mString.length);
        var messageList = message.split('-'); // split on hyphen
        var i = 0;

        for (i = 1; i < messageList.length - 1; i += 2) {
            var val = messageList[i];
            var text = messageList[i + 1];
            document.getElementById("comboID").add(new Option(text, val), null);
        }
    }

    writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data + '</span>');
}

function selected() {
    writeToScreen("Select");
    var myselect = document.getElementById("comboID");
    for (var i = 0; i < myselect.options.length; i++) {
        if (myselect.options[i].selected == true) {
            var link = "auction.html" + "?id=" + myselect.options[i].value + "&name=" + name;
            break
        }
    }
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

function getParam(sname) {
    var params = location.search.substr(location.search.indexOf("?") + 1);
    var sval = "";
    params = params.split("&");
    // split param and value into individual pieces
    for (var i = 0; i < params.length; i++) {
        var temp = params[i].split("=");
        if (temp[0] === sname) {
            sval = temp[1];
        }
    }
    return sval;
}

window.addEventListener("load", init, false);
