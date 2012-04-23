#!/bin/sh
if [ ! -d target ]; then
	mkdir target
fi
pdflatex \
-output-directory target/ \
src/main.tex
