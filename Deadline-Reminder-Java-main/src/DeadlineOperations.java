import java.util.List;

public interface DeadlineOperations {
    void add(Deadline d) throws Exception;
    List<Deadline> getAll() throws Exception;
    void update(Deadline d) throws Exception;
    void delete(int id) throws Exception;
}
