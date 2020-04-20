from django.contrib import admin
from .models import FoodieShoots,Apks,ImageDataset



class FSGroup(admin.ModelAdmin):
    list_display = ('author', 'date_shoot','title')
    list_filter = ('author',)


class ApkGroup(admin.ModelAdmin):
    list_display = ('apk_name','apk_version','apk_date')
    list_filter = ('apk_version','apk_name')

class ImageDatasetG(admin.ModelAdmin):
    list_display = ('dataset_name','dataset_version','dataset_num_classes','dataset_date')
    list_filter = ('dataset_name','dataset_version','dataset_num_classes','dataset_date')

admin.site.register(FoodieShoots,FSGroup)
admin.site.register(Apks,ApkGroup)
admin.site.register(ImageDataset,ImageDatasetG)