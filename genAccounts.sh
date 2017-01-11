#!/bin/bash

# ethcluster.sh 
# <root dir>: root directory for ethereum cluster
# <num of node>: the number of ethereum nodes to make
root=$1
if [ ! -d "$root" ]; then
	mkdir root # make eth cluster root directory
fi
shift
N=$1

# generate master account
m_account=$(geth --datadir ~/$root/data/master --password <(echo -n master) account new)
echo "Master,"${m_account:10:40} >> $root/accounts	# substring func = {var:startIdx:endIdx}

# generate cluster nodes' account
for ((i=0;i<N;++i)); do
	
	# account id 00, 01, 02...
	dd=$(printf "%02d" $i)		
	
	# save the account of nodes
	n_account=$(geth --datadir $root/data/$dd --password <(echo -n $dd) account new)
	
	echo "Node"$dd,${n_account:10:40} >> $root/accounts

done