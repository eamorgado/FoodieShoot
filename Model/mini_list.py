import os,sys,json

#this file creates a list of foods

def interactive():
    def help():
        print('Interactive food adder:')
        print('Folder: ./train')
        print('\t--help\tdisplays interactive commands')
        print('\t--list\tdisplays list so far')
        print('\t--count\tcounts how many items in list')
        print('\t--reset\tresets list back to zero')
        print('\t--undo\tremoves last added item')
        print('\t--exit\tterminates the program')        
        print('\t--complete\tfinished adding items, end interactive')
        print('\t--files\tlists all files in ./train')
    def files(parent):
        return [name for name in os.listdir(parent) if os.path.isfile(name)]
    parent = './train'
    help()
    food_list = []
    while True:
        inp = str(input(">"))
        if(inp != ""):
            if(inp == '--help'):
                help()
            elif(inp == '--list'):
                if not food_list:
                    print('List empty')
                else:
                    print('List: ',str(food_list))
            elif(inp == '--count'):
                if not food_list:
                    print('List empty')
                else:
                    print('Items: ',str(len(food_list)))
                continue
            elif(inp == '--reset'):
                food_list = []
            elif(inp == '--undo'):
                if food_list:
                    del food_list[-1]
            elif(inp == '--exit'):
                print('Ending')
                sys.exit()
            elif(inp == '--complete'):
                print('List completed')
                print('Items: ',len(food_list))
                print('List: ',str(food_list))
                break
            elif(inp == '--files'):
                print('Listing files')
                print(str(files(parent)))
            else: #add to list
                if verify_single(inp,parent):
                    print('Adding ',inp, ' to list')
                    food_list.append(inp)
                else:
                    print('Item ','inp', ' does not exist in ',parent)
        continue
    if not food_list:
        print('Food list empty, proceeding with saved one')
        food_list = backup_list()
    return food_list

def backup_list():
    food_list = [
             'apple_pie',
             'baby_back_ribs',
             'beef_carpaccio',
             'bread_pudding',
             'caesar_salad',
             'cannoli',
             'carrot_cake',
             'cheesecake',
             'chicken_curry',
             'chicken_wings',
             'chocolate_cake',
             'chocolate_mousse',
             'churros',
             'cup_cakes',
             'donuts',
             'dumplings',
             'falafel',
             'fish_and_chips',
             'french_fries',
             'french_toast',
             'fried_rice',
             'garlic_bread',
             'grilled_salmon',
             'guacamole',
             'hot_dog',
             'hot_and_sour_soup',
             'hummus',
             'hamburger',
             'ice_cream',
             'lasagna',
             'macaroni_and_cheese',
             'macarons',
             'nachos',
             'onion_rings',
             'oysters',
             'pancakes',
             'pork_chop',
             'poutine',
             'ramen',
             'ravioli',
             'risotto',
             'spaghetti_bolognese',
             'spaghetti_carbonara',
             'steak',
             'sushi',
             'tacos',
             'waffles',
             'pizza',
             'omelette'
    ]
    print('Items: ',len(food_list))
    print('List: ',str(food_list))
    return food_list    

def verify_single(f,parent):
    return os.path.exists(os.path.join(parent,f))

def verify(data, parent):
    for item in data:
        if not verify_single(item,parent):
            print('Item ', item, ' does not exit in ',parent)
            sys.exit(2)
    
def save(food_list,filename):
    with open(filename,'w') as f:
        json_string = json.dumps(food_list)
        f.write(json_string)
    print('Data written')
    
def main(argv):
    print('Usage: mini_list.py [--interactive]')
    food_list = None
    if(len(argv) == 0):
        food_list = backup_list()
        verify(food_list,"./train")
        
    elif(len(argv) == 1):
        print('Selected interactive mode')
        food_list = interactive()
    filename = str(input("Enter filename (leave extension out): "))
    filename = os.path.splitext(filename)[0] + '.json'
    save(food_list,os.path.join(os.path.join(os.getcwd(),'food_lists'),'filename'))

if __name__ == "__main__":
    main(sys.argv[1:])
