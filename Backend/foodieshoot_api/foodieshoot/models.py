from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User

class FoodieShoots(models.Model):
    author = models.ForeignKey(User,on_delete=models.CASCADE)
    date_shoot = models.DateTimeField(default=timezone.now)
    title = models.CharField(max_length=100)

    def __str__(self):
        return '{}'.format(self.author)
    
    class Meta:
        verbose_name_plural = 'FoodieShoots Pics'

def upload_location(instance, filename):
    filebase, extension = filename.split('.')
    name = instance.apk_name +'.'
    name += instance.apk_version 
    return 'apks/{}.{}'.format(name,extension)

class Apks(models.Model):
    apk_version = models.CharField(max_length=42,primary_key=True)
    apk_name = models.CharField(max_length=255)
    apk_file = models.FileField(upload_to=upload_location,default='foodiesoot.apk')
    apk_date = models.DateTimeField(default=timezone.now)
    def __str__(self):
        return '{} ---> {}'.format(self.apk_name,self.apk_version)
    class Meta:
        verbose_name_plural = 'Apks'