# Java Command-Line Interpreter

A simple shell-like command-line interpreter built in Java. This tool simulates common terminal commands and operations such as navigating directories, managing files, and more—all within a Java program.

## 🛠️ Features

- `cd <directory>` - Change directory
- `mkdir <directory>` - Create one or multiple directories
- `pwd` - Print current working directory
- `rm <file>` - Delete a file
- `rmdir <directory>` - Recursively delete a directory and its contents
- `mv <source> <destination>` - Move or rename a file/directory
- `ls`, `ls -a`, `ls -r` - List files in normal, all, or reverse order
- `touch <file>` - Create or update a file
- `cat <file>` - Print file content
- Output Redirection:
  - `> <file> <content>` - Write content to file (overwrite)
  - `>> <file> <content>` - Append content to file
- `help` - Show available commands
- `Exit` - Exit the shell