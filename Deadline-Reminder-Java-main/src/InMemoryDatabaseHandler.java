import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.deadlinereminder.model.*;

/**
 * Simple in-memory implementation of DeadlineOperations.
 * Useful as a fallback when MySQL is not available.
 */
public class InMemoryDatabaseHandler implements DeadlineOperations {
    private final List<Deadline> list = new ArrayList<>();
    private final AtomicInteger seq = new AtomicInteger(1);

    @Override
    public synchronized void add(Deadline d) {
        d.setId(seq.getAndIncrement());
        list.add(d);
    }

    @Override
    public synchronized List<Deadline> getAll() {
        // return shallow copies to avoid external mutation
        List<Deadline> out = new ArrayList<>();
        for (Deadline d : list) {
            out.add(d);
        }
        return out;
    }

    @Override
    public synchronized void update(Deadline d) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == d.getId()) {
                list.set(i, d);
                return;
            }
        }
        throw new Exception("Item not found");
    }

    @Override
    public synchronized void delete(int id) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                list.remove(i);
                return;
            }
        }
        throw new Exception("Item not found");
    }
}
