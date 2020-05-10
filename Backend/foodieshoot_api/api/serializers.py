from rest_framework import serializers
from users.models import Profile
from django.contrib.auth.models import User
from foodieshoot.models import FoodieShoots


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        fields = ('id','username','email','first_name','last_name',)
        model = User


class RegistrationSerializer(serializers.ModelSerializer):
    #confirm password
    password2 = serializers.CharField(style={'input_type': 'password'}, write_only=True)

    class Meta:
        model = User
        fields = ['email', 'username', 'first_name', 'last_name', 'password', 'password2']
        extra_kwargs = {
            'password': {'write_only': True}
        }
    
    #Override save method
    def save(self):
        user = User(
            email=self.validated_data['email'],
            username=self.validated_data['username'],
            first_name=self.validated_data['first_name'],
            last_name=self.validated_data['last_name']
        )
        password = self.validated_data['password']
        password2 = self.validated_data['password2']

        if password != password2:
            raise serializers.ValidationError({'password': 'Passwords must match.'})
        
        user.set_password(password)
        user.save()
        return user
