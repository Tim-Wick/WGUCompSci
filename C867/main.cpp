#include <iostream>
#include "roster.h"
#include "degree.h"
#include "student.h"
using namespace std;

int main() {
	cout << "Scripting and Programming - Applications – C867" << endl << "Language: C++" << endl << "Tim Wick - 001482822" << endl;
	Roster classRoster;
	cout << "Printing all students:" << endl;
	classRoster.printAll();

	for (int i = 0; i < 5; ++i) {
		classRoster.printAverageDaysInCourse(classRoster.classRosterArray[i]->GetStudentId());
	}

	cout << endl << "Printing invalid email addresses:" << endl;
	classRoster.printInvalidEmails();
	classRoster.printByDegreeProgram(DegreeProgram::SOFTWARE);
	cout << "Removing student with ID A3" << endl;
	classRoster.remove("A3");
	cout << "Printing all students:" << endl;
	classRoster.printAll();
	cout << "Removing student with ID A3 again:" << endl;
	classRoster.remove("A3");
	cout << endl << "Releasing memory allocated by Roster.";
	classRoster.~Roster();
	return 0;
};
