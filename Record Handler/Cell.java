public class Cell {
    private Integer value = null; // -1: Bom, 0-8: Số lượng bom xung quanh
    private boolean isRevealed = false;
    private boolean isFlagged = false;

    public Cell() {}

    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }
    public boolean isRevealed() { return isRevealed; }
    public void setRevealed(boolean revealed) { isRevealed = revealed; }
    public boolean isFlagged() { return isFlagged; }
    public void setFlagged(boolean flagged) { isFlagged = flagged; }
}