class DiskManager{

    private int currentSector1 = 0;
    private int currentSector2 = 0;

    int getNextAvailableSector(int disk){
        if(disk ==0)
            return currentSector1;
        return currentSector2;
    }

    int allocateSpace(int disk){
        int tempSector;
        if(disk ==0){
            tempSector = currentSector1;
            currentSector1 +=1;
        }
        else{
            tempSector = currentSector2;
            currentSector2 +=1;
        }
        return tempSector;
    }

}
