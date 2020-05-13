from django.urls import include,path
from . import views

#REST API
from rest_framework.authtoken.views import obtain_auth_token #drf request

urlpatterns = [
    #Request drf token
    path('account/api-token-auth',obtain_auth_token,name='api_token_auth'),

    #Register user
    path('account/register',views.resgistration_view, name='rest-v1-register'),

    #Login user
    path('account/login',views.RestLogin.as_view(),name='rest-v1-login'),

    #Logout
    path('account/logout',views.RestLogout.as_view(),name='rest-v1-logout'),

    #DeleteAccount
    path('account/delete',views.RestDeleteUser.as_view(),name='rest-v1-deleteaccount'),


    #Foods
    path('foods/analyse',views.GetFoodData.as_view(),name='rest-v1-analysefood'),

    #List all posts for user
    path('foods/posts/list',views.ListUserPosts.as_view(),name='rest-v1-getuserposts'),

    #Save post
    path('foods/posts/save',views.SavePost.as_view(),name='rest-v1-savepost'),

    path('foods/posts/delete',views.DeletePost.as_view(),name='rest-v2-deletepost')

]