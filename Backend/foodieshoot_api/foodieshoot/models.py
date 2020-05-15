from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User

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


def upload_location_dataset(instance,filename):
    name = instance.dataset_name + '_' + instance.dataset_num_classes
    return 'datasets/{}.tar.gz'.format(name)

class ImageDataset(models.Model):
    dataset_version = models.AutoField(primary_key=True)
    dataset_num_classes = models.CharField(max_length=4)
    dataset_name = models.CharField(max_length=255)
    dataset_file = models.FileField(upload_to=upload_location_dataset,default='FoodieShoot_ImageBank.tar.gz')
    dataset_date = models.DateTimeField(default=timezone.now)
    def __str__(self):
        return '{} V{} classes({})'.format(self.dataset_name,self.dataset_version,self.dataset_num_classes)
    class Meta:
        verbose_name_plural = 'Dataset'