import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class MemoryManager {
    private final int logicalMemorySize;
    private final int pageSize;
    private final int physicalMemorySize;
    private final Map<Integer, PageInfo> pageTable;
    private final PageEntry[] physicalMemory;

    public MemoryManager(int logicalMemorySize, int pageSize, int physicalMemorySize) {
        this.logicalMemorySize = logicalMemorySize;
        this.pageSize = pageSize;
        this.physicalMemorySize = physicalMemorySize;
        this.pageTable = new HashMap<>();
        this.physicalMemory = new PageEntry[physicalMemorySize / pageSize];
    }

    public void allocateMemory(int processId, int numPages) {
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
        pageTable.put(processId, new PageInfo(freePages, numPages));

        // Allocate pages in physical memory
        for (int i = 0; i < numPages; i++) {
            physicalMemory[freePages[i]] = new PageEntry(processId, i);
        }

        System.out.println("Allocated " + numPages + " pages in physical memory for process " + processId);
    }

    public int accessMemory(int processId, int logicalAddress) {
        if (!pageTable.containsKey(processId)) {
            System.out.println("Error: Process " + processId + " not found in page table");
            return -1;
        }

        PageInfo pageInfo = pageTable.get(processId);
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter logical memory size: ");
        int logicalMemorySize = scanner.nextInt();

        System.out.print("Enter page size: ");
        int pageSize = scanner.nextInt();

        System.out.print("Enter physical memory size: ");
        int physicalMemorySize = scanner.nextInt();

        MemoryManager memoryManager = new MemoryManager(logicalMemorySize, pageSize, physicalMemorySize);

        System.out.print("Enter process ID for memory allocation: ");
        int processId = scanner.nextInt();

        System.out.print("Enter number of pages to allocate: ");
        int numPages = scanner.nextInt();

        memoryManager.allocateMemory(processId, numPages);

        System.out.print("Enter process ID for memory access: ");
        processId = scanner.nextInt();

        System.out.print("Enter logical address for memory access: ");
        int logicalAddress = scanner.nextInt();

        int physicalAddress = memoryManager.accessMemory(processId, logicalAddress);

        if (physicalAddress != -1) {
            System.out.println("Accessing logical address " + logicalAddress + " for process " + processId +
                    ". Physical address: " + physicalAddress);
        }

        scanner.close();
    }
}
