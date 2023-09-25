import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Template {
    private final String _directory;
    private final boolean _recurse;
    private final Matcher _fileMatcher;

    public Template(String directory, String filePattern, boolean recurse) {
        _directory = directory;
        _recurse = recurse;
        _fileMatcher = Pattern.compile(filePattern).matcher("");
    }

    void run() {
        searchDirectory(new File(_directory));
    }

    private void searchDirectory(File dir) {
        if (!dir.isDirectory()) {
            nonDir(dir);
        }

        if (!dir.canRead()) {
            unreadableDir(dir);
        }

        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                if (file.canRead()) {
                   searchFile(file);
                }
                else {
                    unreadableFile(file);
                }
            }
        }

        if (_recurse) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    searchDirectory(file);
                }
            }
        }
    }

    private void searchFile(File file) {
        String fileName = getFileName(file);
        _fileMatcher.reset(fileName);
        if (_fileMatcher.find()) {
            searchFileLines(file);
        }
    }

    public abstract void searchFileLines(File file);

    private void nonDir(File dir) {
        System.out.println("" + dir + " is not a directory");
    }

    private void unreadableDir(File dir) {
        System.out.println("Directory " + dir + " is unreadable");
    }

    static void unreadableFile(File file) {
        System.out.println("File " + file + " is unreadable");
    }

    String getFileName(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException var3) {
            return "";
        }
    }
}
