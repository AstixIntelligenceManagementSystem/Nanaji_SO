package astix.nanaji_so;

import android.net.Uri;

import java.io.File;

public class CommonInfo
{

	// Its for Live Path on 194 Server



	public static String AppLatLngJsonFile="NanajiSOLatLngJson";
	public static File imageF_savedInstance=null;
	public static String imageName_savedInstance=null;
	public static String clickedTagPhoto_savedInstance=null;
	public static Uri uriSavedImage_savedInstance=null;


	public static String WebManageDSRUrl="http://103.20.212.194/NanaJi/pda/frmIMEImanagement.aspx";
	public static String imei="";

	public static String fileContent="";
	public static final String MejiDairyPreference="MejiDairyPreference";
	public static String WebServicePath="http://103.20.212.194/WebServiceAndroidNanajiSFALive/Service.asmx";

	public static int DistanceRange=500;//Defautl Distance Range to show for store list


	public static String VersionDownloadPath="http://103.20.212.194/downloads/";
	public static String VersionDownloadAPKName="NanajiSO.apk";

	public static String DATABASE_NAME = "DBAdapterLtFoods";
	public static int AnyVisit = 0;

	public static int DATABASE_VERSIONID = 11;      // put this field value based on value in table on the server
	public static String AppVersionID = "1.4";   // put this field value based on value in table on the server
	public static int Application_TypeID = 3; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct


	public static String OrderSyncPath="http://103.20.212.194/ReadXML_NanajiSFALive/DefaultSO.aspx";
	public static String ImageSyncPath="http://103.20.212.194/ReadXML_NanajiStoreMapImagesLive/Default.aspx";

	public static String OrderSyncPathDistributorMap="http://103.20.212.194/ReadXML_NanajiSFALive/DefaultSODistributorMapping.aspx";
	public static String OrderSyncPathDistributorTarget="http://103.20.212.194/ReadXML_NanajiSFALive/DefaultSODistributorTarget.aspx";



	public static String URLImageLinkToViewStoreOverWebProtal="http://103.20.212.194/Nanaji/PDA/frmPDAImgsLive.aspx";


	public static String OrderXMLFolder="NanajiXml";
	public static String ImagesFolder="NanajiImages";

	public static int flgLTFoodsSOOnlineOffLine=0;
	public static int flgNewStoreORStoreValidation=0;

	public static String FinalLatLngJsonFile="NanajiFinalSTLatLngJson";










	// Its for Development  Path on 194 server for SFA



/*


	public static String AppLatLngJsonFile="NanajiSOLatLngJson";
	public static File imageF_savedInstance=null;
	public static String imageName_savedInstance=null;
	public static String clickedTagPhoto_savedInstance=null;
	public static Uri uriSavedImage_savedInstance=null;

	public static String WebManageDSRUrl="http://103.20.212.194/NanaJi_dev/pda/frmIMEImanagement.aspx";

	public static String imei="";

	public static String fileContent="";
	   public static final String MejiDairyPreference="MejiDairyPreference";
    public static String WebServicePath="http://103.20.212.194/WebServiceAndroidNanajiSFADevelopment/Service.asmx";

    public static int DistanceRange=500;//Defautl Distance Range to show for store list


    public static String VersionDownloadPath="http://103.20.212.194/downloads/";
	public static String VersionDownloadAPKName="NanajiSODev.apk";

	public static String DATABASE_NAME = "DBAdapterLtFoods";
	public static int AnyVisit = 0;

	public static int DATABASE_VERSIONID = 9;      // put this field value based on value in table on the server
	public static String AppVersionID = "1.3";   // put this field value based on value in table on the server
	public static int Application_TypeID = 3; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct


	public static String OrderSyncPath="http://103.20.212.194/ReadXML_NanajiSFADevelopment/DefaultSO.aspx";
	public static String ImageSyncPath="http://103.20.212.194/ReadXML_NanajiStoreMapImagesDevelopment/Default.aspx";

	public static String OrderSyncPathDistributorMap="http://103.20.212.194/ReadXML_NanajiSFADevelopment/DefaultSODistributorMapping.aspx";
	public static String OrderSyncPathDistributorTarget="http://103.20.212.194/ReadXML_NanajiSFADevelopment/DefaultSODistributorTarget.aspx";



	public static String URLImageLinkToViewStoreOverWebProtal="http://103.20.212.194/Nanaji/PDA/frmPDAImgsdev.aspx";


		public static String OrderXMLFolder="NanajiXml";
	public static String ImagesFolder="NanajiImages";

		public static int flgLTFoodsSOOnlineOffLine=0;
	public static int flgNewStoreORStoreValidation=0;


	public static String FinalLatLngJsonFile="NanajiFinalSTLatLngJson";
*/






}
