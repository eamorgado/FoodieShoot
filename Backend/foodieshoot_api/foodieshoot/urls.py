from django.urls import path
from . import views

urlpatterns = [
    path('',views.home, name='fs-home'),
    path('home/', views.home, name='fs-home'),
    path('about/',views.about,name='fs-about'),
    path('download/',views.download,name='fs-download'),
]

#handler404 = views.handler404

