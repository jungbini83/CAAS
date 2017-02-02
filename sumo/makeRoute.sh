#!/bin/bash

# makeRoute.sh <type> [options]
# <type>: grid, spider, random

if [ "$#" -eq 1 ]; then
	netType=$1
	shift
	routeoption="-e 180 --intermediate 10 -p 7 -b 0.1"		

elif [ "$#" > 1 ]; then
	netType=$1
	shift
	routeoption="$*"		
else
	"USAGE: makeRoute.sh <type> [options]"
	exit 1
fi

routecmd="python /usr/local/share/sumo/tools/randomTrips.py -n ./map/${netType}/${netType}.net.xml -r ./map/${netType}/${netType}.rou.xml --trip-attributes=departLane=\"best\" departSpeed=\"random\" departPos=\"random\" -o ./map/${netType}/${netType}.trips.xml "${routeoption}

echo -n '1. Making '$netType 'route xml file result: '
$routecmd
echo '2. Making '$netType 'cfg file...'
echo "<configuration>
	<input>
		<net-file value='${netType}.net.xml'/>
		<route-files value='${netType}.rou.xml'/>
		<gui-settings-file value='${netType}.settings.xml'/>
	</input>
	<time>
		<begin value='0'/>
		<end value='1800'/>
	</time>
</configuration>" > ./map/$netType/$netType.sumocfg

echo "<viewsettings>
	<viewport y='200' x='200' zoom='90'/>
	<delay value='1000'/>
</viewsettings>" > ./map/$netType/$netType.settings.xml
