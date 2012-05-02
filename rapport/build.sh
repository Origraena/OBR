#!/bin/sh
if [ ! -d target ]; then
	mkdir target
fi
pdflatex \
-output-directory target/ \
src/main.tex

if [ -f target/main.pdf ]; then
	bibtex target/main.aux
	pdflatex -output-directory target/ src/main.tex
	pdflatex -output-directory target/ src/main.tex
fi

