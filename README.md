# lightweight Scripting Language Interpreter
A lightweight, expressive scripting language interpreter written in Java.
This project implements a dynamically-typed language with modern syntax
features including lexical scoping, first-class functions,
and automatic memory management.

## Features

- variable declarations with `let` and `const`
- Basic data types: numbers, strings, booleans, and `none` for nothing
- Block-level scoping
- Built-in print statement
- Comprehensive error handling, or i like to think so
- BTW this lang isn't for use it is for me to learn how these stuff work

## Project Structure and Components
```
.
├── libs/                  # External dependencies
│   └── jline.jar          # JLine for better REPL experience
├── src/                   # Source code
│   ├── Main.java          # Entry point
│   ├── Tokenizer.java     # Converts text into tokens 
│   ├── Parser.java        # Converts tokens into AST
│   ├── Scope/             # Variables Map and the Parent Scope
│   ├── ExprNode/          # defines evaluate function that every Expr must implements
│   ├── ExprStmt/          # defines evaluate function that every Stmt must implements
│   ├── *Node/             # AST nodes that implements ExprNode
│   ├── *Stmt/             # AST nodes that implements StmtNode
│   └── ...               
├── tests/                 # Test files
│   └── ...                # Test programs
└── Makefile               # Build automation
```

## Getting Started

### Prerequisites
- Linux or if you use Windows try (WSL) 
- Java Development Kit (JDK) 8 or higher
- Make (for building)

### Building the Project
Clone the repository and build using Make:

```bash
git clone [repository-url]
cd [project-name]
make compile
```

### Running the Interpreter

#### REPL Mode
To start the interactive REPL:
```bash
make run
```

#### File Mode
To run a specific source file:
```bash
make run file=path/to/your/file.txt
```

## Language Syntax

### Basic Examples
```javascript
// Variables and constants
let age = 25;
const PI = 3.14159;

// Arithmetic operations
let sum = 10 + 5;
let diff = 10 - 5;
let prod = 10 * 5;
let quot = 10 / 5;

// String operations
let name = "Hello " + "World";

let isTrue = 10 > 5;

// if statments 

let x = 0;
let y = 0;

if (x < 5 and y > 10) {
    print "x < 5 and y > 10";
}
else if (x == 5 or y == 0) {
    if (x == 5) print "x == 5";
    else print "y == 0";
}
else {
    print "only in Ohio";
}
```

## Development

### Adding New Features
1. Add relevant token types in `TokenType.java`
2. Implement new AST nodes if needed
3. Update the parser to handle new syntax
4. Add evaluation logic
5. Add tests in the `tests/` directory

## Testing
Test programs are in `tests/` directory and run using:
```bash
make run file=tests/your_test_file
```

## Contributing
Contributions are not welcome! Please don't submit a Pull Request.

## Acknowledgments
- [Crafting Interpreters](https://craftinginterpreters.com/) by Robert Nystrom helped me a lot throw this project
