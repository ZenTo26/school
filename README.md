-- Create the Students table with additional details
CREATE TABLE Students (
    StudentID INT IDENTITY(1, 1) PRIMARY KEY,
    FirstName NVARCHAR(50),
    LastName NVARCHAR(50),
    DateOfBirth DATE,
    Address NVARCHAR(255),
    PhoneNumber NVARCHAR(15),
    Sex CHAR(1),
    Age AS DATEDIFF(YEAR, DateOfBirth, GETDATE()) -- Calculated Age
);

-- Create the Subjects table
CREATE TABLE Subjects (
    SubjectID INT IDENTITY(1, 1) PRIMARY KEY,
    SubjectName NVARCHAR(100)
);

-- Create the Enrollments table
CREATE TABLE Enrollments (
    EnrollmentID INT IDENTITY(1, 1) PRIMARY KEY,
    StudentID INT,
    SubjectID INT,
    Year INT, -- 1, 2, 3, 4
    Semester INT, -- 1, 2
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID),
    FOREIGN KEY (SubjectID) REFERENCES Subjects(SubjectID)
);

-- Create the Scores table
CREATE TABLE Scores (
    ScoreID INT IDENTITY(1, 1) PRIMARY KEY,
    EnrollmentID INT,
    Score DECIMAL(5, 2), -- Assuming scores are decimal with two decimal places
    GradePoint DECIMAL(3, 2), -- GPA value
    FOREIGN KEY (EnrollmentID) REFERENCES Enrollments(EnrollmentID)
);

-- Create the GPA table
CREATE TABLE GPA (
    GPATableID INT IDENTITY(1, 1) PRIMARY KEY,
    StudentID INT,
    Year INT, -- 1, 2, 3, 4
    Semester INT, -- 1, 2
    GPA DECIMAL(3, 2), -- GPA with 2 decimal places
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID)
);
