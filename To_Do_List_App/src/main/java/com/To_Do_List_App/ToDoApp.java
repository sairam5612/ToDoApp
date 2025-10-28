package com.To_Do_List_App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

class TodoApp extends JFrame {

    // --- Data Model ---
    // We use a custom 'Task' object instead of a String
    private DefaultListModel<Task> taskListModel;

    // --- UI Components ---
    private JList<Task> taskList;
    private JTextField taskField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton clearCompletedButton;

    /**
     * A simple class to hold our task data
     */
    private static class Task {
        private String description;
        private boolean isCompleted;

        public Task(String description) {
            this.description = description;
            this.isCompleted = false;
        }

        public String getDescription() { return description; }
        public boolean isCompleted() { return isCompleted; }
        public void setCompleted(boolean completed) { isCompleted = completed; }
    }

    /**
     * Custom Renderer: This class defines HOW to draw each 'Task' in the JList.
     * This is the key to the "better UI".
     */
    private static class TaskCellRenderer extends JPanel implements ListCellRenderer<Task> {

        private JCheckBox checkBox;
        private JLabel label;
        private Font normalFont;
        private Font strikethroughFont;

        public TaskCellRenderer() {
            setLayout(new BorderLayout(8, 8)); // Increased gap for a cleaner look
            setBorder(new EmptyBorder(10, 10, 10, 10)); // Increased padding for taller rows
            setOpaque(true);

            checkBox = new JCheckBox();
            label = new JLabel();

            // *** FONT SIZE INCREASED HERE ***
            float newFontSize = 18f;

            // Create a font with a strikethrough attribute
            normalFont = label.getFont().deriveFont(newFontSize);
            Map attributes = normalFont.getAttributes();
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            strikethroughFont = new Font(attributes).deriveFont(newFontSize);

            label.setFont(normalFont);
            add(checkBox, BorderLayout.WEST);
            add(label, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index,
                                                      boolean isSelected, boolean cellHasFocus) {

            label.setText(task.getDescription());
            checkBox.setSelected(task.isCompleted());

            // Apply strikethrough and gray color if completed
            if (task.isCompleted()) {
                label.setFont(strikethroughFont);
                label.setForeground(Color.GRAY);
            } else {
                label.setFont(normalFont);
                label.setForeground(list.getForeground());
            }

            // Handle selection colors
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }


    // --- Main Application Constructor ---
    public TodoApp() {
        // --- 1. Set up the Main Window ---
        setTitle("To-Do List");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center window
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding

        // --- 2. Create Model and List ---
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);

        // ** Apply the custom renderer **
        taskList.setCellRenderer(new TaskCellRenderer());


        taskList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Add padding to the list itself
        taskList.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Create the scroll pane
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // --- 3. Create Top Input Panel ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(new EmptyBorder(0, 0, 10, 0)); // Padding at the bottom
        taskField = new JTextField();
        taskField.setFont(new Font("Arial", Font.PLAIN, 16));
        addButton = new JButton("Add Task");

        topPanel.add(taskField, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        // --- 4. Create Bottom Control Panel ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        deleteButton = new JButton("Delete Selected"); // Supports multi-delete
        clearCompletedButton = new JButton("Clear Completed");

        bottomPanel.add(deleteButton);
        bottomPanel.add(clearCompletedButton);

        // --- 5. Add Panels to Frame ---
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- 6. Set up Listeners ---
        setupListeners();
    }

    private void setupListeners() {

        // Add Task (supports button click and pressing Enter)
        // This is a lambda expression, a modern way to write an ActionListener
        Runnable addTaskAction = () -> {
            String taskDescription = taskField.getText().trim();
            if (!taskDescription.isEmpty()) {
                taskListModel.addElement(new Task(taskDescription));
                taskField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Task cannot be empty.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        };
        addButton.addActionListener(e -> addTaskAction.run());
        taskField.addActionListener(e -> addTaskAction.run());



        deleteButton.addActionListener(e -> {
            // Get all selected indices
            int[] selectedIndices = taskList.getSelectedIndices();

            if (selectedIndices.length == 0) {
                // Nothing is selected
                JOptionPane.showMessageDialog(this, "Please select task(s) to delete.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Loop BACKWARDS to avoid index shifting errors
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                taskListModel.remove(selectedIndices[i]);
            }
        });

        // Clear Completed Tasks
        clearCompletedButton.addActionListener(e -> {
            // Loop backwards to avoid index issues when removing
            for (int i = taskListModel.getSize() - 1; i >= 0; i--) {
                if (taskListModel.getElementAt(i).isCompleted()) {
                    taskListModel.remove(i);
                }
            }
        });

        // ** Listener to toggle completion status **
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check for a single, left-button click with NO modifiers
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1 &&
                        !e.isControlDown() && !e.isShiftDown()) {

                    int index = taskList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        Task task = taskListModel.getElementAt(index);
                        task.setCompleted(!task.isCompleted());

                        // Repaint just the one cell that changed
                        taskList.repaint(taskList.getCellBounds(index, index));
                    }
                }
            }
        });
    }

    // --- Main Method ---
    public static void main(String[] args) {
        try {
            // ** Set the Look and Feel to the system's native look **
            // This is the line that replaces the FlatLaf library
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new TodoApp().setVisible(true);
        });
    }
}