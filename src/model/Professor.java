package model;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Represents a professor in a school or university.
 * Each professor has an id, name, seniority level, hiring date, set of disciplines they can teach, and a list of courses they are assigned to.
 */
public class Professor implements Comparable<Professor>  {

    private final int id;
    private final String name;
    private final double seniorityLevel;
    private final Date hiringDate;
    private final Set<String> setOfDisciplines;
    private ArrayList<Course> listOfAffectedCourses;

    /**
     * Constructs a new Professor with the specified id, name, seniority level, hiring date, and set of disciplines.
     *
     * @param id the id of the professor.
     * @param name the name of the professor.
     * @param seniorityLevel the seniority level of the professor.
     * @param hiringDate the hiring date of the professor.
     * @param setOfDisciplines the set of disciplines the professor can teach.
     */
    public Professor(int id, String name, double seniorityLevel, Date hiringDate,Set<String> setOfDisciplines) {
        this.id = id;
        this.name = name;
        this.seniorityLevel = seniorityLevel;
        this.hiringDate = hiringDate;
        this.setOfDisciplines = setOfDisciplines;
    }

    // Getters and setters for the professor's id, name, seniority level, hiring date, set of disciplines, and list of assigned courses.

    /**
     * Compares this professor to another professor based on their seniority level, hiring date, and id.
     *
     * @param professor the professor to compare to.
     * @return a negative integer, zero, or a positive integer as this professor is less than, equal to, or greater than the specified professor.
     */
    @Override
    public int compareTo(Professor professor) {
        if (this.seniorityLevel > professor.seniorityLevel) {
            return 1;
        } else if (this.seniorityLevel < professor.seniorityLevel) {
            return -1;
        } else {
            int dateCompare = this.hiringDate.compareTo(professor.hiringDate);
            if (dateCompare != 0) {
                return -1*dateCompare;
            } else {
                return Integer.compare(this.id, professor.id);
            }
        }
    }

    /**
     * The maximum number of weekly hours a professor can teach.
     */
    public static final int MAX_WEEKLY_HOURS = 30;

    /**
     * Calculates and returns the total weekly hours for this professor based on the courses they are assigned to.
     *
     * @return the total weekly hours for this professor.
     */
    public int getTotalWeeklyHours() {
        int totalHours = 0;
        if (listOfAffectedCourses != null) {
            for (Course course : listOfAffectedCourses) {
                totalHours += course.getWeeklyHours() * course.getNumOfGroups();
            }
        }
        return totalHours;
    }

    /**
     * Returns the id of the professor.
     *
     * @return the id of the professor.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the professor.
     *
     * @return the name of the professor.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the set of disciplines the professor can teach.
     *
     * @return the set of disciplines the professor can teach.
     */
    public Set<String> getSetOfDisciplines() {
        return setOfDisciplines;
    }

    /**
     * Returns the list of courses the professor is assigned to.
     *
     * @return the list of courses the professor is assigned to.
     */
    public ArrayList<Course> getListOfAffectedCourses() {
        return listOfAffectedCourses;
    }

    /**
     * Sets the list of courses the professor is assigned to.
     *
     * @param listOfAffectedCourses the new list of courses the professor is assigned to.
     */
    public void setListOfAffectedCourses(ArrayList<Course> listOfAffectedCourses) {
        this.listOfAffectedCourses = listOfAffectedCourses;
    }

    /**
     * Returns a string representation of this professor.
     *
     * @return a string representation of this professor.
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(hiringDate);

        return "Professor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", seniorityLevel=" + seniorityLevel +
                ", hiringDate=" + formattedDate +
                ", setOfDisciplines=" + setOfDisciplines +
                ", listOfAffectedCourses=" + listOfAffectedCourses +
                '}';
    }
}