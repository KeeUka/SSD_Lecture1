package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class RemoteController {
	
	private int controlDelay = 50;
	private int controlPort = 32000;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String host = "localhost";

	public RemoteController() {
		try {
			socket = new Socket(host, controlPort);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			System.err.println("Cannot connect to host: " + host);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String write(String s) {
		try {
			dos.write(s.getBytes("UTF-8"));
			dos.flush();
			return dis.readLine();
		} catch (IOException e) {
			System.err.println("Could not write.");
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		try {
			dos.close();
			dis.close();
			socket.close();
		} catch (IOException e) {
			System.err.println("Could not close.");
			e.printStackTrace();
		}
	}

	public String init() {
		String s = "";
		s += "var elems = document.getElementsByTagName('div');";
		s += "for(var i in elems) {";
		s += "    if(elems[i].className == 'tile-container') {";
		s += "        tileContainer = elems[i];";
		s += "        break;";
		s += "    }";
		s += "}";
		return write(s);
	}

	public String dispatchEvent(String action, int key) {
		String s = "";
		s += "var ie;";
		s += "var ke = document.createEvent('KeyboardEvent');";
		s += "if (typeof ke.initKeyboardEvent !== 'undefined') {";
		s += "    ie = 'initKeyboardEvent';";
		s += "} else {";
		s += "    ie = 'initKeyEvent';";
		s += "}";
		s += "ke[ie]('" + action + "', true, true, window, false, false, false, false," + key + ", 0);";
		s += "(document.body || document).dispatchEvent(ke);";
		return write(s);
	}

	public String getBoardString() {
		String s = "";
		s += "var res = [];";
		s += "var tiles = tileContainer.children;";
		s += "for(var i= 0; i < tiles.length; i++) {";
		s += "    res.push(tiles[i].className);";
		s += "}";
		s += "res";
		return write(s);
	}

	public int[][] stringToBoardArray(String boardString) {
		int[][] cells = new int[4][4];
		String[] tiles = boardString.replace("{\"result\":[", "").replace("]}", "")
				.replaceAll("\"", "").split(",");
		for (int i = 0; i < tiles.length; i++) {
			String[] arr = tiles[i].split(" ");
			int value = Integer.parseInt(arr[1].split("-")[1]);
			int row = Integer.parseInt(arr[2].split("-")[3]) - 1;
			int col = Integer.parseInt(arr[2].split("-")[2]) - 1;
			cells[row][col] = value;
		}
		return cells;
	}

	public int [][] getBoardArray() {
		return stringToBoardArray(getBoardString());
	}
	
	public int getScore() {
		String s = "";
		s += "var score = '';";
		s += "var nodes = document.querySelector('.score-container').childNodes;";
		s += "for(var i = 0; i < nodes.length; ++i) {";
		s += "    if(nodes[i].nodeType == Node.TEXT_NODE) {";
		s += "        score += nodes[i].textContent;";
		s += "    }";
		s += "}";
		s += "score;";
		String output = write(s);
		output = output.split(":")[1].replaceAll("\"", "").replaceAll("}", "");
		return Integer.parseInt(output);
	}

	public void move(int key) {
		dispatchEvent("keydown", key);
		try {
			Thread.sleep(controlDelay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dispatchEvent("keyup", key);
	}

}
