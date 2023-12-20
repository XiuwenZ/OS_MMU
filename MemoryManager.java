import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class MemoryManager {
    private final int logicalMemorySize;
    private final int pageSize;
    private final int physicalMemorySize;
    private final PageTable pageTable;
    private final PageEntry[] physicalMemory;
    private int nextProcessId; // Automatically generated process ID

    public MemoryManager(int logicalMemorySize, int pageSize, int physicalMemorySize) {
        this.logicalMemorySize = logicalMemorySize;
        this.pageSize = pageSize;
        this.physicalMemorySize = physicalMemorySize;
        this.pageTable = new PageTable();
        this.physicalMemory = new PageEntry[physicalMemorySize / pageSize];
        this.nextProcessId = 1; // Start with process ID 1
    }

    public void allocateMemory(int numPages) {
        if (numPages * pageSize > logicalMemorySize) {
            System.out.println("Error: Not enough logical memory for allocation");
            return;
        }

        // Check if there is enough space in physical memory
        if (numPages > physicalMemorySize / pageSize) {
            System.out.println("Error: Not enough physical memory for allocation");
            return;
        }

        // Find contiguous free pages in physical memory
        int[] freePages = findContiguousFreePages(numPages);

        if (freePages == null) {
            System.out.println("Error: Not enough contiguous free pages in physical memory");
            return;
        }

        // Allocate pages in the page table
        int processId = nextProcessId++;
        pageTable.allocatePages(processId, freePages, numPages);

        // Allocate pages in physical memory
        for (int i = 0; i < numPages; i++) {
            physicalMemory[freePages[i]] = new PageEntry(processId, i);
        }

        System.out.println("Allocated " + numPages + " pages in physical memory for process " + processId);
        displayMemoryState();
    }

    public void deallocateMemory(int processId) {
        if (!pageTable.containsProcess(processId)) {
            System.out.println("Error: Process " + processId + " not found in page table");
            return;
        }

        PageInfo pageInfo = pageTable.getPageInfo(processId);
        int[] allocatedPages = pageInfo.getPages();

        // Deallocate pages in the page table
        pageTable.deallocatePages(processId);

        // Deallocate pages in physical memory
        for (int allocatedPage : allocatedPages) {
            physicalMemory[allocatedPage] = null;
        }

        System.out.println("Deallocated memory for process " + processId);
        displayMemoryState();
    }

    public int accessMemory(int processId, int logicalAddress) {
        if (!pageTable.containsProcess(processId)) {
            System.out.println("Error: Process " + processId + " not found in page table");
            return -1;
        }

        PageInfo pageInfo = pageTable.getPageInfo(processId);
        int pageNum = logicalAddress / pageSize;

        if (pageNum >= pageInfo.getNumPages()) {
            System.out.println("Error: Invalid logical address " + logicalAddress + " for process " + processId);
            return -1;
        }

        int physicalPageNum = pageInfo.getPages()[pageNum];
        return physicalPageNum * pageSize + (logicalAddress % pageSize);
    }

    private int[] findContiguousFreePages(int numPages) {
        int[] freePages = new int[numPages];
        int count = 0;

        for (int i = 0; i < physicalMemory.length; i++) {
            if (physicalMemory[i] == null) {
                freePages[count++] = i;
            } else {
                count = 0;
            }

            if (count == numPages) {
                return freePages;
            }
        }

        return null;
    }

    private void displayMemoryState() {
        System.out.println("Memory State:");
        for (int i = 0; i < physicalMemory.length; i++) {
            if (physicalMemory[i] == null) {
                System.out.print("Free ");
            } else {
                System.out.print("P" + physicalMemory[i].getProcessId() + ":" + physicalMemory[i].getPageNum() + " ");
            }

            if ((i + 1) % (physicalMemorySize / pageSize) == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    private static class PageInfo {
        private final int[] pages;
        private final int numPages;

        public PageInfo(int[] pages, int numPages) {
            this.pages = pages;
            this.numPages = numPages;
        }

        public int[] getPages() {
            return pages;
        }

        public int getNumPages() {
            return numPages;
        }
    }

    private static class PageEntry {
        private final int processId;
        private final int pageNum;

        public PageEntry(int processId, int pageNum) {
            this.processId = processId;
            this.pageNum = pageNum;
        }

        public int getProcessId() {
            return processId;
        }

        public int getPageNum() {
            return pageNum;
        }
    }

    private class PageTable {
        private final Map<Integer, PageInfo> table;

        public PageTable() {
            this.table = new HashMap<>();
        }

        public void allocatePages(int processId, int[] pages, int numPages) {
            table.put(processId, new PageInfo(pages, numPages));
        }

        public void deallocatePages(int processId) {
            table.remove(processId);
        }

        public boolean containsProcess(int processId) {
            return table.containsKey(processId);
        }

        public PageInfo getPageInfo(int processId) {
            return table.get(processId);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter logical memory size: ");
        int logicalMemorySize = scanner.nextInt();

        System.out.print("Enter page size: ");
        int pageSize = scanner.nextInt();

        System.out.print("Enter physical memory size: ");
        int physicalMemorySize = scanner.nextInt();

        MemoryManager memoryManager = new MemoryManager(logicalMemorySize, pageSize, physicalMemorySize);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Allocate Memory");
            System.out.println("2. Deallocate Memory");
            System.out.println("3. Access Memory");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter number of pages to allocate: ");
                    int numPages = scanner.nextInt();
                    memoryManager.allocateMemory(numPages);
                    break;

                case 2:
                    System.out.print("Enter process ID to deallocate memory: ");
                    int processId = scanner.nextInt();
                    memoryManager.deallocateMemory(processId);
                    break;

                case 3:
                    System.out.print("Enter process ID for memory access: ");
                    int accessProcessId = scanner.nextInt();

                    System.out.print("Enter logical address for memory access: ");
                    int logicalAddress = scanner.nextInt();

                    int physicalAddress = memoryManager.accessMemory(accessProcessId, logicalAddress);

                    if (physicalAddress != -1) {
                        System.out.println("Accessing logical address " + logicalAddress + " for process " +
                                accessProcessId + ". Physical address: " + physicalAddress);
                    }
                    break;

                case 4:
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
