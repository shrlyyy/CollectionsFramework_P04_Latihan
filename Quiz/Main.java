import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Main {

    private static ArrayList<Product> products = new ArrayList<>();
    private static DefaultTableModel tableModel;
    private static boolean isEditing = false;
    private static int editingIndex = -1; // Indeks produk yang sedang diedit

    public static void main(String[] args) {
        JFrame frame = new JFrame("Daftar Produk");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columnNames = {"Nama Produk", "Harga"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JTextField nameField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JButton addButton = new JButton("Tambah");
        JButton deleteButton = new JButton("Hapus");

        // Listener untuk menampilkan data produk yang dipilih di field input
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Pastikan hanya diproses saat perubahan selesai
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        Product product = products.get(selectedRow);
                        nameField.setText(product.getName());
                        priceField.setText(String.valueOf(product.getPrice()));
                        editingIndex = selectedRow; // Simpan indeks untuk edit
                        isEditing = true; // Set mode editing
                    } else {
                        nameField.setText("");
                        priceField.setText("");
                        isEditing = false; // Reset mode editing
                    }
                }
            }
        });

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());

                if (isEditing) {
                    // Mode Edit: Update data yang ada
                    products.get(editingIndex).setName(name);
                    products.get(editingIndex).setPrice(price);
                    tableModel.setValueAt(name, editingIndex, 0);
                    tableModel.setValueAt(price, editingIndex, 1);

                    isEditing = false; // Reset mode editing
                } else {
                    // Mode Tambah: Tambahkan produk baru
                    Product product = new Product(name, price);
                    products.add(product);
                    tableModel.addRow(new Object[]{name, price});
                }

                // Reset input field
                nameField.setText("");
                priceField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Harga harus berupa angka!");
            }
        });

        // Listener untuk tombol Hapus
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                products.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                nameField.setText("");
                priceField.setText("");

                // Pastikan jika sedang dalam mode edit, reset kembali ke mode tambah
                isEditing = false;
            } else {
                JOptionPane.showMessageDialog(frame, "Tidak ada yang dipilih.");
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout()); // Layout agar tombol terlihat
        panel.add(new JLabel("Nama:"));
        panel.add(nameField);
        panel.add(new JLabel("Harga:"));
        panel.add(priceField);
        panel.add(addButton);
        panel.add(deleteButton);

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}