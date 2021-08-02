import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BigFileGenerator
{
    public static boolean createBigFile(String path,long size) throws IOException {
        File file = new File(path+"MyBigFile.txt");
        FileOutputStream stream = new FileOutputStream(file);
        if(file.getUsableSpace()<size)
        {
            return false;
        }
        if(!file.exists())
        {
            file.mkdirs();
        }
        file.createNewFile();
        while (file.length()<size)
        {
            stream.write(RandomStringGenerator.randomString(100).getBytes(StandardCharsets.UTF_8));
        }
        return true;
    }

    public static void main(String[] args) {
        try
        {
            createBigFile("",1024*1024);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
