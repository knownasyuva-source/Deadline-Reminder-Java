import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import com.deadlinereminder.model.*;

public class MainFrame extends JFrame {
    private DeadlineOperations dao;
    private DefaultTableModel model;
    private JTable table;

    private JTextField txtId = new JTextField(5);
    private JTextField txtTitle = new JTextField(20);
    private JComboBox<String> cbType = new JComboBox<>(new String[]{"Assignment","Project"});
    private JTextField txtDue = new JTextField(10);
    private JTextField txtSubject = new JTextField(15);
    private JTextField txtTeam = new JTextField(20);

    public MainFrame(DeadlineOperations dao) {
        super("Deadline Reminder (Simple)");
        this.dao = dao;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 420);
        setLayout(new BorderLayout());

        add(buildForm(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
        refreshTable();
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("ID:")); txtId.setEditable(false); p.add(txtId);
        p.add(new JLabel("Title:")); p.add(txtTitle);
        p.add(new JLabel("Type:")); p.add(cbType);
        p.add(new JLabel("Due (YYYY-MM-DD):")); p.add(txtDue);
        p.add(new JLabel("Subject:")); p.add(txtSubject);
        p.add(new JLabel("Team:")); p.add(txtTeam);

        JButton add = new JButton("Add"); add.addActionListener(this::onAdd); p.add(add);
        JButton upd = new JButton("Update"); upd.addActionListener(this::onUpdate); p.add(upd);
        JButton del = new JButton("Delete"); del.addActionListener(this::onDelete); p.add(del);
        JButton ref = new JButton("Refresh"); ref.addActionListener(e -> refreshTable()); p.add(ref);
        return p;
    }

    private JScrollPane buildTable() {
        String[] cols = {"ID","Title","Type","Due","Subject","Team"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> onSelect());
        return new JScrollPane(table);
    }

    private void onSelect() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            txtId.setText(String.valueOf(model.getValueAt(r,0)));
            txtTitle.setText((String)model.getValueAt(r,1));
            cbType.setSelectedItem(model.getValueAt(r,2));
            txtDue.setText(model.getValueAt(r,3).toString());
            txtSubject.setText((String)model.getValueAt(r,4));
            txtTeam.setText((String)model.getValueAt(r,5));
        }
    }

    private void onAdd(ActionEvent e) {
        try {
            Deadline d = fromForm();
            dao.add(d);
            JOptionPane.showMessageDialog(this, "Added: " + d);
            refreshTable();
            clearForm();
        } catch (Exception ex) { showError(ex); }
    }

    private void onUpdate(ActionEvent e) {
        try {
            if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Select a row first"); return; }
            Deadline d = fromForm();
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

    private Deadline fromForm() {
        String title = txtTitle.getText().trim();
        String type = (String) cbType.getSelectedItem();
        Date date = Date.valueOf(LocalDate.parse(txtDue.getText().trim()));
        String subject = txtSubject.getText().trim();
        String team = txtTeam.getText().trim();
        if ("Project".equals(type)) return new Project(title, date, subject, team);
        return new Assignment(title, date, subject, team);
    }

    private void refreshTable() {
        try {
            List<Deadline> list = dao.getAll();
            model.setRowCount(0);
            for (Deadline d : list) model.addRow(new Object[]{d.getId(), d.getTitle(), d.getType(), d.getDueDate(), d.getSubject(), d.getTeamMembers()});
        } catch (Exception e) { showError(e); }
    }

    private void clearForm() { txtId.setText(""); txtTitle.setText(""); txtDue.setText(""); txtSubject.setText(""); txtTeam.setText(""); }
    private void showError(Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }

    public static void main(String[] args) {
        DatabaseHandler db = null;
        try {
            db = new DatabaseHandler();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not connect to MySQL — switching to in-memory database.\nError: " + e.getMessage());
        }

        DeadlineOperations dao = (db != null) ? db : new InMemoryDatabaseHandler();
        DeadlineOperations finalDao = dao;
        SwingUtilities.invokeLater(() -> {
            MainFrame f = new MainFrame(finalDao);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
