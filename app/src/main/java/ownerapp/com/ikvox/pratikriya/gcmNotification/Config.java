package ownerapp.com.ikvox.pratikriya.gcmNotification;

public interface Config {

	// used to share GCM regId with application server - using php app server
	//static final String APP_SERVER_URL = "http://192.168.1.17/gcm/gcm.php?shareRegId=1";

	// GCM server using java
	 String APP_SERVER_URL ="http://feedbotappserver.cgihum6dcd.us-west-2.elasticbeanstalk.com/GCMDeviceRegistration.do?shareRegId=1";
	//static final String APP_SERVER_URL ="http://ikvoxserver.78kuyxr39b.us-west-2.elasticbeanstalk.com/GCMDeviceRegistration.do";
	//"http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// Google Project Number
	 String GOOGLE_PROJECT_ID = "914889143053";
	 String MESSAGE_KEY = "message";

}
