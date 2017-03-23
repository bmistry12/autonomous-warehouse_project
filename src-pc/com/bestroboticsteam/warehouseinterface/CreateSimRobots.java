package com.bestroboticsteam.warehouseinterface;

import com.bestroboticsteam.communication.RobotNames;
import com.bestroboticsteam.robotsmanagement.Direction;
import com.bestroboticsteam.robotsmanagement.RobotInfo;
import com.bestroboticsteam.robotsmanagement.RobotsManager;
import java.awt.Point;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import rp.robotics.MobileRobotWrapper;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.simulation.MovableRobot;
import rp.robotics.simulation.SimulatedRobots;

public class CreateSimRobots {
	public static GridMap map = MyGridMap.createRealWarehouse();
	public static MapBasedSimulation sim = new MapBasedSimulation(map);
	public static GridMapVisualisation mapVis = new GridMapVisualisation(map, sim.getMap());
	private static MobileRobotWrapper<MovableRobot> wrapper;
	private static RobotInfo[] robotArray;
	public static Point dPos = null;
	public static Point jPos = null;
	public static Point hPos = null;
	final static Logger logger = Logger.getLogger(CreateSimRobots.class);
	
	public static GridMapVisualisation robots(RobotsManager robots) {
		robotArray = new RobotInfo[robots.getRobotInfos().length];
		int numOfRobots = getRobotNumber();
		robotArray = getRobotInfos(robots);
		for (int i = 0; i< numOfRobots; i++){
			GridPose gridStart = new GridPose(getPosX(i), getPosY(i), Heading.PLUS_Y);
			logger.info("Visualisation of " + robotArray[i].getName() + " is starting");
			wrapper = sim.addRobot(SimulatedRobots.makeConfiguration(false, false), map.toPose(gridStart));
			RobotSimController controller = new RobotSimController(wrapper.getRobot(), map, gridStart, i);
			controller.start();
			getPos(i);
		}
		MapVisualisationComponent.populateVisualisation(mapVis, sim);
		return mapVis;
	}
	
	public static int getRobotNumber(){
		int numOfRobots = robotArray.length;
		return numOfRobots;
	}
	
	public static RobotInfo[] getRobotInfos(RobotsManager robots){
		return robots.getRobotInfos();
	}
	
	public static int getPosX(int robot){
		Point pos = robotArray[robot].getPosition();
		return pos.x;
	}
	
	public static int getPosY(int robot){
		Point pos = robotArray[robot].getPosition();
		return pos.y;
	}
	
	public static void getPos(int robot){
		Point pos = robotArray[robot].getPosition();
		String name = getName(robot);
		if (name == RobotNames.ROBOT_3_NAME){
			dPos = pos;
		} else if (name == RobotNames.ROBOT_1_NAME){
			jPos = pos;
		} else if (name == RobotNames.ROBOT_2_NAME){
			hPos = pos;
		}
	}
	
	public static Point getGoalPoint(int robot){
		if (getPath(robot).size() == 0){
			Point b = new Point(-1,-1);
			return b;
		} else {
			Point a = robotArray[robot].getCurrentJob().getPosition();
		//	Point a = getPath(robot).removeLast();
			return a;
		} 
	}
	
	public static Direction getDirection(int robot){
		return robotArray[robot].getDirection();
	}
	public static String getName(int robot){
		return robotArray[robot].getName();
	}
	
	public static LinkedList<Point> getPath(int robot){
		return robotArray[robot].getCurrentPath();
	}	
}