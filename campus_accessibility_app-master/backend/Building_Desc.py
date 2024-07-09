buildings = {}

def building(name, entrance1, entrance2, entrance3, entrance4, entrance5, steps1, steps2, steps3, steps4, steps5, doorWidth1, doorWidth2, doorWidth3, doorWidth4, doorWidth5, doorAutomatic1, doorAutomatic2, doorAutomatic3, doorAutomatic4, doorAutomatic5, liftLocation1, liftLocation2, liftDimensions1, liftDimensions2, toiletLocations1, toiletLocations2):
    building = {}
    building["entrance"] = [entrance1, entrance2, entrance3, entrance4, entrance5]
    building["steps"] = [steps1, steps2, steps3, steps4, steps5]
    building["doorwidth"] = [doorWidth1, doorWidth2, doorWidth3, doorWidth4, doorWidth5]
    building["doorautomatic"] = [doorAutomatic1, doorAutomatic2, doorAutomatic3, doorAutomatic4, doorAutomatic5]
    building["liftlocations"] = [liftLocation1, liftLocation2]
    building["liftdimensions"] = [liftDimensions1, liftDimensions2]
    building["toiletlocations"] = [toiletLocations1, toiletLocations2]
    if name in buildings:
        raise Exception(name)
    buildings[name] = building

One_West = building("1W", "Front", "Rear", "East", None, None, "Flat", "Flat", "Ramp", None, None, "130cm (4ft 3in)", "156cm (5ft 1in)", "200cm (6ft 7in)", None, None, "Automatic", "Automatic", "Manual", None, None, "Right side of main foyer", "Left hand side as you enter", "100cm (3ft 3in)", "100cm (3ft 3in)", "Main Foyer", "East Side")
One_West_North = building("1WN", "1W - Level 2", "1W - Level 3", "1W - Level 4", "Rear Entrance (West Corridoor)", "Rear Entrance (East Coridoor)", "Ramp", "Flat", "Ramp", "Ramp", "Steps", "170cm (5ft 7in)", "170cm (5ft 7in)", "170cm (5ft 7in)", "80cm (2ft 7in)", "80cm (2ft 7in)", "Manual", "Manual", "Manual", "Manual", "Manual", "Right side of main foyer", "Left hand side as you enter", "100cm (3ft 3in)", "100cm (3ft 3in)", "Main foyer", "East Side")
Two_West = building("2W", "Front", None, None, None, None, "Flat", None, None, None, None, "130cm (4ft 3in)", None, None, None, None, "Automatic", None, None, None, None, None, None, None, None, None, None)
Three_West = building("3W","Front", None, None, None, None, "Flat", None, None, None, None,"130cm (4ft 3in)", None, None, None, None, "Automatic", None, None, None, None, "Main foyer", None, "100cm (3ft 3in)", None, "Main foyer", None)
Three_West_North = building("3WN", "Front", None, None, None, None, "Flat", None, None, None, None, "130cm (4ft 3in)", None, None, None, None, "Automatic", None, None, None, None, "Main, foyer", None, "100cm (3ft 3in)", None, "Main foyer", None)
Four_West = building("4W", "Front", None, None, None, None, "Flat", None, None, None, None, "130cm (4ft 3in)", None, None, None, None, "Automatic", None, None, None, None, None, None, None, None, None, None)
Five_West = building("5W", "Front", None, None, None, None, "Flat", None, None, None, None, "130cm (4ft 3in)", None, None, None, None, "Automatic", None, None, None, None, "Main, foyer", None, "100cm (3ft 3in)", None, "Main foyer", None)
Six_West_South = building("6WS", "East Entrance", "West Entrance", None, None, None, "Flat", "Ramp", None, None, None, "200cm (6ft 7in)", "170cm (5ft 7in)", None, None, None, "Automatic", "Automatic", None, None, None, None, None, None, None, "Main foyer", None)
Six_West = building("6W", "Computer training rooms entrance", "Computer services entrance", "Outside access level 1", None, None, "Ramp", "Flat", "Ramp", None, None, "80cm (2ft 7in)", "69cm (2ft 3in)", "78cm (2ft 7in)", None, None, "Manual", "Manual", "Manual", None, None, None, None, None, None, "Level 1 at rear of building", None)
Seven_West = building("7W", "East Entrance", None, None, None, None, "Flat", None, None, None, None, "200cm (6ft 7in)", None, None, None, None, "Automatic", None, None, None, None, "Main foyer", None, "100cm (3ft 3in)", None, "Main foyer", None)
Eight_West = building("8W", "Main Entrance", "Level 2 Entrance", None, None, None, "Ramp", "Ramp", None, None, None, "160cm (5ft 3in)", "180cm (5ft 11in)", None, None, None, "Automatic", "Manual", None, None, None, "Next to reception desk", None, "80cm (2ft 7in)", None, "Opposite room 1.10", None)
Nine_West = building("9W", "Main Entrance", "West Entrance", None, None, None, "Ramp", "Steps", None, None, None, "130cm (4ft 3in)", "180cm (5ft 11in)", None, None, None, "Automatic", "Manual", None, None, None, "Main foyer of 5W", None, "100cm (3ft 3in)", None, "Main foyer", None)
Ten_West = building("10W", "Main Entrance", "Rear Entrance", None, None, None, "Flat", "Flat", None, None, None, "150cm (4ft 11in)", "90cm (2ft 11in)", None, None, None, "Automatic", "Automatic", None, None, None, "Right of main entrance", None, "100cm (3ft 3in)", None, "Right at main entrance", None)

One_East = building("1E", "Access via Library", None, None, None, None, "Flat", None, None, None, None, "150cm (4ft 11in)", None, None, None, None, "Automatic", None, None, None, None, "In Library", None, "100cm (3ft 3in)", None, "Ground floor of library", None)
Two_East = building("2E", "Front", None, None, None, None, "Flat", None, None, None, None, "130cm (4ft 3in)", None, None, None, None, "Automatic", None , None, None, None, "Main foyer", None, "100cm (3ft 3in)", None, "First floor", None,)
Three_East = building("3E", "Front", None, None, None, None, "Steps", None, None, None, None, "180cm (5ft 11in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, "By entrance", None,)
Four_East = building("4E", "Front", None, None, None, None, "Flat", None, None, None, None, "130cm (4ft 3in)", None, None, None, None, "Automatic", None, None, None, None, "Main Foyer", None, "100cm (3ft 3in)", None, "First Floor", None)
Four_East_South = building("4ES", "Main Entrance", "Side Entrance", None, None, None, "flat", "flat", None, None, None, "160cm (5ft 3in)", "160cm (5ft 3in)", None, None, None, "Automatic", "Automatic", None, None, None, "Main foyer", None, "90cm (2ft 11in)", None, "On the right near the entrance", None)
Six_East = building("6E", "Main Entrance", None, None, None, None, "Steps", None, None, None, None, "140cm (4ft 7in)", None, None, None, None, "Manual", None, None, None, None, "Main Foyer", None, "90cm (2ft 11in)", None, "By Main Entrance", None)
Eight_East = building("8E", "Access via 4E", "Back Entrance", None, None, None, "Flat", "Steps", None, None, None, "130cm (4ft 3in)", "180cm (5ft 11in)", None, None, None, "Automatic", "Manual", None, None, None, "Main foyer", None, "100cm (3ft 3in)", None, "First Floor", None)
One_South = building("1S", "South", "Near-South", "North", "Near-North", "West", "Flat", "Ramp", "Flat", "Flat", "Flat", "156cm (5ft 1in)", "152cm (4ft 12in)", "95cm (3ft 1in)", "140cm (4ft 7in)", "148cm (4ft 10in)", "Manual", "Manual", "Automatic", "Automatic", "Manual", "South", "North", "79cm (2ft 7in)", "79cm (2ft 7in)", "Right after you enter", None)
Two_South = building("2S", "Front", None, None, None, None, "Ramp", None, None, None, None, "160cm (5ft 3in)", None, None, None, None, "Automatic", None, None, None, None, "Main Entrance", None, "100cm (3ft 3in)", None, "Main foyer", None)
Three_South = building("3S", "West", None, None, None, None, "Ramp", None, None, None, None, "160cm (5ft 3in)", None, None, None, None, "Automatic", None, None, None, None, None, None, None, None, "Main Entrance", None)
Four_South = building("4S", "North", "East", None, None, None, "Flat", "Flat", None, None, None, "148cm (4ft 10in)", "148cm (4ft 10in)", None, None, None, "Manual", "Manual", None, None, None, None, None, None, None, "Main Entrance", None)
Four_South_A = building("4SA", "Access Via 4S", None, None, None, None, "Flat", None, None, None, None, "148cm (4ft 10in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, "In 4A", None)

Library = building("Library", "Main Entrance", None, None, None, None, "Flat", None, None, None, None, "128cm (4ft 2in)", None, None, None, None, "Automatic", None, None, None, None, "Centre of Building", None, "92cm (3ft)", None, "To the right upon exiting lift", None)
Chancellors_Building = building("Chancellors' Building", "Main Entrance", "Rear Entrance", None, None, None, "Flat", "Ramp", None, None, None, "144cm (4ft 9in)", "164cm (5ft 5in)", None, None, None, "Automatic", "Automatic", None, None, None, "Right rear of building", None, "90cm (2ft 11in)", None, "To the right upon entry", None)
Founders_Hall = building("Founders Hall", "Via Student Union", None, None, None, None, "Flat", None, None, None, None, "180cm (5ft 11in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, "First Floor", None)
Students_Union = building("Students' Union", "Main Entrance", "Side Entrance", None, None, None, "Flat", "Flat", None, None, None, "200cm (6ft 7in)", "206cm (6ft 9in)", None, None, None, "Automatic", "Manual", None, None, None, None ,None, None, None, "Second floor", None)
Norwood_House = building("Norwood House", "Main Entrance", None, None, None, None, "Flat", None, None, None, None, "180cm (5ft 11in)", None, None, None, None, "Manual", None, None, None, None, "Via the parade", None, "100cm (3ft 3in)", None, None, None)
The_Edge = building("The Edge", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "169cm (5ft 7in)", None, None, None, None, "Automatic", None, None, None, None, "Left corner as you enter", None, "90cm (2ft 11in)", None, "By the box office", None)
East_Building = building("East Building", "Main Entrance", None, None, None, None, "Flat", None, None, None, None, "160cm (5ft 3in)", None, None, None, None, "Automatic", None, None, None, None, "On the right as you enter", None, "80cm (2ft 7in)", None, "On level 0", None)
Sports_Training_Village = building("Sports Training Village", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "165cm (5ft 5in)", None, None, None, None, "Automatic", None, None, None, None, "Main coridoor on right hand side", None, "98cm (3ft 3in)", None, "By changing room area", None)
Medical_and_Dental_Care = building("Medical and Dental Care", "Main Entrance", "Back Entrance" , None, None, None, "Ramp", "Ramp", None, None, None, "78cm (2ft 7in)", "78cm (2ft 7in)", None, None, None, "Manual", "Manual", None, None, None, None, None, None, None, "End of the waiting area", None)
Polden = building("Polden", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "140cm (4ft 7in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, None, None)
Polden_Court = building("Polden_court", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "140cm (4ft 7in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, None, None)
Malborough_Court = building("Malborough Court", "Entrance to flat blocks", None, None, None, None, "Flat", None, None, None, None, "94cm (3ft 1in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, None, None)
Solsbury_Court = building("Solsbury Court", "Entrance to flat blocks", None, None, None, None, "Flat", None, None, None, None, "94cm (3ft 1in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, None, None)
Woodland_Court = building("Woodland Court", "Entrance to flat blocks", None, None, None, None, "Ramp", None, None, None, None, "109cm (3ft 7in)", None, None, None, None, "Automatic", None, None, None, None, "Block B foyer", None, "90cm (2ft 11in)", None, "Near Entrance", None)
Wessex_House = building("Wessex House", "South", "Rear South", "North", "Rear North", "West", "Flat", "Ramp", "Flat", "Flat", "Flat", "156cm (5ft 1in)", "152cm (4ft 12in)", "95cm (3ft 1in)", "140cm (4ft 7in)", "148cm (4ft 10in)", "Manual", "Manual", "Automatic", "Automatic", "Manual", "South side entrance", "North side entrance", "79cm (2ft 7in)", "79cm (2ft 7in)", "All Floors", None)
Parade_Bar = building("Parade Bar", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "170cm (5ft 7in)", None, None, None, None, "Automatic", None, None, None, None, "Directly ahead of main entrance", None, "(2ft 11in)", None, "To the right as you enter", None)
Westwood_Conygre = building("Westwood(Conygre)", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "78cm (2ft 7in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, None, None)
Westwood_Quantock = building("Westwood(Quantock)", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "78cm (2ft 7in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, None, None)
Westwood_Cotswold = building("Westwood(Cotswold)", "Main Entrance", None, None, None, None, "Flat", None, None, None, None, "78cm (2ft 7in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, None, None)
Westwood_Derhill = building("Westwood(Derhill)", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "78cm (2ft 7in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, "Level 3", None)
Westwood_Wolfson = building("Westwood(Wolfson)", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "83cm (2ft 9in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, "Ground floor", None)
Applied_Biomechanics_Suite = building("Applied Biomechanics Suite", "Main Entrance", None, None, None, None, "Flat", None, None, None, None, "110cm (3ft 7in)", None, None, None, None, "Automatic", None, None, None, None, None, None, None, None, None, None)
Balehous = building("Balehous", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "150cm (4ft 11in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, None, None)
Claverton_Rooms_Restaurant = building("Claverton Rooms Restaurant", "Main Entrance", None, None, None, None, "Flat", None, None, None, None, "64cm (2ft 1in)", None, None, None, None, "Manual", None, None, None, None, "Level 2 in parade bar", None, "89cm (2ft 11in)", None, "First floor to the left of lifts", None)
Department_of_estates = building("Department of Estates", "Main Entrance", None, None, None, None, "Ramp", None, None, None, None, "96cm (3ft 2in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, None, None)
Founders_Sports_Hall = building("Founders Sports Hall", "Main Entrance", None, None, None, None, "Flat", None, None, None, None, "180cm (5ft 11in)", None, None, None, None, "Manual", None, None, None, None, None, None, None, None, "First floor", None)
Fountain_Canteen = building("Fountain Canteen", "Main Entrance", "University Hall", None, None, None, "Steps", "Flat", None, None, None, "70cm (2ft 4in)", "85cm (2ft 9in)", None, None, None, "Manual", "Automatic", None, None, None, None, None, None, None, "University Hall", None)
Lime_Tree = building("Lime Tree", "South Entrance", "North Entrance", None, None, None, "Flat", "Steps", None, None, None, "130cm (4ft 3in)", "130cm (4ft 3in)", None, None, None, "Automatic", "Automatic", None, None, None, None, None, None, None, "Ground floor", None)
Parcel_Room = building("Parcel", "Main Entrance", None, None, None, None, "Lift", None, None, None, None, "100cm (3ft 3in)", None, None, None, None, "Automatic", None, None, None, None, "Leads to Entrance", None, "85cm (2ft 9in)", None, None, None)
Plug_and_Tub = building("Plug and Tub", "By main stairwell", None, None, None, None, "Flat", None, None, None, None, "172cm (5ft 8in)", None, None, None, None, "Automatic", None, None, None, None, "Side Entrance", None, "89cm (2ft 11in)", None, "Rear of the Tub", None)
The_Quads = building("The Quads", "Main Entrance", None, None, None, None, "Flat", None, None, None, None, "109cm (3ft 7in)", None, None, None, None, "Automatic", None, None, None, None, None, None, None, None, None, None)

for name, building in buildings.items():
    newbuilding = {"entrances":[], "lifts":[]}
    for entranceloc, steps, doorwidth, doorautomatic in zip(building["entrance"], building["steps"], building["doorwidth"], building["doorautomatic"]):
        entrance = {"location":entranceloc, "steps":steps, "doorwidth":doorwidth, "doorautomatic":doorautomatic}
        if not all(value == None for value in entrance.values()):
            if any(value == None for value in entrance.values()):
                raise Exception(entrance)
            newbuilding["entrances"].append(entrance)
    for liftlocations, liftdimensions in zip(building["liftlocations"], building["liftdimensions"]):
        lift = {"location":liftlocations, "dimensions":liftdimensions}
        if not all(value == None for value in lift.values()):
            if any(value == None for value in lift.values()):
                raise Exception(lift)
            newbuilding["lifts"].append(lift)
    newbuilding["toiletlocations"] = [toilet for toilet in building["toiletlocations"] if toilet is not None]
    buildings[name] = newbuilding

if __name__ == "__main__":
    expected = {'entrances': [
            {'location': 'South', 'steps': 'Flat', 'doorwidth': '156cm (5ft 1in)', 'doorautomatic': 'Manual'},
            {'location': 'Near-South', 'steps': 'Ramp', 'doorwidth': '152cm (4ft 12in)', 'doorautomatic': 'Manual'},
            {'location': 'North', 'steps': 'Flat', 'doorwidth': '95cm (3ft 1in)', 'doorautomatic': 'Automatic'},
            {'location': 'Near-North', 'steps': 'Flat', 'doorwidth': '140cm (4ft 7in)', 'doorautomatic': 'Automatic'},
            {'location': 'West', 'steps': 'Flat', 'doorwidth': '148cm (4ft 10in)', 'doorautomatic': 'Manual'}
        ],
        'lifts': [{'location': 'South', 'dimensions': '79cm (2ft 7in)'},
            {'location': 'North', 'dimensions': '79cm (2ft 7in)'}
        ],
        'toiletlocations': ['Right after you enter']
    }

    assert buildings["1S"] == expected

