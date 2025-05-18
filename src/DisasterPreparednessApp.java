import utils.FileHandler;
import views.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class DisasterPreparednessApp {
	public static void main(String[] args) {
		try {
			// Set System Look and Feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			// Initialize data files
			FileHandler.createDataFiles();

			// Create and show login window
			SwingUtilities.invokeLater(() -> {
				LoginFrame loginFrame = new LoginFrame();
				loginFrame.setVisible(true);
			});

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Error starting application: " + e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}