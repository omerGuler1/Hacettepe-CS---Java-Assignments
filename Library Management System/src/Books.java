import java.time.LocalDate;

public class Books {
    public static int bookId=1;
    public int numberOfExtensions;
    private int id;
    public boolean isHandwritten;
    LocalDate dueDate;
    private LocalDate borrowedDate;
    Members borrowedBy;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Members getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(Members borrowedBy) {
        this.borrowedBy = borrowedBy;
    }
}