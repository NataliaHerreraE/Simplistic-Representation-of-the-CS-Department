package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a department in a school or university.
 * Each department has a map of courses and a list of professors.
 */
public class Department {
    private HashMap<String, Course> courseMap;
    private ArrayList<Professor> listOfProfs;

    /**
     * Constructs a new Department with the specified list of professors.
     *
     * @param listOfProfs the list of professors in the department.
     */
    public Department(ArrayList<Professor> listOfProfs) {
        this.listOfProfs = listOfProfs;
        this.courseMap = new HashMap<>();

        List<Course> courses = readCoursesFromFile("src/resources/courses_f22.txt");

        HashMap<String, Course> courseMap = new HashMap<>();
        for (Course course : courses) {
            courseMap.put(course.getId(), course);
        }
        setCourseMap(courseMap);
    }

    /**
     * Returns the map of courses in this department.
     *
     * @return the map of courses in this department.
     */
    public HashMap<String, Course> getCourseMap() {
        return courseMap;
    }

    /**
     * Sets the map of courses in this department.
     *
     * @param courseMap the new map of courses in this department.
     */
    public void setCourseMap(HashMap<String, Course> courseMap) {
        this.courseMap = courseMap;
    }

    /**
     * Returns the list of professors in this department.
     *
     * @return the list of professors in this department.
     */
    public ArrayList<Professor> getListOfProfs() {
        return listOfProfs;
    }

    /**
     * Reads courses from a file and adds them to a list.
     *
     * @param filename the name of the file containing the courses
     * @return the list of courses
     */
    private static List<Course> readCoursesFromFile(String filename) {
        List<Course> courses = new ArrayList<>();
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length == 6) {
                    Course course = parseCourse(line);
                    courses.add(course);
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            System.err.println("Erro ao abrir o arquivo " + filename);
        }
        return courses;
    }

    /**
     * Parses a line from a file into a Course object.
     *
     * @param line the line to be parsed
     * @return the created Course object
     */
    private static Course parseCourse(String line) {
        String[] parts = line.split(":");
        String id = parts[0].trim();
        String title = parts[1].trim();
        String discipline = parts[2].trim();
        int numberOfHours = Integer.parseInt(parts[3].trim());
        //String[] listPrerequisites = parts[4].trim().split(",");
        int numOfGroups = Integer.parseInt(parts[5].trim());
        return new Course(id, title, discipline, numberOfHours,numOfGroups);
    }

}