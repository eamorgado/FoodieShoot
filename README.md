# FoodieShoot Project
This project was developed as the final project for the subject of mobile device programming (CC3035). The objective of this project (project 3) is to implement an application to identity different foods in
a place/plate. The application is be able to perform real-time analysis of a video stream and track different foods on a plate as well as calculating its calories, in this application you can also save and visualize all your detected foods if they are saved in a post.

This application is supported by a Django backend and frontend server/site where you can also download the app https://178.79.132.23/ .

To recognize the foods, it uses our custom built dataset with 90 classes of foods and uses a pretrained SSD model that trains on our data using the TensorFlow library (the notebook used to train the model can be seen in the Model folder). Since we want to integrate our model with the app, Tensorflow 2.0 API now supports a new model format (tflite) that can reduce the consuption of resources during inference,
as such, after training the model we convert it to a tflite format.


# Documentation
1.  [The Server](https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Server.md)
2.  Request API
3.  App


# The Server
From the start, we have decided to support our app with a server that would store users and user posts, at the start of the project we were planning to serve our model in the server, as such the preferable framewrok to build the erver would be a Python framework (as Tensorflow as better support for python serving) since we also wanted to keep our database type independent we went with Django, a framework that allows us to build a database independent of implementation as objects. We would then perform REST or gRPC requests for image predictions however, even with kubernetes scalabillity the latency for android requests and responce processing made this approach less than ideal for a production environment, we then moved for the current format:
*   Django server storing User tables and data with REST API endpoints
*   Tensorflow Object detection API model converted to tflite format
*   Android application that incorporates model to perform predictions and interact with the server's REST API

In case you want to test the server yourself, In the Backend folder we have included the requirements for a virtual env where you can test the server locally using Django's development process (python manage.py runserver) however, for a production environment the recommended deployment and serving is through an Apache service, make sure your firewall isn't blocking the email ports as we use them (587) to send user password reset emails.

While developing the app, TensorFlow requires the level 28 for the android API, after android 9.0 Google blocked http requests, since we want to provide, even if at a minimum level, some type of security our apache service is SSL enabled and the HTTPS website has a self signed certificate, no further upgrades were made (the server has no domain).

