#!/bin/sh

#-----------------------------------------
system=experiment/MicrowaveOvenSoftware/PLAMutation
reference="567.1 0.225"

echo "$system"
FILES=./$system/HYPERVOLUME.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"

#-----------------------------------------
system=experiment/MicrowaveOvenSoftware/PLAMutationWithPatterns
reference="567.1 0.225"

echo "$system"
FILES=./$system/HYPERVOLUME.txt
for f in $FILES
do
	echo "Processing $f"
	./experiment/hv-1.3-src/hv -r "$reference" $f >> ./$system/HYPERVOLUME_RESULT.txt
done
echo "\n"
