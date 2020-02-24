from django.shortcuts import render, render_to_response
from django.template import RequestContext



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
def handler404(request, exception, template_name="404.html"):
    response = render_to_response(template_name)
    response.status_code = 404
    return response