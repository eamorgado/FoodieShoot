import json

foods = {
    'foods': []
}
def loadFromFile():
    data = open('labels-unit-quant-cals.txt','r').read().split('\n')
    i = 0
    for line in data:
        print(i)
        i += 1
        label,unit,quantity,cals = line.split(',')
        foods['foods'].append({
            'name': label,
            'unit': unit,
            'quantity': quantity,
            'calories': cals
        })

def interactive():
    n = int(input("Number of foods: "))
    for food in range(n):
        inp = str(input("Enter label [label,unit,quantity,cals]: ")).split(',')
        label = inp[0]
        unit = inp[1]
        quantity = inp[2]
        cals = inp[3]
        foods['foods'].append({
            'name': label,
            'unit': unit,
            'quantity': quantity,
            'calories': cals
        })

print("Start")
interactive()
print("Saving")

with open('food_labels.json','w') as f:
    json.dump(foods,f)

print("Complete")