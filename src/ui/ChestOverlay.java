package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import entities.Player;
import gamestates.PlayingState;
import main.Game;
import objects.Chest;

public class ChestOverlay {
	private PlayingState playingState;

	private JDialog jDialog;
	private JLabel statusLabel; // Label to show status (locked/unlocked)
	private JLabel questionLabel; // Label to show the question
	private JTextField jTextField; // TextField for user input
	private JButton closeButton; // Close button
	private JButton openButton; // Open button
	private int s = Game.scale;
	private String[] questions = { "What does OOP stand for?", "What does SQL stand for?",
			"What is used to repeat code?", "What is a collection of data?", "What is the brain of the computer?" };
	private String[] answers = { "OBJECT ORIENTED PROGRAMMING", "STRUCTURED QUERY LANGUAGE", "LOOP", "DATABASE",
			"CPU" };

	private int currentQuestionIndex = 0; // Track the current question
	private Random rand;
	private Chest currentChest; // Reference to the current chest

	public ChestOverlay(PlayingState playingState) {
		this.playingState = playingState;
		rand = new Random();
		newWindow();
	}

	// Method to set the current chest and update the question
	public void setChest(Chest chest) {
		this.currentChest = chest; // Set the current chest
	}

	private void updateQuestion() {
		// Logic to update the question based on the current chest
		// For example, you can randomly select a question or set a specific one
		currentQuestionIndex = rand.nextInt(questions.length);
		questionLabel.setText(questions[currentQuestionIndex]); // Update the question label
	}

	private void newWindow() {
		jDialog = new JDialog();

		// to be added to the dialog
		jLabelProperties();
		jButtonProperties();
		jTextFieldProperties();
		jDialogProperties();
	}

	private void jDialogProperties() {
		jDialog.setSize(Game.scale * 150, Game.scale * 100); // Adjust size as needed
		jDialog.setLayout(null);
		jDialog.setLocationRelativeTo(null);
		jDialog.setUndecorated(true);
		jDialog.setBackground(new Color(255, 255, 255, 180));

		jDialog.add(statusLabel);
		jDialog.add(questionLabel);
		jDialog.add(jTextField);
		jDialog.add(closeButton);
		jDialog.add(openButton);
	}

	private void jTextFieldProperties() {
		jTextField = new JTextField(); // Initialize JTextField
		jTextField.setDoubleBuffered(true);
		jTextField.setBounds(s * 15, s * 35, s * 120, s * 10); // Set position and size for the JTextField
	}

	private void jLabelProperties() {
		statusLabel = new JLabel("CHEST IS LOCKED", JLabel.CENTER);
		statusLabel.setFont(new Font("Arial Black", Font.PLAIN, 28));
		statusLabel.setBounds(s * 26, s * 5, s * 100, s * 10); // Set position and size for the status label

		questionLabel = new JLabel(questions[currentQuestionIndex], JLabel.CENTER);
		questionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		questionLabel.setBounds(s * 25, s * 15, s * 100, s * 10); // Set position and size for the question label
	}

	private void jButtonProperties() {
		closeButton = new JButton("Close");
		openButton = new JButton("Open");

		closeButton.setBounds(s * 15, s * 50, s * 35, s * 10); // Set position and size for the Close button
		openButton.setBounds(s * 83, s * 50, s * 35, s * 10); // Set position and size for the Open button

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false); // Close the dialog
			}
		});

		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkAnswer(); // Check the answer when Open is clicked
			}
		});
	}

	private void checkAnswer() {
		String text = jTextField.getText().toUpperCase(); // Get user input and convert to uppercase

		if (text.contains(answers[currentQuestionIndex])) {
			statusLabel.setText("CHEST UNLOCKED");
			rewards();
			currentChest.openChest(); // Open the chest when the answer is correct

			jTextField.setText("");
			updateQuestion();
			setVisible(false);

			// Optionally, you can add logic to handle what happens when the chest is
			// unlocked
		} else {
			// Move to the next question or show an error
			statusLabel.setText("CHEST IS LOCKED");
		}
	}

	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
	}

	public void setVisible(boolean isVisible) {
		jDialog.setVisible(isVisible);

		System.out.println(currentQuestionIndex);
		if (isVisible) {
			jTextField.setFocusable(true); // Focus on the text field when shown
		} else {

		}
	}

	private void rewards() {
		UserInterface.redPotionCount += 20;
		UserInterface.bluePotionCount += 20;
		Player.arrowCount += 50;
	}
}