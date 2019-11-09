package memory;

import java.util.ArrayList;
import java.util.List;

public class PageTable
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
    }

    /**
     * allocates memory for a process
     * @param memNeeded byte amount needed for process
     */
    public List<Page> allocateMem(Integer memNeeded)
    {
        double memNeededed = memNeeded;
        double pagesNeeded = Math.ceil(memNeededed/pageSize);
        int procPageCount = (int)pagesNeeded;
        List<Page> allocatedPages = new ArrayList<>();

        while(pagesNeeded > 0)
        {
            for(int i=0; i<pageCount; i++)
            {
                if(frames.get(i).getFree())
                {
                    pageTable.get(currentPage).setFrame(frames.get(i).allocate());
                    pageTable.get(currentPage).setBit(true);
                    allocatedPages.add(pageTable.get(currentPage).getPage());
                    currentPage++;
                    break;
                }
            }
            pagesNeeded--;
        }
        System.out.println("Allocated: " + procPageCount + " pages of memory");
        return allocatedPages;
    }

    public void deallocateMem(List<Page> dePage)
    {
        for(Page p : dePage)
        {
            pageTable.get(p.getPageID()).setBit(false);
        }
    }

}
