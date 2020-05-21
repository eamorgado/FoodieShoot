[FoodieShoot](https://github.com/eamorgado/FoodieShoot/blob/master/README.md)->[Documentation](https://github.com/eamorgado/FoodieShoot/tree/master/Documentation)->Server


# The Server
From the start, we have decided to support our app with a server that would store users and user posts, at the start of the project we were planning to serve our model in the server, as such the preferable framewrok to build the erver would be a Python framework (as Tensorflow as better support for python serving) since we also wanted to keep our database type independent we went with Django, a framework that allows us to build a database independent of implementation as objects. We would then perform REST or gRPC requests for image predictions however, even with kubernetes scalabillity the latency for android requests and responce processing made this approach less than ideal for a production environment, we then moved for the current format:
*   Django server storing User tables and data with REST API endpoints accessible at https://178.79.132.23/
*   Tensorflow Object detection API model converted to tflite format
*   Android application that incorporates model to perform predictions and interact with the server's REST API

In case you want to test the server yourself, In the Backend folder we have included the requirements for a virtual env where you can test the server locally using Django's development process (python manage.py runserver) however, for a production environment the recommended deployment and serving is through an Apache service, make sure your firewall isn't blocking the email ports as we use them (587) to send user password reset emails.

While developing the app, TensorFlow requires the level 28 for the android API, after android 9.0 Google blocked http requests, since we want to provide, even if at a minimum level, some type of security our apache service is SSL enabled and the HTTPS website has a self signed certificate, no further upgrades were made (the server has no domain).

**Important**: During our implementation we added some confidential data, the secret key, the gmail user and pass for our server and the Nutritionix api, to run the server you will have to make a config file in that format.

**Note**: One advantage of using Django (besides the object DB) is the SQL injection protection system that comes built in with the framework and the fact we never directly interact with the user's password, not even admin user's can **directly** access a user password. 
