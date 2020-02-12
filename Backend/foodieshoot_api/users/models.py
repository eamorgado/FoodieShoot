from django.db import models
from django.contrib.auth.models import User
from PIL import Image

def upload_location(instance, filename):
    filebase, extension = filename.split('.')
    return 'profile_pics/{}.{}'.format(instance.user.username,extension)

class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    image = models.ImageField(default='default.png',upload_to=upload_location)

    def __str__(self):
        return f'{self.user.username} Profile'


    #resize picture
    def save(self):
        super().save()
        #resize saved image
        profile = Image.open(self.image.path)

        if profile.height > 300 or profile.width > 300:
            output_size = (300,300)
            profile.thumbnail(output_size)
            profile.save(self.image.path)

