import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Students> students = new ArrayList<>();
    private static List<Academicians> academicians = new ArrayList<>();
    private static List<Members> members = new ArrayList<>();
    public static List<Books> books = new ArrayList<>();
    public static List<Books> booksP = new ArrayList<>();
    public static List<Books> booksH = new ArrayList<>();
    private static List<Books> booksBorrowed = new ArrayList<>();
    private static List<Books> readInLibrary = new ArrayList<>();
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static StringBuilder outputBuilder = new StringBuilder();

    public static void addStudent(Members member) {
        students.add((Students) member);
    }
    public static List<Students> getStudents() {
        return students;
    }
    public static void addAcademicians(Members member) {
        academicians.add((Academicians) member);
    }
    public static List<Academicians> getAcademicians() {
        return academicians;
    }
    public static void addMember(Members member) {
        members.add(member);
    }
    public static List<Members> getMembers() {
        return members;
    }
    public static void addBooksBorrowed(Books book) {
        booksBorrowed.add(book);
    }
    public static List<Books> getBooksBorrowed() {
        return booksBorrowed;
    }
    public static void addReadInLibrary(Books book) {
        readInLibrary.add(book);
    }
    public static List<Books> getReadInLibrary() {
        return readInLibrary;
    }
    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];
        String[] fileContents;
        fileContents = fileInput.readFile(inputFile, true, true);
        for (int i = 0; i < fileContents.length; i++) {
            String[] lineArray = fileContents[i].split("\t");
            String command = lineArray[0];
            switch (command) {
                case "addBook":
                    Books book = new Books();
                    String type = lineArray[1];
                    LibrarySystem.addBook(type, book);
                    break;
                case "addMember":
                    String memberType = lineArray[1];
                    LibrarySystem.addMember(memberType);
                    break;
                case "borrowBook":
                    int Id_Of_Book = Integer.parseInt(lineArray[1])-1;
                    int Id_Of_Library_Member = Integer.parseInt(lineArray[2])-1;
                    LocalDate date = LocalDate.parse(lineArray[3],Main.formatter);
                    LibrarySystem.borrowBook(Id_Of_Book, Id_Of_Library_Member, date);
                    break;
                case "returnBook":
                    int Id_Of_Book1 = Integer.parseInt(lineArray[1])-1;
                    int Id_Of_Library_Member1 = Integer.parseInt(lineArray[2])-1;
                    LocalDate date1 = LocalDate.parse(lineArray[3],Main.formatter);
                    LibrarySystem.returnBook(Id_Of_Book1, Id_Of_Library_Member1, date1);
                    break;
                case "extendBook":
                    int Id_Of_Book2 = Integer.parseInt(lineArray[1])-1;
                    int Id_Of_Library_Member2 = Integer.parseInt(lineArray[2])-1;
                    LocalDate date2 = LocalDate.parse(lineArray[3],Main.formatter);
                    LibrarySystem.extendBook(Id_Of_Book2, Id_Of_Library_Member2, date2);
                    break;
                case "readInLibrary":
                    int Id_Of_Book3 = Integer.parseInt(lineArray[1])-1;
                    int Id_Of_Library_Member3 = Integer.parseInt(lineArray[2])-1;
                    LocalDate date3 = LocalDate.parse(lineArray[3],Main.formatter);
                    LibrarySystem.readInLibrary(Id_Of_Book3, Id_Of_Library_Member3, date3);
                    break;
                case "getTheHistory":
                    LibrarySystem.getTheHistory();
                    break;
            }
        }
        fileOutput.writeToFile(outputFile, String.valueOf(outputBuilder), false, false);
    }
}