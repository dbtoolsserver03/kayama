package jp.co.csj.tools.utils.poi;

public class CsjPosition {

	public CsjPosition(int startPos, int endPos) {
		super();
		this.startPos = startPos;
		this.endPos = endPos;
	}
	int startPos = 0;
	int endPos = 0;
	public int getStartPos() {
		return startPos;
	}
	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}
	public int getEndPos() {
		return endPos;
	}
	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CsjPosition [startPos=" + startPos + ", endPos=" + endPos + "]";
	}
	
	
}
