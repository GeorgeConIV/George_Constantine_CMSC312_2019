package memory;

public class Page
{
    private Integer pageID;
    private Integer pageNum;
    public Page(Integer pageID)
    {
        this.pageID = pageID;
        this.pageNum = pageID;
    }

    public Integer getPageID()
    {
        return pageID;
    }

    public Integer getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }

    @Override
    public String toString()
    {
        return pageNum.toString();
    }
}
