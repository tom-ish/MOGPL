package robot;

public class Point {

	public Point(Integer key, boolean isVisited, Integer x, Integer y, Orientation o, Point parent, Integer c) {
		this.key = key;
		this.isVisited = isVisited;
		this.x = x;
		this.y = y;
		this.orientation = o;
		this.parent = parent;
		this.cost = c;
	}


	private Integer key;
	private Integer x;
	private Integer y;
	private boolean isVisited;
	private Orientation orientation;
	private Point parent;
	private Integer cost;

	public Integer getKey() { return key; }
	public Integer getX() { return x; }
	public Integer getY() { return y; }
	public boolean isVisited() { return isVisited; }
	public Orientation getOrientation(){ return this.orientation; }
	public Point getParent(){ return this.parent; }
	public Integer getCost(){ return this.cost; }

	public void setKey(Integer key) { this.key = key; }
	public void setX(Integer x) { this.x = x; }
	public void setY(Integer y) { this.y = y; }
	public void setVisited(boolean isVisited) { this.isVisited = isVisited; }
	public void setOrientation(Orientation o){ this.orientation = o; }
	public void setParent(Point p){ this.parent = p; }
	public void setCost(Integer c){ this.cost = c; }

	public boolean equals(Object o){
		if(o instanceof Point){
			Point p = (Point) o;
			if(p.getX() == this.getX() && p.getY() == this.getY() && p.getOrientation() == this.getOrientation()){
				return true;
			}
		}
		return false;
	}

	public Point clone(){
		return new Point(this.getKey(), this.isVisited(), this.getX(), this.getY(), this.getOrientation(), this.getParent(), this.getCost());
	}

	public String toString(){
		if(getParent() == null)
			return "(  [" + getX() + "," + getY() + "] | orientation = " + getOrientation() + " | isVisited = " + isVisited() + " | key = " + getKey() + " | cost = " + getCost() +"  )";
		else
			return "(  [" + getX() + "," + getY() + "] | orientation = " + getOrientation() + " | isVisited = " + isVisited() + " | key = " + getKey() + " | parent = [" + getParent().getX() + "," + getParent().getY() + "] " + getParent().getOrientation() + " | cost = " + getCost() + "  )";
	}

}
