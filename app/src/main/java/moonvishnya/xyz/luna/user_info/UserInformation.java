package moonvishnya.xyz.luna.user_info;


public final class UserInformation {


    public static String USERNAME;
    public static String PASSWORD;
    public static int RATING;
    public static String STATUS;
    public static String CURR_STATUS;

    public static String getPHOTO() {
        return PHOTO;
    }

    public static void setPHOTO(String PHOTO) {
        UserInformation.PHOTO = PHOTO;
    }

    public static String PHOTO;

    public static String getEMAIL() {
        return EMAIL;
    }

    public static void setEMAIL(String EMAIL) {
        UserInformation.EMAIL = EMAIL;
    }

    public static String EMAIL;


    public static String getUSERNAME() {
        return USERNAME;
    }

    public static void setUSERNAME(String USERNAME) {
        UserInformation.USERNAME = USERNAME;
    }

    public static int getRATING() {
        return RATING;
    }

    public static void setRATING(int RATING) {
        UserInformation.RATING = RATING;
    }

    public static String getSTATUS() {
        return STATUS;
    }

    public static void setSTATUS(String STATUS) {
        UserInformation.STATUS = STATUS;
    }

    public static String getCurrStatus() {
        return CURR_STATUS;
    }

    public static void setCurrStatus(String currStatus) {
        CURR_STATUS = currStatus;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static void setPASSWORD(String PASSWORD) {
        UserInformation.PASSWORD = PASSWORD;
    }


}
