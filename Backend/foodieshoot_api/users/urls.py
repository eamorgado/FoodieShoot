from django.urls import path
from django.contrib.auth import views as auth_views
from . import views

urlpatterns = [
    path('signup/',views.signup,name='fs-signup'),
    path('signin/',auth_views.LoginView.as_view(template_name='users/signin.html'),name='fs-signin'),
    path('signout/',auth_views.LogoutView.as_view(template_name='users/signout.html'),name='fs-signout'),
    path('profile/',views.profile,name='fs-profile'),
]