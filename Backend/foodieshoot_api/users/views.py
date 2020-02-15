from django.shortcuts import render, redirect, render_to_response
from django.contrib import messages
from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from .forms import UserRegisterForm, UserUpdateForm, ProfileUpdateForm


def signin(request):
    context = {'title': 'Sign In', 'signin': 'true',}
    return render(request,'users/signin.html', context)


def signup(request):
    if request.method == 'POST':
        form = UserRegisterForm(request.POST)
        if form.is_valid():
            form.save() #Hashes password and saves it
            username = form.cleaned_data.get('username')
            messages.success(request,f'Your account has been created {username}! You are able to login')
            return redirect('fs-signin')
    else:
        form = UserRegisterForm()
    context = {'title': 'Sign Up', 'signup': 'true','form': form}
    return render(request,'users/signup.html', context)


@login_required
def profile(request):
    if request.method == 'POST':
        #submit form
        u_form = UserUpdateForm(request.POST, instance=request.user)
        p_form = ProfileUpdateForm(request.POST,request.FILES, instance=request.user.profile)
        if u_form.is_valid() and p_form.is_valid():
            #save
            u_form.save()
            p_form.save()
            messages.success(request,f'Your account has been updated!')
            return redirect('fs-profile')
    else:   
        #pass values from user
        u_form = UserUpdateForm(instance=request.user)
        p_form = ProfileUpdateForm(instance=request.user.profile)
    context = {'title': 'Profile','profiles': 'true','u_form':u_form,'p_form':p_form,}
    return render(request,'users/profile.html',context)


