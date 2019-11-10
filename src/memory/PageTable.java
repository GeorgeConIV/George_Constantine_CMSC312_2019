package memory;

import java.util.ArrayList;
import java.util.List;

public class PageTable implements Runnable
{
    private double pageSize = 8000000; //byte size of a page
    private double memTotal = 4000000000.0;
    private double pageCount = memTotal/pageSize;
    private Integer currentPage = 0;

    //page table can be accessed by incrementing through this list, the array location is equal to the
    //page number
    private List<PageTableEntry> pageTable = new ArrayList<>();
    private List<Page> pages = new ArrayList<>();
    private List<Frame> frames = new ArrayList<>();
    private List<Frame> freeFrames = new ArrayList<>();

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

        int memRemaining = ((int)pageCount) - currentPage;

        if(procPageCount > memRemaining)
        {
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
        System.out.println("[PAGE TABLE] Allocated: " + procPageCount + " pages of memory");
        return allocatedPages;
    }




    public void deallocateMem(List<PageTableEntry> dePage)
    {
        for(PageTableEntry p : dePage)
        {
            System.out.println("[PAGE TABLE] Deallocated page: " + p.getPage().getPageID());
            pageTable.get(p.getPage().getPageID()).setBit(false);
            pageTable.get(p.getPage().getPageID()).getFrame().deallocate();
            currentPage--;
        }
    }

    private void deMem(List<PageTableEntry> dePage)
    {

    }

    //TODO: make this actually work
    @Override
    public void run()
    {

    }
}
