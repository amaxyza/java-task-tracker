# CLI Task Tracker

## Description
This program allows a user to create, update, delete, and view tasks through the command line. The tasks are stored persistently in a JSON file.

## Tech Stack
- Java 21
- Maven
- Gson (json parsing)

## Installation

  ### Prerequisites
  - Java 21 or higher

  ### Build
  ```bash
  mvn clean package
```
  This creates target/task-cli-1.0-SNAPSHOT.jar.

  Run
  ```bash
  java -jar target/task-cli-1.0-SNAPSHOT.jar <command> [args]
```
  Optional: Create an alias

  For easier usage, add an alias to your shell profile:

  Bash/Zsh:
  ```bash
  alias task="java -jar /path/to/task-cli-1.0-SNAPSHOT.jar"
```
  PowerShell:
  ```ps
  function task { java -jar C:\path\to\task-cli-1.0-SNAPSHOT.jar @args }
```
  Then use it as:

  ```
  task add "Buy groceries"
  task list
  task mark-done 1
```

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

roadmap.sh url: https://roadmap.sh/projects/task-tracker

