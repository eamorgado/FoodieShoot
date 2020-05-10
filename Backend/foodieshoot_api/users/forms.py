import re
from django import forms
from django.utils.safestring import mark_safe
from django.contrib.auth.models import User
from django.contrib.auth.forms import UserCreationForm, UserChangeForm, ReadOnlyPasswordHashField
from .models import Profile


class UserRegisterForm(UserCreationForm):
    email = forms.EmailField(required=True)
    first_name = forms.Textarea()
    last_name = forms.Textarea()
    class Meta:
        model = User
        fields = ['username','email','first_name','last_name','password1','password2',]
        widgets = {
            'username': forms.fields.TextInput(attrs={'placeholder': 'username'}),
            'email': forms.fields.TextInput(attrs={'placeholder': 'example@foodieshoot.com'}),
            'first_name': forms.fields.TextInput(attrs={'placeholder': 'First name'}),
            'last_name': forms.fields.TextInput(attrs={'placeholder': 'Last name'}),
        }
    
    def clean_email(self):
        email = self.cleaned_data.get('email')
        username = self.cleaned_data.get('username')
        if email and User.objects.filter(email=email).exclude(username=username).exists():
            raise forms.ValidationError(u'Email addresses must be unique.')
        return email

class UserUpdateForm(forms.ModelForm):
    email = forms.EmailField()
    def __init__(self, *args, **kwargs):
        super(UserUpdateForm, self).__init__(*args, **kwargs)

        for fieldname in ['username','email',]:
            self.fields[fieldname].help_text = None
    class Meta:
        model = User
        fields = ['username', 'email',]
    
    def clean_email(self):
        email = self.cleaned_data.get('email')
        username = self.cleaned_data.get('username')
        if email and User.objects.filter(email=email).exclude(username=username).exists():
            raise forms.ValidationError(u'Email addresses must be unique.')
        return email


class ProfileUpdateForm(forms.ModelForm):
    def __init__(self, *args, **kwargs):
        super(ProfileUpdateForm, self).__init__(*args, **kwargs)

        for fieldname in ['image',]:
            self.fields[fieldname].help_text = None
    class Meta:
        model = Profile
        fields = ['image']