from django.contrib import admin
from .models import FoodPosts

class FPost(admin.ModelAdmin):
    list_display = ('author','title','location','contents','date_shoot')
    list_filter = ('author','location','date_shoot','title')

admin.site.register(FoodPosts,FPost)