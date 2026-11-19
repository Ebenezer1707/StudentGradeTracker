import java.io.*;
import java.util.*;

/**
 * Student Grade Tracker
 * 
 * A console-based application to manage student grades.
 * Features: Add, view, search, remove students and view class statistics.
 * 
 * @author CodeAlpha Internship - Task 1
 */
public class StudentGradeTracker {

    // ─── Data Model ──────────────────────────────────────────────

    static class Student {
        String name;
        double[] grades;
        String[] subjects;

        Student(String name, String[] subjects, double[] grades) {
            this.name = name;
            this.subjects = subjects;
            this.grades = grades;
        }

        double getAverage() {
            double sum = 0;
            for (double g : grades) sum += g;
            return sum / grades.length;
        }

        String getLetterGrade() {
            double avg = getAverage();
            if (avg >= 90) return "A";
            if (avg >= 80) return "B";
            if (avg >= 70) return "C";
            if (avg >= 60) return "D";
            return "F";
        }

        double getHighest() {
            double max = grades[0];
            for (double g : grades) if (g > max) max = g;
            return max;
        }

        double getLowest() {
            double min = grades[0];
            for (double g : grades) if (g < min) min = g;
            return min;
        }
    }

    // ─── Fields ──────────────────────────────────────────────────

    private static final String DATA_FILE = "students.txt";
    private static final List<Student> students = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    // ─── Main ────────────────────────────────────────────────────

    public static void main(String[] args) {
        loadFromFile();
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: addStudent();         break;
                case 2: viewAllStudents();    break;
                case 3: searchStudent();      break;
                case 4: viewStatistics();     break;
                case 5: removeStudent();      break;
                case 6:
                    saveToFile();
                    System.out.println("\n  Data saved. Goodbye!\n");
                    running = false;
                    break;
                default:
                    System.out.println("\n  ✗ Invalid choice. Please try again.\n");
            }
        }

        scanner.close();
    }

    // ─── Menu ────────────────────────────────────────────────────

    private static void printMenu() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║       STUDENT GRADE TRACKER               ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  1. Add Student");
        System.out.println("  2. View All Students");
        System.out.println("  3. Search Student");
        System.out.println("  4. View Class Statistics");
        System.out.println("  5. Remove Student");
        System.out.println("  6. Exit");
        System.out.println();
    }

    // ─── 1. Add Student ──────────────────────────────────────────

    private static void addStudent() {
        System.out.print("  Enter student name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("\n  ✗ Name cannot be empty.\n");
            return;
        }

        int count = readInt("  How many subjects? ");
        if (count <= 0) {
            System.out.println("\n  ✗ Must have at least 1 subject.\n");
            return;
        }

        String[] subjects = new String[count];
        double[] grades = new double[count];

        for (int i = 0; i < count; i++) {
            System.out.print("  Enter name for Subject " + (i + 1) + ": ");
            subjects[i] = scanner.nextLine().trim();

            grades[i] = readDouble("  Enter grade for " + subjects[i] + " (0-100): ");
            if (grades[i] < 0 || grades[i] > 100) {
                System.out.println("  ✗ Grade must be between 0 and 100. Setting to 0.");
                grades[i] = 0;
            }
        }

        Student student = new Student(name, subjects, grades);
        students.add(student);
        saveToFile();

        System.out.printf("\n  ✓ Student added successfully!%n");
        System.out.printf("    Name: %s | Average: %.2f | Grade: %s%n%n",
                student.name, student.getAverage(), student.getLetterGrade());
    }

    // ─── 2. View All Students ────────────────────────────────────

    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("\n  No students found.\n");
            return;
        }

        System.out.println();
        System.out.println("  ┌─────┬────────────────────┬─────────┬───────┐");
        System.out.println("  │ No. │ Name               │ Average │ Grade │");
        System.out.println("  ├─────┼────────────────────┼─────────┼───────┤");

        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("  │ %-3d │ %-18s │ %7.2f │   %s   │%n",
                    (i + 1), truncate(s.name, 18), s.getAverage(), s.getLetterGrade());
        }

        System.out.println("  └─────┴────────────────────┴─────────┴───────┘");
        System.out.println();
    }

    // ─── 3. Search Student ───────────────────────────────────────

    private static void searchStudent() {
        System.out.print("  Enter student name to search: ");
        String query = scanner.nextLine().trim().toLowerCase();

        boolean found = false;
        for (Student s : students) {
            if (s.name.toLowerCase().contains(query)) {
                found = true;
                printStudentDetails(s);
            }
        }

        if (!found) {
            System.out.println("\n  ✗ No student found matching \"" + query + "\"\n");
        }
    }

    private static void printStudentDetails(Student s) {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.printf("  ║  Student: %-26s  ║%n", truncate(s.name, 26));
        System.out.println("  ╠══════════════════════════════════════╣");

        for (int i = 0; i < s.subjects.length; i++) {
            System.out.printf("  ║  %-20s : %7.2f      ║%n", truncate(s.subjects[i], 20), s.grades[i]);
        }

        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.printf("  ║  Average : %-7.2f   Grade : %-3s     ║%n", s.getAverage(), s.getLetterGrade());
        System.out.printf("  ║  Highest : %-7.2f   Lowest : %-6.2f ║%n", s.getHighest(), s.getLowest());
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.println();
    }

    // ─── 4. Class Statistics ─────────────────────────────────────

    private static void viewStatistics() {
        if (students.isEmpty()) {
            System.out.println("\n  No students to show statistics.\n");
            return;
        }

        double classTotal = 0;
        Student topStudent = students.get(0);
        Student lowStudent = students.get(0);

        for (Student s : students) {
            double avg = s.getAverage();
            classTotal += avg;
            if (avg > topStudent.getAverage()) topStudent = s;
            if (avg < lowStudent.getAverage()) lowStudent = s;
        }

        double classAverage = classTotal / students.size();

        // Count grades
        int countA = 0, countB = 0, countC = 0, countD = 0, countF = 0;
        for (Student s : students) {
            switch (s.getLetterGrade()) {
                case "A": countA++; break;
                case "B": countB++; break;
                case "C": countC++; break;
                case "D": countD++; break;
                case "F": countF++; break;
            }
        }

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║         CLASS STATISTICS             ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.printf("  ║  Total Students  : %-16d  ║%n", students.size());
        System.out.printf("  ║  Class Average   : %-16.2f  ║%n", classAverage);
        System.out.printf("  ║  Top Student     : %-16s  ║%n", truncate(topStudent.name, 16));
        System.out.printf("  ║  Lowest Student  : %-16s  ║%n", truncate(lowStudent.name, 16));
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  Grade Distribution                 ║");
        System.out.printf("  ║  A: %-3d  B: %-3d  C: %-3d             ║%n", countA, countB, countC);
        System.out.printf("  ║  D: %-3d  F: %-3d                     ║%n", countD, countF);
        System.out.println("  ╚══════════════════════════════════════╝");
        System.out.println();
    }

    // ─── 5. Remove Student ───────────────────────────────────────

    private static void removeStudent() {
        if (students.isEmpty()) {
            System.out.println("\n  No students to remove.\n");
            return;
        }

        viewAllStudents();
        int index = readInt("  Enter student number to remove: ") - 1;

        if (index < 0 || index >= students.size()) {
            System.out.println("\n  ✗ Invalid student number.\n");
            return;
        }

        String name = students.get(index).name;
        students.remove(index);
        saveToFile();
        System.out.println("\n  ✓ Removed student: " + name + "\n");
    }

    // ─── File I/O ────────────────────────────────────────────────

    private static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Student s : students) {
                StringBuilder sb = new StringBuilder();
                sb.append(s.name);
                for (int i = 0; i < s.subjects.length; i++) {
                    sb.append("|").append(s.subjects[i]).append(":").append(s.grades[i]);
                }
                writer.println(sb.toString());
            }
        } catch (IOException e) {
            System.out.println("  ✗ Error saving data: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                String name = parts[0];
                int count = parts.length - 1;

                if (count <= 0) continue;

                String[] subjects = new String[count];
                double[] grades = new double[count];

                for (int i = 0; i < count; i++) {
                    String[] subParts = parts[i + 1].split(":");
                    subjects[i] = subParts[0];
                    grades[i] = Double.parseDouble(subParts[1]);
                }

                students.add(new Student(name, subjects, grades));
            }
        } catch (IOException e) {
            System.out.println("  ✗ Error loading data: " + e.getMessage());
        }
    }

    // ─── Utility Helpers ─────────────────────────────────────────

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("  ✗ Enter a valid number: ");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return val;
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("  ✗ Enter a valid number: ");
            scanner.next();
        }
        double val = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        return val;
    }

    private static String truncate(String text, int maxLen) {
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 2) + "..";
    }
}
