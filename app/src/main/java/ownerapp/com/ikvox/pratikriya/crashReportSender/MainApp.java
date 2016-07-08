package ownerapp.com.ikvox.pratikriya.crashReportSender;

import android.support.multidex.MultiDexApplication;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by MyMac on 01/07/16.
 */
@ReportsCrashes(formKey = "")
public class MainApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        // instantiate the report sender with the email credentials.
        // these will be used to send the crash report
        ACRAReportSender reportSender = new ACRAReportSender("yourEmail@gmail.com", "yourPassword");

        // register it with ACRA.
        ACRA.getErrorReporter().setReportSender(reportSender);

    }
}

