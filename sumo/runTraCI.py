import os
import sys
import optparse
import subprocess
import random

# we need to import python modules from the $SUMO_HOME/tools directory
try:
    sys.path.append(os.path.join(os.path.dirname(
        __file__), '..', '..', '..', '..', "tools"))  # tutorial in tests
    sys.path.append(os.path.join(os.environ.get("SUMO_HOME", os.path.join(
        os.path.dirname(__file__), "..", "..", "..")), "tools"))  # tutorial in docs
    from sumolib import checkBinary
except ImportError:
    sys.exit(
        "please declare environment variable 'SUMO_HOME' as the root directory of your sumo installation (it should contain folders 'bin', 'tools' and 'docs')")

import traci

def run():
	"""execute the TraCI control loop"""
	runningVehicleSet=set()
	while traci.simulation.getMinExpectedNumber() > 0:
		for veh_id in traci.vehicle.getIDList():			
			print veh_id + '\'s positions are ' + str(traci.vehicle.getPosition(veh_id)[0]) + ' and ' + str(traci.vehicle.getPosition(veh_id)[1])

			if not veh_id in runningVehicleSet:
				print '#######################################################################################'
				os.system("../ethereum/runEthNode.sh " + veh_id)
				print '#######################################################################################'

			runningVehicleSet.add(veh_id)
		
		traci.simulationStep()               
	traci.close()
	sys.stdout.flush()

def get_options():
    optParser = optparse.OptionParser()
    optParser.add_option("--nogui", action="store_true",
                         default=False, help="run the commandline version of sumo")
    options, args = optParser.parse_args()
    return options

# this is the main entry point of this script
if __name__ == "__main__":
	options = get_options()

    # this script has been called from the command line. It will start sumo as a
    # server, then connect and run
	if options.nogui:
		sumoBinary = checkBinary('sumo')
	else:
		sumoBinary = checkBinary('sumo-gui')	

	print '#######################################################################################'
	os.system("../ethereum/runMasterNode.sh ethereum")
	print '#######################################################################################'

	traci.start([sumoBinary, "-c", "./map/grid/grid.sumocfg")

	run()