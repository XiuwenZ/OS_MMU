# Memory Manager Simulation

## Introduction
The Memory Manager Simulation is a Java-based project that provides a simplified simulation of memory management in a computer system. The simulation includes basic functionalities such as memory allocation, deallocation, and memory access, employing paging as the underlying memory management technique. 
The primary goal of this project is to illustrate fundamental concepts related to memory management in operating systems.

## Key Features

### Logical and Physical Memory
- The system simulates both logical and physical memory spaces, allowing users to define their sizes.
- The logical memory is divided into fixed-size pages, and the physical memory is similarly partitioned to accommodate these pages.

### Memory Allocation
- Users can allocate memory for processes by specifying the number of pages required.
- The system checks for available space in both logical and physical memory and allocates contiguous pages to processes.

### Memory Deallocation
- Processes can release allocated memory when they are no longer in use.
- The deallocation process involves freeing up the corresponding pages in both the page table and physical memory.

### Memory Access
- Users can simulate memory access by providing a process ID and a logical address.
- The system converts the logical address to a physical address, allowing users to observe the mapping between logical and physical memory.

### Page Table Implementation
- The simulation includes a simplified page table, implemented as a hash map, to track allocated pages for each process.

### User Interaction
- The project features a command-line interface that enables users to interact with the simulation.
- Users can choose options such as memory allocation, deallocation, memory access, and exit through a menu system.

## How to Use

- Setup: Start by entering the sizes of logical memory, page size, and physical memory when prompted during the initial setup.

- Memory Allocation: Allocate memory for processes by specifying the number of pages to be assigned.

- Memory Deallocation: Release memory for processes by providing the unique process ID associated with the memory to be deallocated.

- Memory Access: Simulate memory access by inputting the process ID and logical address. Observe how the logical address translates into a physical address.

- User Menu: Navigate through the user menu, where you can choose various memory management actions. The menu provides options for memory allocation, deallocation, memory access, and program exit.


## Benefits

- **Educational Purpose:** This project serves as an educational tool to help individuals understand fundamental concepts of memory management in operating systems.
  
- **Concept Reinforcement:** Users can visualize and reinforce their understanding of memory allocation, deallocation, and access within the context of paging.

- **Simplified Simulation:** The simulation provides a simplified yet illustrative model, making it accessible for learning purposes.

## Conclusion
The Memory Manager Simulation project offers a hands-on experience in understanding the basics of memory management. By providing a simple yet effective simulation, users can gain insights into the inner workings of a computer system's memory management mechanisms.
This project is designed for educational use and as a foundation for further exploration into more advanced memory management concepts.
