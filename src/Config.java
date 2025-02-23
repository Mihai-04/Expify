import io.github.cdimascio.dotenv.Dotenv;
import static io.github.cdimascio.dotenv.Dotenv.load;

public class Config {
    private static final Dotenv dotenv = load();

    public static String getApiKey() {
        return dotenv.get("API_KEY");
    }
}
