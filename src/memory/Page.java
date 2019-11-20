package memory;

public class Page
{
    private Integer pageID;
    public Page(Integer pageID)
    {
        this.pageID = pageID;
    }

    public Integer getPageID()
    {
        return pageID;
    }

    @Override
    public String toString()
    {
        return pageID.toString();
    }
}
