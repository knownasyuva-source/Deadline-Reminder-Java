package com.deadlinereminder.dao;

import com.deadlinereminder.model.Deadline;
import java.util.List;

public interface DeadlineDAO {
    void add(Deadline d) throws DataAccessException;
    List<Deadline> getAll() throws DataAccessException;
    void update(Deadline d) throws DataAccessException;
    void delete(int id) throws DataAccessException;
}
