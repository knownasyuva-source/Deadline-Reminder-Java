package com.deadlinereminder;

import com.deadlinereminder.dao.DeadlineDAO;
import com.deadlinereminder.dao.DeadlineDAOImpl;
import com.deadlinereminder.gui.DeadlineFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        try {
            DeadlineDAO dao = new DeadlineDAOImpl();
            SwingUtilities.invokeLater(() -> {
                DeadlineFrame f = new DeadlineFrame(dao);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to start application: " + e.getMessage());
        }
    }
}
