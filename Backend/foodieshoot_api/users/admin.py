from django.contrib import admin
from .models import Profile


#define admin view
class ProgileG(admin.ModelAdmin):
    list_display = ('user',)
    list_filter = ('user',)

admin.site.register(Profile,ProgileG)