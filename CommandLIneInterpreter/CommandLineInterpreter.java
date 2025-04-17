
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.nio.file.*;
import java.util.stream.Collectors;

public class CommandLineInterpreter {

    private String CurrentDirectory;

    private ArrayList<String> split(String S) {
        ArrayList<String> list = new ArrayList<>();
        int pos;
        while (!S.isEmpty()) {
            String Word;
            if (S.charAt(0) == '\"') {
                int endQuote = S.indexOf("\"", 1);
                Word = S.substring(1, endQuote);
                list.add(Word);
                S = S.substring(endQuote + 1).trim();
            } else {
                pos = S.indexOf(" ");
                if (pos >= 0) {
                    Word = S.substring(0, pos);
                    S = S.substring(pos + 1).trim();
                } else {
                    Word = S;
                    S = "";
                }
                list.add(Word);
            }
        }
        return list;
    }

    private boolean IsDirectoryExistInCurrentPath(String DirectoryName) {
        File file = new File(this.CurrentDirectory + '\\');
        String[] List = file.list();
        if (List != null) {
            for (String dir : List) {
                if (DirectoryName.equals(dir))
                    return true;
            }
        }
        return false;
    }

    public CommandLineInterpreter(String Directory) {
        this.CurrentDirectory = Directory;

    }

    public CommandLineInterpreter() {
        CurrentDirectory = "C:";
    }

    public String GetCurrentDirectory() {
        return CurrentDirectory;
    }
    public boolean cdCommand(String Parameter) {
        if (Parameter.equals("..")) {
            int LastSlashIndex = CurrentDirectory.lastIndexOf("\\");
            if (LastSlashIndex != -1)
                CurrentDirectory = CurrentDirectory.substring(0, LastSlashIndex);
            return true;
        } else if (!Parameter.contains("\\")) {
            if (IsDirectoryExistInCurrentPath(Parameter)) {
                CurrentDirectory = CurrentDirectory + "\\" + Parameter;
                return true;
            }
        } else if (Parameter.contains("\\")) {
            File Path = new File(Parameter);
            if (Path.exists()) {
                CurrentDirectory = Parameter;
                return true;
            }
        }
        return false;
    }
    public boolean mkdir(String Directory) {
        ArrayList<String> List = split(Directory);
        for (String dir : List) {
            String Path = this.CurrentDirectory + '\\' + dir;
            File file = new File(Path);
            if (!file.mkdir())
                return false;
        }
        return true;
    }
    public boolean mvCommand(String sourcePath, String destinationPath) {
        File source = new File(CurrentDirectory + '\\' + sourcePath);
        File destination = new File(CurrentDirectory + '\\' + destinationPath);

        if (!source.exists()) {
            System.out.println("Source file or directory does not exist.");
            return false;
        }

        if (destination.exists() && destination.isDirectory()) {
            destination = new File(destination, source.getName());
        }

        try {
            Files.move(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean rmCommand(String fileName) {
        File file = new File(CurrentDirectory + '\\' + fileName);

        if (!file.exists())
        {
            System.out.println("File does not exist.");
            return false;
        }
        return file.delete();
    }
    public boolean rmDir(String dirName) {
        File dir = new File(CurrentDirectory + '\\' + dirName);

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Directory does not exist.");
            return false;
        }

        File[] contents = dir.listFiles();
        if (contents != null) {
            for (File file : contents) {
                if (file.isDirectory())
                {
                    rmDir(file.getPath());
                } else {
                    file.delete();
                }
            }
        }
        return dir.delete();
    }

    public String pwdCommand() {
        return CurrentDirectory;
    }
    public List<String> lsCommand() {
        File dir = new File(CurrentDirectory);
        String[] files = dir.list();
        return files != null ? Arrays.asList(files) : new ArrayList<>();
    }

    public List<String> lsACommand() {
        File dir = new File(CurrentDirectory);
        File[] files = dir.listFiles();

        return files != null
                ? Arrays.stream(files).map(File::getName).collect(Collectors.toList())
                : new ArrayList<>();
    }

    public List<String> lsRCommand() {
        List<String> files = lsCommand();
        Collections.reverse(files);
        return files;
    }

    public boolean touch(String fileName) {
        File file = new File(CurrentDirectory + '\\' + fileName);
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + fileName);
                return true;
            } else {
                if (file.setLastModified(System.currentTimeMillis())) {
                    System.out.println("File updated: " + fileName);
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating/updating the file.");
            e.printStackTrace();
        }
        return false;
    }
    public void cat(String fileName) {
        File file = new File(CurrentDirectory + '\\' + fileName);
        if (!file.exists() || !file.isFile()) {
            System.out.println("File does not exist.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }
    public boolean redirectOutput(String fileName, String content) {
        File file = new File(CurrentDirectory + '\\' + fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            System.out.println("Output redirected to: " + fileName);
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred while redirecting output.");
            e.printStackTrace();
            return false;
        }
    }
    public boolean appendOutput(String fileName, String content) {
        File file = new File(CurrentDirectory + '\\' + fileName);
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(content);
            writer.write(System.lineSeparator());
            System.out.println("Output appended to: " + fileName);
            return true;
        }
        catch (IOException e)
        {
            System.out.println("An error occurred while appending output.");
            e.printStackTrace();
            return false;
        }
    }

    public List<String> pipeCommand(String firstCommand, String secondCommand) {
        List<String> firstResult = executeCommand(firstCommand);

        if (secondCommand.startsWith("grep ")) {
            String pattern = secondCommand.substring(5).trim();
            return firstResult.stream()
                    .filter(line -> line.contains(pattern))
                    .collect(Collectors.toList());
        }

        System.out.println("Pipe command not supported for: " + secondCommand);
        return new ArrayList<>();
    }



    private List<String> executeCommand(String command) {
        String[] parts = command.split(" ");
        String cmd = parts[0];
        List<String> args = Arrays.asList(parts).subList(1, parts.length);

        switch (cmd) {
            case "ls":
                return lsCommand();
            case "ls-a":
                return lsACommand();
            case "ls-r":
                return lsRCommand();
            default:
                System.out.println("Unsupported command: " + cmd);
                return new ArrayList<>();
        }
    }
    public void help() {
        System.out.println("Available commands:");
        System.out.println("cd <directory>       - Change the current directory to <directory>.");
        System.out.println("mkdir <directory>    - Create a new directory with the specified <directory> name.");
        System.out.println("pwd                  - Print the current directory path.");
        System.out.println("rm <file>            - Delete the specified <file>.");
        System.out.println("rmdir <directory>    - Delete the specified <directory> and its contents.");
        System.out.println("touch <file>         - Create a new file or update the last modified time of <file>.");
        System.out.println("cat <file>           - Display the contents of <file>.");
        System.out.println("mv <source> <destination> - Move a file or directory from <source> to <destination>.");
        System.out.println("ls                   - List files in the current directory.");
        System.out.println("ls -a                - List all files, including hidden ones, in the current directory.");
        System.out.println("ls -r                - List files in reverse order.");
        System.out.println("< <file> <content>   - Redirect output to <file>, replacing its current content.");
        System.out.println("<< <file> <content>  - Append <content> to <file> without overwriting existing content.");
        System.out.println("For piping:");
        System.out.println("<command> | grep <pattern> - Filter the output of a command to include lines with <pattern>.");
        System.out.println("Exit                 - Exit the command line interpreter.");
        System.out.println();
    }

    private void DoWriteAndAppend(String Line)
    {
        String Command=Line.substring(Line.indexOf('>'),Line.indexOf('>')+2);
        String FileName=Line.substring(0, Line.indexOf(' '));
        Line=Line.substring(Line.lastIndexOf('>')+1);
        Line = Line.trim();
        String Content =Line;
        if (Command.equals(">>"))
        {
            appendOutput(FileName,Content);
        }
        else
            redirectOutput(FileName,Content);
    }

    private boolean DoCommand(String Line) {
        Line = Line.trim();
        if (Line.contains(" > ") ||Line.contains(" >> "))
        {
            DoWriteAndAppend(Line);
            return true;
        }
        int pos = Line.indexOf(' ');
        String Command;
        if(pos!=-1)
            Command =Line.substring(0, pos);
        else
            Command = Line;
        int Pos2;
        switch(Command)
        {
            case "cd":
                cdCommand(Line.substring(pos + 1));
                return true;
            case "mkdir":
                mkdir(Line.substring(pos + 1));
                return true;
            case "pwd":
                System.out.println(pwdCommand());
                return true;
            case "rm":
                if(!rmCommand(Line.substring(pos + 1)))
                    System.out.println("Can't Delete "+ Line.substring(pos + 1));
                return true;
            case ">":
                Pos2 = Line.lastIndexOf(' ');
                redirectOutput(Line.substring(pos+1, Pos2),Line.substring(Pos2+1));
                return true;
            case ">>":
                Pos2 = Line.lastIndexOf(' ');
                appendOutput(Line.substring(pos+1, Pos2),Line.substring(Pos2+1));
                return true;
            case "cat":
                cat(Line.substring(pos+1));
                return true;
            case "touch":
                touch(Line.substring(pos+1));
                return true;
            case "ls":
                List<String> list = lsCommand();
                for (int i = 0; i < list.size(); i++)
                {
                    System.out.println(list.get(i));
                }
                return true;
            case "ls -a":
                List<String> list1 = lsACommand();
                for (int i = 0; i < list1.size(); i++)
                {
                    System.out.println(list1.get(i));
                }
                return true;
            case "ls -r":
                List<String> list2 = lsRCommand();
                for (int i = list2.size()-1; i >= 0; i--)
                {
                    System.out.println(list2.get(i));
                }
                return true;
            case "rmdir":
                rmDir(Line.substring(pos+1));
                return true;
            case "mv":
                Pos2 = Line.lastIndexOf(' ');
                String source = Line.substring(pos + 1, Pos2);
                String destination = Line.substring(Pos2 + 1);
                if (!mvCommand(source, destination)) {
                    System.out.println("Failed to move " + source + " to " + destination);
                }
                return true;
            case "help":
                help();
                return true;
            default:
                return false;
        }
    }
    public void Run()
    {
        Scanner input = new Scanner(System.in);
        String Command=null;
        while (true)
        {
            Command = input.nextLine();
            if(Command.equals("Exit"))
                break;
            if(!DoCommand(Command))
                System.out.println("Wrong Command");
        }
    }

}