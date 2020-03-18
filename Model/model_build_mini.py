import os, json
from Data import DataProcessor
from Model import ImageClassificationNN

def load(filename):
    with open(filename,'r') as f:
        return json.load(f)

#Create subset of data with less classes for faster (5-6h) training
print('Food list files:')
print("If it does not exist, exit script and run python mini_list.py [--interactive]")
for file in os.listdir(os.path.join(os.getcwd(),'food_lists')):
    if file.endswith('.json'):
        print('----> ',file)
print()
food_list = str(input(">"))
food_list = os.path.join(os.path.join(os.getcwd(),'food_lists'),food_list)
food_list = load(food_list)
leng = len(food_list)


src_train = os.path.join(os.getcwd(),'train')
src_test = os.path.join(os.getcwd(),'test')

dest_train = os.path.join(os.getcwd(),'train_mini')
dest_test = os.path.join(os.getcwd(),'test_mini')

dp = DataProcessor()
print('Creating mini training data ...')
dp.dataMiniDataset(food_list,src_train,dest_train)
dest_train_number = dp.dataCountDir(dest_train)

print('Creating mini testing data ...')
dp.dataMiniDataset(food_list,src_test,dest_test)
dest_test_number = dp.dataCountDir(dest_test)


#set config
n_classes = leng
img_width,img_height = 229,229
train_data_dir,validation_data_dir = dest_train,dest_test
nb_train_samples,nb_validation_samples = dest_train_number, dest_test_number
batch_size = 100

#Build model
img_classification = ImageClassificationNN()
train_datagen,test_datagen,train_generator,validation_generator = img_classification.modelTune(
    n_classes,
    img_width,img_height,
    train_data_dir,validation_data_dir,
    nb_train_samples,nb_validation_samples,
    batch_size
)

history_name = 'history_mini_'+leng+'class.log'
model_name = 'model_mini_'+leng+'.h5'

history_name = os.path.join(os.path.join(os.getcwd(),'histories'),history_name)
model_name = os.path.join(os.path.join(os.getcwd(),'models'),model_name)
model = img_classification.modelBuild(units=leng)
history, model = img_classification.modelTrain()


