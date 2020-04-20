from django.shortcuts import render
from users.models import Profile
from django.contrib.auth.models import User
from foodieshoot.models import FoodieShoots
from . import serializers


#REST packages
from rest_framework import generics
from rest_framework.views import APIView
from rest_framework.response import Response


class ListUser(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = serializers.UserSerializer