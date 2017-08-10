package com.rqlite;

public interface rqlite {
	
	public enum ReadConsistencyLevel {
		NONE("none"),
		WEAK("weak"),
		STRONG("strong");
		
		private String value;
		
	    private ReadConsistencyLevel(String value) {
	        this.value = value;
	    }

	    public String value() {
	        return this.value;
	    }		
	}
	
	public void Query(String q);
	
	public void Execute(String q);
}