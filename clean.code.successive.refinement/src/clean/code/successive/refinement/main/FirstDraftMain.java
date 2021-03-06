package clean.code.successive.refinement.main;

import java.text.ParseException;

import clean.code.successive.refinement.first.draft.*;

public class FirstDraftMain {

	public static void main(String[] args) {

		try {
			Args arg = new Args("l,p#,d*", args);
			boolean logging = arg.getBoolean('l');
			int port = arg.getInt('p');
			String directory = arg.getString('d');
			executeApplication(logging, port, directory);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private static void executeApplication(boolean logging, int port,
			String directory) {
		System.out.println("logging: " + logging + ", port: " + port
				+ ", directory:" + directory);
	}

}
