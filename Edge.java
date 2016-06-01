package edu.iastate.cs311.hw5;



public class Edge {
	String from;
	String to;
	public Edge(String from, String to){
		if(from == null || to == null){
			throw new IllegalArgumentException("Null values not allowed");
		}else{
			//do noting here. good to go 
		}
		this.from = from;
		this.to = to;
	}
	
	@Override
	public int hashCode(){
		return this.from.hashCode() + this.to.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Edge){
			Edge otherEdge = (Edge) obj;
			return from.equals(otherEdge.from) && to.equals(otherEdge.to);
		}else{
			return false;
		}
	}
}
