from rest_framework import serializers
from rest_framework.authtoken.models import Token
import django.contrib.auth.password_validation as validators
from users.models import Profile
from django.contrib.auth.models import User
from django.db.models import Q
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
            last_name=self.validated_data['last_name'],
            password = self.validated_data['password']
        )
        #user.set_password(password)
        user.save()
        return user


class UserLoginSerializer(serializers.ModelSerializer):
    email = serializers.EmailField(label='Email',required=False,allow_blank=True)
    class Meta:
        model = User
        fields = ['email','password',]
        extra_kwargs = {
            'password': {'write_only': True}
        }
    def validate(self,data):
        email = data.get("email",None)
        password = data["password"]
        if not email:
            raise serializers.ValidationError({"user":"Email missing"})
        user = User.objects.filter(email=email)
        if user.exists() and user.count() == 1:
            user = user.first()
            if user.password != password:
                raise serializers.ValidationError({"credentials":"Invalid credentials"})
            token,_ = Token.objects.get_or_create(user=user)
            data['token'] = token.key
            data["username"] = user.username
            data["email"] = user.email
            data["first_name"] = user.first_name
            data["last_name"] = user.last_name
        else:
            raise serializers.ValidationError({"user":"User does not exist"})
        return data

class UserLoginSerializerToken(serializers.ModelSerializer):
    token = serializers.CharField(required=True,allow_blank=False)
    class Meta:
        model = User
        fields = ['token',]
    def validate(self,data):
        token = data["token"]
        if not token or token == "":
            raise serializers.ValidationError({"token": "Missing token"})
        user = Token.objects.get(key=token).user
        if not user:
            raise serializers.ValidationError({"token": "No user for token"})
        data["username"] = user.username
        data["password"] = user.password
        data["email"] = user.email
        data["first_name"] = user.first_name
        data["last_name"] = user.last_name
        return data
        
        