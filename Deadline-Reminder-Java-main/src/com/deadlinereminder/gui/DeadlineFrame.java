package com.deadlinereminder.gui;

import com.deadlinereminder.dao.*;
import com.deadlinereminder.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class DeadlineFrame extends JFrame {
    private DeadlineDAO dao;
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField txtId = new JTextField(5);
    private JTextField txtTitle = new JTextField(20);
    private JComboBox<String> cbType = new JComboBox<>(new String[]{"Assignment","Project"});
    private JTextField txtDue = new JTextField(10); // yyyy-mm-dd
    private JTextField txtSubject = new JTextField(15);
    private JTextField txtTeam = new JTextField(20);

    public DeadlineFrame(DeadlineDAO dao) {
        super("Deadline Reminder");
        this.dao = dao;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        refreshTable();
    }

    private JPanel buildFormPanel() {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT));

        p.add(new JLabel("ID:"));
        txtId.setEditable(false);
        p.add(txtId);

        p.add(new JLabel("Title:")); p.add(txtTitle);
        p.add(new JLabel("Type:")); p.add(cbType);
        p.add(new JLabel("Due (YYYY-MM-DD):")); p.add(txtDue);
        p.add(new JLabel("Subject:")); p.add(txtSubject);
        p.add(new JLabel("Team:")); p.add(txtTeam);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(this::onAdd);
        p.add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(this::onUpdate);
        p.add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(this::onDelete);
        p.add(btnDelete);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> refreshTable());
        p.add(btnRefresh);

        return p;
    }

    private JScrollPane buildTablePanel() {
        String[] cols = new String[]{"ID","Title","Type","Due","Subject","Team"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> onTableSelect());
        return new JScrollPane(table);
    }

    private void onTableSelect() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            txtId.setText(tableModel.getValueAt(r,0).toString());
            txtTitle.setText((String) tableModel.getValueAt(r,1));
            cbType.setSelectedItem(tableModel.getValueAt(r,2));
            txtDue.setText(tableModel.getValueAt(r,3).toString());
            txtSubject.setText((String) tableModel.getValueAt(r,4));
            txtTeam.setText((String) tableModel.getValueAt(r,5));
        }
    }

    private void onAdd(ActionEvent e) {
        try {
            Deadline d = createDeadlineFromForm();
            dao.add(d);
            JOptionPane.showMessageDialog(this, "Added: " + d);
            refreshTable();
            clearForm();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void onUpdate(ActionEvent e) {
        try {
            if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Select a row first"); return; }
            Deadline d = createDeadlineFromForm();
            d.setId(Integer.parseInt(txtId.getText()));
            dao.update(d);
            JOptionPane.showMessageDialog(this, "Updated");
            refreshTable();
        } catch (Exception ex) { showError(ex); }
    }

    private void onDelete(ActionEvent e) {
        try {
            if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Select a row first"); return; }
            int id = Integer.parseInt(txtId.getText());
            dao.delete(id);
            JOptionPane.showMessageDialog(this, "Deleted");
            refreshTable();
            clearForm();
        } catch (Exception ex) { showError(ex); }
    }

    private Deadline createDeadlineFromForm() {
        String title = txtTitle.getText().trim();
        String type = (String) cbType.getSelectedItem();
        String due = txtDue.getText().trim();
        Date sqlDate = Date.valueOf(LocalDate.parse(due));
        String subject = txtSubject.getText().trim();
        String team = txtTeam.getText().trim();
        if ("Project".equals(type)) {
            return new Project(title, sqlDate, subject, team);
        } else {
            return new Assignment(title, sqlDate, subject, team);
        }
    }

    private void refreshTable() {
        try {
            List<Deadline> list = dao.getAll();
            tableModel.setRowCount(0);
            for (Deadline d : list) {
                tableModel.addRow(new Object[]{d.getId(), d.getTitle(), d.getType(), d.getDueDate(), d.getSubject(), d.getTeamMembers()});
            }
        } catch (Exception e) { showError(e); }
    }

    private void clearForm() {
        txtId.setText(""); txtTitle.setText(""); txtDue.setText(""); txtSubject.setText(""); txtTeam.setText("");
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}
