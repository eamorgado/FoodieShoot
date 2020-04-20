# Generated by Django 2.2.6 on 2020-04-11 21:32

from django.db import migrations, models
import django.utils.timezone
import foodieshoot.models


class Migration(migrations.Migration):

    dependencies = [
        ('foodieshoot', '0003_apks'),
    ]

    operations = [
        migrations.CreateModel(
            name='ImageDataset',
            fields=[
                ('dataset_version', models.AutoField(primary_key=True, serialize=False)),
                ('dataset_num_classes', models.CharField(max_length=4)),
                ('dataset_name', models.CharField(max_length=255)),
                ('dataset_file', models.FileField(default='FoodieShoot_ImageBank.tar.gz', upload_to=foodieshoot.models.upload_location_dataset)),
                ('dataset_date', models.DateTimeField(default=django.utils.timezone.now)),
            ],
            options={
                'verbose_name_plural': 'Dataset',
            },
        ),
    ]