package platform.youtube;

import utils.Regex;

public class YoutubeRegex {
    public static String getToken(String body) {
        return Regex.get("itemSectionRenderer.*?token\":\"(.*?)\"", body).group(1);
    }

    public static String getApi(String body) {
        return Regex.get("innertubeApiKey\":\"(.*?)\"", body).group(1);
    }
}
