import cv2
import numpy as np
import os, random, collections
from collections import defaultdict
import matplotlib.image as img
import matplotlib.pyplot as plt

import tensorflow as tf
import tensorflow.keras.backend as K
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
from tensorflow.keras import regularizers
from tensorflow.keras.applications.inception_v3 import InceptionV3
from tensorflow.keras.models import Sequential, Model
from tensorflow.keras.layers import Dense, Dropout, Activation, Flatten
from tensorflow.keras.layers import Convolution2D, MaxPooling2D, ZeroPadding2D, GlobalAveragePooling2D, AveragePooling2D
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.callbacks import ModelCheckpoint, CSVLogger
from tensorflow.keras.optimizers import SGD
from tensorflow.keras.regularizers import l2
from tensorflow import keras
from tensorflow.keras import models

print("TensorFlow Version: {}".format(tf.__version__))

class ImageClassificationNN():
    def __init__(self):
        K.clear_session()
        print('Model session initiated')

    def modelTune(self,n_classes,img_width,img_height,train_data_dir,validation_data_dir,nb_train_samples,nb_validation_samples,batch_size): 
        self.n_classed = n_classes
        self.nb_train_samples = nb_train_samples
        self.nb_validation_samples = nb_validation_samples
        self.batch_size = batch_size

        train_datagen = ImageDataGenerator(
            rescale=1. / 255,
            shear_range=0.2,
            zoom_range=0.2,
            horizontal_flip=True
        )
        test_datagen = ImageDataGenerator(rescale=1. / 255)

        train_generator = train_datagen.flow_from_directory(
            train_data_dir,
            target_size=(img_height, img_width),
            batch_size=batch_size,
            class_mode='categorical'
        )
        validation_generator = test_datagen.flow_from_directory(
            validation_data_dir,
            target_size=(img_height,img_width),
            batch_size=batch_size,
            class_mode='categorical'
        )

        self.train_datagen = train_datagen
        self.test_datagen = test_datagen
        self.train_generator = train_generator
        self.validation_generator = validation_generator
        return train_datagen,test_datagen,train_generator,validation_generator

    def modelBuild(self,activation='softmax',loss='categorical_crossentropy', metrics=['accuracy'],units=101):
        inception = InceptionV3(weights='imagenet',include_top=False)
        x = inception.output
        x = GlobalAveragePooling2D()(x)
        x = Dense(128,activation='relu')(x)
        x = Dropout(0.2)(x)

        predictions = Dense(units,kernel_regularizer=regularizers.l2(0.005), activation=activation)(x)
        self.model = Model(inputs=inception.input, outputs=predictions)
        self.model.compile(optimizer=SGD(lr=0.0001, momentum=0.9), loss=loss, metrics=metrics)
        print(self.model.summary)
        return self.model

    def modelTrain(self,epochs=30):
        out_folder = './checkpoints'
        if not os.path.exists(out_folder):
            os.makedirs(out_folder)
        filepath = out_folder + '/model-{epoch:02d}-{val_accuracy:-2f}.hdf5'
        checkpointer = ModelCheckpoint(filepath, monitor='val_accuracy', verbose=1, save_best_only=False, save_weights_only=False,save_frequency=1)
        csv_logger = CSVLogger('history_3class.log')

        self.history = self.model.fit(self.train_generator,
                        steps_per_epoch=self.nb_train_samples // batch_size,
                        validation_data=self.validation_generator,
                        validation_steps=self.nb_validation_samples // batch_size,
                        epochs=epochs,
                        verbose=1,
                        callbacks=[csv_logger, checkpointer])
        
        self.model.save('model_food101.hdf5')
        return self.history, self.model

    def modelPredictClass(self,model,images,food_list,show=True):
        for img in images:
            img = image.load_img(img, target_size=(299, 299))
            img = image.img_to_array(img)                    
            img = np.expand_dims(img, axis=0)         
            img /= 255.                                     

            pred = model.predict(img)
            index = np.argmax(pred)
            food_list.sort()
            pred_value = food_list[index]
            if show:
                plt.imshow(img[0])                           
                plt.axis('off')
                plt.title(pred_value)
                plt.show()