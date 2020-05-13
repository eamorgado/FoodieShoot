from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User
from api.fields import JSONField

class FoodPosts(models.Model):
    author = models.ForeignKey(User,on_delete=models.CASCADE)
    title = models.CharField(max_length=128)
    contents = JSONField(null=True, blank=True)
    location = models.CharField(max_length=512,blank=True)
    date_shoot = models.DateTimeField(default=timezone.now)
    

    def __str__(self):
        return '{} - {} -- {}'.format(self.author,self.title,self.date_shoot)
    
    class Meta:
        verbose_name_plural = 'FoodieShoots User Posts'