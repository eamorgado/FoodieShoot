from django.urls import path
from django.contrib.auth import views as auth_views
from . import views

urlpatterns = [
    path('singup/',views.singup,name='fs-singup'),
    path('singin/',auth_views.LoginView.as_view(template_name='users/singin.html'),name='fs-singin'),
    path('singout/',auth_views.LogoutView.as_view(template_name='users/singout.html'),name='fs-singout'),
    path('profile/',views.profile,name='fs-profile'),
]