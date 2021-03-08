package main.map;

public class Edge {
	public int room1;
	public int room2;
	
	public Edge(int room1, int room2) {
		this.room1 = room1;
		this.room2 = room2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + room1;
		result = prime * result + room2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (room1 != other.room1)
			return false;
		if (room2 != other.room2)
			return false;
		return true;
	}
}
