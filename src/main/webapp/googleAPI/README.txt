Copyright 2011 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*********************SUMMARY*********************
This zip file includes web pages and JavaScript that call
the Google Prediction API to make predictions, stream updates
and call a hosted model.

Google Prediction API documentation available at
http://code.google.com/apis/predict/

*********************REQUIREMENTS****************
This sample requires the following items:

1. The Google Chrome browser (may not display properly on other browsers).
2. A web server where you can save the files.
3. Admin access to a trained categorical model with example format
   "category","text"

*********************SETUP***********************
1. Unzip all files.
2. Open the iodemo.js page with an editor and change the following values:
     * Replace "io11/my_data" with the name of a trained model that you
       can access (not a hosted model).
     * Replace "CHANGEME" with your OAuth2.0 client ID. Get your
       ID from the API Access page of https://code.google.com/apis/console#:overview:access
3. Save the files to a web server.
4. Activate the Google Prediction API at https://code.google.com/apis/console#:overview
5. In the API Access pane:
      * Add the URL of your iopredict.html file to the "Redirect URIs" list
      * Add iopredict.html's web host name to the "JavaScript Origins" list

*********************USAGE***********************
Open iopredict.html in Google Chrome and click the "Login" button on the
top right of the page. This will request access to your Google account.
Access will be valid for about an hour. iopredict has the following tabs:

Predict:
       Request a prediction for an arbitrary text snippet. Enter your text
       in the box and click "Predict" to send the text to a categorical model
       and show the resulting category.

Adapt:
       Send a streaming update by entering a text snippet in the "text input"
       box and a new category in the hashtag box, and clicking
       "Update".

Subscribe:
       Send a prediction request to your trained model, and also request
       sentiment analysis (positive or negative) against a hosted model
       by entering a text snippet and clicking "Predict".
