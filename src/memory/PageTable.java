package memory;

import ProcessStuff.OSGlobals;

import java.util.ArrayList;
import java.util.List;

public class PageTable
{
    private double pageSize = 8000; //byte size of a page
    private double memTotal = 4000000000.0;
    private double pageCount = memTotal/pageSize;
    private Integer currentPage = 0;

    //page table can be accessed by incrementing through this list, the array location is equal to the
    //page number
    private List<PageTableEntry> pageTable = new ArrayList<>();
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
     */
    public List<PageTableEntry> allocateMem(Integer memNeeded)
    {
        double memNeededed = memNeeded;
        double pagesNeeded = Math.ceil(memNeededed/pageSize);
        int procPageCount = (int)pagesNeeded;
        List<PageTableEntry> allocatedPages = new ArrayList<>();

        memRemaining = ((int)pageCount) - currentPage;

        if(procPageCount > memRemaining)
        {
            if(OSGlobals.debug)
                System.out.println("[PAGE TABLE] Not enough free memory to allocate");
            return allocatedPages;
        }

        while(pagesNeeded > 0)
        {
            for(int i=0; i<pageCount; i++)
            {
                if(!pageTable.get(i).getBit())
                {
                    pageTable.get(i).setFrame(frames.get(i).allocate());
                    pageTable.get(i).setBit(true);
                    allocatedPages.add(pageTable.get(i));
                    currentPage++;
                    break;
                }
            }
            pagesNeeded--;
        }
        if(OSGlobals.debug)
            System.out.println("[PAGE TABLE] Allocated: " + procPageCount + " pages of memory");
        return allocatedPages;
    }

    public boolean enoughFree(int requesting)
    {
        return requesting > memRemaining;
    }


    public void deallocateMem(List<PageTableEntry> dePage)
    {
        if(OSGlobals.debug)
            System.out.println("[PAGE TABLE] Deallocated "  + dePage.size() + " pages");
        for(PageTableEntry p : dePage)
        {

            pageTable.get(p.getPage().getPageID()).setBit(false);
            pageTable.get(p.getPage().getPageID()).getFrame().deallocate();
            currentPage--;
        }
    }

    public List<PageTableEntry> getNotFreePages()
    {
        List<PageTableEntry> things = new ArrayList<>();

        for(PageTableEntry p : pageTable)
            if(p.getBit())
                things.add(p);

        return things;
    }

    public int getMemRemaining()
    {
        return (int)pageCount - currentPage;
    }

    private void deMem(List<PageTableEntry> dePage)
    {

    }

}
