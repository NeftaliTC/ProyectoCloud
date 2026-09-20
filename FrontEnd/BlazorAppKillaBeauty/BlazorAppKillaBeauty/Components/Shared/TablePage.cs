namespace BlazorAppKillaBeauty.Components.Shared;

public class TablePage<T>
{
    public List<T> Items { get; set; } = new();
    public int TotalRecords { get; set; }
}

public record PageQuery(int Page, int PageSize);
