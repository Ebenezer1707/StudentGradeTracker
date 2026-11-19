# 📚 Student Grade Tracker

A simple console-based Java application to manage student grades. Built as **Task 1** for CodeAlpha Internship.

## Features

- ➕ Add students with grades for multiple subjects
- 📊 View all students with their grades, average, and letter grade
- 🔍 Search for a specific student by name
- 📈 View class statistics (highest, lowest, class average)
- ❌ Remove a student record
- 💾 Persistent storage using text file (`students.txt`)

## How to Run

### Prerequisites
- Java JDK 8 or higher

### Compile & Run

```bash
cd StudentGradeTracker/src
javac StudentGradeTracker.java
java StudentGradeTracker
```

## Grading Scale

| Percentage | Grade |
|------------|-------|
| 90 – 100   | A     |
| 80 – 89    | B     |
| 70 – 79    | C     |
| 60 – 69    | D     |
| Below 60   | F     |

## Project Structure

```
StudentGradeTracker/
├── README.md
├── .gitignore
└── src/
    └── StudentGradeTracker.java
```

## Sample Output

```
╔════════════════════════════════════════════╗
║       STUDENT GRADE TRACKER               ║
╚════════════════════════════════════════════╝

1. Add Student
2. View All Students
3. Search Student
4. View Class Statistics
5. Remove Student
6. Exit

Enter your choice: 1
Enter student name: John Doe
How many subjects? 3
Enter grade for Subject 1: 85
Enter grade for Subject 2: 92
Enter grade for Subject 3: 78

✓ Student added successfully!
  Name: John Doe | Average: 85.00 | Grade: B
```

## Author

**CodeAlpha Internship** – Java Programming Task 1
