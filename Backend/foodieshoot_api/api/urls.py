from django.urls import include,path
from . import views

urlpatterns = [
    path('',views.ListUser.as_view()),
    path('rest-auth/',include('rest_auth.urls')),
]