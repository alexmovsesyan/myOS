class Disk
{
    static final int NUM_SECTORS = 1024;

    StringBuffer sectors[] = new StringBuffer[NUM_SECTORS];

    void write(int sector, StringBuffer data)throws InterruptedException{
        Thread.sleep(200);
        sectors[sector] = data;
    }
    void read(int sector, StringBuffer data)throws InterruptedException{
        Thread.sleep(200);
        data.append(sectors[sector]);
    }

}
