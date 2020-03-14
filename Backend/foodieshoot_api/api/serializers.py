from rest_framework import serializers
from users.models import Profile
from django.contrib.auth.models import User
from foodieshoot.models import FoodieShoots

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        fields = ('id','username','email','first_name','last_name',)
        model = User