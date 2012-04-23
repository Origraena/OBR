#!/bin/sh
if [ ! -f target/main.pdf ]; then
	echo 'Run build.sh first.'
fi
mv target/main.pdf target/outils_analyse_base_regles.pdf

