#!/bin/bash

# createNet.sh <type>
# <type>: grid, spider, random

netType=$1

mapDir="map/"$netType
if [ ! -d $mapDir ]; then
	mkdir -p $mapDir
fi

if [ $netType == "grid" ]; then
	netcmd="netgenerate --grid --grid.number=5 --grid.length=100 --output-file=./map/${netType}/${netType}.net.xml"
	routecmd="python /usr/local/share/sumo/tools/randomTrips.py -n ./map/${netType}/${netType}.net.xml -e 180 -r ./map/${netType}/${netType}.rou.xml --trip-attributes=departLane=\"best\" departSpeed=\"random\" departPos=\"random\" --intermediate 10 -p 5 -o ./map/${netType}/${netType}.trips.xml"		
elif [ $netType == "spider" ]; then
	netcmd="netgenerate --spider --arms=5 --circles=3 --radius=100 --output-file=./map/${netType}/${netType}.net.xml"
	routecmd="python /usr/local/share/sumo/tools/randomTrips.py -n ./map/${netType}/${netType}.net.xml -e 180 -r ./map/${netType}/${netType}.rou.xml --trip-attributes=departLane=\"best\" departSpeed=\"random\" departPos=\"random\" --intermediate 10 -p 5 -o ./map/${netType}/${netType}.trips.xml"		
elif [ $netType == "random" ]; then
	netcmd="netgenerate --rand --rand.iterations=30 --output-file=./map/${netType}/${netType}.net.xml"
	routecmd="python /usr/local/share/sumo/tools/randomTrips.py -n ./map/${netType}/${netType}.net.xml -e 180 -r ./map/${netType}/${netType}.rou.xml --trip-attributes=departLane=\"best\" departSpeed=\"random\" departPos=\"random\" --intermediate 10 -p 5 -o ./map/${netType}/${netType}.trips.xml"		

fi


echo -n '1.Making '$netType 'network xml file result: '
$netcmd
echo -n '2.Making '$netType 'route xml file result: '
$routecmd
echo '3.Making '$netType 'cfg file...'
./makeConf.sh $netType
echo '4. Running sumo...'
sumo-gui -c ./map/$netType/$netType.sumocfg