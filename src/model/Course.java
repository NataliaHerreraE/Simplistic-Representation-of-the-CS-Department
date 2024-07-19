package model;

/**
 * Represents a course in a school or university.
 * Each course has an id, title, discipline, number of hours, and number of groups.
 */
public class Course {
    private final String id;
    private final String title;
    private final String discipline;
    private final int numberOfHours;
    private int numOfGroups;

    /**
     * Constructs a new Course with the specified id, title, discipline, number of hours, and number of groups.
     *
     * @param id the id of the course.
     * @param title the title of the course.
     * @param discipline the discipline of the course.
     * @param numberOfHours the number of hours of the course.
     * @param numOfGroups the number of groups in the course.
     */
    public Course(String id, String title, String discipline, int numberOfHours, int numOfGroups) {
        this.id = id;
        this.title = title;
        this.discipline = discipline;
        this.numberOfHours = numberOfHours;
        this.numOfGroups = numOfGroups;
    }

    /**
     * Constructs a new Course by copying the details from the specified course.
     *
     * @param course the course to copy.
     */
    public Course (Course course){
        this.id = course.id;
        this.title = course.title;
        this.discipline = course.discipline;
        this.numberOfHours = course.numberOfHours;
        this.numOfGroups = course.numOfGroups;
    }

    /**
     * Returns the id of this course.
     *
     * @return the id of this course.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the title of this course.
     *
     * @return the title of this course.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the discipline of this course.
     *
     * @return the discipline of this course.
     */
    public String getDiscipline() {
        return discipline;
    }

    /**
     * Returns the number of hours in this course.
     *
     * @return the number of hours in this course.
     */
    public int getNumberOfHours() {
        return numberOfHours;
    }
    /**
     * Returns the number of groups in this course.
     *
     * @return the number of groups in this course.
     */
    public int getNumOfGroups() {
        return numOfGroups;
    }

    /**
     * Sets the number of groups in this course.
     *
     * @param numOfGroups the new number of groups in this course.
     */
    public void setNumOfGroups(int numOfGroups) {
        this.numOfGroups = numOfGroups;
    }

    /**
     * Decreases the number of groups in this course by the specified amount.
     *
     * @param numOfGroups the amount to decrease the number of groups by.
     */
    public void decreaseNumOfGroups(int numOfGroups) {
        this.numOfGroups -=  numOfGroups;
    }

    /**
     * Returns the number of weekly hours for this course.
     * The number of weekly hours is determined by the total number of hours for the course.
     *
     * @return the number of weekly hours for this course.
     */
    public int getWeeklyHours(){
        return switch (this.numberOfHours) {
            case 45 -> 3;
            case 60 -> 4;
            case 75 -> 5;
            case 90 -> 6;
            default -> 0;
        };
    }
}