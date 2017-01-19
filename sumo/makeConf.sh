#!/bin/bash

# makeConf.sh <netType>
# <netType>: network Type> grid, spider, random

netType=$1

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
