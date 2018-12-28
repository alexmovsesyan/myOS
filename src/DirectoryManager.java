import java.io.*;
import java.util.*;

class FileInfo
    {
        int diskNumber;
        int startingSector;
        int fileLength;
    }

class DirectoryManager
{
    Hashtable<String, FileInfo> T = new Hashtable<String, FileInfo>();

    void enter(String key, FileInfo file){
        T.put(key, file);
    }

    FileInfo lookup(String key){
        return T.get(key);
    }

    void print(){
        System.out.println(T);
    }
}
