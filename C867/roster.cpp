#include <string>
#include <iostream>
#include <regex>
#include "roster.h"
#include "student.h"
#include "degree.h"
using namespace std;

// Constructor for Roster class
Roster::Roster() {	
	const string studentData[] =
	{ "A1,John,Smith,John1989@gm ail.com,20,30,35,40,SECURITY",
		"A2,Suzan,Erickson,Erickson_1990@gmailcom,19,50,30,40,NETWORK",
		"A3,Jack,Napoli,The_lawyer99yahoo.com,19,20,40,33,SOFTWARE",
		"A4,Erin,Black,Erin.black@comcast.net,22,50,58,40,SECURITY",
		"A5,Tim,Wick,tjwick77@gmail.com,28,30,30,30,SOFTWARE"
	};

	studentRosterLength = sizeof(studentData) / sizeof(studentData[0]);
	this->classRosterArray = new Student * [studentRosterLength];

	for (int i = 0; i < 5; ++i) {
		string currentStudent = studentData[i];
		// Split student information into an array
		string studentInfoArr[9];
		int infoPosition = 0;
		// Initialize first comma position
		int startOfString = 0;
		// Loop string
		for (int j = 0; j < currentStudent.length(); ++j) {
			if (currentStudent[j] == ',') {
				// Put substring between previous comma and this comma into array
				studentInfoArr[infoPosition] = currentStudent.substr(startOfString, j - startOfString);
				// Need to add 1 to account for comma taking up a spot in the count
				startOfString = j + 1;
				++infoPosition;
			};
		};
		// Add last data item - degree program
		studentInfoArr[8] = currentStudent.substr(startOfString, currentStudent.length());
		// Set up needed conversions
		int age = stoi(studentInfoArr[4]);
		int daysInCourse[3] = { stoi(studentInfoArr[5]), stoi(studentInfoArr[6]), stoi(studentInfoArr[7]) };
		// Create new Student and add to array
		classRosterArray[i] = new Student(studentInfoArr[0], studentInfoArr[1], studentInfoArr[2], studentInfoArr[3], age, daysInCourse, getDegreeProgram(studentInfoArr[8]));
	};
	return;
};

// Destructor
Roster::~Roster() {
	delete[] classRosterArray;
}

// Add method for Roster class to add a student to the roster
void Roster::add(string studentID, string firstName, string lastName, string emailAddress, int age, int daysInCourse1, int daysInCourse2, int daysInCourse3, DegreeProgram degreeProgram) {
	int newRosterLength = studentRosterLength + 1;
	Student** newClassRosterArray = new Student*[newRosterLength];
	for (int i = 0; i < newRosterLength; ++i) {
		newClassRosterArray[i] = classRosterArray[i];
	};
	int daysInCourse[3] = { daysInCourse1, daysInCourse2, daysInCourse3 };
	newClassRosterArray[newRosterLength - 1] = new Student(studentID, firstName, lastName, emailAddress, age, daysInCourse, degreeProgram);
	delete classRosterArray;
	this->classRosterArray = new Student * [newRosterLength];
	this->studentRosterLength = newRosterLength;
	for (int i = 0; i < newRosterLength; ++i) {
		classRosterArray[i] = newClassRosterArray[i];
	}
	delete[] newClassRosterArray;
	return;
};

// Remove method for Roster class to remove a student from the roster
void Roster::remove(string studentID) {
	for (int i = 0; i < studentRosterLength; ++i) {
		if (classRosterArray[i]->GetStudentId() == studentID) {
			for (int j = i; j < studentRosterLength - 1; ++j) {
				classRosterArray[j] = classRosterArray[j + 1];
			};
			--studentRosterLength;
			return;
		};
	};
	cout << "StudentId " << studentID << " not found." << endl;;
};


// Print method to print information on each student in the roster
void Roster::printAll() {
	for (int i = 0; i < studentRosterLength; ++i) {
		classRosterArray[i]->Print();
	}
	cout << endl;
};

// Method to print after days in courses by student ID
void Roster::printAverageDaysInCourse(string studentID) {
	for (int i = 0; i < studentRosterLength; ++i) {
		if (classRosterArray[i]->GetStudentId() == studentID) {
			int totalDays{};
			for (int j = 0; j < 3; ++j) {
				totalDays += classRosterArray[i]->GetNumDays()[j];
			};
			cout << "Average number of days in three courses for " << studentID << ": " << totalDays / 3;
		};
	};
	cout << endl;
}

// Method to print infalid emails
void Roster::printInvalidEmails() {
	regex emailRegex("[a-zA-Z0-9_.]+\\@[a-zA-Z0-9_.]+\\.(com|edu|net|org)");
	for (int i = 0; i < studentRosterLength; ++i) {
		string emailAddress = classRosterArray[i]->GetEmailAddress();
		if (!regex_match(emailAddress, emailRegex)) {
			cout << "Invalid email address: " << emailAddress << endl;
		};
	};
	cout << endl;
};

// Method to print student information by degree program
void Roster:: printByDegreeProgram(DegreeProgram degreeProgram) {
	string degreeProgramString = printDegreeProgram(degreeProgram);
	cout << "Students in the " << degreeProgramString << " degree program:" << endl;
	for (int i = 0; i < studentRosterLength; ++i) {
		if (degreeProgram == classRosterArray[i]->GetDegreeProgram()) {
			classRosterArray[i]->Print();
		};
	};
	cout << endl;
};

// Helper function to get DegreeProgram from a string
DegreeProgram Roster::getDegreeProgram(string degreeProgram) const {
	if (degreeProgram == "SECURITY") {
		return DegreeProgram::SECURITY;
	}
	else if (degreeProgram == "NETWORK") {
		return DegreeProgram::NETWORK;
	} 
	else {
		return DegreeProgram::SOFTWARE;
	};
};

// Helpter function to get a string for a DegreeProgram
string Roster::printDegreeProgram(DegreeProgram degreeProgram) const {
	switch (degreeProgram) {
	case DegreeProgram::SECURITY:
		return "Security";
	case DegreeProgram::NETWORK:
		return "Network";
	case DegreeProgram::SOFTWARE:
		return "Software";
	};
};
