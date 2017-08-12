package castle;

import java.util.HashMap;

public class Room {
    private String description;
    private HashMap<String, Room> exits = new HashMap<String,Room>();

    public Room(String description) 
    {
        this.description = description;
    }

    
    public void setExit(String dir , Room room) {
    	exits.put(dir, room);
    }
    
    public String getRoomdec() {
    	
    }
    
    

    @Override
    public String toString()
    {
        return description;
    }
}
