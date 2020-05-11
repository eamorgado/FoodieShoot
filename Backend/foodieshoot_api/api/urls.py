from django.urls import include,path
from . import views

#REST API
from rest_framework.authtoken.views import obtain_auth_token #drf request

urlpatterns = [
    path('',views.ListUser.as_view()),
    path('list-user/',views.ListSingleUser.as_view(),name='rest-list-user'),

    #Request drf token
    path('account/api-token-auth',obtain_auth_token,name='api_token_auth'),

    #Register user
    path('account/register',views.resgistration_view, name='rest-v1-register'),

    #Login user
    path('account/login',views.login_view,name='rest-v1-login'),

    #Logout
    path('account/logout',views.RestLogout.as_view(),name='rest-v1-logout')
]