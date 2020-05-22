[FoodieShoot](https://github.com/eamorgado/FoodieShoot/blob/master/README.md)->[Documentation](https://github.com/eamorgado/FoodieShoot/tree/master/Documentation)->App

# The Application
All the files for the application can be found in [Applications/FoodDetection](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection). If you notice,
in [Applications](https://github.com/eamorgado/FoodieShoot/tree/master/Applications) we have two folders, FoodDetection and FoodClassification, when we started the project we were doing image clasification which was not what needed to be implemented, that folder (FoodeClassification) is deprecated and remains only as a reference.

To incorporate the Tensorflow model in our app we needed to implement the object detection android API, we used Tensorflow's example app as a reference for our implementation, using the Tensorflow object detection api we had to set our target SDK to be of at least level 28.

For the design the initial plans can be assed [here](https://github.com/eamorgado/FoodieShoot/issues/1) we would seperate our app into two major parts:
*   Anonymous usage
*   Authenticated usage

In the Anonynous usage we woul provide the login and sign up screens and provided an option to use the camera for a quick real time prediction, if the user wanted to analyse the foods it would need to authenticate.

We will now describe the overall [structure of our app](#Structure) and will later present the [instructions and demo of the app](#Instructions/Demo)

## Structure
The app will need to interact with the user's camera, will have to perform json requests and parse json requests. Though we will perform request to the server we also want to provide imediate caloric info, to do so we store a json file with the calories for each ofthe model's class which then is loaded to memory, here is the overall structure:
+ [The Auxiliar/](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/Auxiliar) folder is responsible for the auxilar classes for network requests and response parser as well as form validators
    + The [Auxiliar/Network/NetworkManager.java](https://github.com/eamorgado/FoodieShoot/blob/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/Auxiliar/Network/NetworkManager.java) class is responsible for all the network requests using a volley queue
    + The [Auxiliar/Network/RegisterParser/](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/Auxiliar/Network/RegisterParser) folder contains all the register request response parsers
    + The [Auxiliar/CalorieParser/](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/Auxiliar/CalorieParser) folder is responsible for parsing the saved food calories in the calorie json file
    + The [Auxiliar/FoodDetection/](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/Auxiliar/FoodDetection) folder is responsible for two tasks, displaying the caloric info on the sreen and parsing the food server posts
    + The [Auxiliar/FoodDetection/FoodPosts/](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/Auxiliar/FoodDetection/FoodPosts) folder has all the parsers for post lists and analysis
+  The [Configs/Configurations.java](https://github.com/eamorgado/FoodieShoot/blob/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/Configs/Configurations.java) file contains all the server major configurations, the necessary endpoints and the logout request
+  The [Authentication/](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/Authentication) folder has the login and sign up classes
+  The [Authenticated/](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/Authenticated) folder has all the activity classes that are only accessible once the user authenticates
    +   The [Authenticated/Posts/](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/Authenticated/Posts) folder contains the preview and save request for a server analysis as well as all the classes responsible for finding the user's last known location
+  The [SplashActivity.java](https://github.com/eamorgado/FoodieShoot/blob/master/Applications/FoodDetection/app/src/main/java/com/ciber/foodieshoot/applications/detection/SplashActivity.java) file is the main activity that at startup checks if the user saved the token (keep me logged in) and if so try to authenticate the user
+ All the other java files and foldersare responsble for the integration of Tensorflow object detection api and model into our android app

## Instructions/Demo
