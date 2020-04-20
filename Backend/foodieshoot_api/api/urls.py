from django.urls import include,path
from . import views

#REST API
from rest_framework.authtoken.views import obtain_auth_token #drf request

urlpatterns = [
    path('',views.ListUser.as_view()),
    path('api-token-auth/',obtain_auth_token,name='api_token_auth'), #Request drf token
]