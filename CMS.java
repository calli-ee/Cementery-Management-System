import java.util.Scanner;

// Base class for shared attributes and methods
class CemeteryManagementBase {
    protected static Scanner input = new Scanner(System.in);

    // Method for displaying a message
    protected void displayMessage(String message) {
        System.out.println(message);
    }
}

// Class to encapsulate Internment details
class Internment {
    private String burialID;
    private String name;
    private String dateOfInternment;
    private String plotID;
    private int plotSize; // in square meters
    private int plotPrice; // price of the plot

    public Internment(String burialID, String name, String dateOfInternment, String plotID, int plotSize) {
        this.burialID = burialID;
        this.name = name;
        this.dateOfInternment = dateOfInternment;
        this.plotID = plotID;
        this.plotSize = plotSize;
        this.plotPrice = plotSize * 10; // Calculate the price ($10 per sqm)
    }

    public String getBurialID() {
        return burialID;
    }

    public String getName() {
        return name;
    }

    public String getDateOfInternment() {
        return dateOfInternment;
    }

    public String getPlotID() {
        return plotID;
    }

    public int getPlotSize() {
        return plotSize;
    }

    public int getPlotPrice() {
        return plotPrice;
    }
}


// Class for managing internment records
class InternmentManager extends CemeteryManagementBase {
    String[] burialIDs = new String[100];
    String[] names = new String[100];
    String[] datesOfInternment = new String[100];
    String[] plotIDs;
	int[] plotSize = new int[100]; // Declare arrays for plot size and price
	int[] plotPrice = new int[100];
    Boolean[] isOccupied;
    int internmentCount;
    int plotCount;
	

    public InternmentManager(String[] plotIDs, Boolean[] isOccupied) {
        this.plotIDs = plotIDs;
        this.isOccupied = isOccupied;
    }

    public int manageInternments(int plotCount) {
        this.plotCount = plotCount;

        int choice;
        do {
            displayInternmentRecords();

            displayMessage("\nInternment Management:");
            displayMessage("1. Add Internment Record");
            displayMessage("2. Update Internment Record");
            displayMessage("0. Back to Main Menu");
            System.out.print("\nEnter your choice [0-2]: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> {
                    if (!addInternment()) { // If no plots are available, exit the loop
                        choice = 0;
                    }
                }
                case 2 -> updateInternment(); // Change to update method
                case 0 -> displayMessage("Returning to main menu...");
                default -> displayMessage("Invalid choice. Try again.");
            }
        } while (choice != 0);
        return internmentCount;
    }

	private void displayInternmentRecords() {
		if (internmentCount > 0) {
			displayMessage("\n-----------------------------");
			displayMessage("Internment Records:");
			displayMessage("-----------------------------");
			for (int i = 0; i < internmentCount; i++) {
				displayMessage("Burial ID: " + burialIDs[i]);
				displayMessage("Name of the Deceased: " + names[i]);
				displayMessage("Date of Internment: " + datesOfInternment[i]);
				displayMessage("Plot ID: " + plotIDs[i]);
				displayMessage("Plot Size: " + plotSize[i] + " sqm");
				displayMessage("Plot Price: $" + plotPrice[i]);
				displayMessage("-----------------------------");
			}
		} else {
			displayMessage("\n-----------------------------");
			displayMessage("No burial records available.");
			displayMessage("-----------------------------");
		}
	}


	public boolean addInternment() {
		if (!hasAvailablePlots()) {
			displayMessage("\nNo available plots. Returning to main menu...");
			return false; // Indicate no further action needed
		}

		// Display available plots before entering Burial ID
		displayAvailablePlots();

		System.out.print("\nEnter Burial ID: ");
		String burialID = input.nextLine();

		// Check if the Burial ID already exists
		for (int i = 0; i < internmentCount; i++) {
			if (burialIDs[i].equals(burialID)) {
				displayMessage("\nBurial ID already exists. Please enter a unique Burial ID.");
				return true; // Stay in the internment menu
			}
		}

		System.out.print("Enter Name of the Deceased: ");
		String name = input.nextLine();
		System.out.print("Enter Date of Internment (DD/MM/YYYY): ");
		String date = input.nextLine();
		System.out.print("Enter Plot ID: ");
		String plotID = input.nextLine();

		// Prompt for the plot size (sqm)
		System.out.print("Enter Plot Size (sqm): ");
		int plotSize = input.nextInt();  // Fixed plotSize to int
		input.nextLine(); // Consume the newline character

		if (plotAvailable(plotID)) {
			addInternment(burialID, name, date, plotID, plotSize); // Pass plot size to the method
			return true; // Indicate success
		} else {
			displayMessage("\nInvalid Plot ID or Plot is Occupied.\n");
			return true; // Stay in the internment menu
		}
	}


	// Original addInternment with plotSize and plotPrice
	public void addInternment(String burialID, String name, String dateOfInternment, String plotID, int plotSize) {
		burialIDs[internmentCount] = burialID;
		names[internmentCount] = name;
		datesOfInternment[internmentCount] = dateOfInternment;
		this.plotIDs[internmentCount] = plotID;
		this.plotSize[internmentCount] = plotSize;
		this.plotPrice[internmentCount] = plotSize * 10; // Calculate the price

		markPlotOccupied(plotID);
		internmentCount++;
		displayMessage("\nInternment Record Added Successfully.");
	}
	
	// Method for updating internment details
    private void updateInternment() {
        if (internmentCount == 0) {
            displayMessage("\nNo internment records available to update.");
            return;
        }

        System.out.print("\nEnter Burial ID to update: ");
        String burialID = input.nextLine();

        for (int i = 0; i < internmentCount; i++) {
            if (burialIDs[i].equals(burialID)) {
                displayMessage("Found Burial ID: " + burialID);
                displayMessage("Current Name: " + names[i]);
                displayMessage("Current Date of Internment: " + datesOfInternment[i]);
                displayMessage("Current Plot ID: " + plotIDs[i]);

                // Ask for new details
                System.out.print("\nEnter new Name of the Deceased (or press Enter to keep the current name): ");
                String newName = input.nextLine();
                if (!newName.isEmpty()) names[i] = newName;

                System.out.print("Enter new Date of Internment (DD/MM/YYYY) (or press Enter to keep the current date): ");
                String newDate = input.nextLine();
                if (!newDate.isEmpty()) datesOfInternment[i] = newDate;

                System.out.print("Enter new Plot ID (or press Enter to keep the current plot ID): ");
                String newPlotID = input.nextLine();
                if (!newPlotID.isEmpty()) {
                    if (plotAvailable(newPlotID)) {
                        plotIDs[i] = newPlotID; // Update plot ID
                    } else {
                        displayMessage("Invalid Plot ID or Plot is Occupied. Keeping old Plot ID.");
                    }
                }

                displayMessage("\nInternment Record Updated Successfully.");
                return;
            }
        }
        displayMessage("\nBurial ID not found.");
    }
	
	// Method to display available plots and their status
	private void displayAvailablePlots() {
		displayMessage("\nAvailable Plots:");
		displayMessage("-----------------------------");
		boolean availableFound = false;

		for (int i = 0; i < plotCount; i++) {
			if (!isOccupied[i]) {
				displayMessage("Plot ID: " + plotIDs[i] + " - Status: Available");
				availableFound = true;
			}
		}

		if (!availableFound) {
			displayMessage("No plots available.");
		}
	}
    private boolean hasAvailablePlots() {
        for (int i = 0; i < plotCount; i++) {
            if (!isOccupied[i]) {
                return true; // If any plot is available, return true
            }
        }
        return false; // No available plots
    }

    private boolean plotAvailable(String plotID) {
        for (int i = 0; i < plotCount; i++) {
            if (plotIDs[i].equals(plotID) && !isOccupied[i]) {
                return true;
            }
        }
        return false;
    }

    private void markPlotOccupied(String plotID) {
        for (int i = 0; i < plotCount; i++) {
            if (plotIDs[i].equals(plotID)) {
                isOccupied[i] = true;
                break;
            }
        }
    }

	
}

// Class for generating burial certificates
class BurialCertificateGenerator {
    public static void generateCertificate(String burialID, String name, String date, String plotID, int plotSize, int plotPrice) {
        System.out.println("\n-----------------------------");
        System.out.println("BURIAL CERTIFICATE");
        System.out.println("-----------------------------");
        System.out.println("Burial ID: " + burialID);
        System.out.println("Name of Deceased: " + name);
        System.out.println("Date of Internment: " + date);
        System.out.println("Plot ID: " + plotID);
        System.out.println("Plot Size: " + plotSize + " sqm");
        System.out.println("Plot Price: $" + plotPrice);
        System.out.println("-----------------------------");
        System.out.println("This certificate is issued to confirm the burial details.");
    }
}


// Class for managing plots
class PlotManager extends CemeteryManagementBase {
    private String[] plotIDs;
    private Boolean[] isOccupied;
    private int plotCount;

    public PlotManager(String[] plotIDs, Boolean[] isOccupied) {
        this.plotIDs = plotIDs;
        this.isOccupied = isOccupied;
        this.plotCount = 0;
    }

    public int managePlots() {
        int choice;
        do {
            // Display the list of current plots and their occupancy status
            displayPlotRecords();

            displayMessage("\nPlot Management:");
            displayMessage("1. Add Plot");
            displayMessage("2. Remove Plot");
            displayMessage("0. Back to Main Menu");
            System.out.print("\nEnter your choice [0-2]: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> addPlot();
                case 2 -> removePlot();
                case 0 -> displayMessage("Returning to main menu...");
                default -> displayMessage("Invalid choice. Try again.");
            }
        } while (choice != 0);
        return plotCount;
    }

    private void displayPlotRecords() {
        if (plotCount > 0) {
            displayMessage("\n-----------------------------");
            displayMessage("Plot Records:");
            displayMessage("-----------------------------");
            for (int i = 0; i < plotCount; i++) {
                String status = isOccupied[i] ? "Occupied" : "Available";
                displayMessage("Plot ID: " + plotIDs[i] + " - Status: " + status);
            }
            displayMessage("-----------------------------");
        } else {
            displayMessage("\n-----------------------------");
            displayMessage("No plots available.");
            displayMessage("-----------------------------");
        }
    }

    private void addPlot() {
        if (plotCount >= plotIDs.length) {
            displayMessage("\nCannot add more plots, array limit reached.");
            return;
        }

        System.out.print("\nEnter Plot ID: ");
        String newPlotID = input.nextLine(); // Get the new plot ID

        // Check if the Plot ID already exists
        for (int i = 0; i < plotCount; i++) {
            if (plotIDs[i].equals(newPlotID)) {
                displayMessage("\nPlot ID already exists. Please enter a unique Plot ID.");
                return; // Exit the method if a duplicate is found
            }
        }

        // If Plot ID is unique, add the plot
        plotIDs[plotCount] = newPlotID;
        isOccupied[plotCount] = false; // Mark the plot as available
        plotCount++;
        displayMessage("\nPlot Added Successfully.");
    }

    private void removePlot() {
        if (plotCount == 0) { // Check if there are any plots to remove
            displayMessage("\nNo plots available to remove.");
            return;
        }
        
        System.out.print("\nEnter Plot ID to remove: ");
        String plotID = input.nextLine();

        for (int i = 0; i < plotCount; i++) {
            if (plotIDs[i].equals(plotID)) {
                if (isOccupied[i]) {
                    displayMessage("\nCannot remove an occupied plot.");
                    return;
                }
                shiftPlotRecords(i);
                plotCount--;
                displayMessage("\nPlot Removed Successfully.");
                return;
            }
        }
        displayMessage("\nPlot ID not found.");
    }

    private void shiftPlotRecords(int index) {
        for (int i = index; i < plotCount - 1; i++) {
            plotIDs[i] = plotIDs[i + 1];
            isOccupied[i] = isOccupied[i + 1];
        }
    }
}

// Class for maintenance tasks
class MaintenanceManager extends CemeteryManagementBase {
    String[] maintenanceTasks = new String[100];
    String[] taskStatus = new String[100]; // Array to hold the status of each task
    int taskCount = 0;

    public void manageMaintenance() {
        int choice;
        do {
            // Display maintenance tasks first
            viewTasks();

            displayMessage("\nMaintenance Management:");
            displayMessage("1. Add Maintenance Task");
            displayMessage("2. Remove Maintenance Task");
            displayMessage("3. Change Task Status");
            displayMessage("0. Back to Main Menu");
            System.out.print("\nEnter your choice [0-3]: ");
            choice = input.nextInt();
            input.nextLine();  // Consume the newline character

            switch (choice) {
                case 1 -> addTask();
                case 2 -> removeTask();
                case 3 -> changeStatus();
                case 0 -> displayMessage("Returning to main menu...");
                default -> displayMessage("Invalid choice. Try again.");
            }
        } while (choice != 0);
    }

    private void addTask() {
        if (taskCount >= maintenanceTasks.length) {
            displayMessage("\nCannot add more tasks, array limit reached.");
            return;
        }

        System.out.print("\nEnter Maintenance Task: ");
        String task = input.nextLine();
        maintenanceTasks[taskCount] = task;

        // Set initial status to "Pending"
        taskStatus[taskCount] = "Pending";

        taskCount++;
        displayMessage("Maintenance task added successfully.");
    }

    private void viewTasks() {
        if (taskCount == 0) {
            displayMessage("\nNo maintenance tasks available.");
        } else {
            displayMessage("\n-----------------------------");
            displayMessage("Maintenance Tasks:");
            displayMessage("-----------------------------");
            for (int i = 0; i < taskCount; i++) {
                displayMessage((i + 1) + ". " + maintenanceTasks[i] + " - Status: " + taskStatus[i]);
            }
            displayMessage("-----------------------------");
        }
    }

    private void removeTask() {
        if (taskCount == 0) {
            displayMessage("\nNo tasks to remove.");
            return;
        }

        System.out.print("\nEnter the task number to remove: ");
        int taskNumber = input.nextInt();
        input.nextLine();  // Consume the newline character

        if (taskNumber < 1 || taskNumber > taskCount) {
            displayMessage("Invalid task number.");
            return;
        }

        // Shift the tasks down after removal
        for (int i = taskNumber - 1; i < taskCount - 1; i++) {
            maintenanceTasks[i] = maintenanceTasks[i + 1];
            taskStatus[i] = taskStatus[i + 1];
        }
        maintenanceTasks[taskCount - 1] = null; // Clear the last task
        taskStatus[taskCount - 1] = null; // Clear the status
        taskCount--;
        displayMessage("Maintenance task removed successfully.");
    }

    private void changeStatus() {
        if (taskCount == 0) {
            displayMessage("\nNo tasks available to change status.");
            return;
        }

        System.out.print("\nEnter the task number to change its status: ");
        int taskNumber = input.nextInt();
        input.nextLine();  // Consume the newline character

        if (taskNumber < 1 || taskNumber > taskCount) {
            displayMessage("Invalid task number.");
            return;
        }

        // Display status options
        displayMessage("\nSelect new status for task: " + maintenanceTasks[taskNumber - 1]);
        displayMessage("1. Pending");
        displayMessage("2. In Progress");
        displayMessage("3. Completed");
        System.out.print("\nEnter your choice [1-3]: ");
        int statusChoice = input.nextInt();
        input.nextLine();  // Consume the newline character

        switch (statusChoice) {
            case 1 -> taskStatus[taskNumber - 1] = "Pending";
            case 2 -> taskStatus[taskNumber - 1] = "In Progress";
            case 3 -> taskStatus[taskNumber - 1] = "Completed";
            default -> displayMessage("Invalid choice. Status not changed.");
        }

        displayMessage("Task status updated successfully.");
    }
}

// Main class for the cemetery management system
public class CMS {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Initial data arrays for plot management
        String[] plotIDs = new String[100];
        Boolean[] isOccupied = new Boolean[100];

        // Initialize plots as not occupied
        for (int i = 0; i < isOccupied.length; i++) {
            isOccupied[i] = false;
        }

        PlotManager plotManager = new PlotManager(plotIDs, isOccupied);
        InternmentManager internmentManager = new InternmentManager(plotIDs, isOccupied);
        MaintenanceManager maintenanceManager = new MaintenanceManager();

        int choice;
        int plotCount = 0, internmentCount = 0;

        do {
            System.out.println("\n-----------------------------------------");
            System.out.println("Cemetery Management System Main Menu:");
            System.out.println("-----------------------------------------");
            System.out.println("1. Manage Plots");
            System.out.println("2. Manage Internment Records");
            System.out.println("3. Generate Burial Certificate");
            System.out.println("4. Manage Maintenance Tasks");  // New option for maintenance tasks
            System.out.println("0. Exit");
            System.out.print("\nEnter your choice [0-4]: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> plotCount = plotManager.managePlots();
                case 2 -> internmentCount = internmentManager.manageInternments(plotCount);
                case 3 -> generateBurialCertificate(internmentManager); // Call method to generate burial certificate
                case 4 -> maintenanceManager.manageMaintenance(); // Call maintenance manager
                case 0 -> System.out.println("\nExiting Cemetery Management System...");
                default -> System.out.println("\nInvalid choice. Please try again.");
            }
        } while (choice != 0);

        System.out.println("\nThank you for using the Cemetery Management System!");
        input.close();
    }

	private static void generateBurialCertificate(InternmentManager internmentManager) {
		System.out.print("\nEnter Burial ID to generate certificate: ");
		Scanner input = new Scanner(System.in);
		String burialID = input.nextLine();

		for (int i = 0; i < internmentManager.internmentCount; i++) {
			if (internmentManager.burialIDs[i].equals(burialID)) {
				BurialCertificateGenerator.generateCertificate(internmentManager.burialIDs[i], internmentManager.names[i], internmentManager.datesOfInternment[i], internmentManager.plotIDs[i], internmentManager.plotSize[i], internmentManager.plotPrice[i]);
				return;
			}
		}
		System.out.println("\nBurial ID not found.");
	}
}