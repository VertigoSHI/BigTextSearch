public class Main
{
    public static void main(String[] args) {
        String root = System.getProperty("user.dir");
        String sourcePath = root + "/MyBigFile.txt";
        String resultPath = root+"/src/main/resources";
        try {
            BigFileReader bigFileReader = new BigFileReader(sourcePath);
            DataSplitManager.init(1000,resultPath);
            Data lineData;
            while ((lineData = bigFileReader.readLine())!=null)
            {
                DataSplitManager.dataGiveOut(lineData);
            }
            Data result = DataSplitManager.findResult();
            System.out.println(result);
            System.out.println(Math.abs(result.hashCode())%1000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
