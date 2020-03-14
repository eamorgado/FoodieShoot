from django.shortcuts import render
from rest_framework import generics
from users.models import Profile
from django.contrib.auth.models import User
from foodieshoot.models import FoodieShoots
from . import serializers

class ListUser(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = serializers.UserSerializer