SRC_PATH='./src:/home/swan/prog/git/moca/common/graphs/java/src'
#PACKAGES='obr moca.graphs moca.operators moca.comparators moca.lists moca.graphs.edges moca.graphs.vertices'
PACKAGES='obr'
javadoc \
-d target/doc \
-sourcepath $SRC_PATH \
-subpackages $PACKAGES \
-private \
-use \
-windowtitle "Base Rule Analyser" \
-doctitle "<b>B</b>ase <b>R</b>ule <b>A</b>nalyser" \
-header "OBR" \
-bottom "Hadrien Negros, Swan Rocher, Universite Montpellier 2, 2012" 
