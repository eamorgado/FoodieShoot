import os,json, requests
from django.conf import settings
from django.shortcuts import render
from users.models import Profile
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login,logout
from foodieshoot.models import FoodieShoots
from api import serializers
from api.serializers import RegistrationSerializer, UserLoginSerializer, UserLoginSerializerToken


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

#Nutrients
@api_view(['POST',])
def get_nutrients_for_foods(request):
    if request.method == 'POST':
        if 'token' in request.data:
            serializer = UserLoginSerializerToken(data=request.data)
            data = {'status': 'fail'}
            if serializer.is_valid():
                _ = serializer.validated_data
                #Request nutrients
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
                    if len(response) >= 1:
                        data['total_calories'] = total_cals
                        data['processed'] = response
                        data["status"] = 'success'
            else:
                data["error"] = serializer.errors
            return Response(data)


#TEST ==> REMOVE AFTER
#List all users even if request user is not authenticated
class ListUser(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = serializers.UserSerializer

class ListSingleUser(APIView):
    permission_classes = (IsAuthenticated,)

    def get(self,request):
        content = {'message': 'Hello'}
        return Response(content)