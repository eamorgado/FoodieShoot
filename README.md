# FoodieShoot Project
This project was developed as the final project for the subject of mobile device programming (CC3035). The objective of this project (project 3) is to implement an application to identity different foods in
a place/plate. The application is be able to perform real-time analysis of a video stream and track different foods on a plate as well as calculating its calories, in this application you can also save and visualize all your detected foods if they are saved in a post.

This application is supported by a Django backend and frontend server/site where you can also download the app https://178.79.132.23/ .

To recognize the foods, it uses our custom built dataset with 90 classes of foods and uses a pretrained SSD model that trains on our data using the TensorFlow library (the notebook used to train the model can be seen in the Model folder). Since we want to integrate our model with the app, Tensorflow 2.0 API now supports a new model format (tflite) that can reduce the consuption of resources during inference,
as such, after training the model we convert it to a tflite format.


# Documentation
1.  [The Server](https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Server.md)
2.  [Request API](https://github.com/eamorgado/FoodieShoot/blob/master/Documentation/Request_API.md)
3.  App
