import os
from django.conf import settings
from django.views.static import serve
from django.shortcuts import render, render_to_response
from django.http import HttpResponse
from django.template import RequestContext
from .models import Apks, ImageDataset
from wsgiref.util import FileWrapper
import struct


def home(request):
    context = {'home': 'true',}
    return render(request,'foodieshoot/home.html',context)

def download(request):
    context = {'title': 'Download','download': 'true'}
    return render(request,'foodieshoot/download.html',context)

def about(request):
    context = {'title': 'About','about': 'true',}
    return render(request,'foodieshoot/about.html',context)


#Custom 404 handler
def view_404(request,exception=None):
    return render(request,"foodieshoot/404.html",status=404)

def view_500(request,exception=None):
    return render(request,"foodieshoot/500.html",status=500)


def download_apk(request,name=None,version=None):
    if not name:    
        name = 'FoodieShoot'
    all_files = Apks.objects.filter(apk_name=name)
    if not all_files:
        return HttpResponse('<h1>File does not exist<h1>')
    if version:
        all_files = Apks.objects.filter(apk_name=name,apk_version=version)
        if not all_files:
            return HttpResponse('<h1>File does not exist<h1>')
        return serve(request,all_files.apk_file.url,document_root=settings.BASE_DIR)
    else:
        file = None
        version = 0.0
        for ap in all_files:
            try:
                a_version = float(ap.apk_version)
                if a_version >= version:
                    file = ap.apk_file.url
                    version = a_version
            except ValueError:
                return HttpResponse('<h1>500<h1>')
        filepath = settings.BASE_DIR + file
        fsock = open(filepath,"rb")
        response = HttpResponse(fsock,content_type='application/vnd.android.package-archive')
        response['Content-Disposition'] = 'attachment; filename=FoodieShoot_' + str(version) + '.apk'
        return response

def download_dataset(request):
    name = 'FoodieShoot_ImageBank'
    all_files = ImageDataset.objects.filter(dataset_name=name)
    if not all_files:
        return HttpResponse('<h1>File does not exist<h1>')
    else:
        file = None
        version = 0.0
        for ap in all_files:
            try:
                a_version = float(ap.dataset_version)
                if a_version >= version:
                    file = ap.dataset_file.url
                    version = a_version
            except ValueError:
                return HttpResponse('<h1>500<h1>')
        filepath = settings.BASE_DIR + file
        fsock = open(filepath,"rb")
        response = HttpResponse(fsock,content_type='application/gzip')
        response['Content-Disposition'] = 'attachment; filename=FoodieShoot_ImageBank.tar.gz'
        return response
    
def terms_and_conditions(request):
    context = {'title': 'Terms and Conditions'}
    return render(request,'foodieshoot/terms.html',context)

    
