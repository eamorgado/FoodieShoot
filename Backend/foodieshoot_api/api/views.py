from django.shortcuts import render
from users.models import Profile
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login
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
        
        return Response(data)

@api_view(['POST',])
def login_view(request):
    if request.method == 'POST':
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

class UserLogin(APIView):
    serializer_class = UserLoginSerializer
    def post(self,request):
        return Response(UserLoginSerializer(data=request.data).data)

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