from django.contrib import admin
from .models import FoodieShoots,Apks



class FSGroup(admin.ModelAdmin):
    list_display = ('author', 'date_shoot','title')
    list_filter = ('author',)


class ApkGroup(admin.ModelAdmin):
    list_display = ('apk_name','apk_version','apk_date')
    list_filter = ('apk_version','apk_name')

admin.site.register(FoodieShoots,FSGroup)
admin.site.register(Apks,ApkGroup)