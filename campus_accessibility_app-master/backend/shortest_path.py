
########## START OF MAP CREATION ##########

def add_all(distance, access, notes,x,y,dis_matrix,lev_matrix,note_matrix):
    x=x-1
    y=y-1
    dis_matrix[x][y]=distance
    dis_matrix[y][x]=distance
    lev_matrix[x][y]=access
    lev_matrix[y][x]=access
    note_matrix[x][y]=notes
    note_matrix[y][x]=notes

def dir_add(dir_matrix,node_from,node_through,node_to,directions):
    node_from=node_from-1
    node_through=node_through-1
    node_to=node_to-1
    if node_from!=-1 and node_to!=-1:
        dir_matrix[node_from][node_through].append(node_to)
        dir_matrix[node_from][node_through].append(directions)
    elif node_from==-1:
        #end
        dir_matrix[node_through][node_to].append(directions)
    elif node_to ==-1:
        #start
        dir_matrix[node_from][node_through].append(directions)


def print_all():
    for row in range(0,height):
        new_arr=[]
        for col in range(0,width):
            new_arr.append(dis_matrix[row][col])
        print(new_arr)
    print(3*"\n")
    for row in range(0,height):
        new_arr=[]
        for col in range(0,width):
            new_arr.append(lev_matrix[row][col])
        print(new_arr)
    print(3*"\n")
    for row in range(0,height):
        new_arr=[]
        for col in range(0,width):
            new_arr.append(note_matrix[row][col])
        print(new_arr)
    print(3*"\n")
    for row in range(0,height):
        new_arr=[]
        for col in range(0,width):
            new_arr.append(dir_matrix[row][col])
        print(new_arr)

def make_graph(dis_matrix,lev_matrix,note_matrix,dir_matrix):
    #1
    add_all(100,4,["Stairs"],1,2,dis_matrix,lev_matrix,note_matrix)
    add_all(65,2,None,1,3,dis_matrix,lev_matrix,note_matrix)
    add_all(200,1,None,1,4,dis_matrix,lev_matrix,note_matrix)
    #2
    add_all(140,2,["Slight Slope"],2,3,dis_matrix,lev_matrix,note_matrix)
    add_all(60,1,None,2,6,dis_matrix,lev_matrix,note_matrix)
    #3
    add_all(270,4,["Stairs"],3,10,dis_matrix,lev_matrix,note_matrix)
    add_all(400,1,["Under Parade"],3,21,dis_matrix,lev_matrix,note_matrix)
    add_all(80,2,["Steep Slope"],3,23,dis_matrix,lev_matrix,note_matrix)
    #4
    add_all(110,1,None,4,5,dis_matrix,lev_matrix,note_matrix)
    #5
    add_all(150,1,None,5,9,dis_matrix,lev_matrix,note_matrix)
    #6
    add_all(80,1,None,6,7,dis_matrix,lev_matrix,note_matrix)
    #7
    add_all(120,1,None,7,8,dis_matrix,lev_matrix,note_matrix)
    #8
    add_all(180,1,None,8,15,dis_matrix,lev_matrix,note_matrix)
    add_all(100,1,None,8,25,dis_matrix,lev_matrix,note_matrix)
    add_all(75,1,None,8,30,dis_matrix,lev_matrix,note_matrix)
    #9
    add_all(150,1,None,9,10,dis_matrix,lev_matrix,note_matrix)
    #10
    add_all(120,1,None,10,11,dis_matrix,lev_matrix,note_matrix)
    add_all(158,1,None,10,22,dis_matrix,lev_matrix,note_matrix)
    #11
    add_all(80,1,None,11,12,dis_matrix,lev_matrix,note_matrix)
    add_all(163,1,None,11,13,dis_matrix,lev_matrix,note_matrix)
    add_all(150,1,None,11,22,dis_matrix,lev_matrix,note_matrix)
    #12
    add_all(150,1,None,12,13,dis_matrix,lev_matrix,note_matrix)
    #13
    add_all(140,2,["Slope","Curving Slope"],13,14,dis_matrix,lev_matrix,note_matrix)
    #14
    add_all(90,1,None,14,20,dis_matrix,lev_matrix,note_matrix)
    #15
    add_all(135,1,None,15,16,dis_matrix,lev_matrix,note_matrix)
    add_all(60,1,None,15,28,dis_matrix,lev_matrix,note_matrix)
    #16
    add_all(80,1,None,16,17,dis_matrix,lev_matrix,note_matrix)
    #17
    add_all(120,1,None,17,18,dis_matrix,lev_matrix,note_matrix)
    add_all(140,1,None,17,29,dis_matrix,lev_matrix,note_matrix)
    #18
    add_all(60,1,None,18,19,dis_matrix,lev_matrix,note_matrix)
    #19
    add_all(110,1,None,19,20,dis_matrix,lev_matrix,note_matrix)
    #20
    add_all(110,1,None,20,21,dis_matrix,lev_matrix,note_matrix)
    #21
    add_all(100,2,["Stair or Thin Elevator"],21,27,dis_matrix,lev_matrix,note_matrix)
    add_all(60,1,None,21,29,dis_matrix,lev_matrix,note_matrix)
    #22
    add_all(110,1,["Elevator"],22,24,dis_matrix,lev_matrix,note_matrix)
    add_all(60,4,["Stairs"],22,25,dis_matrix,lev_matrix,note_matrix)
    #23
    add_all(70,1,None,23,24,dis_matrix,lev_matrix,note_matrix)
    #24
    add_all(40,1,None,24,25,dis_matrix,lev_matrix,note_matrix)
    #25
    add_all(30,1,None,25,26,dis_matrix,lev_matrix,note_matrix)
    add_all(75,1,None,25,30,dis_matrix,lev_matrix,note_matrix)
    #26
    add_all(45,1,None,26,27,dis_matrix,lev_matrix,note_matrix)
    #28
    add_all(60,1,None,28,29,dis_matrix,lev_matrix,note_matrix)


    #1
    dir_add(dir_matrix,1,2,0,"Walk towards the centre of the university and take the stairs on the left between the 2 parts of Polden")
    dir_add(dir_matrix,1,3,0,"Walk towards 10W")
    dir_add(dir_matrix,1,4,0,"Walk through the carpark until you reach a path at the bottom, walk to it")
    dir_add(dir_matrix,1,2,6,"After making it up the stairs keep walking straight until you reach a path, turn right and walk along the pavement")
    dir_add(dir_matrix,1,3,21,"Go under the Parade and follow the path until you come out the other side")
    dir_add(dir_matrix,1,3,23,"Walk up the slope beside 8W")
    dir_add(dir_matrix,1,4,5,"Turn left and follow the path")
    dir_add(dir_matrix,0,1,2,"Once up the stairs you are in the back of Polden by Polden Court")
    dir_add(dir_matrix,0,1,3,"Once passed the car park the entrance to 10W is on the right side of the road")
    dir_add(dir_matrix,0,1,4,"Once passed the car park the medical centre is right in front of you")

    #2
    dir_add(dir_matrix,2,1,0,"Go around the back of Polden and follow the building to your left until you reach stairs going down, once down them take a right")
    dir_add(dir_matrix,2,3,0,"Start moving directly away from polden court and when you reach the road turn right and go down the hill")
    dir_add(dir_matrix,2,6,0,"Start moving directly away from polden court and when you reach the road follow it")
    dir_add(dir_matrix,2,6,7,"Keep following the road")
    dir_add(dir_matrix,2,1,4,"Go into the carpark and go to the south side (if you turn left into the car park then you're facing south) where there is a path to the medical centre")
    dir_add(dir_matrix,2,3,10,"Between 8 and 10 West there is a set of stairs. Go down them and then go up the rock stairs and follow the path. At the dead end turn right and follow the path until you reach the crossroads by the lake")
    dir_add(dir_matrix,2,3,21,"Take a left to go under the parade to the other side")
    dir_add(dir_matrix,2,3,23,"Go up the slope that runs beside 8 West (should be to your left)")
    dir_add(dir_matrix,0,2,1,"Walk between polden court and polden to the stairs between the 2 polden buildings, once there take a left")
    dir_add(dir_matrix,0,2,3,"Walk down the slope to 8 and 10 W")
    dir_add(dir_matrix,0,2,6,"Keep walking and you'll be outside Westwood")

    #3
    dir_add(dir_matrix,3,1,0,"Walk away from 8W through the car park")
    dir_add(dir_matrix,3,2,0,"Cross the road and go up the hill/slope")
    dir_add(dir_matrix,3,10,0,"Between 8 and 10 West there is a set of stairs, go down them and then go up the rock stairs and follow the path. At the dead end turn right and follow the path until you reach the crossroads by the lake")
    dir_add(dir_matrix,3,21,0,"Go under the parade and follow it to the other side")
    dir_add(dir_matrix,3,23,0,"Go up the slope at the side of 8W")
    dir_add(dir_matrix,3,1,2,"X")
    dir_add(dir_matrix,3,1,4,"Go though the car park and turn left. Follow it to the back where there is a path that leads to the medical centre")
    dir_add(dir_matrix,3,2,1,"X")
    dir_add(dir_matrix,3,2,6,"Walk up the slope/hill then turn right")
    dir_add(dir_matrix,3,6,2,"X")
    dir_add(dir_matrix,3,10,9,"When at the crossroads take a right and walk down the road (the road goes between 1S and 3S)")
    dir_add(dir_matrix,3,10,11,"At the crossroads go towards the lake and follow it keeping the lake to your left")
    dir_add(dir_matrix,3,10,22,"At the crossroads walk towards the lake, then take a left to walk towards the main campus")
    dir_add(dir_matrix,3,21,20,"When out of the parade keep walking straight until you cross the road, then turn right and walk down until you cross the pavement again")
    dir_add(dir_matrix,3,21,27,"X")
    dir_add(dir_matrix,3,21,29,"When out of the parade walk until you see the stairs on the left that lead up the parade, go down the path that's to the right - at the base of those stairs")
    dir_add(dir_matrix,3,23,24,"When up the slope walk through Wessex house and keep going straight")
    dir_add(dir_matrix,0,3,1,"Cross the road to get to the Polden bulding")
    dir_add(dir_matrix,0,3,2,"Walk up the hill and take the left and you'll be at polden court")
    dir_add(dir_matrix,0,3,10,"Between 8 and 10 West there is a set of stairs, go down them and then go up the rock stairs and follow the path. At the dead end turn right and follow the path until you reach the crossroads by the lake")
    dir_add(dir_matrix,0,3,21,"Go under the parade and exit the other side and you'll be at the bus stop")
    dir_add(dir_matrix,0,3,23,"Go up the slope and you'll be on the parade with Wessex house in front of you")

    #4
    dir_add(dir_matrix,4,1,0,"Directly opposite the medical centre there is a path that goes through the car park, follow it and go straight on")
    dir_add(dir_matrix,4,5,0,"When facing the medical centre turn left and follow the path")
    dir_add(dir_matrix,4,1,2,"When through the car park cross the road and turn left, taking the stairs on your left")
    dir_add(dir_matrix,4,1,3,"When at the end of the car park turn right and follow the road")
    dir_add(dir_matrix,4,5,9,"Follow the road until you reach a T")
    dir_add(dir_matrix,0,4,1,"Walk through the cark park and out the other side then cross the road to reach Polden")
    dir_add(dir_matrix,0,4,5,"Walk down the path and you'll reach the sport court and the lodge")

    #5
    dir_add(dir_matrix,5,4,0,"When facing the sports court turn right and follow the path")
    dir_add(dir_matrix,5,9,0,"When facing the sports court turn left and follow the path")
    dir_add(dir_matrix,5,4,1,"When you reach the medical centre take the path to your right and walk through the car park")
    dir_add(dir_matrix,5,9,10,"When you reach the T junction turn left")
    dir_add(dir_matrix,0,5,4,"Keep following the path and the medical centre will be on your left")
    dir_add(dir_matrix,0,5,9,"Keep following the path and you will reach the T junction by the S buildings")

    #6
    dir_add(dir_matrix,6,2,0,"Walk towards Polden Court")
    dir_add(dir_matrix,6,7,0,"Walk away from Polden Court")
    dir_add(dir_matrix,6,2,3,"When the road turns to the left follow it")
    dir_add(dir_matrix,6,2,1,"When the road turns to the left, cross the road and go behind the building that the road runs along. Turn left and keep going until you go down the stairs")
    dir_add(dir_matrix,6,7,8,"Keep following the road")
    dir_add(dir_matrix,0,6,2,"Keep following the road and cross it when it turns and you'll be at polden Court")
    dir_add(dir_matrix,0,6,7,"Keep walking until the laundry place is on your left")

    #7
    dir_add(dir_matrix,7,6,0,"Walk along the road with the westwood buildings to your right")
    dir_add(dir_matrix,7,8,0,"Walk along the road with the westwood buildings to your left")
    dir_add(dir_matrix,7,6,2,"Keep following the road")
    dir_add(dir_matrix,7,8,15,"When you pass Brendon court keep following the road")
    dir_add(dir_matrix,7,8,25,"When you reach Brendon court turn right and take the path that goes beside 1WN and 1W")
    dir_add(dir_matrix,7,8,30,"When you reach brendon court turn right and take the path that goes diagonally out from the road")
    dir_add(dir_matrix,0,7,6,"Walk along the road and you'll end up behind 7W")
    dir_add(dir_matrix,0,7,8,"Walk along the road until Brendon court is on your left")

    #8
    dir_add(dir_matrix,8,7,0,"Walk along the road with Brendon Court to your right")
    dir_add(dir_matrix,8,15,0,"Walk along the road with Brendon Court to your left")
    dir_add(dir_matrix,8,25,0,"Walk forward, taking the path beside 1WN and 1W")
    dir_add(dir_matrix,8,30,0,"Take the path opposide Brendon Court that is diagonal off of the road")
    dir_add(dir_matrix,8,7,6,"Follow the road")
    dir_add(dir_matrix,8,15,16,"Follow the road and you'll pass the post office on your left at a crossroad, when you do keep going straight")
    dir_add(dir_matrix,8,15,28,"Follow the road and you'll pass the post office on your left at a crossroad, when you do take the right")
    dir_add(dir_matrix,8,25,24,"When you reach the main parade turn right and follow it")
    dir_add(dir_matrix,8,25,26,"When you reach the main parade turn left and follow it")
    dir_add(dir_matrix,8,25,30,"X")
    dir_add(dir_matrix,8,30,25,"X")
    dir_add(dir_matrix,0,8,7,"Follow the road and the Nursery / Laundry building will be on your right")
    dir_add(dir_matrix,0,8,15,"Follow the road and at a crossroad the parcel office will be on your left")
    dir_add(dir_matrix,0,8,25,"Once you've gone through the path you will reach the parade, next to the library, on your left")
    dir_add(dir_matrix,0,8,30,"Once you're down the path you'll reach 3E and the back door to the Chancellors' building")

    #9
    dir_add(dir_matrix,9,5,0,"Take the path that goes through the car park")
    dir_add(dir_matrix,9,10,0,"Take the path that goes between the 2S and 3S buildings")
    dir_add(dir_matrix,9,5,4,"Keep following the path")
    dir_add(dir_matrix,9,10,3,"When at the crossroads take the left. When you reach another junction go left, then the first right which will go down some rocky stairs. Then go up the stairs infront of you")
    dir_add(dir_matrix,9,10,11,"When at the junction take the left and follow the path, keeping the lake to your left")
    dir_add(dir_matrix,9,10,22,"When at the crossroads continue straight on then take a right once you reach the back of 4W")
    dir_add(dir_matrix,0,9,5,"Follow the path until the sports field is on your right")
    dir_add(dir_matrix,0,9,10,"Follow the path until you're at the crossroads by the lake")

    #10
    dir_add(dir_matrix,10,3,0,"Take the path opposite the lake and follow it until you reach a crossroad, take a left, then a right down the rocky stairs, then go up the stairs at the end")
    dir_add(dir_matrix,10,9,0,"Take the path that goes between the 1S and 3S buildings")
    dir_add(dir_matrix,10,11,0,"Walk along the lake with it to your left")
    dir_add(dir_matrix,10,22,0,"Walk up the path that keeps the lake to your right, then turn right once you reach the University Hall")
    dir_add(dir_matrix,10,3,1,"When up the stairs follow the road beside the carpark")
    dir_add(dir_matrix,10,3,2,"When up the stairs cross the road by the hill/slope and go up it")
    dir_add(dir_matrix,10,3,21,"When up the stairs walk forward until you're by the road, then turn right and go under the parade")
    dir_add(dir_matrix,10,3,23,"When up the stairs walk following the path to your right and you'll find a slope; go up it")
    dir_add(dir_matrix,10,9,5,"Take the first right (it will be just after the 2S building)")
    dir_add(dir_matrix,10,11,12,"When at the crossroad by the other side of the lake take a right")
    dir_add(dir_matrix,10,11,13,"When at the crossroad by the other side of the lake go straight on")
    dir_add(dir_matrix,10,11,22,"10->11->22 X")
    dir_add(dir_matrix,10,22,11,"10->22->11 X")
    dir_add(dir_matrix,10,22,24,"Take the elevator")
    dir_add(dir_matrix,10,22,25,"Turn left and go up the stairs")
    dir_add(dir_matrix,0,10,3,"Once up the stairs you'll be at 10W with 8W to your right")
    dir_add(dir_matrix,0,10,9,"Down the road you will find S1, S2 and S3")
    dir_add(dir_matrix,0,10,11,"At the other side of the lake you'll find the crossroads")
    dir_add(dir_matrix,0,10,22,"When at the paved area you'll be at the main lake section")

    #11
    dir_add(dir_matrix,11,10,0,"Go alongside the lake with it to your right")
    dir_add(dir_matrix,11,12,0,"Go down the path that leads right to the car park")
    dir_add(dir_matrix,11,13,0,"Go down the path that's flanked by trees")
    dir_add(dir_matrix,11,22,0,"Go along the path that keeps the lake on your left and when you reach the back of 2E, take a left")
    dir_add(dir_matrix,11,10,3,"When at the crossroads go straight on and follow the path. When you reack a crossroad take a left, then take the first right down the rocky stairs and go up the next stairs.")
    dir_add(dir_matrix,11,10,9,"At the crossroads take a left down the path which is flanked by 1S and 3S")
    dir_add(dir_matrix,11,10,22,"11->10->22 X")
    dir_add(dir_matrix,11,12,13,"11->12->13 X")
    dir_add(dir_matrix,11,13,12,"11->13->12 X")
    dir_add(dir_matrix,11,13,14,"When you reach the car park, turn left and follow it. Use the traffic light to cross the road, then follow the path by taking a left. When it meets with the Sports training Village, take a left again")
    dir_add(dir_matrix,11,22,24,"Take the elevator")
    dir_add(dir_matrix,11,22,25,"Go up the stairs that lead to the parade")
    dir_add(dir_matrix,0,11,10,"After you pass the lake you'll have reached the crossroads")
    dir_add(dir_matrix,0,11,12,"When you reach the car park you'll be by the Milner Centre and 4SA")
    dir_add(dir_matrix,0,11,13,"When you reach the end of the road you'll be at the car park")
    dir_add(dir_matrix,0,11,22,"Once you reach the paved section between the stairs and the lake, you have arrived.")

    #12
    dir_add(dir_matrix,12,11,0,"Walk down the path that keeps the Milner Centre on your left")
    dir_add(dir_matrix,12,13,0,"Walk alongside the car park")
    dir_add(dir_matrix,12,11,10,"When at the crossroads, take the left")
    dir_add(dir_matrix,12,11,13,"12->11->13 X")
    dir_add(dir_matrix,12,11,22,"When at the crossroads go straight on and take a left when you reach the back of 2E")
    dir_add(dir_matrix,12,13,11,"12->13->11 X")
    dir_add(dir_matrix,12,13,14,"When at the road, cross it and take a left. When you reack the Sports Training Village take another left")
    dir_add(dir_matrix,0,12,11,"When you reach the crossroads by the lake you'll be at your destination")
    dir_add(dir_matrix,0,12,13,"You will reach the south car park")

    #13
    dir_add(dir_matrix,13,11,0,"Take the path flanked by trees to the crossroads")
    dir_add(dir_matrix,13,12,0,"Follow the car park, keeping it to your left, and you'll reach the Milner Centre")
    dir_add(dir_matrix,13,14,0,"Cross the road and take a left, then again when you reach the Sports Training Village")
    dir_add(dir_matrix,13,11,10,"At the crossroads, go straight on, keeping the lake to your right")
    dir_add(dir_matrix,13,11,12,"13->11->12 X")
    dir_add(dir_matrix,13,11,22,"At the crossroads, take a right and then a left when you reach the end of 2E")
    dir_add(dir_matrix,13,12,11,"13->12->11 X")
    dir_add(dir_matrix,13,14,20,"If you follow the road you'll have The Edge on your right")
    dir_add(dir_matrix,0,13,11,"Following the path will lead you to the crossroads by the lake")
    dir_add(dir_matrix,0,13,12,"At the end of the car park is the Milner Centre and 4SA")
    dir_add(dir_matrix,0,13,14,"On your right will be the entrance to the STV")

    #14
    dir_add(dir_matrix,14,13,0,"Go down the path, keeping the STV on your left, then take the first right and cross the road to make it to the south car park")
    dir_add(dir_matrix,14,20,0,"Go along the path, keeping the Sports Training Village to your right")
    dir_add(dir_matrix,14,13,11,"When at the car park, take a right down the path flanked by trees")
    dir_add(dir_matrix,14,13,12,"When at the car park, follow it to the other side")
    dir_add(dir_matrix,14,20,19,"When you reach the crossing, take a right and follow the path to the East Building")
    dir_add(dir_matrix,14,20,21,"Follow the road and cross it to keep going straight, take a left to stay beside the Arrivals Square")
    dir_add(dir_matrix,0,14,13,"When at the car park you'll reach 5S")
    dir_add(dir_matrix,0,14,20,"At the end of the road The Edge will be on your right")

    #15
    dir_add(dir_matrix,15,8,0,"Go down the path, keeping the parcel office to your right")
    dir_add(dir_matrix,15,16,0,"Go along the path between Eastwod and Malbourough Court")
    dir_add(dir_matrix,15,28,0,"Go down the path opposite the parcel office, leading to the Chancellors' Building")
    dir_add(dir_matrix,15,8,7,"Keep following the path when you reach Brendon Court")
    dir_add(dir_matrix,15,8,25,"When you reach Brendon Court, take the path to your left that goes beside 1WN")
    dir_add(dir_matrix,15,8,30,"When you reach Brendon Court, take a left and take the path that goes diagonal between 3E and the Library")
    dir_add(dir_matrix,15,16,17,"At the T junction take a right")
    dir_add(dir_matrix,15,28,29,"When at the Chancellors' Building, pass it until you can take a left")
    dir_add(dir_matrix,0,15,8,"Keep walking along the path and you'll soon be between Brendon Court and 1WN")
    dir_add(dir_matrix,0,15,16,"Soon you'll reach a T junction where Woodland court is")
    dir_add(dir_matrix,0,15,28,"The Chancellors' building will be on your right")

    #16
    dir_add(dir_matrix,16,15,0,"At the T junction take the path off the straight line")
    dir_add(dir_matrix,16,17,0,"Go down the path between Solsbury Court and Woodland Court")
    dir_add(dir_matrix,16,15,8,"When you reach the crossroads by the parcel office, keep following the road")
    dir_add(dir_matrix,16,15,28,"When at the crossroads by the parcel office, take the left to walk by the Chancellors' Building")
    dir_add(dir_matrix,16,17,18,"Keep following the road until you walk through the East car park")
    dir_add(dir_matrix,16,17,29,"There will be a path on your right that goes between the Quads and Solsbury Court, which is opposite the East Accomodation Centre. Go through it")
    dir_add(dir_matrix,0,16,15,"At the crossroads, the parcel office is to your right")
    dir_add(dir_matrix,0,16,17,"The East Accomodaion Centre will be to your left")

    #17
    dir_add(dir_matrix,17,16,0,"Go up the path that keeps Woodland Court to your right")
    dir_add(dir_matrix,17,18,0,"Go down the path that goes between The Quads buildings and cross to the other side of the car park")
    dir_add(dir_matrix,17,29,0,"Go thought the path opposite the East Accomodation Centre that goes between The Quads and Solsbury Court")
    dir_add(dir_matrix,17,16,15,"Take a left when you reach the Eastwood buildings")
    dir_add(dir_matrix,17,18,19,"When you reach the opposite side of the car park, take a right")
    dir_add(dir_matrix,17,29,21,"when you reach the other side of the path, follow it past the Lime Tree, and then take a left")
    dir_add(dir_matrix,17,29,28,"When you reach the other side of the path keep following it and take a right when you reach a T junction.")
    dir_add(dir_matrix,0,17,16,"If you follow the path you'll reach Eastwood")
    dir_add(dir_matrix,0,17,18,"At the other side of the car park, you will reach some sport courts")
    dir_add(dir_matrix,0,17,29,"At the other side of the path the Lime Tree is to your left")

    #18
    dir_add(dir_matrix,18,17,0,"Follow the main road that crosses along the east car park to the accomodation")
    dir_add(dir_matrix,18,19,0,"Keep the car park to your right as you walk alongside it")
    dir_add(dir_matrix,18,17,16,"When you reach the East Accomodation Centre keep walking straight on")
    dir_add(dir_matrix,18,17,29,"When you reach the East Accomodation Centre take the left that goes between The Quads and Solsbury court")
    dir_add(dir_matrix,18,19,20,"Keep walking past the East building, following the path")
    dir_add(dir_matrix,0,18,17,"Soon you'll find the East Accomodation Centre")
    dir_add(dir_matrix,0,18,19,"The East Building will be on your left")

    #19
    dir_add(dir_matrix,19,18,0,"Walk along the car park, keeping it to your left")
    dir_add(dir_matrix,19,20,0,"Walk along the car park, keeping it to your right")
    dir_add(dir_matrix,19,18,17,"Turn left when and cross the car park, and follow the road")
    dir_add(dir_matrix,19,20,14,"At the road, take a left")
    dir_add(dir_matrix,19,20,21,"At the road, cross to your right")
    dir_add(dir_matrix,0,19,18,"Following the road, you'll reach the overflow car park")
    dir_add(dir_matrix,0,19,20,"The Edge will soon be on your left")

    #20
    dir_add(dir_matrix,20,14,0,"Walk along the road, keeping it to your right")
    dir_add(dir_matrix,20,19,0,"Follow the road, keeping the edge to your right")
    dir_add(dir_matrix,20,21,0,"Cross the road that goes to the bus stops, and then take the first left to cross to the U1 bus stop")
    dir_add(dir_matrix,20,14,13,"On the right there will be a path, take it and then cross the road")
    dir_add(dir_matrix,20,19,18,"Once at the East Building, keep following the road")
    dir_add(dir_matrix,20,21,3,"When by the parade, go under it until you reach the other side")
    dir_add(dir_matrix,20,21,27,"When by the bus stop, take the stairs up to the parade")
    dir_add(dir_matrix,20,21,29,"When at the bust stop, take a right and walk through and past the stairs")
    dir_add(dir_matrix,0,20,14,"The Sports Training Village entrance will be on your left")
    dir_add(dir_matrix,0,20,19,"East Building will be on your right")
    dir_add(dir_matrix,0,20,21,"The bus stop will be on your left")

    #21
    dir_add(dir_matrix,21,3,0,"Go under the parade, to the other side")
    dir_add(dir_matrix,21,20,0,"Go away from the parade, and then take a right and follow the road, and cross it to reach The Edge")
    dir_add(dir_matrix,21,27,0,"Walk up the stairs to the parade")
    dir_add(dir_matrix,21,29,0,"Move away from the bus stop, and through the green")
    dir_add(dir_matrix,21,3,1,"When out of the parade, walk along the road and through to the car park")
    dir_add(dir_matrix,21,3,2,"When out of the parade, cross to the right side and go up the slope/hill")
    dir_add(dir_matrix,21,3,10,"When out of the parade follow the path on the left, and go down the stairs between 10W and 8W. Then go up the stone stairs and take the left, then the right at the crossroads. You'll reach the crossroads by the lake")
    dir_add(dir_matrix,21,20,14,"Follow the road, keeping it on your right")
    dir_add(dir_matrix,21,20,19,"Go through the east car park")
    dir_add(dir_matrix,21,27,26,"Pass under Norwood House")
    dir_add(dir_matrix,21,29,17,"Take the path beside the Lime Tree that is next to Solsbury Court")
    dir_add(dir_matrix,21,29,28,"Walk to the left side of the green where the Chancellors' Building is")
    dir_add(dir_matrix,0,21,3,"When at the other side of the parade 8W and 10W are along the path to your left")
    dir_add(dir_matrix,0,21,20,"The Edge is on the other side of the road")
    dir_add(dir_matrix,0,21,27,"The SU is at the right, at the top of the stairs")
    dir_add(dir_matrix,0,21,29,"The Lime Tree is on the right")

    #22
    dir_add(dir_matrix,22,10,0,"Start by facing the lake, and then take the path to the right that goes beside the Univeristy Hall. Then take a left and follow the path until you reach the crossroads by the lake")
    dir_add(dir_matrix,22,11,0,"Start by facing the lake then follow the path to the left. Once behind 2E, take a right and follow the path down the crossroads by the lake")
    dir_add(dir_matrix,22,24,0,"Take the elevator")
    dir_add(dir_matrix,22,25,0,"Go up the stairs to the parade")
    dir_add(dir_matrix,22,10,3,"At the crossroads, go right and follow the path until you reach a crossroads, then take a left. Then take the first right down the rocky stairs, and up the stairs immediately after")
    dir_add(dir_matrix,22,10,9,"At the crossroads go straight on down by 1S and 3S")
    dir_add(dir_matrix,22,10,11,"22->10->11 X")
    dir_add(dir_matrix,22,11,10,"22->11->10 X")
    dir_add(dir_matrix,22,11,12,"At the crossroads go straight on to the Milner Centre")
    dir_add(dir_matrix,22,11,13,"At the crossroads, take a left through the path flanked by trees")
    dir_add(dir_matrix,22,24,23,"When exiting the cafe, take a left and walk along the parade")
    dir_add(dir_matrix,22,24,25,"When out of the cafe, take a right and walk along the parade")
    dir_add(dir_matrix,22,25,8,"When up the stairs, walk forwards to the library and down the path that's between the Library and 1W")
    dir_add(dir_matrix,22,25,26,"When up the stairs, take a right and follow the parade")
    dir_add(dir_matrix,22,25,30,"When at the top of the stairs, go towards the library and go down the path that's between the library and 1E")
    dir_add(dir_matrix,0,22,10,"At the crossroads, you'll be by the lake with S3 and S1 beside you")
    dir_add(dir_matrix,0,22,11,"Once at the crossroads you you have arrived")
    dir_add(dir_matrix,0,22,24,"Take the elevator")
    dir_add(dir_matrix,0,22,25,"At the top of the stairs, the library will be in front of you")

    #23
    dir_add(dir_matrix,23,3,0,"Start by walking away from Wessex House on the 8W side of the parade, and walk down the slope")
    dir_add(dir_matrix,23,24,0,"Go through Wessex House")
    dir_add(dir_matrix,23,3,1,"When at the bottomn of the slope, follow the road and go through the car park")
    dir_add(dir_matrix,23,3,2,"When at the bottom of the slope, take a right and go up the hill/slope")
    dir_add(dir_matrix,23,3,10,"When at the bottom of the slope follow the path to the left and go down the stairs, then up the rocky stairs. Then take a left. When you reach the crossroads, take a right and follow it to the crossroads by the lake")
    dir_add(dir_matrix,23,3,21,"When at the bottom of the slope, take a right and go around the building to be by the road. Go under the parade")
    dir_add(dir_matrix,23,24,22,"Take the elevator")
    dir_add(dir_matrix,23,24,25,"Keep walking along the parade towards the library")
    dir_add(dir_matrix,0,23,3,"At the bottom of the slope 10W is to your left")
    dir_add(dir_matrix,0,23,24,"When through Wessex House, Fresh if to your left, and 1-4W are all in front of you (1W & 3W to your left and 2W & 4W on your right)")

    #24
    dir_add(dir_matrix,24,22,0,"Take the elevator")
    dir_add(dir_matrix,24,23,0,"Go along the parade, keeping 2W & 4W to your left")
    dir_add(dir_matrix,24,25,0,"Go along the parade, towards the library")
    dir_add(dir_matrix,24,22,10,"Take the elevator")
    dir_add(dir_matrix,24,22,11,"Take the elevator")
    dir_add(dir_matrix,24,22,25,"24->22->25 X")
    dir_add(dir_matrix,24,23,3,"When through Wessex House, make sure you're on the left side of the parade and walk down the slope")
    dir_add(dir_matrix,24,25,8,"Just before the library, take a left down the path between the library and 1W")
    dir_add(dir_matrix,24,25,22,"Take a right down the stairs to the lake")
    dir_add(dir_matrix,24,25,26,"Walk past the library towards Norwood")
    dir_add(dir_matrix,0,24,22,"Take the elevator")
    dir_add(dir_matrix,0,24,23,"Once through Wessex House, the entrance to 8W is to your left")
    dir_add(dir_matrix,0,24,25,"The library is on your left")

    #25
    dir_add(dir_matrix,25,8,0,"Start by going down the path between 1W and the library")
    dir_add(dir_matrix,25,22,0,"Start by going down the stairs opposite the library")
    dir_add(dir_matrix,25,24,0,"Have the library on your right, then walk along the parade")
    dir_add(dir_matrix,25,26,0,"Have the library to your left, then walk along the library")
    dir_add(dir_matrix,25,30,0,"Take the path between the library and 1E")
    dir_add(dir_matrix,25,8,7,"When you reach the road, turn left and follow it")
    dir_add(dir_matrix,25,8,15,"When you reach the road, turn right and follow it")
    dir_add(dir_matrix,25,8,30,"25->8->30 X")
    dir_add(dir_matrix,25,22,10,"When at the bottom of the stairs, turn right and walk past University Hall. Then turn left and walk until you reach the crossroads by the lake")
    dir_add(dir_matrix,25,22,11,"When at the bottom of the lake, take the path along the left. When behind 2E, turn right and move until you find the crossroads")
    dir_add(dir_matrix,25,22,24,"25->22->24 X")
    dir_add(dir_matrix,25,24,22,"Take the elevator")
    dir_add(dir_matrix,25,24,23,"Walk through Wessex Building")
    dir_add(dir_matrix,25,26,27,"Walk under Norwood House")
    dir_add(dir_matrix,25,30,8,"25->30->8 X")
    dir_add(dir_matrix,0,25,8,"When you reach the road, Brendon Court is on the other side")
    dir_add(dir_matrix,0,25,22,"At the bottomn of the stairs is the lake")
    dir_add(dir_matrix,0,25,24,"Fresh is to your right, 1-4W are around you with 1W & 3W on your right and 2W & 4W on your left")
    dir_add(dir_matrix,0,25,26,"1E is to your left and Norwood House is adjacent")
    dir_add(dir_matrix,0,25,30,"3E is to your right, as is the back entrance to the Chancellors' Building")

    #26
    dir_add(dir_matrix,26,25,0,"Start by moving away from Norwood House")
    dir_add(dir_matrix,26,27,0,"Start by going under Norwood House")
    dir_add(dir_matrix,26,25,8,"When you reach the library, there is a path to your right, go down it")
    dir_add(dir_matrix,26,25,22,"Take a left down the stairs towards the lake")
    dir_add(dir_matrix,26,25,24,"Keep following the parade")
    dir_add(dir_matrix,26,25,30,"Just before the library, there is a path to the right, go down it")
    dir_add(dir_matrix,26,27,21,"Go down the stairs at the end of the parade, or use the elevator")
    dir_add(dir_matrix,0,26,25,"The library is on your right")
    dir_add(dir_matrix,0,26,27,"The SU is to your left")

    #27
    dir_add(dir_matrix,27,21,0,"Start by moving down the stairs or taking the elevator")
    dir_add(dir_matrix,27,26,0,"Start by going under Norwood House")
    dir_add(dir_matrix,27,21,3,"At the bottom of the parade, turn to go under it")
    dir_add(dir_matrix,27,21,20,"At the bottom of the parade, walk forward & then take a right to follow the road towards The Edge. Cross the road")
    dir_add(dir_matrix,27,21,29,"At the bottom of the parade, turn left and go through to the green area")
    dir_add(dir_matrix,27,26,25,"Keep going towards the library")
    dir_add(dir_matrix,0,27,21,"At the bottom of the parade, the U1 bus stop is right in front of you")
    dir_add(dir_matrix,0,27,26,"1E is to your right and 2E to your left")

    #28
    dir_add(dir_matrix,28,15,0,"Walk along the path next to the Chancellors' Building keeping it to your left")
    dir_add(dir_matrix,28,29,0,"Walk to the Lime Tree on the opposite side of the green")
    dir_add(dir_matrix,28,15,8,"When at the road, turn left and follow it")
    dir_add(dir_matrix,28,15,16,"When at the road, turn right and follow it")
    dir_add(dir_matrix,28,29,17,"When at the Lime Tree go down the path between it and Solsbury Court")
    dir_add(dir_matrix,28,29,21,"Follow the path down, until you reach the bus stop")
    dir_add(dir_matrix,0,28,15,"Once you reach the road, the parcel office is on the other side")
    dir_add(dir_matrix,0,28,29,"The Lime Tree is infront of you")

    #29
    dir_add(dir_matrix,29,17,0,"Start by going down the path between the Lime Tree and Solsbury Court")
    dir_add(dir_matrix,29,21,0,"Start by walking down the path accross the green, and then take a left and cross the road to the bus stop")
    dir_add(dir_matrix,29,28,0,"Cross the green and walk towards the Chancellors' Building")
    dir_add(dir_matrix,29,17,16,"Take a left once you reach the road")
    dir_add(dir_matrix,29,17,18,"Once you reach the road, turn right")
    dir_add(dir_matrix,29,21,3,"Turn right, and go under the parade to the other side")
    dir_add(dir_matrix,29,21,20,"At the bus stop take a left, and cross the road. Then take a right and follow the path, and cross the road again")
    dir_add(dir_matrix,29,21,27,"Turn right and go up the parade eithe rby the leevator or stairs")
    dir_add(dir_matrix,29,28,15,"When at the Chancellors' Building, keep walking up along the path keeping the Chancellors' Building to your left")
    dir_add(dir_matrix,0,29,17,"When you reach the road, the East Accomodation building is in front of you")
    dir_add(dir_matrix,0,29,21,"The U1 stop is infront of you")
    dir_add(dir_matrix,0,29,28,"The Chancellors' building is on your left")

    #30
    dir_add(dir_matrix,30,8,0,"Go down the path to your right")
    dir_add(dir_matrix,30,25,0,"Go down the path to your left")
    dir_add(dir_matrix,30,8,7,"When you reach the road take a left")
    dir_add(dir_matrix,30,8,15,"When you reach the road take a right")
    dir_add(dir_matrix,30,8,25,"30->8->25 X")
    dir_add(dir_matrix,30,25,8,"30->25->8 x")
    dir_add(dir_matrix,30,25,22,"Go straight ahead and down the stairs")
    dir_add(dir_matrix,30,25,24,"When you reach the parade, turn right")
    dir_add(dir_matrix,30,25,26,"When you reach the parade, turn left")


    return dis_matrix,lev_matrix,note_matrix,dir_matrix

########## END OF MAP CREATION ##########

########## START OF DIJKSTRA ##########

def shortest_path(start,end,user_access, reported):
    # Assuming node numbers entered start at 1
    start = start - 1
    end = end - 1
    
    width, height = 30, 30

    dis_matrix=[[0 for x in range(width)] for y in range(height)]#meters
    lev_matrix=[[0 for x in range(width)] for y in range(height)]#1-5
    note_matrix=[[0 for x in range(width)] for y in range(height)]#array of notes if any
    dir_matrix=[[0 for x in range(width)] for y in range(height)]
    for row in range(0,width):
        for col in range(0,height):
            dir_matrix[row][col]=[]
    make_graph(dis_matrix,lev_matrix,note_matrix,dir_matrix)
        
    visited=[0 for x in range(len(dis_matrix))]#0 if not visited, 1 if visited
    distance=[0 for x in range(len(dis_matrix))]#sets an array that checks is a node has been visited yet, 1000000 for no, and any number is how far that one it
    current_shortest = [0 for x in range(len(dis_matrix))]#holds a tupple includng total legth to node poining at, and the node that points to the node in that position
    pointers=[0 for x in range(len(dis_matrix))]#shows which node points to it?
    for i in range(0,len(current_shortest)):
        current_shortest[i]=(0,0)

    for i in range(0,len(reported)):
        dis_matrix[reported[i][0]-1][reported[i][1]-1]=0
        dis_matrix[reported[i][1]-1][reported[i][0]-1]=0

    position=start
    visited[start]=1
    
    solved=False
    looping=True
    while looping:
        prev_position=position
        all_surrounding_nodes(dis_matrix,position,current_shortest,distance,visited,lev_matrix,user_access)
        position = visit_shortest_node(current_shortest,distance,pointers,visited)

        if visited[end]!=0:
            solved=True
            looping=False

        if position==prev_position:
            looping=False

    if solved==True:
        route = calculate_route(pointers,start,end)
        directions = calculate_directions(route, dir_matrix)

        for i in range(0,len(route)):
            route[i]=route[i]+1

        #print("route", route)
        #print("directions", directions)

        estimatedDistance = distance[end] # in metres
        estimatedTime = (int(distance[end])/100)*1.2 # in minutes

        return {"estimatedDistance":estimatedDistance, "estimatedTime":estimatedTime, "route":route, "directions":directions}
    else:
        #print("Impossible")
        return None
        
def calculate_directions(route, direction_graph):
    directon_arr=[]
    for i in range(0,len(route)):
        if i-1>=0 and i+1 <len(route):
            arr = direction_graph[route[i-1]][route[i]]
            for e in range(0,len(arr)):
                if arr[e]==route[i+1]:
                    directon_arr.append(arr[e+1])
        elif i==0:
            directon_arr.append(direction_graph[route[i]][route[i+1]][0])
        else:
            directon_arr.append(direction_graph[route[i-1]][route[i]][len(direction_graph[route[i-1]][route[i]])-1])

    return directon_arr
        
def all_surrounding_nodes(graph,position,current_shortest,distance,visited,access_graph,user_access):
    for i in range(0,len(graph)):
        if graph[position][i]!=0 and (distance[position]+graph[position][i]<current_shortest[i][0] or current_shortest[i][0]==0) and visited[i]==0 and access_graph[position][i]<=user_access:
            current_shortest[i]=(distance[position]+graph[position][i],position)

def visit_shortest_node(current_shortest,distance,pointers,visited):
    shortest=100000
    position=0
    for i in range(0,len(current_shortest)):
        if current_shortest[i][0]<shortest and current_shortest[i][0]!=0:
            shortest=current_shortest[i][0]
            position=i

    visited[position]=1
    distance[position]=shortest
    pointers[position]=current_shortest[position][1]
    current_shortest[position]=(0,0)

    return position

def calculate_route(pointers,start,end):
    route=[end]
    current_node=end
    while route[len(route)-1]!=start:
        route.append(pointers[current_node])
        current_node=pointers[current_node]
    flipped_route=[0 for x in range(len(route))]
    for i in range(0,len(route)):
        flipped_route[i]=route[len(route)-(i+1)]
    return flipped_route

########## END OF DIJKSTRA ##########

