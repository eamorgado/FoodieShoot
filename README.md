# FoodieShoot Project
This project was developed as the final project for the subject of mobile device programming (CC3035). The objective of this project (project 3) is to implement an application to identity different foods in
a place/plate. The application is be able to perform real-time analysis of a video stream and track different foods on a plate as well as calculating its calories, in this application you can also save and visualize all your detected foods if they are saved in a post.

This application is supported by a Django backend and frontend server/site where you can also download the app https://178.79.132.23/ .

To recognize the foods, it uses our custom built dataset with 90 classes of foods and uses a pretrained SSD model that trains on our data using the TensorFlow library (the notebook used to train the model can be seen in the Model folder). Since we want to integrate our model with the app, Tensorflow 2.0 API now supports a new model format (tflite) that can reduce the consuption of resources during inference,
as such, after training the model we convert it to a tflite format.

In case you want to download and test the app you can [download it in our website](https://178.79.132.23/download/) or in [this](https://github.com/eamorgado/FoodieShoot/tree/master/Apks) repositorty or even in our [Aptoide store](https://foodieshoot.en.aptoide.com/app?store_name=ciber-foodieshoot). Since the app is not deployed on PlayStore you will need to enable unknown sources and allow the installation on your device, you can find out how to use the application [here](https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/App.md), to use the full features of the app you will need to authenticate, consenting to our [terms and conditions](https://178.79.132.23/terms/). If you want a better look into the development process you can check out our project [here](https://github.com/eamorgado/FoodieShoot/projects/1).


If you are using this app as  reference, you can check our dependency gradle file [here](https://github.com/eamorgado/FoodieShoot/blob/master/Applications/FoodDetection/app/build.gradle) and the app graddle [here](https://github.com/eamorgado/FoodieShoot/blob/master/Applications/FoodDetection/build.gradle).

# Documentation
1.  [The Server](https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Server.md)
2.  [Request API](https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Request_API.md)
3.  [App](https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/App.md)
