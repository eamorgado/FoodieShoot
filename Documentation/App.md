[FoodieShoot](https://github.com/eamorgado/FoodieShoot/blob/master/README.md)->[Documentation](https://github.com/eamorgado/FoodieShoot/tree/master/Documentation)->App

# The Application
All the files for the application can be found in [Applications/FoodDetection](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection). If you notice,
in [Applications](https://github.com/eamorgado/FoodieShoot/tree/master/Applications) we have two folders, FoodDetection and FoodClassification, when we started the project we were doing image clasification which was not what needed to be implemented, that folder (FoodeClassification) is deprecated and remains only as a reference.

To incorporate the Tensorflow model in our app we needed to implement the object detection android API, we used Tensorflow's example app as a reference for our implementation, using the Tensorflow object detection api we had to set our target SDK to be of at least level 28.

For the design the initial plans can be assed [here](https://github.com/eamorgado/FoodieShoot/issues/1) we would seperate our app into two major parts:
*   Anonymous usage
*   Authenticated usage

In the Anonynous usage we woul provide the login and sign up screens and provided an option to use the camera for a quick real time prediction, if the user wanted to analyse the foods it would need to authenticate.

We will now describe the overall [structure of our app](#Structure) and will later present the [instructions and demo of the app](#Instructions-and-Demo)

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

## Instructions and Demo
As we have already said the app is comprised of two modes, the mode with no authentication where the user has limite access to the application functionalities and thus,can only detect the food but not analyse or save it. We will show the instructions for [first time use](#First-Use) and for [authenticated use](#Authenticated-Use)

**Important**: This demo will be in English as the phose used for the demo had an English system however, we have added support for four languages: English, German, Portuguese and Spanish, most of the app has translations for these languages except for the post response are the messages are given by the server. 

### First Use
If you are using the app for the first time you will see the splash screen 
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/splash_screen.jpg" width=20%>
</p>
At which point you are presented with the login screen and sign up screen(to get there scroll down on the text for the credentials in login sreen
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/login_screen.jpg" width=20%>
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/signup_scree.jpg" width=20%>
</p>

You can authenticate or if you **want to use the app with limited access** you can click on the "Continue with no account", if you do so you will see the camera and be presented with the request permissions
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/camera_permission_screen.jpg" width=20%>
</p>
Once you give the permission the camera will initiate the prediction for individual objects in an image, do consider that the model only works with 300x300 images as such the app will scale down your image if necessary so be pattient
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/camera_detection.jpg" width=20%>
</p>
If you scroll up on the up arrow you sould see more options an information, whenever the model preicts something we output all of its predictions and the total calories into a table to facilitate the visualization
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/camera_detection_table.jpg" width=20%>
</p>
You can then analyse the imageby clicking analyse image however, since you are not authenticated you cannot use this function
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/camera_analysis_no_account.jpg" width=20%>
</p>

### Authenticated Use
After you have logged in or signed up (you don't need to always do this if you check the Keep me logged in) you have access to the full app, in the main page you will have two navbars, a side one and a bottom one with only the home, camera and posts options, the main home page will have these same instructions only in a more compact way
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/home_screen.jpg" width=20%>
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/navbar_screen.jpg" width=20%>
</p>
If you scroll down you can always refresh the app. If you go to posts, since we haven't yet made any posts it is empty, even after refreshing
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/posts_no_posts_refresh_screen.jpg" width=20%>
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/posts_no_post_screen.jpg" width=20%>
</p>

You can start the post by opening the camera and detecting objects, after it has detected objects (not empty) and you have clicked the analyse image, you will reveive a notification informing you that we have started the analysis and once it is done you will be moved to the preview page, since we also want to store the user's location you will need to grant the app access to location, after all permissions are set you can visualize the post priview and can either discard the post (ignores it and goes back to the camera) or save it to the server
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/camera_analysis_location_request.jpg" width=20%>
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/camera_post_preview.jpg" width=20%>
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/camera_post_preview_2.jpg" width=20%>
</p>

After saving the post you will be redirected to the post page and will see all your posts (after refreshing) ordered by most recent, if the app was able to find your location you will also see it
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/posts_page_location.jpg" width=20%>
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/posts_see_1.jpg" width=20%>
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/posts_see_2.jpg" width=20%>
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/posts_see_3.jpg" width=20%>
</p>
You can also delete a posts
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/posts_delete_post.jpg" width=20%>
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/posts_deleted_post.jpg" width=20%>
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/posts_navbar.jpg" width=20%>
</p>

You can also visit the about page
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/about.jpg" width=20%>
</p>

In the app you can also logout by opening the nav bar and clicking on logout, after that all your data saved in the device (the auth token) will be deleted and you will need to authentiate to access the full version of the app
<p align="center">
    <img src="https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Images/logout.jpg" width=20%>
</p>
