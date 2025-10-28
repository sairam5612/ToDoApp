# Java Swing To-Do List Application

This is a desktop To-Do list application built with Java Swing. It was created as part of the Elevate Labs Java Developer Internship (Task 6).

This application allows users to add, manage, and delete tasks through a clean, native-looking user interface. It demonstrates advanced Swing concepts, including custom list renderers, data models, and multi-selection.




## 🚀 Features

* **Add Tasks:** Quickly add new tasks using the text field and "Add" button, or by pressing the **Enter key**.
* **Mark as Complete:** Simply click on any task in the list to toggle its completion status.
* **Visual Feedback:** Completed tasks are visually marked with a **checkbox** and **strikethrough text**.
* **Delete Tasks:** Select one or more tasks and delete them.
* **Multi-Select:** Supports multi-delete by using **`Ctrl+Click`** to select multiple tasks at once.
* **Clear Completed:** A dedicated button to remove all completed tasks from the list.
* **Error Handling:** Provides user-friendly `JOptionPane` dialogs for errors (e.g., adding an empty task, deleting with no selection).
* **Native UI:** Uses Java's `SystemLookAndFeel` to make the application look and feel native to the user's operating system (Windows, macOS, or Linux).

## 💡 Core Concepts Demonstrated

This project utilizes several key Java Swing and OOP concepts:

* **GUI Components:** `JFrame` (the main window), `JPanel` (for layout), `JList` (for the tasks), `JScrollPane` (for the scrollbar), `JButton`, and `JTextField`.
* **Layout Managers:** `BorderLayout` for the main window structure and `FlowLayout` for the button panel.
* **Event Handling:** `ActionListener` (for buttons and text field) and `MouseAdapter` (for clicking on the list items).
* **Swing Data Models:** `DefaultListModel` to dynamically manage the data (tasks) shown in the `JList`.
* **Custom `ListCellRenderer`:** A custom `TaskCellRenderer` class (as an inner class) is used to draw each list item as a panel containing a `JCheckBox` and a `JLabel`. This is what allows for the strikethrough text and checkbox UI.
* **OOP:** A custom `Task` inner class is used to store both the task's description and its completion status (`boolean`).
* **Thread Safety:** The application is correctly launched on the Event Dispatch Thread (EDT) using `SwingUtilities.invokeLater`.

## 🖥️ How to Run

1.  **Compile the code:**
    ```sh
    javac TodoApp.java
    ```

2.  **Run the application:**
    ```sh
    java TodoApp
    ```
