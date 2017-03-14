package com.bestroboticsteam.warehouseinterface;

import java.awt.Color;
import java.awt.Dimension;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.visualisation.GridMapVisualisation;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;

public class InterfaceView extends JFrame {
	final static Logger logger = Logger.getLogger(InterfaceView.class);

	// Go on right hand panel - jobs
	public JPanel jobInProgPanel = new JPanel();
	public JPanel jobListPanel = new JPanel();

	// create visualisation of graph
	GridMapVisualisation mapVis = CreateSimRobots.robots();

	public JPanel robotStatus = new JPanel();
	public JLabel commLabel = new JLabel();
	// will display list of orders -> from JobSelection class via interfaceController
	public JTextArea text = new JTextArea();
	// will display orders in progress -> from jobAssignment via iC
	public JTextArea text2 = new JTextArea();
	public JButton cancel;
	public JPanel buttonPanel = new JPanel();
	
	public InterfaceView() {
		this.setTitle("Warehouse Management Interface");
		this.setSize(1200, 700);// set size of frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// right hand panel - jobs
		JPanel jobPanel = new JPanel();
		JLabel toDoLabel = new JLabel("Job List");
		JLabel doingLabel = new JLabel("Jobs in Progress");
		
		// list of jobs in progress
		toDoLabel.setPreferredSize(new Dimension(150, 20));
		toDoLabel.setOpaque(true);
		text2.setPreferredSize(new Dimension(270, 350));
		text2.setOpaque(true);
		text2.setEditable(false);

		// list of first 10 jobs
		doingLabel.setPreferredSize(new Dimension(150, 20));
		doingLabel.setOpaque(true);
		text.setPreferredSize(new Dimension(220, 350));
		text.setOpaque(true);
		text.setEditable(false);

		jobInProgPanel.setPreferredSize(new Dimension(300, 400));
		jobListPanel.setPreferredSize(new Dimension(300, 400));
		buttonPanel.setPreferredSize(new Dimension(50,350));
		
		robotStatus.setPreferredSize(new Dimension(600, 50));

		// this is just for testing - ignore it
		jobInProgPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		jobListPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		text.setBorder(BorderFactory.createLineBorder(Color.pink));
		text2.setBorder(BorderFactory.createLineBorder(Color.red));
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.yellow));
		text.setBackground(null);
		text2.setBackground(null);
		robotStatus.setBorder(BorderFactory.createLineBorder(Color.cyan));
		
		jobListPanel.add(toDoLabel);
		jobListPanel.add(text);
		jobListPanel.add(buttonPanel);
		jobInProgPanel.add(doingLabel);
		jobInProgPanel.add(text2);
		jobPanel.add(jobListPanel);
		jobPanel.add(jobInProgPanel);
		robotStatus.add(commLabel);
		jobPanel.add(robotStatus);
		buttonPanel.setVisible(true);
		jobListPanel.setVisible(true);
		jobInProgPanel.setVisible(true);
		jobPanel.setVisible(true);

		// create split pane and add the map and job panels to it respectively
		JSplitPane split = new JSplitPane();
		split.setSize(1000, 300);
		split.setDividerLocation(500);
		split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(mapVis);
		split.setRightComponent(jobPanel);

		// add split pane to frame
		this.add(split);
		this.setVisible(true);
	}

	private JButton addButton() {
		cancel = new JButton();
		cancel.setPreferredSize(new Dimension(30,20));
		buttonPanel.add(cancel);
		return cancel;
	}
	
	// job list methods
	public void setJobList(String jobs) {	
		logger.debug("Input fron IC " + jobs);
		String newline = "\n";
		String[] parts = jobs.split(" : ");
		for (int i = 0; i < parts.length; i++) {
			logger.debug("order adding to list: " + parts[i]);
			text.append(parts[i] + newline);
			addButton();
		}
	}

	public void addOneToJobList(String job) {
		String newline = "\n";
		text.append(job + newline);
	}

	public void emptyJobList() {
		text.setText("");
	}

	public String getJobList() {
		return text.getText();
	}

	// job in progress list methods
	public void setInProgList(String jobProgText){
		emptyProgList();
		String newline = "\n";
		String[] parts = jobProgText.split(" : ");
		for (int i = 0; i < parts.length; i++) {
			text2.append(parts[i] + newline);
		}
	}

	public void emptyProgList() {
		text2.setText("");
	}

	public String getInProgList() {
		return text2.getText();
	}

}