from rest_framework import serializers
import django.contrib.auth.password_validation as validators
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
    
    def validate(self, data):
        """
        Check that the start is before the stop.
        """
        email = data['email']
        username = data['username']
        password = data['password']
        password2 = data['password2']
        errors = {}
        flag = False

        if User.objects.filter(email=email):
            #User with email exists
            flag = True
            errors['email'] = 'Email already exists.'

        if User.objects.filter(username=username):
            flag = True
            errors['username'] = 'Username already exists'

        if password != password2:
            flag = True
            errors['password'] = ['Passwords must match.']
        
        user = User(
            email=email,
            username=username,
            password=password
        )
        try:
            validators.validate_password(password=password, user=user)
        except Exception as e:
            flag = True
            l = list(e.messages)
            if 'password' in errors:
                errors['password'].extend(l)
            else:
                errors['password'] = l
        

        if flag:
            raise serializers.ValidationError(errors)
        return data

    #Override save method
    def save(self):
        user = User(
            email=self.validated_data['email'],
            username=self.validated_data['username'],
            first_name=self.validated_data['first_name'],
            last_name=self.validated_data['last_name'],
            password = self.validated_data['password']
        )
        #user.set_password(password)
        user.save()
        return user
