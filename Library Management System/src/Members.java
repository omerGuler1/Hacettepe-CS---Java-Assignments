import java.util.ArrayList;
import java.util.List;

public class Members {
    static int memberId=1;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public List<Books> booksBorrowed = new ArrayList<>();
}