import course_management.*;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void run() throws SQLException {
        boolean validUser = false;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello! Welcome to the REGIE!");

        String username = null;
        String password;
        AccountAuth accountAuth = new AccountAuth();

        while (!validUser) {
            try {
                System.out.println("Please enter your username");
                username = scanner.nextLine();

                System.out.println("Please enter your password");
                password = scanner.nextLine();

                boolean validUsername = accountAuth.isStudentUserNameValid(username);
                boolean matchedPassword = accountAuth.isStudentPasswordMatches(username, password);

                validUser = (validUsername && matchedPassword);

                if (!validUser) {
                    System.out.println("Invalid username or password, please re-enter!");
                }
            } catch (Exception e) {
                System.out.println("An error occurred during login. Please try again.");
            }
        }
        String userType = accountAuth.getUserType(username);
        System.out.println("Welcome! " + username);
        System.out.println("You are a " + userType);
        if (userType.equals("STUDENT")) {
            studentMenu(scanner, accountAuth, username);
        } else {
            adminMenu(scanner, accountAuth, username);
        }


    }

    private static void studentMenu(Scanner scanner, AccountAuth accountAuth, String username) {
        boolean running = true;
        CourseManager courseManager = CourseManager.getInstance();
        LabManager labManager = LabManager.getInstance();

        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. All Course this Quarter");
            System.out.println("2. Current Available courses");
            System.out.println("3. Enroll a course");
            System.out.println("4. Print current schedule");
            System.out.println("5. Logout\n");
            System.out.println("Please enter your choice:");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    courseManager.getAllCourses();
                    break;
                case "2":
                    courseManager.openedCourses();
                    break;
                case "3":
                    // enroll a course
                    /*
                    1. isfull?
                    2. < 3 enrolled
                    3. enroll, add capacity
                    4. isfull? closed: else
                    5. has lab? print available labs
                    6. enroll lab, add capacity
                    7. isfull? closed: else
                     */
                    boolean done = false;
                    while (!done) {
                        try {
                            System.out.println("Enter the courseName that your would like to enroll: ");
                            String courseName = scanner.nextLine();
                            // get the correct courseName input
                            if (courseManager.getIdByName(courseName) != -1) {
                                System.out.println("Found the course successfully!");
                                boolean isCourseFull = courseManager.isCourseFull(courseName);
                                // check if the course full
                                if (!isCourseFull) {
                                    int enrollCount = accountAuth.enrollNum(username);
                                    if (enrollCount < 3) {
                                        if (!accountAuth.isUserAlreadyEnrolledInCourse(courseName, accountAuth.getId(username))) {
                                            // is student allowed to enroll
                                            System.out.println("You are good to enroll!");
                                            String courseNum = courseManager.getCourseNumByName(courseName);
                                            boolean sectionSelection = false;
                                            while (!sectionSelection) {
                                                System.out.println("Please pick the section you would like to enroll: \n");
                                                courseManager.getSectionNumbersByCourseName(courseName);
                                                System.out.println();
//                                            courseManager.getCourseSection(courseName);
                                                String sectionNum = scanner.nextLine();

                                                if (courseManager.validSectionNumbersOfCourseName(courseName, sectionNum)) {
                                                    // get the correct sectionNum input
                                                    int userId = accountAuth.getId(username);
                                                    accountAuth.enrollACourse(courseNum, courseName, userId, sectionNum);
                                                    // enroll the course

                                                    courseManager.incrementCapacity(courseName, sectionNum); // add 1 to capacity
                                                    if (courseManager.isCourseFull(courseName)) {
                                                        // check if the course full after this enrollment
                                                        courseManager.closedCourse(courseName);
                                                    }
                                                    sectionSelection = true;
                                                    System.out.println("Let me check if the course has a lab...");

                                                    if (courseManager.courseHasLabs(courseName)) {
                                                        System.out.println("Yes, there is a necessary lab for the course!");
                                                        courseManager.getAvailableCourseLab(courseName);
                                                        boolean labSelected = false;
                                                        while (!labSelected) {
                                                            System.out.println("Enter the labNum that your would like to enroll: ");
                                                            String labNum = scanner.nextLine();
                                                            boolean validLab = labManager.isLabNameValid(labNum);
                                                            if (validLab) {
                                                                int course_id = courseManager.getIdByName(courseName);
                                                                // enroll lab
                                                                // increment
                                                                // need to closed or not
                                                                accountAuth.enrollALab(labNum, course_id, userId);
                                                                labManager.incrementLabCapacity(labNum);
                                                                if (labManager.isLabFill(labNum)) {
                                                                    labManager.closeLab(labNum);
                                                                }
                                                                labSelected = true;
                                                            } else {
                                                                System.out.println("The Lab doesnt exist, Please choose a Lab from the following list!");
                                                                courseManager.getAvailableCourseLab(courseName);
                                                            }

                                                        }
                                                        System.out.println("/////////////////////////////////////////////////");
                                                        System.out.println("You are all set!");
                                                        System.out.println("/////////////////////////////////////////////////");
                                                        if (accountAuth.hasTimeWarning(userId)) {
                                                            System.out.println("------------WARNING------------");
                                                            System.out.println("There is a time conflict in your schedule!");
                                                            System.out.println("------------WARNING------------");
                                                        }

                                                    } else {
                                                        System.out.println("/////////////////////////////////////////////////");
                                                        System.out.println("You are all set! There is no lab for this course.");
                                                        System.out.println("/////////////////////////////////////////////////");
                                                        if (accountAuth.hasTimeWarning(userId)) {
                                                            System.out.println("------------WARNING------------");
                                                            System.out.println("There is a time conflict in your schedule!");
                                                            System.out.println("------------WARNING------------");
                                                        }
                                                    }
                                                    done = true;

                                                } else {
                                                    System.out.println("--//-- Invalid Course! Please try again! --//--");
//                                                System.out.println("--//-- The " + courseName + "has a valid sectionNumber: " + courseManager.getCourseSectionNumByCourseName(courseName) + " --//--");
                                                }
                                            }
                                        } else {
                                            System.out.println("You already enrolled this course! Choose another one.");
                                        }

                                    } else {
                                        System.out.println("--//-- You cannot take over 3 courses  --//--");
                                        System.out.println("Try Again OR Apply for overload!");
                                    }
                                } else {
                                    System.out.println("--//-- Sorry the the course is Closed Now, pick another one. --//--");
                                }
                            } else {
                                System.out.println("--//-- Invalid courseName! --//--");
                                System.out.println("Please enter the correct one, OR enter 'quit' to go to previous page.");
                                String res = scanner.nextLine();
                                if (res.equals("quit")) {
                                    done = true;
                                }
                            }

                        } catch (Exception e) {
                            System.out.println("An error occurred: " + e.getMessage());
                            System.out.println("Please try again.");
                            // Optionally reset any variables if needed or handle specific types of exceptions
                        }
                    }
                    break;
                case "4":
                    accountAuth.currentSchedule(accountAuth.getId(username));
                    break;
                case "5":
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void adminMenu(Scanner scanner, AccountAuth accountAuth, String username) throws SQLException {
        boolean running = true;
        CourseManager courseManager = CourseManager.getInstance();

        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. All Course this Quarter");
            System.out.println("2. Current Available courses");
            System.out.println("3. Add courses");
            System.out.println("4. Find courses less than 5 enrolled");
            System.out.println("5. Logout\n");
            System.out.println("Please enter your choice:");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    courseManager.getAllCourses();
                    break;
                case "2":
                    courseManager.openedCourses();
                    break;
                case "3":
                    // add course
                    // 1. a regular course or with capacity
                    // 2. assign location and schedule
                    // 3. has lab? add/not
                    // 4. assign location and schedule for labs
                    System.out.println("Welcome to the course adding management!\n");
                    boolean done = false;
                    while (!done) {


                        System.out.println("Please tell me the courseName of the course that you would like to add for the Quarter!");
                        String courseName = scanner.nextLine();
                        boolean existedCourse = courseManager.uniqueCourseName(courseName);
                        while (existedCourse) {
                            System.out.println("This course name already exists. Please enter a different course name:");
                            courseName = scanner.nextLine();
                            existedCourse = courseManager.uniqueCourseName(courseName);
                        }


                        System.out.println("What about the courseNum (e.g. MPCS 51410) ?");
                        String courseNum = scanner.nextLine();
                        boolean existedCourseNum = courseManager.uniqueCourseNum(courseNum);
                        while (existedCourseNum) {
                            System.out.println("This courseNum already exists. Please enter a different courseNum (e.g. MPCS 51410): ");
                            courseNum = scanner.nextLine();
                            existedCourseNum = courseManager.uniqueCourseNum(courseNum);
                        }


                        // set the sectionNum
                        System.out.println("Does the course has multiple section? (y/n)");
                        String hasSections = scanner.nextLine();
                        if (hasSections.equals("y") || hasSections.equals("Y") || hasSections.equals("yes") || hasSections.equals("YES")) {
                            System.out.println("Good to know that! What is the section number for the current course then?");
                            String sectionNum = scanner.nextLine();

                            // set capacity
                            System.out.println("Does the course has specific capacity? (y/n) Otherwise, the default capacity is 5.");
                            String hasCapacity = scanner.nextLine();
                            if (hasCapacity.equals("y") || hasCapacity.equals("Y") || hasCapacity.equals("yes") || hasCapacity.equals("YES")) {
                                System.out.println("Good to know that! What is the capacity for the current course then?");
                                int courseCapacity = Integer.parseInt(scanner.nextLine());
                                // add the course in courses table
                                Course course = new Course(courseName, courseNum, sectionNum, courseCapacity, 0);
                                courseManager.addCourseWithCapacity(course);

                                // assign course's location
                                assignCourseLocation(courseManager.getIdByCourse(course), courseManager);
                                assignCourseSchedule(courseManager.getIdByCourse(course), courseManager);

                                System.out.println("Does the course has a lab? (y/n)");
                                String hasLab = scanner.nextLine();
                                if (hasLab.equals("y") || hasLab.equals("Y") || hasLab.equals("yes") || hasLab.equals("YES")){
                                    System.out.println("Good to know that, lets set up the lab!");

                                    setUpLab(courseManager.getIdByCourse(course));

                                    done =true;

                                }else{
                                    System.out.println("Got it, Course adding is done");
                                    done = true;
                                }
                            } else {
                                System.out.println("Got it! The default course capacity is 5!");
                                int courseCapacity = 5;
                                // add the course in courses table
                                Course course = new Course(courseName, courseNum, sectionNum, courseCapacity, 0);
                                courseManager.addCourseWithCapacity(course);

                                // assign course's location
                                assignCourseLocation(courseManager.getIdByCourse(course), courseManager);
                                assignCourseSchedule(courseManager.getIdByCourse(course), courseManager);

                                System.out.println("Does the course has a lab? (y/n)");
                                String hasLab = scanner.nextLine();
                                if (hasLab.equals("y") || hasLab.equals("Y") || hasLab.equals("yes") || hasLab.equals("YES")){
                                    System.out.println("Good to know that, lets set up the lab!");

                                    setUpLab(courseManager.getIdByCourse(course));

                                    done =true;
                                }else{
                                    System.out.println("Got it, Course adding is done");
                                    done = true;
                                }
                            }

                        } else {
                            System.out.println("Got it! The default sectionNum is 001");
                            String sectionNum = "001";

                            // set capacity
                            System.out.println("Does the course has specific capacity? (y/n) Otherwise, the default capacity is 5.");
                            String hasCapacity = scanner.nextLine();
                            if (hasCapacity.equals("y") || hasCapacity.equals("Y") || hasCapacity.equals("yes") || hasCapacity.equals("YES")) {
                                System.out.println("Good to know that! What is the capacity for the current course then?");
                                int courseCapacity = Integer.parseInt(scanner.nextLine());
                                // add the course in courses table
                                Course course = new Course(courseName, courseNum, sectionNum, courseCapacity, 0);
                                courseManager.addCourseWithCapacity(course);

                                // assign course's location
                                assignCourseLocation(courseManager.getIdByCourse(course), courseManager);
                                assignCourseSchedule(courseManager.getIdByCourse(course), courseManager);

                                System.out.println("Does the course has a lab? (y/n)");
                                String hasLab = scanner.nextLine();
                                if (hasLab.equals("y") || hasLab.equals("Y") || hasLab.equals("yes") || hasLab.equals("YES")){
                                    System.out.println("Good to know that, lets set up the lab!");

                                    setUpLab(courseManager.getIdByCourse(course));

                                    done =true;
                                }else{
                                    System.out.println("Got it, Course adding is done");
                                    done = true;
                                }

                            } else {
                                System.out.println("Got it! The default course capacity is 5!");
                                int courseCapacity = 5;
                                // add the course in courses table
                                Course course = new Course(courseName, courseNum, sectionNum, courseCapacity, 0);
                                courseManager.addCourseWithCapacity(course);

                                // assign course's location
                                assignCourseLocation(courseManager.getIdByCourse(course), courseManager);
                                assignCourseSchedule(courseManager.getIdByCourse(course), courseManager);

                                System.out.println("Does the course has a lab? (y/n)");
                                String hasLab = scanner.nextLine();
                                if (hasLab.equals("y") || hasLab.equals("Y") || hasLab.equals("yes") || hasLab.equals("YES")){
                                    System.out.println("Good to know that, lets set up the lab!");

                                    setUpLab(courseManager.getIdByCourse(course));

                                    done =true;
                                }else{
                                    System.out.println("Got it, Course adding is done");
                                    done = true;
                                }
                            }

                        }
                    }


                    break;
                case "4":
                    System.out.println("Here are the course that has less than 5 student enrolled\n");
                    courseManager.courseLessThan5Enrolled();
                    System.out.println("Do you want to remove them? (y/n)");
                    String decision = scanner.nextLine();
                    if (decision.equals("y") || decision.equals("Y") || decision.equals("yes") || decision.equals("YES")) {
                        courseManager.removeCourseLessThan5Enrolled();
                        System.out.println("Remove the courses SUCCESSFULLY!");
                    } else {
                        System.out.println("Got it!");
                    }
                    break;
                case "5":
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void setUpLab(int courseId) throws SQLException {
        LabManager labManager = LabManager.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is labNum? (e.g. S101)");
        String labNum = scanner.nextLine();
        boolean existedLabNum = labManager.labNumExists(labNum, courseId);
        while (existedLabNum) {
            System.out.println("This Lab Num already exists. Please enter a different labNum:");
            labNum = scanner.nextLine();
            existedLabNum = labManager.labNumExists(labNum, courseId);
        }
        System.out.println("what is the capacity of the lab?");
        int labCapacity = Integer.parseInt(scanner.nextLine());
        Lab lab = new Lab(labNum);
        lab.setSectionCapacity(labCapacity);
        labManager.addSection(lab, courseId);
        System.out.println("Got it!");


        assignLabLocation(labManager.getIdByNum(lab), labManager);
        assignLabSchedule(labManager.getIdByNum(lab), labManager);


    }

    private static void assignLabSchedule(int idByNum, LabManager labManager) {
        Scanner scanner = new Scanner(System.in);

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String day = "";
        boolean isValidDay = false;
        while (!isValidDay){
            System.out.println("Pick the day of the lab (Monday/Tuesday/Wednesday/Thursday/Friday)");
            day = scanner.nextLine().trim();
            for (String d : days) {
                if (d.equalsIgnoreCase(day)) { // .equalsIgnoreCase() ignores case when comparing strings
                    isValidDay = true;
                    break; // Exit the loop if a match is found
                }
            }
        }

        System.out.println("What is the start time? (e.g. 13:30)");
        String startTime = scanner.nextLine();

        System.out.println("What is the end time? (e.g. 20:30)");
        String endTime = scanner.nextLine();
        Schedule schedule = new Schedule(day,startTime,endTime);
        labManager.assignSchedule(idByNum,schedule);
        System.out.println("All set for the lab schedule: day: "+ day+" start from: "+startTime+" end at "+endTime);

    }

    private static void assignLabLocation(int idByNum, LabManager labManager) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Where is the lab's building location? (e.g. JCL)");
        String building = scanner.nextLine();

        System.out.println("Which room of the " + building);
        String room = scanner.nextLine();
        Locations locations = new Locations(building,room);
        labManager.assignLocation(idByNum, locations);
        System.out.println("All set for the lab location: in the building: "+building+"; room: "+ room);
    }

    public static void assignCourseLocation(int courseId, CourseManager courseManager) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Where is the course's building location? (e.g. JCL)");
        String building = scanner.nextLine();

        System.out.println("Which room of the " + building);
        String room = scanner.nextLine();
        Locations locations = new Locations(building,room);
        courseManager.assignLocation(courseId, locations);
        System.out.println("All set for the course location: in the building: "+building+"; room: "+ room);

    }

    public static void assignCourseSchedule(int courseId, CourseManager courseManager) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String day = "";
        boolean isValidDay = false;
        while (!isValidDay){
            System.out.println("Pick the day of the course (Monday/Tuesday/Wednesday/Thursday/Friday)");
            day = scanner.nextLine().trim();
            for (String d : days) {
                if (d.equalsIgnoreCase(day)) { // .equalsIgnoreCase() ignores case when comparing strings
                    isValidDay = true;
                    break; // Exit the loop if a match is found
                }
            }
        }

        System.out.println("What is the start time? (e.g. 13:30)");
        String startTime = scanner.nextLine();

        System.out.println("What is the end time? (e.g. 20:30)");
        String endTime = scanner.nextLine();
        Schedule schedule = new Schedule(day,startTime,endTime);
        courseManager.assignSchedule(courseId,schedule);
        System.out.println("All set for the course schedule: day: "+ day+" start from: "+startTime+" end at "+endTime);
    }

    public static void main(String[] args) throws SQLException, CapacityException {
        run();

    }
}