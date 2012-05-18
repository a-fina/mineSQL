   // Copyright 2011 Google Inc.

   // Licensed under the Apache License, Version 2.0 (the "License");
   // you may not use this file except in compliance with the License.
   // You may obtain a copy of the License at

   //     http://www.apache.org/licenses/LICENSE-2.0

   // Unless required by applicable law or agreed to in writing, software
   // distributed under the License is distributed on an "AS IS" BASIS,
   // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   // See the License for the specific language governing permissions and
   // limitations under the License.

   // NOTE: You will need to change the name of the model called in prediction.predict and also insert your key from code.google.com/apis/console where it says "CHANGEME" below
googleapis.auth.checkLoginComplete();

function predict() {
  prediction.predict(
    {'data': 'io11/my_data', 
     'input': {'csvInstance': [ document.getElementById('predictquery').value ]}})
    .execute(predictionCallback);
   prediction.hostedmodels.predict(
     {'hostedModelName': 'sample.sentiment', 
      'input': {'csvInstance': [ document.getElementById('predictquery').value ]}})
     .execute(hostedModelCallback);
}

function sendUpdate(snippet) {
  prediction.training.update(
    {'data': 'io11/my_data', 
     'classLabel': [ document.getElementById('topic').value],
     'csvInstance': [ snippet ]})
    .execute(updateCallback);
}

function init() {
    checkStatus();
}

function login() {
    var config = {
	'client_id': '681512503508.apps.googleusercontent.com',
	'scope': 'https://www.googleapis.com/auth/prediction',
    };
    googleapis.auth.login(config, checkStatus);
}

function checkStatus() {
    var token = googleapis.auth.getToken();
    console.log(token);
    if (token) {
      document.getElementById('loginstatus').innerHTML='Logged In';
    } else {
      document.getElementById('loginstatus').innerHTML='Logged Out';
    }
}


//
// Functions for loading messages to use for model updating.
//
function loadUpdates() {
  var updatesRequest = new XMLHttpRequest();
  updatesRequest.overrideMimeType("application/json");
  updatesRequest.open('GET', 'updates2.json', true);
  updatesRequest.onreadystatechange = function () {
    if (updatesRequest.readyState == 4) {
      var updatesJSON = jQuery.parseJSON(updatesRequest.responseText);
      displayUpdates(updatesJSON);
    }
  }
  updatesRequest.send(null);
}

function displayUpdates(jsonUpdates) {
  for (var update in jsonUpdates) {
    if (jsonUpdates.hasOwnProperty(update)) {
      var item = jsonUpdates[update];
      sendUpdate(item.snippet);
      $("#results").append(
          '<div class="update" id="update' + item + '">'+
             ' <span class="formText" id="updateText">Text:</span>'+
             ' <span class="snippet">'+ item.snippet +'</span><br />'+
             ' <span class="formText" id="updateLabel">Label:</span>'+
             '<span class="datalabel">'+ document.getElementById('topic').value + '</span>' +
          '</div>');
    }
  }
}

//
// Callbacks for the Prediction API requests.
//
function predictionCallback(resp) {
    if ('error' in resp) {
	console.log(resp['error']);
    }
    console.log('prediction callback!');
    console.log(resp);
    document.getElementById('tag_category').innerHTML=resp['outputLabel'];
}

function updateCallback(resp) {
    if ('error' in resp) {
	console.log(resp['error']);
    }
    console.log('update callback!');
    console.log(resp);
}

function hostedModelCallback(resp) {
    if ('error' in resp) {
	console.log(resp['error']);
    }
    console.log('hosted model callback!');
    console.log(resp);
    document.getElementById('tag_sentiment').innerHTML=resp['outputLabel'];
}
