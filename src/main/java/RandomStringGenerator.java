import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class RandomStringGenerator
{
    static String charValue = "abcdefghijklmnopqrstuvwxyz";
    public static String randomString(int maxLen)
    {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<=random.nextInt(maxLen);i++)
        {
            builder.append(charValue.charAt(random.nextInt(charValue.length())));
        }
        return builder.toString()+"\n";
    }
}
