#include <iostream>
#include <stdlib.h>
#include <time.h>
#include <string>

char randomLetter() {
	return (char) (rand()%26)+'a';
}

int main(int argc, char** argv) {
	srand(time(0));

	if (argc <= 8)
		return -1;

	int nbRules, nbPredicates, nbTerms, maxArity, maxNbAtoms;
	int cstProba = 50;
	int termSize = 3;
	int predicateSize = 2;
	std::string predicate_open = "<";
	std::string predicate_close = ">";
	std::string term_separator = ",";
	std::string atom_separator = ";";
	std::string head_separator = "-->";

	nbRules = atoi(argv[1]);
	nbPredicates = atoi(argv[2]);
	nbTerms = atoi(argv[3]);
	maxArity = atoi(argv[4]);
	maxNbAtoms = atoi(argv[5]);
	predicateSize = atoi(argv[6]);
	termSize = atoi(argv[7]);
	cstProba = atoi(argv[8]);

	if ((nbRules <= 0) || (nbPredicates <= 0) || (nbTerms <= 0) || (maxArity <= 0) || (maxNbAtoms <= 0))
		return -1;

	std::string terms[nbTerms];
	std::string predicates[nbPredicates];
	int predicatesArity[nbPredicates];
	int predicateID, nbAtoms;
	bool cst = false;

	// terms
	for (int i = 0 ; i < nbTerms ; i++) {
		if (rand()%100 <= cstProba)
			cst = true;
		else
			cst = false;
		if (cst)
			terms[i] += '\'';
		for (int l = 0 ; l < termSize ; l++)
			terms[i] += randomLetter();
		if (cst)
			terms[i] += '\'';
	}

	// predicates arity
	for (int i = 0 ; i < nbPredicates ; i++) {
		predicatesArity[i] = rand()%maxArity + 1;
		for (int l = 0 ; l < predicateSize ; l++) {
			predicates[i] += randomLetter();
		}
	}

	for (int i = 0 ; i < nbRules ; i++) {
		nbAtoms = rand()%maxNbAtoms + 2;
		// hypotheses
		for (int j = 0 ; j < nbAtoms-1 ; j++) {
			predicateID = rand()%nbPredicates;
			std::cout << predicates[predicateID] << predicate_open;
			for (int k = 0 ; k < predicatesArity[predicateID]-1 ; k++) {
				std::cout << terms[rand()%nbTerms] << term_separator;
			}
			std::cout << terms[rand()%nbTerms] << predicate_close;
			if (j != nbAtoms-2)
				std::cout << atom_separator;
		}
		// head
		std::cout << head_separator;
		predicateID = rand()%nbPredicates;
		std::cout << predicates[predicateID] << predicate_open;
		for (int k = 0 ; k < predicatesArity[predicateID]-1 ; k++) {
			std::cout << terms[rand()%nbTerms] << term_separator;
		}
		std::cout << terms[rand()%nbTerms] << predicate_close;
		std::cout << std::endl;
	}

	return 1;

}

