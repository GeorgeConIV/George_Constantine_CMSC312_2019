package memory;

public class PageTableEntry
{
    private boolean validInvalidBit;
    private Frame frame;
    private Page page;

    public PageTableEntry(boolean validInvalidBit, Frame frame, Page page)
    {
        this.validInvalidBit = validInvalidBit;
        this.frame = frame;
        this.page = page;
    }

    public void setFrame(Frame frame)
    {
        this.frame = frame;
    }

    public void setBit(boolean bit)
    {
        validInvalidBit = bit;
    }

    public Frame getFrame()
    {
        return frame;
    }

    public Page getPage()
    {
        return page;
    }

    public boolean getBit()
    {
        return validInvalidBit;
    }

    @Override
    public String toString()
    {
        return "Page id: " + page.toString() + " " + frame.toString();
    }

}
