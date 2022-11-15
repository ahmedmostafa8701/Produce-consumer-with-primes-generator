package com.example.osassignment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Controller implements EventHandler<ActionEvent>{
	ToolBox toolBox = new ToolBox();
	Report report;
	//
	File file;
	FileWriter fileWriter;
	String fileName;
	int sampleSize;
	int bufferSize;
	boolean running = false;

	//

	@FXML
	private Button ButtonChooseFile;
	@FXML
	private Button ButtonStartProducer;
	@FXML
	private Label label1LargestPrime;
	@FXML
	private Label labelChooseFile;
	@FXML
	private Label labelElapsedTime;
	@FXML
	private Label labelNumberOfPrime;
	@FXML
	private Label labelProgramRunning;
	@FXML
	private TextField textBufferSize;
	@FXML
	private TextField textSampleSize;
	private MyProducer producer;
	private MyConsumer consumer;
	private Timeline timeline_elapsed_time;

	@FXML
	void RunLogic(MouseEvent event) {
		try {
			sampleSize = Integer.parseInt(textSampleSize.getText());
			bufferSize = Integer.parseInt(textBufferSize.getText());
		} catch (Exception e) {
			e.getStackTrace();
		}
//		System.out.println(sampleSize + " " + bufferSize);
		if (sampleSize != 0 && bufferSize != 0 && fileName != null && file != null) {
			labelProgramRunning.setText("The Program is running");
			labelProgramRunning.setTextFill(Color.GREEN);
			timeline_elapsed_time = new Timeline(new KeyFrame(Duration.millis(1), this));
			Queue<Integer> buffer = new LinkedList<>();
			Object shared = new Object();
			producer = new MyProducer(buffer, bufferSize, sampleSize, shared);
			consumer = new MyConsumer(buffer, fileWriter, shared);
			producer.setStartTime(System.currentTimeMillis());
			producer.setConsumer(consumer);
			producer.start();
			consumer.start();
			timeline_elapsed_time.setCycleCount(Animation.INDEFINITE);
			timeline_elapsed_time.play();
		} else {
			labelProgramRunning.setText("Enter Text fields values !!!");
		}
	}
	@FXML
	void chooseFile(MouseEvent event) {
		fileName = toolBox.fileHandler.filePicker();
		file = new File(fileName);
		String showString = "";
		int idx = fileName.lastIndexOf("\\");
		for (int i = idx + 1; i < fileName.length(); i++) {
			showString += fileName.charAt(i);
		}
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			System.out.println("Can't write on file");
		}
		labelChooseFile.setText(showString);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if (consumer.status == false) {
			labelProgramRunning.setText("finished process");
			timeline_elapsed_time.stop();
		}
		labelElapsedTime.setText(System.currentTimeMillis() - producer.getStartTime() + " ms");
		label1LargestPrime.setText(producer.max + "");
		labelNumberOfPrime.setText(producer.size + "");
	}
}