#!/bin/sh

#-----------------------------------------
system=MicrowaveOvenSoftware/PLAMutation
reference="1304.0 0.126"

echo "\n$system"
FILES=./$system/HYPERVOLUME.txt
for f in $FILES
do
	echo "Processing $f"
	echo $f >> ./$system/HYPERVOLUME_RESULT.txt
	./hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
	echo "\n" >> ./$system/HYPERVOLUME_RESULT.txt
done


#-----------------------------------------
system=MicrowaveOvenSoftware/PLAMutationWithPatterns
reference="1304.0 0.126"

echo "\n$system"
FILES=./$system/HYPERVOLUME.txt
for f in $FILES
do
	echo "Processing $f"
	echo $f >> ./$system/HYPERVOLUME_RESULT.txt
	./hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
	echo "\n" >> ./$system/HYPERVOLUME_RESULT.txt
done