[FoodieShoot](https://github.com/eamorgado/FoodieShoot/blob/master/README.md)->[Documentation](https://github.com/eamorgado/FoodieShoot/tree/master/Documentation)->Request API

# Request API
After implementing the server we add to implement android support, that support is through a REST API that allows a user to:
+   [login](#Login)
+   [sign up](#Sign-up)
+   [logout](#Logout)
+   [get user profile pic](#Profile-pic)
+   [delete user account](#Delete-user-account)
+   [analyse detected foods](#Analyse-food-data)
+   [save food analisis in a post](#Save-a-post)
+   [list all user posts](#List-all-user-posts)
+   [delete a sepecific post](#Post-delete)

The REST API works though token authentication, it will generate a token for user logging or registering and will require that session token for all other requests, the token and an expiration time and will be removed once a user loggs out.

**Important**: The base path for all rest requests is (host)**/api/v1/**

## Account requests
This requests are related in some way for account functions, each of this requests retunrs error responses with json data to describe the type of error, in the android app we parse this errors to inform the user of the possible error cause.  
Some details can also be found on [this](https://github.com/eamorgado/FoodieShoot/issues/7) issue.  

### Sign up
This request sends the user sign up info and if successful returns the user token    

**POST** account/register
```json
{
  "email": "test@gmail.com",
  "username": "test",
  "first_name": "Test",
  "last_name": "Account",
  "password": "test@123",
  "password2": "test@123
}
```
**Response**:
```json
{
    "status": "success",
    "email": "test@gmail.com",
    "username": "test",
    "first_name": "Test",
    "last_name": "Account",
    "token": "0192dd6d6290e1dfc26ebba058271a591b0658e7"
}
```

### Login
This request sends user credentials to the server (email and password) and the server return the auth token and user data, or it can also send the user token instead to login, this feature is used when a user decides to save the login, the android app will store the token and validate the user at the start. 

*   Normal request  
    **POST** account/login
    ```json
    {
      "email": "test@gmail.com",
      "password": "test@123"
    }
    ```
    **Response**:
    ```json
    {
        "status": "success",
        "email": "test2@gmail.com",
        "username": "test2",
        "first_name": "",
        "last_name": "Account",
        "token": "0192dd6d6290e1dfc26ebba058271a591b0658e7"
    }
    ```
*   Token request  
    **POST** account/login  
    Header: **Session-Token** = "Token 0192dd6d6290e1dfc26ebba058271a591b0658e7"  
    **Response**:
    ```json
    {
        "status": "success",
        "email": "test2@gmail.com",
        "username": "test2",
        "first_name": "",
        "last_name": "Account",
        "token": "0192dd6d6290e1dfc26ebba058271a591b0658e7"
    }
    ```

### Profile pic
This request returns the png file for the user  
**GET** account/profile  
Header: **Session-Token** = "Token 0192dd6d6290e1dfc26ebba058271a591b0658e7"    
**Response**: png file

### Logout
This request loggs out the user and deletes the token, this request also supports post and get.  
**POST** account/logout
```json
{
  "token": "0192dd6d6290e1dfc26ebba058271a591b0658e7"
}
```
**GET** account/logout  
Header: **Session-Token** = "Token 0192dd6d6290e1dfc26ebba058271a591b0658e7"   
**Response**:
```json
{
  "status": "success"
}
```

### Delete user account
This request will completly remove all user data  
**POST** account/delete
```json
{
    "token": "0192dd6d6290e1dfc26ebba058271a591b0658e7"
}
```
**Response**:
```json
{
    "status": "success"
}
```


## Food requests
As you can follow on [this](https://github.com/eamorgado/FoodieShoot/issues/30) issue, we use the [Nutritionix's API](https://www.nutritionix.com/business/api) to analyse each of our foods, to save costs, we store the free app key on our server, a user performs a food analysis request and we precess it and performa the analysis request to the third party api and then return the processed response back to the user.  
In this requests a user can send a list of foods detected and receive the analysis, it can also send the response back to the server along with a title and location and the server will save that post. The user can also get the list of all his posts sorted by desc date.


### Analyse food data
In this request the user sends a list of detected foods and the server will return the total amount of calories as well as the description for each food, if the list of foods contains repeated foods, the server also returns the number of occurences.

**POST** foods/analyse
```json
{
	"token": "0192dd6d6290e1dfc26ebba058271a591b0658e7",
	"foods": ["Wine","Wine","Bread","Apple"]
}
```
**Response**:
```json
{
    "status": "success",
    "contents": {
        "total_calories": 199.15,
        "processed": [
            {
                "Name": "Wine",
                "Serving quantity": 1,
                "Serving unit": "glass",
                "Serving weight (grams)": 147,
                "Total calories": 122.01,
                "Total fat": 0,
                "Total saturated fat": 0.0,
                "Cholesterol": 0.0,
                "Sodium": 0.0,
                "Total carbs": 3.82,
                "Fiber": 0.0,
                "Sugar": 0.0,
                "Protein": 0.1,
                "Potassium": 0.0,
                "Occurrences": 2
            },
            {
                "Name": "Bread",
                "Serving quantity": 1,
                "Serving unit": "slice",
                "Serving weight (grams)": 29,
                "Total calories": 77.14,
                "Total fat": 0.97,
                "Total saturated fat": 0.2,
                "Cholesterol": 0,
                "Sodium": 142.1,
                "Total carbs": 14.33,
                "Fiber": 0.78,
                "Sugar": 1.64,
                "Protein": 2.57,
                "Potassium": 36.54,
                "Occurrences": 3
            }
        ]
    }
}
```

### Save a post
If the user decides to save the received food analysis, using this request, the server will save the contents of that post, the user can also provide a title and a location, the server will tag the post with the timestamp date

**POST** foods/posts/save
```json
{
	"token": "0192dd6d6290e1dfc26ebba058271a591b0658e7",
	"save": "True",
	"title": "Test",
	"location": "London",
	"contents": {
        "total_calories": 199.15,
        "processed": [
            {
                "Name": "Wine",
                "Serving quantity": 1,
                "Serving unit": "glass",
                "Serving weight (grams)": 147,
                "Total calories": 122.01,
                "Total fat": 0,
                "Total saturated fat": 0.0,
                "Cholesterol": 0.0,
                "Sodium": 0.0,
                "Total carbs": 3.82,
                "Fiber": 0.0,
                "Sugar": 0.0,
                "Protein": 0.1,
                "Potassium": 0.0,
                "Occurrences": 2
            },
            {
                "Name": "Bread",
                "Serving quantity": 1,
                "Serving unit": "slice",
                "Serving weight (grams)": 29,
                "Total calories": 77.14,
                "Total fat": 0.97,
                "Total saturated fat": 0.2,
                "Cholesterol": 0,
                "Sodium": 142.1,
                "Total carbs": 14.33,
                "Fiber": 0.78,
                "Sugar": 1.64,
                "Protein": 2.57,
                "Potassium": 36.54,
                "Occurrences": 3
            }
        ]
    }
}
```
**Response**:
```json
{
    "status": "success"
}
```

### List all user posts
In this API you can also get all the user posts sorted by most recent, the server returns the list of posts (empty if user hasn't made any posts), this request supports both post and get formats


**GET** foods/posts/list  
Header: **Session-Token** = "Token 0192dd6d6290e1dfc26ebba058271a591b0658e7"  

**POST** foods/posts/list
```json
{
  "token": "0192dd6d6290e1dfc26ebba058271a591b0658e7"
}
```
**Response**:
```json
{
    "size": 1,
    "posts": [
        {
            "title": "Test",
            "location": "London",
            "date": "2020-05-21T22:08:06.418312Z",
            "contents": {
                "total_calories": 199.15,
                "processed": [
                    {
                        "Name": "Wine",
                        "Serving quantity": 1,
                        "Serving unit": "glass",
                        "Serving weight (grams)": 147,
                        "Total calories": 122.01,
                        "Total fat": 0,
                        "Total saturated fat": 0.0,
                        "Cholesterol": 0.0,
                        "Sodium": 0.0,
                        "Total carbs": 3.82,
                        "Fiber": 0.0,
                        "Sugar": 0.0,
                        "Protein": 0.1,
                        "Potassium": 0.0,
                        "Occurrences": 2
                    },
                    {
                        "Name": "Bread",
                        "Serving quantity": 1,
                        "Serving unit": "slice",
                        "Serving weight (grams)": 29,
                        "Total calories": 77.14,
                        "Total fat": 0.97,
                        "Total saturated fat": 0.2,
                        "Cholesterol": 0,
                        "Sodium": 142.1,
                        "Total carbs": 14.33,
                        "Fiber": 0.78,
                        "Sugar": 1.64,
                        "Protein": 2.57,
                        "Potassium": 36.54,
                        "Occurrences": 3
                    }
                ]
            }
        }
    ]
}
```

### Post delete
The user can also delete a posts, to do so, since the server tags the post with the date to the ms, we can get the unique post among all the user posts and then delete it

**POST** foods/posts/delete
```json
{
	"token": "0192dd6d6290e1dfc26ebba058271a591b0658e7",
	"delete": "2020-05-21T22:08:06.418312Z"
}
```
**Response**
```json
{
    "status": "success"
}
```





