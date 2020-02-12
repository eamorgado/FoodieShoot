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