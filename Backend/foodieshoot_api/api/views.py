import os,json, requests
from django.conf import settings
from django.shortcuts import render
from users.models import Profile
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login,logout
from api import serializers
from api.serializers import RegistrationSerializer, UserLoginSerializer, UserLoginSerializerToken

from api.models import FoodPosts

#REST packages
from rest_framework import generics, status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view


#Registration view
@api_view(['POST',])
def resgistration_view(request):
    args = ['email','username','first_name','last_name','password','password2']
    if request.method == 'POST':
        try:
            flag = False
            data = {'status': 'fail'}
            for param in args:
                if param not in request.data or (not request.data[param] or request.data[param][0] == ""):
                    flag = True
                    if 'error' not in data:
                        data['error'] = {
                            'form_data': {}
                        }
                    data['error']['form_data'][param] = 'Param missing or empty'
            if flag:
                return Response(data)
            serializer = RegistrationSerializer(data=request.data)
            if serializer.is_valid():
                user = serializer.save()
                token,_ = Token.objects.get_or_create(user=user)
                data['status'] = "success"
                data['email'] = user.email
                data['username'] = user.username
                data['first_name'] = user.first_name
                data['last_name'] = user.last_name
                data['token'] = token.key
            else:
                data['error'] = serializer.errors            
            return Response(data,status=status.HTTP_200_OK)
        except Exception as e:
            return Response(status=status.HTTP_200_OK)

@api_view(['POST',])
def login_view(request):
    if request.method == 'POST':
        try:
            if 'token' in request.data:
                serializer = UserLoginSerializerToken(data=request.data)
            else:
                serializer = UserLoginSerializer(data=request.data)
            if serializer.is_valid():
                data = serializer.validated_data
                username = data["username"]
                password = data["password"]
                user = authenticate(username=username, password=password)
                if user:
                    login(request,user)
                del data["password"]
                data["status"] = 'success'
            else:
                data = {}
                data["status"] = 'fail'
                data["error"] = serializer.errors
            return Response(data)
        except Exception as e:
            return Response(status=status.HTTP_400_BAD_REQUEST)


class RestLogout(APIView):
    permission_classes = (IsAuthenticated,)
    def get(self,request):
        try:
            request.user.auth_token.delete()
        except (AttributeError, ObjectDoesNotExist):
            pass
        logout(request)
        data = {"status": "success"}

        return Response(data)


def getKeys():
    with open(os.path.join(settings.BASE_DIR,'nutrition_api.json')) as f:
        confs = json.load(f)
        id,key,endpoint = confs['NUTRITION_APP_ID'],confs['NUTRITION_APP_KEY'],confs['NUTRITION_APP_ENDPOINT']
        return id,key,endpoint

def requestNutrition(food):
    id,key,endpoint = getKeys()
    headers = {
        'x-app-id': id,
        'x-app-key': key,
        'Content-Type': 'application/json'
    }
    body = {
        'query': str(food)
    }

    response = requests.post(endpoint,headers=headers,data=json.dumps(body))
    return response.text.encode('utf8')

def getNutritionDesiredKeys():
    keys = {
        "serving_qty": "Serving quantity",
        "serving_unit": "Serving unit",
        "serving_weight_grams": "Serving weight (grams)",
        "nf_calories": "Total calories",
        "nf_total_fat": "Total fat",
        "nf_saturated_fat": "Total saturated fat",
        "nf_cholesterol": "Cholestrol",
        "nf_sodium": "Sodium",
        "nf_total_carbohydrate": 'Total carbs',
        "nf_dietary_fiber": "Fiber",
        "nf_sugars": "Sugar",
        "nf_protein": "Protein",
        "nf_potassium": "Potassium"
    }
    return keys

def processFood(food):
    response = json.loads(requestNutrition(food))
    if 'message' in response or 'foods' not in response:
        return {'error': 'error on sending request'}
    data = response['foods'][0]
    keys = getNutritionDesiredKeys()
    content = {'name': food}
    for key,v in keys.items():
        if str(key) in data:
            content[v] = data[key]
    return content    

def savePost(user,title,location,data):
    if not location:
        location = "Location unknown"
    if not title:
        title = "Title unknown"
    post = FoodPosts(
        author=user,
        title=title,
        contents=json.dumps(data),
        location=location
    )
    post.save()

#Nutrients
class GetFoodData(APIView):  
    def post(self,request):
        data = {'status': 'fail'}
        try:
            serializer = UserLoginSerializerToken(data=request.data)
        except Exception:
            return Response({"status":"fail","error": "Token missing or incorrect"})
        
        if serializer.is_valid():
            user_info = serializer.validated_data
            username = user_info["username"]
            user = User.objects.get(username=username)
        else:
            data["error"] = serializer.errors
            return Response(data)

        if 'foods' in request.data:
            foods = request.data['foods']
            if not isinstance(foods,list):
                foods = [foods]
            
            response = []
            total_cals = 0
            for food in foods:
                processed = processFood(food)
                if 'error' not in processed:
                    cals = processed['Total calories']
                    total_cals += cals
                    response.append(processed)
                else:
                    data["error"] = "Error processing food"
                    return Response("error")

            if len(response) >= 1:
                data['contents'] = {
                    'total_calories': total_cals,
                    'processed':  response
                }
                data["status"] = 'success'
                '''
                location = request.data['location'] if 'location' in request.data else None
                title = request.data['title'] if 'title' in request.data else None
                entry = {
                    'total_calories': total_cals,
                    'processed': response
                }
                savePost(user,title,location,entry)
                '''
        else:
            data["error"] = "Key foods not found"
        return Response(data)
        
class SavePost(APIView):
    def __savePost(self,user,title,location,entry):
        if not location:
            location = "Location unknown"
        if not title:
            title = "Title unknown"
        if not entry:
            entry = {"error": "No data"}
        post = FoodPosts(
            author=user,
            title=title,
            contents=json.dumps(entry),
            location=location
        )
        post.save()

    def post(self,request):
        data = {'status': 'fail'}
        try:
            serializer = UserLoginSerializerToken(data=request.data)
        except Exception:
            return Response({"status":"fail","error": "Token missing or incorrect"})
        
        if serializer.is_valid():
            user_info = serializer.validated_data
            username = user_info["username"]
            user = User.objects.get(username=username)
        else:
            data["error"] = serializer.errors
            return Response(data)

        if 'save' in request.data and bool(request.data['save']):
            location = request.data['location'] if 'location' in request.data else None
            title = request.data['title'] if 'title' in request.data else None
            contents = request.data['contents'] if 'contents' in request.data else None
            self.__savePost(user,title,location,contents)
            data['status'] = "success"
        else:
            data["error"] = "No save option given"
        return Response(data)
     

class ListUserPosts(APIView):
    #permission_classes = (IsAuthenticated,)
    def __getPosts(self,user):
        all_posts = FoodPosts.objects.filter(author=user)        
        response = {
            'size': len(all_posts)
        }
        posts = []
        for post in all_posts:
            contents = {
                'title': post.title,
                'location': post.location,
                'date': post.date_shoot,
                'contents': post.contents
            }
            posts.append(contents)
        response['posts'] = posts
        return response

    def get(self,request):
        try:
            token = request.META.get('HTTP_AUTHORIZATION').split()[1]
            user = Token.objects.get(key=token).user
        except Exception:
            return Response({"status":"fail","error": "Token missing or incorrect"})
        
        response = self.__getPosts(user)
        return Response(response)
    
    def post(self,request):
        try:
            token = request.data.get('token')
            user = Token.objects.get(key=token).user
        except Exception:
            return Response({"status":"fail","error": "Token missing or incorrect"})

        response = self.__getPosts(user)
        return Response(response)


class DeletePost(APIView):
    def __deletePost(self,user,post):
        try:
            p = FoodPosts.objects.get(author=user,date_shoot=post).delete()
            return True
        except Exception:
            return False

    def post(self,request):
        try:
            token = request.data.get('token')
            user = Token.objects.get(key=token).user
        except Exception:
            return Response({"status":"fail","error": "Token missing or incorrect"})
        
        if 'delete' not in request.data:
            return Response({"status":"fail","error": "No posts selected"})
        posts_to_delete = request.data['delete']
        if not isinstance(posts_to_delete,list):
            posts_to_delete = [posts_to_delete]
        
        for post in posts_to_delete:
            ok = self.__deletePost(user,post)
            if not ok:
                return Response({"status":"fail","error": "Failed to delete a post"})
        return Response({"status":"success"})
        