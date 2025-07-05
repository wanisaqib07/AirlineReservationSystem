import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Flight {
    String number, source, destination;
    int seats;

    Flight(String number, String source, String destination, int seats) {
        this.number = number;
        this.source = source;
        this.destination = destination;
        this.seats = seats;
    }

    public String getDetails() {
        return number + " | " + source + " ➝ " + destination + " | Seats: " + seats;
    }
}

class Reservation {
    String username;
    Flight flight;

    Reservation(String username, Flight flight) {
        this.username = username;
        this.flight = flight;
    }

    public String getDetails() {
        return "User: " + username + " | Flight: " + flight.number;
    }
}

public class AirlinereservationGUI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField usernameField, loginField;
    private JPasswordField passwordField, loginPassField;
    private String currentUser;

    private ArrayList<Flight> flights = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();

    public AirlinereservationGUI() {
        addSampleFlights();
        setupGUI();
    }

    private void addSampleFlights() {
        flights.add(new Flight("AI101", "Delhi", "Mumbai", 5));
        flights.add(new Flight("AI202", "Bangalore", "Kolkata", 3));
        flights.add(new Flight("AI303", "Jammu", "Chandigarh", 2));
    }

    private void setupGUI() {
        frame = new JFrame("Airline Reservation System");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPanel(), "Login");
        mainPanel.add(menuPanel(), "Menu");
        mainPanel.add(viewFlightsPanel(), "ViewFlights");
        mainPanel.add(bookFlightPanel(), "BookFlight");
        mainPanel.add(myReservationsPanel(), "MyReservations");

        frame.add(mainPanel);
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        cardLayout.show(mainPanel, "Login");
    }

    private JPanel loginPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        panel.setBorder(BorderFactory.createTitledBorder("Login/Register"));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginField = new JTextField();
        loginPassField = new JPasswordField();

        JButton registerBtn = new JButton("Register");
        JButton loginBtn = new JButton("Login");

        panel.add(new JLabel("Register - Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Register - Password:"));
        panel.add(passwordField);
        panel.add(registerBtn);
        panel.add(new JLabel("---- OR ----"));
        panel.add(new JLabel("Login - Username:"));
        panel.add(loginField);
        panel.add(new JLabel("Login - Password:"));
        panel.add(loginPassField);
        panel.add(loginBtn);

        registerBtn.addActionListener(e -> {
            currentUser = usernameField.getText();
            JOptionPane.showMessageDialog(frame, "Registered & logged in as " + currentUser);
            cardLayout.show(mainPanel, "Menu");
        });

        loginBtn.addActionListener(e -> {
            currentUser = loginField.getText();
            JOptionPane.showMessageDialog(frame, "Logged in as " + currentUser);
            cardLayout.show(mainPanel, "Menu");
        });

        return panel;
    }

    private JPanel menuPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Main Menu"));

        JButton viewBtn = new JButton("View Flights");
        JButton bookBtn = new JButton("Book Flight");
        JButton myResBtn = new JButton("My Reservations");
        JButton logoutBtn = new JButton("Logout");

        panel.add(viewBtn);
        panel.add(bookBtn);
        panel.add(myResBtn);
        panel.add(logoutBtn);

        viewBtn.addActionListener(e -> cardLayout.show(mainPanel, "ViewFlights"));
        bookBtn.addActionListener(e -> cardLayout.show(mainPanel, "BookFlight"));
        myResBtn.addActionListener(e -> cardLayout.show(mainPanel, "MyReservations"));
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            JOptionPane.showMessageDialog(frame, "Logged out successfully!");
            cardLayout.show(mainPanel, "Login");
        });

        return panel;
    }

    private JPanel viewFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Available Flights"));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        JButton back = new JButton("Back");

        for (Flight f : flights) {
            area.append(f.getDetails() + "\n");
        }

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);

        back.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        return panel;
    }

    private JPanel bookFlightPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(BorderFactory.createTitledBorder("Book Flight"));

        JTextField flightField = new JTextField();
        JButton bookBtn = new JButton("Book");
        JButton backBtn = new JButton("Back");

        panel.add(new JLabel("Enter Flight Number to Book:"));
        panel.add(flightField);
        panel.add(bookBtn);
        panel.add(backBtn);

        bookBtn.addActionListener(e -> {
            String num = flightField.getText().trim();
            Flight selected = null;
            for (Flight f : flights) {
                if (f.number.equalsIgnoreCase(num) && f.seats > 0) {
                    selected = f;
                    break;
                }
            }
            if (selected != null) {
                selected.seats--;
                reservations.add(new Reservation(currentUser, selected));
                JOptionPane.showMessageDialog(frame, "Booking successful!");
            } else {
                JOptionPane.showMessageDialog(frame, "Flight not found or full!");
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        return panel;
    }

    private JPanel myReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("My Reservations"));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> reservationList = new JList<>(listModel);
        JButton refreshBtn = new JButton("Refresh");
        JButton cancelBtn = new JButton("Cancel Selected");
        JButton backBtn = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(backBtn);

        refreshBtn.addActionListener(e -> {
            listModel.clear();
            for (Reservation r : reservations) {
                if (r.username.equals(currentUser)) {
                    listModel.addElement(r.getDetails());
                }
            }
            if (listModel.isEmpty()) {
                listModel.addElement("No reservations found.");
            }
        });

        cancelBtn.addActionListener(e -> {
            int index = reservationList.getSelectedIndex();
            if (index >= 0 && !listModel.get(index).startsWith("No")) {
                String selectedText = listModel.get(index);
                String flightNum = selectedText.split("Flight: ")[1].trim();

                Reservation toRemove = null;
                for (Reservation r : reservations) {
                    if (r.username.equals(currentUser) && r.flight.number.equalsIgnoreCase(flightNum)) {
                        r.flight.seats++;
                        toRemove = r;
                        break;
                    }
                }
                if (toRemove != null) {
                    reservations.remove(toRemove);
                    listModel.remove(index);
                    JOptionPane.showMessageDialog(frame, "Reservation cancelled.");
                }
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        panel.add(new JScrollPane(reservationList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        new AirlinereservationGUI();
    }
}