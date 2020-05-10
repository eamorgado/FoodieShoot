from django.shortcuts import render
from users.models import Profile
from django.contrib.auth.models import User
from foodieshoot.models import FoodieShoots
from api import serializers
from api.serializers import RegistrationSerializer


#REST packages
from rest_framework import generics, status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from rest_framework.decorators import api_view


#Registration view
@api_view(['POST',])
def resgistration_view(request):
    if request.method == 'POST':
        serializer = RegistrationSerializer(data=request.data)
        data = {}

        if serializer.is_valid():
            user = serializer.save()
            data['status'] = "Success"
            data['email'] = user.email
            data['username'] = user.username
            data['first_name'] = user.first_name
            data['last_name'] = user.last_name
        else:
            data['error'] = serializer.errors
        
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