from django.contrib import admin
from .models import FoodieShoots



class FSGroup(admin.ModelAdmin):
    list_display = ('author', 'date_shoot','title')
    list_filter = ('author',)

admin.site.register(FoodieShoots,FSGroup)
