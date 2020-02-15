from django.urls import path
from django.contrib.auth import views as auth_views
from . import views

urlpatterns = [
    path('singup/',views.singup,name='fs-singup'),
    path('singin/',auth_views.LoginView.as_view(template_name='users/singin.html'),name='fs-singin'),
    path('singout/',auth_views.LogoutView.as_view(template_name='users/singout.html'),name='fs-singout'),
    path('profile/',views.profile,name='fs-profile'),
    path('password-reset/',auth_views.PasswordResetView.as_view(template_name='users/password_reset.html'),name='password_reset'),
    path('password-reset/done/',auth_views.PasswordResetDoneView.as_view(template_name='users/password_reset_done.html'),name='password_reset_done'),
    path('password-reset-confirm/<uidb64>/<token>/',auth_views.PasswordResetConfirmView.as_view(template_name='users/password_reset_confirm.html'),name='password_reset_confirm'),
    path('password-reset-complete/',auth_views.PasswordResetCompleteView.as_view(template_name='users/password_reset_complete.html'),name='password_reset_complete'),
]
