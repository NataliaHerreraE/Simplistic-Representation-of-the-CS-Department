import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import datastructures.MyPriorityQueue;
import model.Professor;
import model.Course;
import model.Department;

/**
 * Main application class for the professor assignment system.
 */
public class MainApplication {

    /**
     * The main method of the application.
     * It initializes the system, reads professors and courses from files, assigns courses to professors, and prints the assignments.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {

        // Listas  para almacenar los profesores
        ArrayList<Professor> listOfProfs = new ArrayList<>();
        MyPriorityQueue<Professor> profProcessingQueue = new MyPriorityQueue<>();
        System.out.println("\nInitializing the system...");
        // Lee los datos de los profesores desde un archivo y los carga en las estructuras de datos
        readProfessorsFromFile("src/resources/profs.txt", listOfProfs, profProcessingQueue);

        // Crea un nuevo departamento con la lista de profesores
        Department computerScienceDepartment = new Department(listOfProfs);

        // Procesa a cada profesor en el queue de prioridad para asignarles cursos
        while (!profProcessingQueue.isEmpty()) {
            Professor professor = profProcessingQueue.dequeue();
            fetchProfessorSelection(professor, computerScienceDepartment);
        }
        // Imprime el resultado de las asignaciones
        printAffectations(listOfProfs,computerScienceDepartment);
    }

    /**
     * Fetches the professor's course selection from a file and assigns courses to the professor.
     *
     * @param professor the professor whose course selection is to be fetched
     * @param courseMap the department containing the courses
     */
    private static void fetchProfessorSelection(Professor professor, Department courseMap) {
        // Construye el nombre del archivo a partir del ID del profesor
        String professorId =  String.valueOf(professor.getId());
        String filename = "src/resources/" + professorId + "_selection.txt";

        try {
            // Intenta leer la selección de cursos del profesor desde el archivo
            readProfessorSelectionFromFile(filename, professor, courseMap);
        } catch (Exception e) {
            //e.printStackTrace();
            // Maneja excepciones si el archivo no existe o hay errores al abrirlo
            System.err.println("File does not exist or error while opening the file. " + filename);
        }

    }


    /**
     * Reads a professor's course selection from a file and assigns courses to the professor.
     * The file should contain the total weekly hours the professor can work and the courses they wish to teach.
     * Each line in the file should either contain a single integer (the total weekly hours) or two comma-separated values (the course ID and the number of groups the professor wishes to teach).
     * If a line in the file is not in the correct format, an error message is printed to the console.
     * If the file cannot be opened, an error message is printed to the console.
     *
     * @param filename the name of the file containing the professor's course selection
     * @param professor the professor whose course selection is to be read
     * @param courseMap the department containing the courses
     */
    public static void readProfessorSelectionFromFile(String filename, Professor professor, Department courseMap) {
        int totalHours = 0;
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 1) {
                    //En este contexto, compara el número de horas que el profesor quiere trabajar (convertido de String a int) con el máximo permitido (Professor.MAX_WEEKLY_HOURS).
                    totalHours = Math.min(Integer.parseInt(parts[0]), Professor.MAX_WEEKLY_HOURS);
                }
                else if (parts.length == 2) {
                    // Si hay dos valores, se asume que son el ID del curso y el número de grupos que desea impartir
                    assignCourseToProfessor(parts, totalHours, professor, courseMap);
                }
                else {
                    // Si el formato no es correcto, imprime un mensaje de error
                    System.out.println("Invalid line format.");
                }
            }
        }
        catch (FileNotFoundException e) {
            // Maneja la excepción si el archivo no se encuentra
            System.err.println("Error during file operation " + filename);
        }
        catch (Exception e) {
            // Maneja cualquier otra excepción que pueda ocurrir
            System.err.println("Error " + e.getMessage());
        }
    }

    /**
     * Assigns a course to a professor based on the professor's preferences and availability.
     * The method first checks if the course exists and if there are any groups available.
     * If the course exists and there are groups available, it checks if the professor is qualified to teach the course and if they have enough available hours.
     * If the professor is qualified and has enough hours, it assigns as many groups as possible to the professor.
     * If the professor is not qualified or does not have enough hours, it prints a message to the console.
     * If the course does not exist or there are no groups available, it prints a message to the console and adds the course to the course map with a null value.
     *
     * @param parts a string array containing the course ID and the number of groups the professor wishes to teach
     * @param totalHours the total number of hours the professor can work in a week
     * @param professor the professor to whom the course is to be assigned
     * @param courseMap the department containing the courses
     */
    private static void assignCourseToProfessor(String[] parts, int totalHours, Professor professor, Department courseMap) {
        String courseId = parts[0];
        // Comprueba si el curso existe y si hay grupos disponibles
        Course course = courseMap.getCourseMap().get(courseId);

        if (course != null && course.getNumOfGroups() >= 0) {
            int requestedGroups = Integer.parseInt(parts[1].trim());
            int availableGroups = Math.min(requestedGroups, course.getNumOfGroups());

            // Comprueba si el profesor está calificado para enseñar el curso y si tiene suficientes horas disponibles
            if (professor.getSetOfDisciplines().contains(course.getDiscipline()) && totalHours >= professor.getTotalWeeklyHours()) {
                // Calcula cuántos grupos puede enseñar el profesor con las horas disponibles
                int numOfGroupsAvailable = Math.min((totalHours - professor.getTotalWeeklyHours()) / course.getWeeklyHours(), availableGroups);
                Course newCourse = new Course(course); // Crea una nueva instancia del curso
                newCourse.setNumOfGroups(numOfGroupsAvailable);// Establece el número de grupos que el profesor enseñará

                //Adición del nuevo curso a la lista de cursos afectados del profesor
                ArrayList<Course> currentCourses = Optional.ofNullable(professor.getListOfAffectedCourses()).orElse(new ArrayList<>());
                currentCourses.add(newCourse);
                professor.setListOfAffectedCourses(currentCourses);
                //Se reduce el número de grupos disponibles del curso original por la cantidad que el profesor enseñará.
                course.decreaseNumOfGroups(numOfGroupsAvailable);
            } else {

                System.out.printf("The professor %s cannot be assigned to this course or has already reached the weekly hours limit.%n", professor.getName());
            }
        } else {
            // Imprime un mensaje si el curso solicitado no existe o no hay grupos disponibles
            System.out.printf("The professor %s requested Course %s, but not found.%n", professor.getName(), courseId);
            courseMap.getCourseMap().put(courseId, null); // Agrega el curso con un valor nulo al mapa de cursos si no existe
        }
    }

    /**
     * Reads professors from a file and adds them to a list and a priority queue.
     *
     * @param filename the name of the file containing the professors
     * @param listOfProfs the list to which the professors are to be added
     * @param profProcessingQueue the priority queue to which the professors are to be added
     */
    public static void readProfessorsFromFile(String filename, ArrayList<Professor> listOfProfs, MyPriorityQueue<Professor> profProcessingQueue) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Professor professor = parseProfessor(line); // Parsea la línea para crear un objeto Professor
                listOfProfs.add(professor); // Añade el profesor a la lista
                profProcessingQueue.enqueue(professor);  // Añade el profesor a la cola de prioridad
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error during file operation: " + filename);
        }
    }

    /**
     * Parses a line from a file into a Professor object.
     *
     * @param line the line to be parsed
     * @return the created Professor object, or null if the line could not be parsed
     */
    private static Professor parseProfessor(String line){
        String[] parts = line.split(":");
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        double seniorityLevel = Double.parseDouble(parts[2]);
        Date hiringDate = parseDate(parts[3]);
        String[] disciplines = parts[4].split(",");
        Set<String> myProfDisciplines = new HashSet<>();
        for (String discipline : disciplines) {
            myProfDisciplines.add(discipline.trim());
        }
        return new Professor(id, name, seniorityLevel, hiringDate, myProfDisciplines);
    }

    /**
     * Reads courses from a file and adds them to a list.
     *
     * @param filename the name of the file containing the courses
     * @return the list of courses
     */
    public static List<Course> readCoursesFromFile(String filename) {
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
            System.err.println("Error during file operation " + filename);
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

    /**
     * Parses a date string into a Date object.
     *
     * @param dateString the date string to be parsed
     * @return the created Date object, or null if the date string could not be parsed
     */
    public static Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            //e.printStackTrace();
            return null;
        }
    }


    public static void printAffectations(ArrayList<Professor> listOfProfs,Department computerScienceDepartment){
        System.out.println("\nComputer Science Department Affections:");
        System.out.println("---------------------------------------");
        for (Course course : computerScienceDepartment.getCourseMap().values()) {
            if (course != null ) {
                System.out.println(" Course: " + course.getId() + " - " + course.getTitle() +
                        "\n\t Available Groups: " + course.getNumOfGroups() +
                        ", Number of Hours: " + course.getNumberOfHours() +
                        ", Weekly Hours: " + course.getWeeklyHours() +
                        "\n-----------------------------------------------------------------");
            }
        }

        System.out.println("\nProfessor Affectations:");
        System.out.println("-----------------------");
        for (Professor professor : listOfProfs) {
            System.out.println("Professor: " + professor.getName());
            ArrayList<Course> affectedCourses = professor.getListOfAffectedCourses();
            if (affectedCourses != null) {
                int totalWeeklyHours = 0;
                for (Course course : affectedCourses) {
                    totalWeeklyHours += course.getWeeklyHours() * course.getNumOfGroups();
                    System.out.println("  Course: " + course.getId() + " - " + course.getTitle() +
                            ", Groups: " + course.getNumOfGroups() +
                            ", Weekly Hours: " + course.getWeeklyHours() * course.getNumOfGroups());
                }
                System.out.println("  Total Weekly Hours: " + totalWeeklyHours);
            } else {
                System.out.println("  No courses assigned.");
            }
            System.out.println("\n-----------------------");
        }

    }
}
