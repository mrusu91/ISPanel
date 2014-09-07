package com.vrx.ispanel.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellExecutor {

	private static ShellExecutor instance;

	private ShellExecutor() {

	}

	public static ShellExecutor getInstance() {
		if (instance == null)
			instance = new ShellExecutor();
		return instance;
	}

	public BufferedReader exec(String[] cmdarray) throws IOException,
			InterruptedException {
		Process process = Runtime.getRuntime().exec(cmdarray);
		if (process.waitFor() == 0) {
			return new BufferedReader(new InputStreamReader(
					process.getInputStream()));
		}
		throw new IOException("Termination with errors!");
	}
}
