rootdir="/Users/tony/development/AndroidStudioProjects/Monopoly/design-assets/exports/Space"
collectdir="/Users/tony/development/AndroidStudioProjects/Monopoly/design-assets/exports/Space/collected"

folder1="Card/Chance"
folder2="Card/Community Chest"
folder3="Corner"
folder4="Property"
folder5="Railroad"
folder6="Tax"
folder7="Utility"

# tag community chest and chance files before copying
cd "$rootdir/$folder1"
for f in *; do mv "$f" "chance_$f" ; done
cd "$rootdir/$folder2"
for f in *; do mv "$f" "cchest_$f" ; done
cd "$rootdir"

# just copy everything else
cp -R $rootdir/$folder1/. $collectdir/
cp -R $rootdir/"$folder2"/. $collectdir/
cp -R $rootdir/$folder3/. $collectdir/
cp -R $rootdir/$folder4/. $collectdir/
cp -R $rootdir/$folder5/. $collectdir/
cp -R $rootdir/$folder6/. $collectdir/
cp -R $rootdir/$folder7/. $collectdir/

cd $collectdir

# map filenames to their lowercase variety, append "tile_" prefix, and replace spaces with underscores in filenames
for name in *; do mv "$name" "${name// /_}"; done
for i in *; do mv $i `echo $i | tr [:upper:] [:lower:]`; done
for f in *; do mv "$f" "tile_$f" ; done
