from Data import DataProcessor

print('Downloading Food101 image data and extracting in ...')
dp = DataProcessor()
dp.dataExtraction()

print('Seperating data into train and test folders')
#Creating data
images = 'food-101/images'
train = 'food-101/meta/train.txt'
test = 'food-101/meta/test.txt'

print('Creating training data ...')
dp.dataPrepare(train,images,'train')
_ = dp.dataCountDir('./train')

print('Creating training data ...')
dp.dataPrepare(test,images,'test')
_ = dp.dataCountDir('./test')

pritn('\nData seperated')



