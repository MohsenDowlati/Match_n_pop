import java.awt.*;

public class Language {
    private boolean isFarsi;
    public Language(int isFarsi) {
        if (isFarsi==0)
            this.isFarsi=true;
        else
            this.isFarsi=false;
    }

    public Font font(){
        if (isFarsi)
            return new Font("B Nazanin",Font.BOLD,20);
        return new Font("MV Boli",Font.BOLD,20);
    }

    public boolean isFarsi() {
        return isFarsi;
    }

    public String Title() {
        if (isFarsi)
            return "توپ‌های رنگی";
        return "colorful balls";
    }
    public String menuButtons(int btn) {
        if (isFarsi) {
            //randomStart Button
            if (btn == 0)
                return "شروع تصادفی";
            //fileStart Button
            else if (btn == 1)
                return "شروع از فایل";
            //scores Button
            else
                return "مشاهده امتیازات";
        } else {
            //randomStart Button
            if (btn == 0)
                return "Start randomly";
                //fileStart Button
            else if (btn == 1)
                return "Start from File";
                //scores Button
            else
                return "High Scores";
        }
    }
    public String backToMenu(){
        if (isFarsi)
            return "منو";
        return "Menu";
    }
    public String are_you_sure(){
        if (isFarsi)
            return "آیا مطمئنید؟";
        return "Are you sure?";
    }
    public String Exit(){
        if (isFarsi)
            return "خروج";
        return "Exit";
    }
    public String scoreLabel(){
        if (isFarsi)
            return "امتیاز: ";
        return "Score: ";
    }
    public String not_found_exception_title(){
        if (isFarsi)
            return "خطا داده";
        return "data error";
    }
    public String not_found_exception(){
        if (isFarsi)
            return "مشکلی رخ داده است.";
        return "Some things went wrong.";
    }
    public String submit_name(){
        if (isFarsi)
            return "نامتان چیست؟";
        return "What's your name?";
    }
}
