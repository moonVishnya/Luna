package moonvishnya.xyz.luna.server_info;

public class Server {

    public static final String SERVER_NAME = "http://moonvishnya.xyz";

    public static final String UPLOAD_SCRIPT = "/image.php&username=";


    // username + password
    public static final String AUTHENTICATION_SCRIPT =
            "/authenticator.php?action=authentication&username=";

    //username + password + email
    public static final String REGISTRATION_SCRIPT =
            "/registration.php?action=registration&username=";

    // username + author
    public static final String JSON_MESSAGESLOADER_SCRIPT =
            "/messagesLoader.php?action=load&username=";

    // username
    public static final String JSON_CONVSLOADER_SCRIPT =
            "/messageBoxLoader.php?action=load&username=";

    // username + client + time + content
    public static final String MESSAGESENDER_SCRIPT = "";

    //username
    public static final String USERLOADER_SCRIPT =
            "/userloader.php?action=load&username=";
}
