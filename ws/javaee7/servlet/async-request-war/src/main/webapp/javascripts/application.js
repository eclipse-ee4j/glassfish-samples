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
var count = 0;
var app = {
   url: '/async-request-war/chat',
   initialize: function() {
      $('login-name').focus();
      app.listen();
   },
   listen: function() {
      $('comet-frame').src = app.url + '?' + count;
      count ++;
   },
   login: function() {
      var name = $F('login-name');
      if(! name.length > 0) {
	 $('system-message').style.color = 'red';
	 $('login-name').focus();
	 return;
      }

      var query =
	 'action=login' +
	 '&name=' + encodeURI($F('login-name'));
      new Ajax.Request(app.url, {
	 postBody: query,
         onFailure: function(r) {
            $('display').style.color = 'red';
            $('display').textContent = 'Fail to make ajax call ' 
                                       + r.status + " Error"; 
         },
	 onSuccess: function() {
            $('system-message').style.color = '#2d2b3d';
            $('system-message').innerHTML = name + ':'; 
             
            $('login-button').disabled = true;
            $('login-form').style.display = 'none';
            $('message-form').style.display = '';
	    $('message').focus();
	 }
      });
   },
   post: function() {
      var message = $F('message');
      if(!message > 0) {
	 return;
      }
      $('message').disabled = true;
      $('post-button').disabled = true;

      var query =
	 'action=post' +
	 '&name=' + encodeURI($F('login-name')) +
	 '&message=' + encodeURI(message);
      new Ajax.Request(app.url, {
	 postBody: query,
	 onComplete: function() {
	    $('message').disabled = false;
	    $('post-button').disabled = false;
	    $('message').focus();
	    $('message').value = '';
	 }
      });
   },
   update: function(data) {
      var p = document.createElement('p');
      p.innerHTML = data.name + ':<br/>' + data.message;
      
      $('display').appendChild(p);

      //new Fx.Scroll('display').down();
   }
};
var rules = {
   '#login-name': function(elem) {
      Event.observe(elem, 'keydown', function(e) {
	 if(e.keyCode == 13) {
	    $('login-button').focus();
	 }
      });
   },
   '#login-button': function(elem) {
      elem.onclick = app.login;
   },
   '#message': function(elem) {
      Event.observe(elem, 'keydown', function(e) {
	 if(e.keyCode == 13) {
	    $('post-button').focus();
	 } else if(e.e.ctrlKey && e.keyCode == 13) {
             $('message').textContent = "\n"
         }
      });
   },
   '#post-button': function(elem) {
      elem.onclick = app.post;
   }
};
Behaviour.addLoadEvent(app.initialize);
Behaviour.register(rules);
