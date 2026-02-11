# CLI Task Tracker

## Description
This program allows a user to create, update, delete, and view tasks through the command line. The tasks are stored persistently in a JSON file.

## Tech Stack
- Java 21
- Maven
- Gson (json parsing)

## Requirements
- PREREQ: Java 21
  
1. Download the .jar file from this repository. (Optionally move it to a convenient location on the file system)
2. In the same working directory as the .jar, type "task-cli --version" to make sure the program is functional.

# Features
Run the program by using
```
java -jar task-cli ...
```
1. ... task-cli add "<your task>"
2. ... task-cli delete <id>
3. ... task-cli update <id> "<your task>"
4. ... tasl-cli list <done | in-progress | todo | {blank}>

# Code Layout
```
  ┌─────────────────────────────────────────────────────────┐
  │                        CLI                              │
  │                      (Main.java)                        │
  │         Parses commands: add, update, delete,           │
  │         mark-in-progress, mark-done, list               │
  └─────────────────────┬───────────────────────────────────┘
                        │
                        ▼
  ┌─────────────────────────────────────────────────────────┐
  │                   TaskManager                           │
  │              (TaskManager.java)                         │
  │   • CRUD operations on tasks                            │
  │   • JSON persistence via Gson                           │
  │   • Auto-incrementing ID assignment                     │
  └─────────────────────┬───────────────────────────────────┘
                        │
            ┌───────────┴───────────┐
            ▼                       ▼
  ┌──────────────────┐    ┌──────────────────┐
  │      Task        │    │   tasks.json     │
  │   (Task.java)    │    │   (storage)      │
  │                  │    │                  │
  │ • id             │    │ Persists task    │
  │ • description    │◄──►│ data between     │
  │ • status         │    │ sessions         │
  │ • timestamps     │    │                  │
  └──────────────────┘    └──────────────────┘
```


