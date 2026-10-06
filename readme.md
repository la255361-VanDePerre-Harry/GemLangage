# 💎 GEM Programming Language

[![Language](https://img.shields.io/badge/Language-Java_17%2B-orange.svg)](https://www.oracle.com/java/)
[![Architecture](https://img.shields.io/badge/Architecture-Tree--Walking_Interpreter-blue.svg)]()
[![Status](https://img.shields.io/badge/Status-Active_Development-brightgreen.svg)]()
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**GEM** is a strongly-typed, imperative, interpreted programming language engineered in Java. Designed with a clean and clear syntax, GEM offers explicit variable mutability control, static primitive typing, first-class functions, lexical block scoping, and syntactic desugaring.

---

## 🌟 Key Features

- **Strict Mutability Control**: Explicit variable intent using `const` (immutable) and `mut` (mutable) binding semantics.
- **Explicit Type Annotations**: Primitive support for integers (`int`), booleans (`bool`), and strings (`string`).
- **First-Class Functions**: Define reusable procedures and functions (`fn`) with explicit parameter signatures and return handling.
- **Lexical Scoping & Isolation**: Dynamic environment chain supporting nested lexical blocks (`{ ... }`) and variable shadowing.
- **Control Flow & AST Desugaring**: Standard `if/else` logic, `while` loops, and `for` loops syntactically desugared into clean `while` AST structures.
- **Lazy Logical Evaluation**: Short-circuit execution for logical AND (`&&`) and OR (`||`) expressions.

---

## 🏗️ Compiler & Runtime Pipeline

GEM implements a classic **Tree-Walking Interpreter** pipeline:

```
                          ┌──────────────────────────┐
                          │     GEM Source Code      │
                          └─────────────┬────────────┘
                                        │
                                        ▼
  ┌─────────────────┐        ┌────────────────────┐
  │  Lexical Analysis│ ───►  │    Lexer Stream    │
  └─────────────────┘        └──────────┬─────────┘
                                        │
                                        ▼
  ┌─────────────────┐        ┌────────────────────┐
  │Syntactic Parsing│ ───►  │Abstract Syntax Tree│  (Desugaring pass)
  └─────────────────┘        └──────────┬─────────┘
                                        │
                                        ▼
  ┌─────────────────┐        ┌────────────────────┐
  │ AST Interpreter │ ───►  │ Scoped Runtime Env │  (Tree-Walk Execution)
  └─────────────────┘        └────────────────────┘
```

1. **Lexer (`lexer`)**: Converts raw string input into a strongly-typed stream of `Token` instances while preserving line numbers for precise diagnostic error messaging.
2. **Parser (`parser`)**: Employs Recursive Descent parsing to build the Abstract Syntax Tree (`Expr` and `Stmt` nodes). Performs AST desugaring to simplify higher-level constructs (such as transforming `for` loops into equivalent `while` blocks).
3. **Interpreter (`interpreter`)**: Recursively evaluates and executes AST nodes against hierarchical `Environment` scopes, handling runtime control flow interruptions (such as `return` execution) via dedicated unwind exceptions.

---

## 📁 Repository Structure

```text
.
├── src/
│   ├── ast/                  # Abstract Syntax Tree node definitions
│   │   ├── Stmt.java         # Base abstract statement node
│   │   ├── Expr.java         # Base abstract expression node
│   │   ├── AssignStmt.java   # Variable assignment node
│   │   ├── VarDeclStmt.java  # Declaration (const / mut) node
│   │   ├── FunctionStmt.java # Function definition node
│   │   ├── ReturnStmt.java   # Return control statement
│   │   ├── IfStmt.java       # Conditional statement node
│   │   ├── WhileStmt.java    # Iterative loop node
│   │   ├── BlockStmt.java    # Lexical block scope node
│   │   ├── PrintStmt.java    # Console printing statement
│   │   ├── BinaryExpr.java   # Binary arithmetic/comparison node
│   │   ├── LogicalExpr.java  # Short-circuit logical expression node
│   │   ├── UnaryExpr.java    # Unary prefix operation node
│   │   ├── CallExpr.java     # Function call expression node
│   │   ├── VariableExpr.java # Variable resolution expression node
│   │   └── LiteralExpr.java  # Constant literal expression node
│   ├── lexer/                # Lexical scanner package
│   │   ├── Token.java        # Token data model (type, lexeme, line)
│   │   ├── TokenType.java    # Lexical token enumerations
│   │   └── Lexer.java        # Character-by-character scanner
│   ├── parser/               # Syntactic analysis package
│   │   └── Parser.java       # Recursive descent parser with AST desugaring
│   ├── interpreter/          # AST execution & runtime engine
│   │   ├── Environment.java  # Lexical scope symbol table
│   │   ├── GemFunction.java  # Callable runtime function representation
│   │   ├── Interpreter.java  # Tree-walking AST evaluator
│   │   └── ReturnException.java # Control-flow unwind mechanism
│   └── Main.java             # Entry point runner
└── README.md
```

---

## 💻 GEM Language Syntax

### 1. Variables & Mutability
```gem
// Immutable constant declaration
const maxCount : int = 100

// Mutable variable declaration
mut currentScore : int = 10
currentScore = currentScore + 15

// Primitive types
const message : string = "Welcome to GEM!"
mut isActive : bool = true
```

### 2. Control Flow
```gem
mut temperature : int = 22

if temperature > 30 {
    print "It is hot outside!"
} else {
    print "Moderate temperature."
}

// Iterative loop (while)
mut counter : int = 0
while counter < 3 {
    print counter
    counter = counter + 1
}

// Syntactic desugaring (for loop)
for (mut i : int = 0; i < 5; i = i + 1) {
    print i
}
```

### 3. Functions & Scope
```gem
// Function with return value
fn addNumbers(a : int, b : int) : int {
    return a + b
}

// Procedure (void return)
fn displayHeader(title : string) {
    print "=== " + title + " ==="
}

displayHeader("Execution Test")
const total : int = addNumbers(15, 27)
print total
```

---

## 🛠️ Build & Getting Started

Comming soon with an external interpreter.

---
