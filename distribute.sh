echo Removing distribute/
rm -rf distribute/
echo Making distribute/
mkdir distribute
echo Executing sbt
sbt assembly
echo Copying files
ls target/scala-2.11 -t | grep assembly | head -1 | xargs -r -I {} cp target/scala-2.11/{} distribute/variousminos.jar
cp -r lib/ distribute/lib/
cp -r res/ distribute/res/
rm -fv `find distribute/res/ -name *.xcf -type f`
rm -fv `find distribute/res/ -name *.bmp -type f`
cp -r stage/ distribute/stage/
cp -r save/ distribute/save/
cp -r doc/ distribute/doc/
cp *.bat distribute/
cp *.sh distribute/
cp *.md distribute/
cp *.txt distribute/
echo Finished
