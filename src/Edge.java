
public class Edge {
	String start;
	String end;
	int distance;
	int time;
	int cost;
	public Edge(String start, String end, String distance, String time, String cost) {
		this.start = start;
		this.end = end;
		this.distance = Integer.parseInt(distance);
		this.time = Integer.parseInt(time);;
		this.cost = Integer.parseInt(cost);;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
}
