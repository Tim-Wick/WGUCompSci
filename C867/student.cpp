#include <string>
#include <iostream>
#include "degree.h"
#include "student.h"
using namespace std;

// Student class constructors
Student::Student() {
	studentId = "A0";
	firstName = "No";
	lastName = "One";
	emailAddress = "noone@nothing.com";
	age = 0;
	for (int i = 0; i < 3; ++i) {
		numDays[i] = 0;
	};
	degreeProgram = DegreeProgram::SOFTWARE;
};

Student::Student(string studentId, string firstName, string lastName, string emailAddress, int age, int numDays[3], DegreeProgram degreeProgram) {
	this->studentId = studentId;
	this->firstName = firstName;
	this->lastName = lastName;
	this->emailAddress = emailAddress;
	this->age = age;
	for (int i = 0; i < 3; ++i) {
		this->numDays[i] = numDays[i];
	};
	this->degreeProgram = degreeProgram;
};

// Setters
void Student::SetStudentId(string studentId) {
	this->studentId = studentId;
};

void Student::SetFistName(string firstName) {
	this->firstName = firstName;
}

void Student::SetLastName(string lastName) {
	this->lastName = lastName;
};

void Student::SetEmailAddress(string emailAddress) {
	this->emailAddress = emailAddress;
};

void Student::SetAge(int age) {
	this->age = age;
};

void Student::SetNumDays(int numDays[3]) {
	for (int i = 0; i < 3; ++i) {
		this->numDays[i] = numDays[i];
	};
};

void Student::setDegreeProgram(DegreeProgram degreeProgram) {
	this->degreeProgram = degreeProgram;
};

// Getters
string Student::GetStudentId() const {
	return this->studentId;
};

string Student::GetFirstName() const {
	return this->firstName;
};

string Student::GetLastName() const {
	return this->lastName;
};

string Student::GetEmailAddress() const {
	return this->emailAddress;
};

int Student::GetAge() const {
	return this->age;
};

int* Student::GetNumDays() {
	return this->numDays;
}

DegreeProgram Student::GetDegreeProgram() const {
	return this->degreeProgram;
};

// Print function
void Student::Print() const {
	cout << studentId << "   First Name : " << firstName << "   Last Name : " << lastName << "	Age : " << age << "   daysInCourse : {"; 
	for (int i = 0; i < 2; ++i) {
		cout << numDays[i] << ", ";
	};
	cout << numDays[2];
	cout << "} DegreeProgram : " << printDegreeProgram(degreeProgram) << endl;
};

// Helpter function to get a string for a DegreeProgram
string Student::printDegreeProgram(DegreeProgram degreeProgram) const {
	switch (degreeProgram) {
		case DegreeProgram::SECURITY:
			return "Security";
		case DegreeProgram::NETWORK:
			return "Network";
		case DegreeProgram::SOFTWARE:
			return "Software";
	};
};
