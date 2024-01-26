import java.time.LocalDate;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class LibrarySystem {

    /**
     * This method adds a new book to the system.
     * @param type A string indicating the type of book to be added ("P" for Printed, "H" for Handwritten).
     * @param book The book object to be added.
     */
    public static void addBook(String type, Books book){
        // Check if the book is printed or handwritten
        if (type.equals("P")){
            book.isHandwritten = false;
            book.setId(Books.bookId);
            Main.books.add(book);
            Main.booksP.add(book);
            Books.bookId++;
            Main.outputBuilder.append("Created new book: Printed [id: "+book.getId()+"]\n");
        } else if (type.equals("H")){
            book.isHandwritten = true;
            book.setId(Books.bookId);
            Main.books.add(book);
            Main.booksH.add(book);
            Books.bookId++;
            Main.outputBuilder.append("Created new book: Handwritten [id: "+book.getId()+"]\n");
        }
    }

    /**
     * This method adds a new library member to the system.
     * @param memberType A string indicating the type of member to be added ("S" for Student, "A" for Academician).
     */
    public static void addMember(String memberType){
        if (memberType.equals("S")){
            // Create a new student member, set its ID, and add it to the student list and the main member list
            Students member = new Students(Members.memberId);
            member.setId(Members.memberId);
            Main.addStudent(member);
            Main.addMember(member);
            Members.memberId++;
            Main.outputBuilder.append("Created new member: Student [id: "+member.getId()+"]\n");
        } else if (memberType.equals("A")){
            // Create a new academician member, set its ID, and add it to the academician list and the main member list
            Academicians member = new Academicians(Members.memberId);
            member.setId(Members.memberId);
            Main.addAcademicians(member);
            Main.addMember(member);
            Members.memberId++;
            Main.outputBuilder.append("Created new member: Academic [id: "+member.getId()+"]\n");
        }
    }

    /**
     * This method allows a library member to borrow a book from the library.
     * @param Id_Of_Book An integer representing the ID of the book to be borrowed.
     * @param Id_Of_Library_Member An integer representing the ID of the library member borrowing the book.
     * @param date A LocalDate object representing the date on which the book is being borrowed.
     */
    public static void borrowBook(int Id_Of_Book, int Id_Of_Library_Member, LocalDate date){
        Books book = Main.books.get(Id_Of_Book);
        Members member;
        LocalDate dueDate;
        member = Main.getMembers().get(Id_Of_Library_Member);
        if (!(book.borrowedBy == null)){
            Main.outputBuilder.append("You cannot borrow this book!\n");
            return;
        }
        if (member instanceof Students){
            if (!book.isHandwritten){
                if (member.booksBorrowed.size()<2) {
                    dueDate = date.plusDays(7);
                    book.setDueDate(dueDate);
                    book.setBorrowedDate(date);
                    member.booksBorrowed.add(book);
                    Main.addBooksBorrowed(book);
                    book.setBorrowedBy(member);
                    Main.outputBuilder.append("The book ["+book.getId()+"] was borrowed by member ["+member.getId()+"] at "+date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"\n");
                } else{
                    Main.outputBuilder.append("You have exceeded the borrowing limit!\n");
                }
            } else {
                Main.outputBuilder.append("You can not read Handwritten book\n");
            }
        }else{
            if (!book.isHandwritten){
                if (member.booksBorrowed.size()<4) {
                    dueDate = date.plusDays(14);
                    book.setDueDate(dueDate);
                    book.setBorrowedDate(date);
                    member.booksBorrowed.add(book);
                    Main.addBooksBorrowed(book);
                    book.setBorrowedBy(member);
                    Main.outputBuilder.append("The book ["+book.getId()+"] was borrowed by member ["+member.getId()+"] at "+date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"\n");
                } else{
                    Main.outputBuilder.append("You have exceeded the borrowing limit!\n");
                }
            } else {
                Main.outputBuilder.append("You can not read Handwritten book\n");
            }
        }
    }

    /**
     * Returns a borrowed book by a member on a given date.
     *
     * @param Id_Of_Book            the ID of the book to be returned
     * @param Id_Of_Library_Member  the ID of the member returning the book
     * @param date                  the date the book is returned
     */
    public static void returnBook(int Id_Of_Book, int Id_Of_Library_Member, LocalDate date){
        Books book = Main.books.get(Id_Of_Book);
        Members member;
        member = Main.getMembers().get(Id_Of_Library_Member);
        // If the book is returned after the due date, calculate a late fee
        if (book.getDueDate()!=null && book.getDueDate().isBefore(date)){
            Duration duration = Duration.between(book.dueDate.atStartOfDay(), date.atStartOfDay());
            long daysBetween = duration.toDays();
            String fee = Long.toString(daysBetween);
            Main.getBooksBorrowed().removeIf(index -> index.getId() == book.getId());
            member.booksBorrowed.removeIf(index -> index.getId() == book.getId());
            book.setBorrowedBy(null);
            book.setDueDate(null);
            Main.outputBuilder.append("The book ["+book.getId()+"] was returned by member ["+member.getId()+"] at "+date+" Fee: "+fee+"\n");
        // If the book is returned on time, remove it from the member's list of borrowed books and the library's list
        } else if (book.getBorrowedBy()!=null){
            Main.getBooksBorrowed().removeIf(index -> index.getId() == book.getId());
            member.booksBorrowed.removeIf(index -> index.getId() == book.getId());
            Main.getReadInLibrary().removeIf(index -> index.getId() == book.getId());
            book.setBorrowedBy(null);
            book.setDueDate(null);
            Main.outputBuilder.append("The book ["+book.getId()+"] was returned by member ["+member.getId()+"] at "+date+" Fee: 0\n");
        // If the book was not borrowed in the first place, print an error message
        }else if(book.getDueDate()==null){
            Main.outputBuilder.append("The book ["+book.getId()+"] was not borrowed\n");
        }
    }

    public static void extendBook(int Id_Of_Book, int Id_Of_Library_Member, LocalDate date) {
        // Get the book and member objects from their respective maps using the IDs provided.
        Books book = Main.books.get(Id_Of_Book);
        Members member;
        member = Main.getMembers().get(Id_Of_Library_Member);
        // Check if the book is actually borrowed by the specified member.
        if (book.getBorrowedBy() == null || book.getBorrowedBy().getId() != member.getId()) {
            Main.outputBuilder.append("The book ["+book.getId()+"] is not borrowed by member ["+member.getId()+"]!\n");
            return;
        }
        // Check if the book is being read in the library (and thus cannot be extended).
        if (book.getBorrowedBy() != null && Main.getReadInLibrary().contains(book)){
            Main.outputBuilder.append("You cannot extend the deadline of a book read in the library.\n");
        }
        // If this is the first time the book is being extended, update the due date accordingly.
        if (book.numberOfExtensions == 0) {
            LocalDate newDeadLine;
            if (member instanceof Students) {
                newDeadLine = date.plusDays(7);
            } else {
                newDeadLine = date.plusDays(14);
            }
            book.setDueDate(newDeadLine);
            Main.outputBuilder.append("The deadline of book ["+book.getId()+"] was extended by member ["+member.getId()+"] at "+ date+"\n");
            Main.outputBuilder.append("New deadline of book ["+book.getId()+"] is "+newDeadLine+"\n");
            book.numberOfExtensions++;
        }
        // If the book has already been extended once, disallow further extensions.
        else{
            Main.outputBuilder.append("You cannot extend the deadline!\n");
        }
    }

    public static void readInLibrary(int Id_Of_Book, int Id_Of_Library_Member, LocalDate date){
        // Get the book and member objects
        Books book = Main.books.get(Id_Of_Book);
        Members member;
        member = Main.getMembers().get(Id_Of_Library_Member);
        // Check if the book is handwritten and the member is a student
        if (member instanceof Students){
            if (book.isHandwritten){
                Main.outputBuilder.append("Students can not read handwritten books!\n");
                return;
            }
        }
        // Check if the book is already borrowed by someone else.
        if (!(book.borrowedBy == null)){
            Main.outputBuilder.append("You can not read this book!\n");
            return;
        }
        // Update the book's "borrowedBy" and "borrowedDate" fields to reflect that it is being read in the library.
        book.setBorrowedBy(member);
        book.setBorrowedDate(date);
        Main.addReadInLibrary(book);
        Main.outputBuilder.append("The book ["+book.getId()+"] was read in library by member ["+member.getId()+"] at "+date+"\n");
    }

    public static void getTheHistory(){
        Main.outputBuilder.append("History of library:\n");
        Main.outputBuilder.append("\nNumber of students: "+ Main.getStudents().size()+"\n");
        for (int j=0; j<Main.getStudents().size(); j++){
            Main.outputBuilder.append("Student [id: "+Main.getStudents().get(j).getId()+"]\n");
        }

        Main.outputBuilder.append("\nNumber of academics: "+ Main.getAcademicians().size()+"\n");
        for (int j=0; j<Main.getAcademicians().size(); j++){
            Main.outputBuilder.append("Academic [id: "+Main.getAcademicians().get(j).getId()+"]\n");
        }

        Main.outputBuilder.append("\nNumber of printed books: "+ Main.booksP.size()+"\n");
        for (int j=0; j<Main.booksP.size(); j++){
            Main.outputBuilder.append("Printed [id: "+Main.booksP.get(j).getId()+"]\n");
        }

        Main.outputBuilder.append("\nNumber of handwritten books: "+ Main.booksH.size()+"\n");
        for (int j=0; j<Main.booksH.size(); j++){
            Main.outputBuilder.append("Handwritten [id: "+Main.booksH.get(j).getId()+"]\n");
        }

        Main.outputBuilder.append("\nNumber of borrowed books: "+ Main.getBooksBorrowed().size()+"\n");
        for (int j=0; j<Main.getBooksBorrowed().size(); j++) {
            Main.outputBuilder.append("The book ["+Main.getBooksBorrowed().get(j).getId()+"] was borrowed by member ["
                    +Main.getBooksBorrowed().get(j).getBorrowedBy().getId()+"] at "+Main.getBooksBorrowed().get(j).getBorrowedDate()+"\n");
        }

        Main.outputBuilder.append("\nNumber of books read in library: "+ Main.getReadInLibrary().size());
        for (int j=0; j<Main.getReadInLibrary().size(); j++) {
            Main.outputBuilder.append("\nThe book ["+Main.getReadInLibrary().get(j).getId()+"] was read in library by member ["
                    +Main.getReadInLibrary().get(j).getBorrowedBy().getId()+"] at "+Main.getReadInLibrary().get(j).getBorrowedDate()+"");
        }
    }
}