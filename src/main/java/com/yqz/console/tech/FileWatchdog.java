package com.yqz.console.tech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class FileWatchdog extends Thread {

	/**
	 * The default delay between every file modification check, set to 60
	 * seconds.
	 */
	static final private long DEFAULT_DELAY = 60000;
	/**
	 * The name of the file to observe for changes.
	 */
	protected String filename;

	/**
	 * The delay to observe between every check. By default set
	 * {@link #DEFAULT_DELAY}.
	 */
	protected long delay = DEFAULT_DELAY;

	File file;
	long lastModif = 0;
	boolean warnedAlready = false;
	boolean interrupted = false;

	public static void main(String[] args) throws InterruptedException {
		FileWatchdog dog = new FileWatchdog("e:\\doc\\z.txt") {

			@Override
			protected void doOnChange() {

				try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
					StringBuilder sb = new StringBuilder();
					while (true) {
						String line = reader.readLine();
						if (line == null)
							break;
						sb.append(line);
					}
					System.out.println(sb.toString());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		};
		dog.setDelay(1000);
		dog.start();
		dog.join();
	}

	protected FileWatchdog(String filename) {
		super("FileWatchdog");
		this.filename = filename;
		file = new File(filename);
		setDaemon(true);
		checkAndConfigure();
	}

	/**
	 * Set the delay to observe between each check of the file changes.
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	abstract protected void doOnChange();

	protected void checkAndConfigure() {
		boolean fileExists;
		try {
			fileExists = file.exists();
		} catch (SecurityException e) {
			System.out.println("Was not allowed to read check file existance, file:[" + filename + "].");
			interrupted = true; // there is no point in continuing
			return;
		}

		if (fileExists) {
			long l = file.lastModified(); // this can also throw a
											// SecurityException
			if (l > lastModif) { // however, if we reached this point this
				lastModif = l; // is very unlikely.
				doOnChange();
				warnedAlready = false;
			}
		} else {
			if (!warnedAlready) {
				System.out.println("[" + filename + "] does not exist.");
				warnedAlready = true;
			}
		}
	}

	public void run() {
		while (!interrupted) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// no interruption expected
			}
			checkAndConfigure();
		}
	}
}
