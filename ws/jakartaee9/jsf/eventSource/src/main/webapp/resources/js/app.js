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

/**
 * Server Sent Event message handling function dynamically builds a table
 * based on the message data.
 */ 
function stockHandler(event) {
    var eventData = event.data.toString().replace(/^\s*/,"").replace(/\s*$/,"");
    var stockTable = document.getElementById("stockTable");
    var rowCount = stockTable.rows.length - 1;
    if (eventData.length == 0) {
        stockTable.style.visibility="hidden";
        clearTableRows(stockTable, rowCount);
        return;
    }
    var quotes = eventData.split(" ");
    if (quotes.length !== rowCount || quotes.length == 0) {
        clearTableRows(stockTable, rowCount);
    }
    if (quotes !== null) {
        stockTable.style.visibility="visible";
        var newRow, newCell;
        for (var i = 0; i < quotes.length; i++) {
            var fields = quotes[i].split(":");
            if (document.getElementById(fields[0]) !== 'undefined' &&
                document.getElementById(fields[0]) !== null) {
                document.getElementById(fields[0]).innerHTML = fields[0];
                document.getElementById(fields[0]+1).innerHTML = fields[1];
                document.getElementById(fields[0]+2).innerHTML = fields[2];
                if (fields[3] == "UP") {
                    document.getElementById(fields[0]+3).innerHTML =
                        "<img src='resources/up_g.gif'/>";
                } else if (fields[3] == "DOWN") {
                    document.getElementById(fields[0]+3).innerHTML =
                        "<img src='resources/down_r.gif'/>";
                } else {
                    document.getElementById(fields[0]+3).innerHTML = "";
                }
            } else {
                newRow = stockTable.insertRow(stockTable.rows.length);
                newCell = newRow.insertCell(newRow.cells.length);
                newCell.id = fields[0];
                newCell.innerHTML = fields[0];
                newCell.align = "center";
                newCell.width = "20%";
                newCell = newRow.insertCell(newRow.cells.length);
                newCell.id = fields[0]+1;
                newCell.innerHTML = fields[1];
                newCell.align = "center";
                newCell.width = "20%";
                newCell = newRow.insertCell(newRow.cells.length);
                newCell.id = fields[0]+2;
                newCell.innerHTML = fields[2];
                newCell.align = "center";
                newCell.width = "20%";
                newCell = newRow.insertCell(newRow.cells.length);
                newCell.id = fields[0]+3;
                if (fields[3] == "UP") {
                    document.getElementById(fields[0]+3).innerHTML =
                        "<img src='resources/up_g.gif'/>";
                } else if (fields[3] == "DOWN") {
                    document.getElementById(fields[0]+3).innerHTML =
                        "<img src='resources/down_r.gif'/>";
                } else {
                    document.getElementById(fields[0]+3).innerHTML = "";
                }

                newCell.align = "center";
                newCell.width = "20%";
            }
        }
    }
}

function clockHandler(event) {
    var eventData = event.data.toString().replace(/^\s*/,"").replace(/\s*$/,"");
    var clock = document.getElementById("clock");
    clock.innerHTML = eventData;
}

function rssHandler(event) {
    var eventData = event.data.toString().replace(/^\s*/,"").replace(/\s*$/,"");
    var rssInfo = document.getElementById("rssInfo");
    if (eventData.length == 0) {
        rssInfo.style.visibility="hidden";
    } else {
        rssInfo.style.visibility="visible";
    }
    rssInfo.innerHTML="";
    var rssArray = eventData.split("*****");
    for (var i = 0; i <= rssArray.length; i++) {
        rssInfo.innerHTML += rssArray[i] + "<br/>";
    }
    
}

function clearTableRows(stockTable, rowCount) {
    for (var i = 1; i <= rowCount; i++) {
        stockTable.deleteRow(i);
    }
}
