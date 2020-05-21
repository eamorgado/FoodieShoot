[FoodieShoot](https://github.com/eamorgado/FoodieShoot/blob/master/README.md)->[Documentation](https://github.com/eamorgado/FoodieShoot/tree/master/Documentation)->App

# The Application
All the files for the application can be found in [Applications/FoodDetection](https://github.com/eamorgado/FoodieShoot/tree/master/Applications/FoodDetection). If you notice,
in [Applications](https://github.com/eamorgado/FoodieShoot/tree/master/Applications) we have two folders, FoodDetection and FoodClassification, when we started the project we were doing image clasification which was not what needed to be implemented, that folder (FoodeClassification) is deprecated and remains only as a reference.

To incorporate the Tensorflow model in our app we needed to implement the object detection android API, we used Tensorflow's example app as a reference for our implementation, using the Tensorflow object detection api we had to set our target SDK to be of at least level 28.

For the design the initial plans can be assed [here](https://github.com/eamorgado/FoodieShoot/issues/1) we would seperate our app into two major parts:
*   Anonymous usage
*   Authenticated usage

In the Anonynous usage we woul provide the login and sign up screens and provided an option to use the camera for a quick real time prediction, if the user wanted to analyse the foods it would need to authenticate.
