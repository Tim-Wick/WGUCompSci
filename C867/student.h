#ifndef STUDENT_H
#define STUDENT_H

#include <string>
#include "degree.h"
using namespace std;

class Student {
public:
	// Constructors
	Student();
	Student(string studentId, string firstName, string lastName, string emailAddress, int age, int numDays[3], DegreeProgram degreeProgramm);

	// Setters
	void SetStudentId(string studentId);
	void SetFistName(string firstName);
	void SetLastName(string lastName);
	void SetEmailAddress(string emailAddress);
	void SetAge(int age);
	void SetNumDays(int numDays[3]);
	void setDegreeProgram(DegreeProgram degreeProgram);

	// Getters
	string GetStudentId() const;
	string GetFirstName() const;
	string GetLastName() const;
	string GetEmailAddress() const;
	int GetAge() const;
	int* GetNumDays();
	DegreeProgram GetDegreeProgram() const;
	
	// Print function
	void Print() const;

private:
	// Private variables
	string studentId;
	string firstName;
	string lastName;
	string emailAddress;
	int age;
	int numDays[3];
	DegreeProgram degreeProgram;
	// Private helper function
	string printDegreeProgram(DegreeProgram degreeProgram) const;
};

#endif
