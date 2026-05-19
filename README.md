# 💣 Minesweeper DSA

> A classic Minesweeper game built with Java Swing, applying custom Data Structures & Algorithms from scratch — no `java.util` collections used for core logic.

**Course:** Data Structures and Algorithms — International University  
**Instructor:** Tran Thanh Tung

---

## 📸 Features at a Glance

| Feature | Description |
|---|---|
| 🎮 Classic Minesweeper | Left-click to reveal, right-click to flag |
| ↩️ Unlimited Undo | Ctrl+Z — powered by a custom **Stack** |
| 🌊 Flood Fill | Auto-reveal empty regions via **BFS + Queue** |
| 🏆 Leaderboard | Top 10 scores sorted by custom **Insertion Sort** on **LinkedList** |
| 💾 Save / Load | Persist and resume any game session |
| 📊 Statistics | Track win rate across all sessions |
| 🎯 3 Difficulty Levels | Easy / Medium / Hard + Custom board |
| 🔒 Safe First Click | Mines placed after first click — always safe |

---

## 🗂️ Project Structure

```
MinesweeperDSA/
└── src/
    ├── dsa/                        # Custom DSA implementations (no java.util!)
    │   ├── Stack.java              # Array-based stack with dynamic resizing
    │   ├── Queue.java              # Circular array queue (wrap-around)
    │   ├── LinkedList.java         # Doubly linked list with Insertion Sort
    │   └── HashTable.java          # Separate chaining hash table
    │
    ├── model/                      # Domain objects
    │   ├── Cell.java               # Single board cell (mine, revealed, flagged)
    │   ├── Board.java              # 2D array grid of cells
    │   ├── GameState.java          # Full game state (status, timer, counters)
    │   └── GameRecord.java         # Game result with scoring formula
    │
    ├── handler/                    # Business logic & algorithms
    │   ├── BoardHandler.java       # C1 — Board init, mine placement, adjacency
    │   ├── GameLogicHandler.java   # C2 — BFS flood fill, cell reveal, undo
    │   ├── GameStateHandler.java   # C4 — Win / Lose detection
    │   ├── RecordHandler.java      # C5 — Record storage & sorted leaderboard
    │   └── SaveHandler.java        # C6 — File-based save & load
    │
    ├── ui/
    │   └── GameFrame.java          # C3 — Full Swing GUI, menus, board rendering
    │
    └── Main.java                   # Entry point
```


### 📚 BFS on 2D Grid → Flood Fill
```
Graph Lectures — BFS / DFS
```
- The grid is treated as an implicit graph: each cell = vertex, each of 8 neighbors = edge
- BFS traverses connected components of empty cells — same concept as graph BFS

---

## 🎮 How to Play

| Action | Control |
|---|---|
| Reveal a cell | Left Click |
| Place / Remove flag | Right Click |
| Undo last action | `Ctrl + Z` |
| New game | Menu → Game → New Game |
| Save game | Menu → Game → Save Game |
| Load game | Menu → Game → Load Game |
| View leaderboard | Menu → Records → Leaderboard |

**Win:** Reveal all cells that don't contain mines  
**Lose:** Click on a mine

---

## 🎯 Difficulty Levels

| Level | Grid | Mines | Mine Ratio |
|---|---|---|---|
| 🟢 Easy | 9 × 9 | 10 | 12.3% |
| 🟡 Medium | 16 × 16 | 40 | 15.6% |
| 🔴 Hard | 16 × 30 | 99 | 20.6% |
| ⚙️ Custom | 5–30 × 5–30 | User-defined | Variable |

---

## 🚀 Getting Started

### Prerequisites
- Java JDK 11 or higher (JDK 17 / 21 recommended)

### Build & Run

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/MinesweeperDSA.git
cd MinesweeperDSA

# 2. Compile
mkdir -p out
find src -name "*.java" | xargs javac -d out

# 3. Run
java -cp out Main
```

### Generated Files
After running, the following files will appear in the project root:

| File | Description |
|---|---|
| `minesweeper_records.txt` | All game results (auto-created) |
| `minesweeper_save.txt` | Saved game state (created on Save) |

---

## 📐 Scoring Formula

```
Score = (mines / totalCells) × 10000 + max(0, 3000 − timeTaken)
```

- Higher mine density = higher base score  
- Faster completion = higher time bonus  
- Score is 0 for lost games

---

## 📁 Key Files Explained

| File | Role | DSA Used |
|---|---|---|
| `BoardHandler.java` | Init board, place mines, calculate adjacency | 2D Array, HashTable |
| `GameLogicHandler.java` | Reveal cells, flood fill, undo | Stack, Queue (BFS) |
| `GameStateHandler.java` | Detect win/lose, reveal all mines | 2D Array traversal |
| `RecordHandler.java` | Store & sort game records | LinkedList, Insertion Sort |
| `SaveHandler.java` | Persist and restore game state | File I/O, 2D Array |
| `GameFrame.java` | Full GUI — menus, board drawing, events | Java Swing |

---

## 🔧 Design Decisions

**Why custom DSA instead of `java.util`?**  
The course requires demonstrating understanding of how data structures work internally. Using `java.util.ArrayDeque` hides the implementation. Each class in `dsa/` mirrors what the course slides teach — array-based Stack, circular Queue, doubly linked list, and separate-chaining HashTable.

**Why separate `dsa/` package?**  
Single Responsibility Principle — `Stack.java` only knows how to be a Stack. `GameLogicHandler.java` only knows game logic. This also allows the same `Stack` or `LinkedList` to be reused by multiple handlers without duplication.

**Why BFS instead of recursive DFS for flood fill?**  
As noted in Week 6 slides: *"recursion is not efficient — try to transform to non-recursive approach"*. BFS with a Queue avoids stack overflow on large boards and has cleaner iterative control flow.

---

## 📊 Algorithm Complexity Summary

| Operation | Algorithm | Time Complexity |
|---|---|---|
| Mine placement | Random + HashTable lookup | O(mines) avg |
| Adjacency count | 2D Array scan (8 directions) | O(R × C) |
| Flood fill | BFS with circular Queue | O(R × C) |
| Undo | Stack pop + deep copy | O(R × C) |
| Win check | 2D Array linear scan | O(R × C) |
| Add game record | LinkedList insertLast | O(1) |
| Sort leaderboard | Insertion Sort on LinkedList | O(n²) |
| Safe zone check | HashTable containsKey | O(1) avg |

---

## 📜 License

This project is for educational purposes as part of the Data Structures and Algorithms course at International University.