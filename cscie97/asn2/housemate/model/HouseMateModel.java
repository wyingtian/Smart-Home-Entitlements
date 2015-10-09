package cscie97.asn2.housemate.model;
import cscie97.asn2.housemate.model.IOTDevices.Appliance;
import cscie97.asn2.housemate.model.IOTDevices.Ava;
import cscie97.asn2.housemate.model.IOTDevices.Camera;
import cscie97.asn2.housemate.model.IOTDevices.Door;
import cscie97.asn2.housemate.model.IOTDevices.Light;
import cscie97.asn2.housemate.model.IOTDevices.Oven;
import cscie97.asn2.housemate.model.IOTDevices.Pandora;
import cscie97.asn2.housemate.model.IOTDevices.Refrigerator;
import cscie97.asn2.housemate.model.IOTDevices.Sensor;
import cscie97.asn2.housemate.model.IOTDevices.SmokeDetector;
import cscie97.asn2.housemate.model.IOTDevices.TV;
import cscie97.asn2.housemate.model.IOTDevices.Thermostat;
import cscie97.asn2.housemate.model.IOTDevices.Window;
import cscie97.asn2.housemate.model.Occupants.Adult;
import cscie97.asn2.housemate.model.Occupants.Child;
import cscie97.asn2.housemate.model.Occupants.Occupant;
import cscie97.asn2.housemate.model.Occupants.Pet;
import cscie97.asn2.housemate.model.Occupants.Unknown;
import cscie97.asn2.housemate.model.exception.AppNotFoundException;
import cscie97.asn2.housemate.model.exception.HouseNotFoundException;
import cscie97.asn2.housemate.model.exception.RoomNotFoundException;
import cscie97.asn2.housemate.model.exception.SensorNotFoundException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;


/**
 * This class is a singleton class perform define, show, set, add command 
 * for the objects of the house.
 * @author ying
 *
 */
public class HouseMateModel implements ServiceInterface {

	private static final ServiceInterface MODEL = new HouseMateModel();
	HashMap<String, House> homeMap ;															// hashset
	HashMap<String, Occupant> allOccupantMap;

	private HouseMateModel() {
		homeMap = new HashMap<String, House>();
		allOccupantMap = new HashMap<String, Occupant>();
	}
	public static ServiceInterface getInstance() {
		return MODEL;
	}
	
	/**
	 * This method create a house object
	 * add added the house object to HouseMateModel allHouseMap.
	 * @param tokens the String[] is the tokenized command
	 * @param auth_token for access control  
	 */
	@Override
	public void defineHouse(String[] tokens, String auth_token ) {
		// check the format of the input, the length has to be 3;
		if (tokens.length < 3) {
			System.err.println("Please add the name of the house");
		} else if (tokens.length > 3) {
			System.err.println("The name should not contain space");
		} else {
			String houseName;
			houseName = tokens[2];
			if (homeMap.containsKey(houseName)) {
				// if the name already exist
				System.out.println(houseName + " this Name exists ");
			} else {
				// create the object and then put it in the house map
				House house1 = new House(houseName);
				homeMap.put(houseName, house1);
				System.out.println("The house defined! House name is " + "\""
						+ houseName + "\"");
			}
		}
	}

	/**
	 * Create room object
	 * @param tokens
	 * @param auth_token
	 */
	@Override
	public void defineRoom(String[] tokens, String auth_token) {
		// check if the the format is right

		if (tokens.length == 9 && tokens[3].equals("floor")
				&& tokens[5].equals("type") && tokens[7].equals("house")) {

			// try to find the house it belongs to
			if (!homeMap.containsKey(tokens[8])) {
				System.err
						.println("The House have not been created");
			} else {
				House roomIn;
				roomIn = homeMap.get(tokens[8]); // find the house it is in;
				// check if the room name already exist
				if (roomIn.roomMap.containsKey(tokens[2])) {
					System.err.println(tokens[2] + " name already exist!");
				}
				// if name not exist creaet new object.
				else {
					Room room = new Room(tokens[2], tokens[4], tokens[6],
							roomIn);
					// add the room to the house map
					roomIn.roomMap.put(tokens[2], room);
					System.out.println("The Room has been created! "
							+ room.roomInfo());
				}
			}
		} else {
			System.err
					.println("invalid input, Please try to define the room again");
		}

	}
	/**
	 * create occupant object
	* @param tokens the String[] is the tokenized command
	 * @param auth_token for access control 
	 */

	@Override
	public void defineOccupant(String[] tokens, String auth_token) {
		// check the format of the input, the length has to be 3;
		if (tokens.length < 5) {
			System.err.println("Please add the all the info of the occupant");
		} else if (tokens.length > 5) {
			System.err
					.println("Please define the name and type of the occupant");
		} else {
			String occuName;
			occuName = tokens[2];
			String occuType;
			occuType = tokens[4];
			if (allOccupantMap.containsKey(occuName)) {
				// if the name already exist
				System.err
						.println(occuName
								+ "this Name exists, If there are people with same name, Please differientated them ");
			} else {
				// create the object and then put it in the house map
				Occupant occupant;
				switch (tokens[4]) {
				case "adult":

					occupant = new Adult(occuName, occuType);
					allOccupantMap.put(occuName, occupant);
					System.out.println("Name: " + occuName + "  Type: "
							+ occuType + " defined!");
					break;
				case "child":
					occupant = new Child(occuName, occuType);
					allOccupantMap.put(occuName, occupant);
					System.out.println("Name: " + occuName + "  Type: "
							+ occuType + " defined!");
					break;
				case "pet":
					occupant = new Pet(occuName, occuType);
					allOccupantMap.put(occuName, occupant);
					System.out.println("Name: " + occuName + "  Type: "
							+ occuType + " defined!");
					break;
				case "unknown":

					occupant = new Unknown(occuName, occuType);
					allOccupantMap.put(occuName, occupant);
					System.out.println("Name: " + occuName + "  Type: "
							+ occuType + " defined!");
					break;
				default:
					System.err.println("Unknow type of occupant!");
				}

			}
		}
	}

	/**
	 * add occupant to house
	 * @param tokens the String[] is the tokenized command
	 * @param auth_token for access control 
	 */

	@Override
	public void addOccupant2House(String[] tokens, String auth_token) {
		// if the person is not defiened yet
		if (!allOccupantMap.containsKey(tokens[2])) {
			System.err.println("Can't find " + tokens[2]);
		}
		// if the room is not defined yet
		if (!homeMap.containsKey(tokens[4])) {
			System.err.println("Can't find " + tokens[4]);
		}
		// if the house and person are both defined
		if (allOccupantMap.containsKey(tokens[2])
				&& homeMap.containsKey(tokens[4])) {
			Occupant theOccup;
			House theHouse;
			theOccup = allOccupantMap.get(tokens[2]);
			theHouse = homeMap.get(tokens[4]);
			theOccup.addHouse(theHouse);
			theHouse.addOccupant(theOccup);
			System.out.println("Occupant " + tokens[2] + " is added to " + " House "
					+ tokens[4]);
		}
	}

	/**
	 * find a house based on name
	 * @param house
	 * @return
	 */
	@Override
	public House findHouse(String house, String auth_token) {
		try {
			if (homeMap.keySet().contains(house)) {
				return homeMap.get(house);
			} else {
				throw new HouseNotFoundException(house);
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * find the room based on the location
	 * <houseName>:<roomName> 
	 * @param input
	 * @return
	 */
	@Override
	public Room findRoom(String input, String auth_token) {
		House house;

		String[] loca = input.split(":");

		try {
			if (loca.length == 2) {
				house = findHouse(loca[0], auth_token);
				if (house == null) {
					throw new RoomNotFoundException("The Room --" + loca[1]
							+ "-- is not found because the House--" + loca[0]
							+ " --is not Found");
				} else if (!house.hasRoom(loca[1])) {
					throw new RoomNotFoundException("The house " + loca[0]
							+ " exist, but the Room-- " + loca[1]
							+ "-- is not Found" ,house );
				} else {
					return house.getRoom(loca[1]);
				}
			} else
				throw new RoomNotFoundException(
						"the findRoom input has wrong format");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * find the sensor based on the location
	 * @param input
	 * @return
	 */
	@Override
	public Sensor findSensor(String input,String auth_token) {
		String[] loca = input.split(":");
		String[] locaNSen = new String[2];
		Room sensorInRoom;

		try {
			if (loca.length != 3) {
				throw new SensorNotFoundException(
						"the findSensor input has wrong format: " + input);
			} else {
				locaNSen[0] = loca[0] + ":" + loca[1];
				sensorInRoom = findRoom(locaNSen[0],auth_token);
				if (sensorInRoom == null) {
					throw new SensorNotFoundException(
							"the sensor not found because the location of the sensor --"
									+ locaNSen[0] + "--is not found");
				} else if (!sensorInRoom.hasSensor(loca[2])) {
					throw new SensorNotFoundException("the location--"
							+ locaNSen[0] + " exsists but the Sensor--"
							+ loca[2] + " is not found");
				} else {
					return sensorInRoom.getSensor(loca[2]);
				}
			}
		} catch (SensorNotFoundException e) {
			return null;
		}
	}

	/**
	 * find the sensor based on the location
	 * @param input
	 * @return
	 */
	@Override
	public Appliance findAppliance(String input, String auth_token) {
		String[] loca = input.split(":");
		String[] locaNSen = new String[2];
		Room AppInRoom;

		try {
			if (loca.length != 3) {
				throw new AppNotFoundException(
						"the findAppliance input has wrong format: " + input);
			} else {
				locaNSen[0] = loca[0] + ":" + loca[1];
				AppInRoom = findRoom(locaNSen[0],auth_token);
				if (AppInRoom == null) {
					throw new AppNotFoundException(
							"the appliance not found because the location of the sensor --"
									+ locaNSen[0] + "--is not found");
				} else if (!AppInRoom.hasAppliance(loca[2])) {
					throw new AppNotFoundException("the location--"
							+ locaNSen[0] + " exists but the appliance--"
							+ loca[2] + " is not found");
				} else {
					return AppInRoom.getAppliance(loca[2]);
				}
			}
		} catch (AppNotFoundException e) {
			return null;
		}
	}
	/**
	 *create appliance object
	 * Note: appliance is an abstract class, it create its subclass based on input
	 * @param tokens the String[] is the tokenized command
	 * @param auth_token for access control
	 */
	@Override
	public void defineSensor(String[] tokens, String auth_token) {
		// theRoom variable to find the Room that the user give
		Room theRoom;
		Sensor theSensor;

		// if the length is not valid
		if (tokens.length < 7) {
			System.err.println("Need more info to define sensor");
		} else if (tokens.length > 7) {
			System.err.println("Please check your define sensor format");
		} else if ((theRoom = findRoom(tokens[6],auth_token)) == null) {

			System.err
					.println("The room"
							+ tokens[6]
							+ " is not found, Please check again or define the room first");
		} else {

			switch (tokens[4]) {
			case "smoke_detector":
				SmokeDetector theSmDe;
				theSmDe = new SmokeDetector(tokens[2], tokens[4], theRoom);
				theRoom.sensorMap.put(tokens[2], theSmDe);
				System.out.println("the smoke_detector has been defined."
						+ theSmDe.showInfo());
				theSmDe.setDefault();
				break;
			case "camera":
				theSensor = new Camera(tokens[2], tokens[4], theRoom);
				theRoom.sensorMap.put(tokens[2], theSensor);
				System.out.println("the camera has been defined."
						+ theSensor.showInfo());
				break;
			case "Ava":
				theSensor = new Ava(tokens[2], tokens[4], theRoom);
				theRoom.sensorMap.put(tokens[2], theSensor);
				System.out.println("the Ava has been defined."
						+ theSensor.showInfo());
				break;
			default:
				System.out.println("Unknow type of sensor!");
			}

		}

	}

	/**
	 *create appliance object
	 * Note: appliance is an abstract class, it create its subclass based on input
	 * @param tokens the String[] is the tokenized command
	 * @param auth_token for access control
	 */
	@Override
	public void defineAppliance(String[] tokens,String auth_token) {
		// theRoom variable to find the Room that the user give
		Room theRoom;

		// if the length is not valid
		if (tokens.length < 7) {
			System.err.println("Need more info to define appliance");
		} else if (tokens.length > 7) {
			System.err
					.println("Please check your define appliance statement format");
		} else if ((theRoom = findRoom(tokens[6],auth_token)) == null) {

			System.err
					.println("The room"
							+ tokens[6]
							+ " is not found, Please check again or define the room first");
		} else {

			switch (tokens[4]) {
			case "TV":
				TV theTV;
				theTV = new TV(tokens[2], tokens[4], theRoom);
				theRoom.applianceMap.put(tokens[2], theTV);
				System.out.println("TV has been defined."
						+ theTV.showInfo());
				theTV.setDefault();
				break;
			case "oven":
				Oven theOven;
				theOven = new Oven(tokens[2], tokens[4], theRoom);
				theRoom.applianceMap.put(tokens[2], theOven);
				System.out.println("Oven has been defined."
						+ theOven.showInfo());
				theOven.setDefault();
				break;
			case "refrigerator":
				Refrigerator theRegrigerator;
				theRegrigerator = new Refrigerator(tokens[2], tokens[4], theRoom);
				theRoom.applianceMap.put(tokens[2], theRegrigerator);
				System.out.println("Regrigerator has been defined."
						+ theRegrigerator.showInfo());
				theRegrigerator.setDefault();
				break;
			case "pandora" :
				Pandora thePandora;
				thePandora = new Pandora(tokens[2], tokens[4], theRoom);
				theRoom.applianceMap.put(tokens[2], thePandora);
				System.out.println("Pandora has been defined."
						+ thePandora.showInfo());
				thePandora.setDefault();
				break;
			case "light":
				Light theLight;
				theLight = new Light(tokens[2], tokens[4], theRoom);
				theRoom.applianceMap.put(tokens[2], theLight);
				System.out.println("Light has been defined."
						+ theLight.showInfo());
				theLight.setDefault();
				break;
			case "door":
				Door theDoor;
				theDoor = new Door(tokens[2], tokens[4], theRoom);
				theRoom.applianceMap.put(tokens[2], theDoor);
				System.out.println("Door has been defined."
						+ theDoor.showInfo());
				theDoor.setDefault();
				break;
			case "window":
				Window theWindow;
				theWindow = new Window(tokens[2], tokens[4], theRoom);
				theRoom.applianceMap.put(tokens[2], theWindow);
				System.out.println("Window has been defined."
						+ theWindow.showInfo());
				theWindow.setDefault();
				break;
			case "thermostat":
				Thermostat theThermostat;
				theThermostat = new Thermostat(tokens[2], tokens[4], theRoom);
				theRoom.applianceMap.put(tokens[2], theThermostat);
				System.out.println("Thermostat has been defined."
						+ theThermostat.showInfo());
				theThermostat.setDefault();
				break;
					
			default:
				System.err.println("Unknow type of appliance!");
			}

		}

	}
	/**
	 * set the status of the sensor or appliance 
	 * @param tokens the String[] is the tokenized command
	 * @param auth_token for access control
	 */
	
	@Override
	public void setSenOrApp(String[] tokens, String auth_token) {
		if (tokens[1].equals("sensor")) {
			Sensor theSensor;
			theSensor = findSensor(tokens[2],auth_token);
			if (theSensor != null) {
				System.out.println(theSensor.showStatus());
			}else{
				try {
					throw new SensorNotFoundException(tokens[2]+ " is not Found" );
				} catch (SensorNotFoundException e) {
				}
			}
		} else if (tokens[1].equals("appliance")) {
			Appliance theApp;
			theApp = findAppliance(tokens[2],auth_token);
			if (theApp != null && tokens.length == 7) {
				theApp.changeStatus(tokens[4],tokens[6]);
				theApp.showStatus(tokens[4]);
				 theApp.configMode();
			}else{
				try {
					throw new AppNotFoundException(tokens[2]+ " is not Found" );
				} catch (AppNotFoundException e) {
				}
			}
		}
	}
	/**
	 * show the status of the sensor or appliance 
	 * @param tokens the String[] is the tokenized command
	 * @param auth_token for access control
	 */

	@Override
	public void showSenOrApp(String[] tokens, String auth_token) {
		if (tokens[1].equals("sensor")) {
			Sensor theSensor;
			theSensor = findSensor(tokens[2],auth_token);
			if (theSensor != null) {
				System.out.println(theSensor.showStatus());
			}
		} else if (tokens[1].equals("appliance")) {
			Appliance theApp;
			theApp = findAppliance(tokens[2],auth_token);
			
			if (theApp != null && tokens.length == 5 ) {
				theApp.showStatus(tokens[4]);
				
			}else if(theApp != null && (tokens.length == 4 || tokens.length == 3)){
				theApp.showAllStatus();
			}
		}

	}
	/**
	 * show all the configuration of the house
	 * including all the rooms and devices and their status
	 * @param tokens
	 * @param auth_token
	 */

	@Override
	public void showConfigHouse(String[] tokens, String auth_token) {
		House theHouse = findHouse(tokens[3],auth_token);
		if (theHouse != null) {
			theHouse.showOccupInHouse();
			theHouse.showRoomInHouse();
		}
	}

	/**
	 * show all the configuration of the room
	 * including all the  devices and their status
	 * @param tokens
	 * @param auth_token
	 */
	@Override
	public void showConfigRoom(String[] tokens, String auth_token) {
		Room theRoom = findRoom(tokens[3],auth_token);
		if (theRoom != null) {
			System.out.println(theRoom.roomInfo());
			theRoom.showSenInRoom();
			theRoom.showAppInRoom();
		}
	}

	/**
	 * find a house based on name
	 * @param house
	 * @return
	 */
	@Override
	public void showConfigAllHouse(String auth_token) {
		for (String house : homeMap.keySet()) {
			homeMap.get(house).showOccupInHouse();
			homeMap.get(house).showRoomInHouse();
		}
	}
}