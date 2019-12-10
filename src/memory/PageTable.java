package memory;

import ProcessStuff.OSGlobals;

import java.util.ArrayList;
import java.util.List;

public class PageTable
{
    private double pageSize = 8000000; //byte size of a page
    private double memTotal = 40000000000.0;
    private double pageCount = memTotal/pageSize;
    private Integer currentPage = 0;
    private Integer pageAllocatedCount = 0;

    //page table can be accessed by incrementing through this list, the array location is equal to the
    //page number
    private List<PageTableEntry> pageTable = new ArrayList<>();
    private List<PageTableEntry> allocatedPages = new ArrayList<>();
    private List<Page> pages = new ArrayList<>();
    private List<Frame> frames = new ArrayList<>();
    private List<Frame> freeFrames;
    int memRemaining;

    public PageTable()
    {
        int intPages = 0;
        while(intPages < pageCount)
        {
            Frame initFrame = new Frame(intPages);
            Page initPage = new Page(intPages);

            frames.add(initFrame);
            pages.add(initPage);

            PageTableEntry entry = new PageTableEntry(false, initFrame, initPage);
            pageTable.add(entry);

            intPages++;
        }
        freeFrames = frames;
    }

    /**
     * allocates memory for a process
     * @param memNeeded byte amount needed for process
     * @return a list of pageTableEntries that was allocated to a process
     */
    public synchronized List<PageTableEntry> allocateMem(Integer memNeeded)
    {
        double memNeededed = memNeeded;
        double pagesNeeded = Math.ceil(memNeededed/pageSize);
        int procPageCount = (int)pagesNeeded;
        List<PageTableEntry> pages = new ArrayList<>();

        memRemaining = ((int)pageCount) - currentPage;

        if(procPageCount > memRemaining)
        {
            if(OSGlobals.debug)
                System.out.println("[PAGE TABLE] Not enough free memory to allocate");
            return pages;
        }

        while(pagesNeeded > 0)
        {
            for(int i=0; i<pageCount; i++)
            {
                if(!pageTable.get(i).getBit())
                {
                    pageTable.get(i).setFrame(frames.get(i).allocate());
                    pageTable.get(i).setBit(true);
                    pages.add(pageTable.get(i));
                    currentPage++;

                    pageTable.get(i).setPageNum(pageAllocatedCount);
                    allocatedPages.add(pageTable.get(i));
                    pageAllocatedCount++;

                    break;
                }
            }
            pagesNeeded--;
        }
        if(OSGlobals.debug)
            System.out.println("[PAGE TABLE] Allocated: " + procPageCount + " pages of memory");
        return pages;
    }

    public boolean enoughFree(int requesting)
    {
        return requesting > memRemaining;
    }


    /**
     * the list allocatedPages is the effective page table. it keeps a list of the page table entries
     * where the pages are always ordered correctly. Page table is a collection of all frames and pages,
     * used to facilitate functionality.
     * @param dePage list of pages to be deallocated
     */
    public synchronized void deallocateMem(List<PageTableEntry> dePage)
    {
        if(OSGlobals.debug)
            System.out.println("[PAGE TABLE] Deallocated "  + dePage.size() + " pages");
        for(PageTableEntry p : dePage)
        {
            allocatedPages.remove(p);
            pageTable.get(p.getPage().getPageID()).setBit(false);
            pageTable.get(p.getPage().getPageID()).getFrame().deallocate();
            currentPage--;
        }

        int count=0;
        for(PageTableEntry p : allocatedPages)
        {
            p.setPageNum(count);
            count++;
        }
        //System.out.println(toString());
    }

    public List<PageTableEntry> getNotFreePages()
    {
        List<PageTableEntry> things = new ArrayList<>();

        for(PageTableEntry p : pageTable)
            if(p.getBit())
                things.add(p);

        return things;
    }

    private void deMem(List<PageTableEntry> dePage)
    {

    }

    public int getAmountFree()
    {
        return memRemaining;
    }

    public int getTotal()
    {
        return (int)pageCount;
    }

    @Override
    public String toString()
    {
        String str = "---PAGE TABLE---";
        for(PageTableEntry p : allocatedPages)
        {
            str = str + p.toString() + "\n";
        }
        return str;
    }

}
