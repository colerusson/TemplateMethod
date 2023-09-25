import java.io.*;
import java.util.*;

public class LineCount extends Template {
	private static int _totalLineCount;
	
	public LineCount(String directory, String filePattern, boolean recurse) {
		super(directory, filePattern, recurse);
		_totalLineCount = 0;
	}

	public void searchFileLines(File file) {
		try {
			Reader reader = new BufferedReader(new FileReader(file));
			int curLineCount = 0;
			try {
				Scanner input = new Scanner(reader);
				while (input.hasNextLine()) {
					String line = input.nextLine();
					++curLineCount;
					++_totalLineCount;
				}
			} finally {
				System.out.println(curLineCount + "  " + file);
				reader.close();
			}
		} catch (IOException e) {
			unreadableFile(file);
		}
	}

	public static void main(String[] args) {
		String directory;
		String filePattern;
		boolean recurse;
		
		if (args.length == 2) {
			recurse = false;
			directory = args[0];
			filePattern = args[1];
		}
		else if (args.length == 3 && args[0].equals("-r")) {
			recurse = true;
			directory = args[1];
			filePattern = args[2];
		}
		else {
			usage();
			return;
		}
		
		LineCount lineCounter = new LineCount(directory, filePattern, recurse);
		lineCounter.run();
		System.out.println("TOTAL: " + _totalLineCount);
	}
	
	private static void usage() {
		System.out.println("USAGE: java LineCount {-r} <dir> <file-pattern>");
	}

}
