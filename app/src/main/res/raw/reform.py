import json

f = open('property_data.json')
t = f.read()
tiles = json.loads(t)
f.close()

# tag every tile with an position from 0-39
for i in range(40):
    tiles[i]["position"] = i

props = list(filter(lambda e: e["type"] == "property", tiles))

# add mortgage value, fix color field
for p in props:
    p["mortgage"] = int(p["cost"])/2
    # color field fixing
    if p["position"] in [1,3]:
        p["color"] = "PURPLE"
    elif p["position"] in [6,8,9]:
        p["color"] = "LIGHT_BLUE"
    elif p["position"] in [11,13,14]:
        p["color"] = "PINK"
    elif p["position"] in [16,18,19]:
        p["color"] = "ORANGE"
    elif p["position"] in [21,23,24]:
        p["color"] = "RED"
    elif p["position"] in [26,27,29]:
        p["color"] = "YELLOW"
    elif p["position"] in [31,32,34]:
        p["color"] = "GREEN"
    elif p["position"] in [37,39]:
        p["color"] = "DARK_BLUE"

# now do utilities
utils = list(filter(lambda e: e["type"] == "utility", tiles))
for u in utils:
    u["mortgage"] = int(p["cost"])/2

# and railroads
rrs = list(filter(lambda e: e["type"] == "railroad", tiles))
for r in rrs:
    r["mortgage"] = int(p["cost"])/2

# merge all buyable tiles together
buyables = props + utils + rrs

# sort them by ID
psorted = []
for p in buyables:
    for s in psorted:
        if p["position"] < s["position"]:
            psorted.insert(psorted.index(s), p)
            break
        else:
            continue
    psorted.append(p)

# remove the stupid group data from all of the tiles we can buy
for p in psorted:
    try:
        del(p["group"])
    except KeyError as e:
        continue

# FINALLY, write the output
f = open('property_data_cleaned.json', 'w')
f.write(json.dumps(psorted))
f.close()
print "done"
