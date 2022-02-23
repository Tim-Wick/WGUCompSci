#ifndef ROSTER_H
#define ROSTER_H

#include "degree.h"
#include "student.h"

using namespace std;

class Roster {
public:
	Roster();
	Student** classRosterArray;
	void add(string studentID, string firstName, string lastName, string emailAddress, int age, int daysInCourse1, int daysInCourse2, int daysInCourse3, DegreeProgram degreeprogram);
	void remove(string StudentID);
	void printAll();
	void printAverageDaysInCourse(string studentID);
	void printInvalidEmails();
	void printByDegreeProgram(DegreeProgram degreeProgram);
	~Roster();
private:
	int studentRosterLength;
	string printDegreeProgram(DegreeProgram degreeProgram) const;
	DegreeProgram getDegreeProgram(string degreeProgram) const;
};

#endif
