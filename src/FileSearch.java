import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileSearch extends Template {
	private static Matcher _searchMatcher;
	private static int _totalMatches;
	
	public FileSearch(String directory, String filePattern, String searchPattern, boolean recurse) {
		super(directory, filePattern, recurse);
		_searchMatcher = Pattern.compile(searchPattern).matcher("");
		_totalMatches = 0;
	}
	
	public void searchFileLines(File file) {
		try {
			Reader reader = new BufferedReader(new FileReader(file));
			int curMatches = 0;
			try {
				Scanner input = new Scanner(reader);
				while (input.hasNextLine()) {
					String line = input.nextLine();
					_searchMatcher.reset(line);
					if (_searchMatcher.find()) {
						if (++curMatches == 1) {
							System.out.println("FILE: " + file);
						}

						System.out.println(line);
						++_totalMatches;
					}
				}
			} finally {
				reader.close();
				if (curMatches > 0) {
					System.out.println("MATCHES: " + curMatches);
				}
			}
		} catch (IOException e) {
			unreadableFile(file);
		}
	}

	public static void main(String[] args) {
		String directory;
		String filePattern;
		String searchPattern;
		boolean recurse;
		
		if (args.length == 3) {
			recurse = false;
			directory = args[0];
			filePattern = args[1];
			searchPattern = args[2];
		}
		else if (args.length == 4 && args[0].equals("-r")) {
			recurse = true;
			directory = args[1];
			filePattern = args[2];
			searchPattern = args[3];
		}
		else {
			usage();
			return;
		}
		
		FileSearch fileSearch = new FileSearch(directory, filePattern, searchPattern, recurse);
		fileSearch.run();
		System.out.println("TOTAL MATCHES: " + _totalMatches);
	}

	private static void usage() {
		System.out.println("USAGE: java FileSearch {-r} <dir> <file-pattern> <search-pattern>");
	}

}
