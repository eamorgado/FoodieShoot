from django.urls import path
from django.conf.urls.static import static
from django.conf import settings
from . import views

urlpatterns = [
    path('',views.home, name='fs-home'),
    path('home/', views.home, name='fs-home'),
    path('about/',views.about,name='fs-about'),
    path('download/',views.download,name='fs-download'),
    path('download/apk/',views.download_apk,name='fs-download-apk'),
    path('download/dataset/',views.download_dataset,name='fs-download-dataset'),
]
#urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)


