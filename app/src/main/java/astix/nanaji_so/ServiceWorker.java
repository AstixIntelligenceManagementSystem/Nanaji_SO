package astix.nanaji_so;

import android.content.Context;
import android.content.ContextWrapper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ServiceWorker {



	public int chkTblStoreListContainsRow=1;
	private Context context;

	//Live Path WebServiceAndroidParagSFATesting
	//public String UrlForWebService="http://115.124.126.184/WebServiceAndroidParagSFA/Service.asmx";

	//Testing Path
	//public String UrlForWebService="http://115.124.126.184/WebServiceAndroidParagSFATesting/Service.asmx";

	public static int flagExecutedServiceSuccesfully=0;
	public String UrlForWebService=CommonInfo.WebServicePath.trim();




	Locale locale  = new Locale("en", "UK");
	String pattern = "###.##";
	DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance(locale);
	//private ServiceWorker _activity;
	private ContextWrapper cw;
	String movie_name;
	String director;
	String flgNewVersion;
	//int counts;
	public String currSysDate;
	public String SysDate;

	public int newStat = 0;
	public int timeout=0;



	public ServiceWorker getAvailableAndUpdatedVersionOfAppNew(Context ctx,String uuid,String CurDate,int DatabaseVersion,int ApplicationID)
	{

		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);
		dbengine.open();

		decimalFormat.applyPattern(pattern);

		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/GetIMEIVersionDetailStatusNew";
		final String METHOD_NAME = "GetIMEIVersionDetailStatusNew";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		//Create request
		SoapObject table = null; // Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; // Its the client petition to the web service
		SoapObject tableRow = null; // Contains row of table
		SoapObject responseBody = null; // Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();


		// // System.out.println("Kajol 100");

		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);



			// // System.out.println("Kajol 101");
			client.addProperty("uuid", uuid.toString());
			client.addProperty("DatabaseVersion", DatabaseVersion);
			client.addProperty("ApplicationID", ApplicationID);

			// // System.out.println("Kajol 102");
			sse.setOutputSoapObject(client);
			// // System.out.println("Kajol 103");
			sse.bodyOut = client;
			// // System.out.println("Kajol 104");

			androidHttpTransport.call(SOAP_ACTION, sse);

			// // System.out.println("Kajol 1");

			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// // System.out.println("Kajol 2 :"+totalCount);
			String resultString=androidHttpTransport.responseDump;

			String name=responseBody.getProperty(0).toString();

			// // System.out.println("Kajol 3 :"+name);

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);


			dbengine.droptblUserAuthenticationMstrTBL();
			dbengine.createtblUserAuthenticationMstrTBL();
			dbengine.dropAvailbUpdatedVersionTBL();
			dbengine.createAvailbUpdatedVersionTBL();



			NodeList tblSchemeStoreMappingNode = doc.getElementsByTagName("tblUserAuthentication");
			for (int i = 0; i < tblSchemeStoreMappingNode.getLength(); i++)
			{
				String flgUserAuthenticated="0";
				String flgAppStatus="0";
				String DisplayMessage="No Message";
				String flgValidApplication="0";
				String	MessageForInvalid="No Message";
				int CoverageAreaNodeID=0;
				int CoverageAreaNodeType=0;


				Element element = (Element) tblSchemeStoreMappingNode.item(i);

				NodeList StoreIDNode = element.getElementsByTagName("flgUserAuthenticated");
				Element line = (Element) StoreIDNode.item(0);
				flgUserAuthenticated=xmlParser.getCharacterDataFromElement(line);

				NodeList flgAppStatusNode = element.getElementsByTagName("flgAppStatus");
				line = (Element) flgAppStatusNode.item(0);
				if(flgAppStatusNode.getLength()>0)
				{
					flgAppStatus=xmlParser.getCharacterDataFromElement(line);
				}
				NodeList DisplayMessageNode = element.getElementsByTagName("DisplayMessage");
				line = (Element) DisplayMessageNode.item(0);
				if(DisplayMessageNode.getLength()>0)
				{
					DisplayMessage=xmlParser.getCharacterDataFromElement(line);
				}

				NodeList flgValidApplicationNode = element.getElementsByTagName("flgValidApplication");
				line = (Element) flgValidApplicationNode.item(0);
				if(flgValidApplicationNode.getLength()>0)
				{
					flgValidApplication=xmlParser.getCharacterDataFromElement(line);
				}
				NodeList MessageForInvalidNode = element.getElementsByTagName("MessageForInvalid");
				line = (Element) MessageForInvalidNode.item(0);
				if(MessageForInvalidNode.getLength()>0)
				{
					MessageForInvalid=xmlParser.getCharacterDataFromElement(line);
				}

				NodeList CoverageAreaNodeIDNode = element.getElementsByTagName("CoverageAreaNodeID");
				line = (Element) CoverageAreaNodeIDNode.item(0);
				if(CoverageAreaNodeIDNode.getLength()>0)
				{
					CoverageAreaNodeID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
				}

				NodeList CoverageAreaNodeTypeNode = element.getElementsByTagName("CoverageAreaNodeType");
				line = (Element) CoverageAreaNodeTypeNode.item(0);
				if(CoverageAreaNodeTypeNode.getLength()>0)
				{
					CoverageAreaNodeType=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
				}

				//dbengine.savetblUserAuthenticationMstr(flgUserAuthenticated);
				dbengine.savetblUserAuthenticationMstr(flgUserAuthenticated,flgAppStatus,DisplayMessage,flgValidApplication,MessageForInvalid,CoverageAreaNodeID,CoverageAreaNodeType);

			}


			NodeList tblSchemeMstrNode = doc.getElementsByTagName("tblAvailableVersion");
			for (int i = 0; i < tblSchemeMstrNode.getLength(); i++)
			{


				String VersionID = "0";
				String VersionSerialNo= "NA";
				String VersionDownloadStatus= "NA";
				Date pdaDate=new Date();
				SimpleDateFormat sdfPDaDate = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
				String fDatePda = sdfPDaDate.format(pdaDate).toString().trim();
				String ServerDate= fDatePda;

				Element element = (Element) tblSchemeMstrNode.item(i);
				NodeList SchemeIDNode = element.getElementsByTagName("VersionID");
				Element line = (Element) SchemeIDNode.item(0);
				VersionID=xmlParser.getCharacterDataFromElement(line);
				// System.out.println("Kajol tblSchemeMstr: VersionID" +VersionID );


				NodeList SchemeNameNode = element.getElementsByTagName("VersionSerialNo");
				line = (Element) SchemeNameNode.item(0);
				VersionSerialNo=xmlParser.getCharacterDataFromElement(line);
				// System.out.println("Kajol tblSchemeMstr: VersionSerialNo " +VersionSerialNo );



				NodeList SchemeApplicationIDNode = element.getElementsByTagName("VersionDownloadStatus");
				line = (Element) SchemeApplicationIDNode.item(0);
				VersionDownloadStatus=xmlParser.getCharacterDataFromElement(line);
				// System.out.println("Kajol tblSchemeMstr: VersionDownloadStatus " +VersionDownloadStatus );

				NodeList SchemeAppliedRuleNode = element.getElementsByTagName("ServerDate");
				line = (Element) SchemeAppliedRuleNode.item(0);
				ServerDate=xmlParser.getCharacterDataFromElement(line);
				// System.out.println("Kajol tblSchemeMstr: ServerDate " +ServerDate );


				dbengine.savetblAvailbUpdatedVersion(VersionID.trim(), VersionSerialNo.trim(),VersionDownloadStatus.trim(),ServerDate);

			}

			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e) {

			// System.out.println("Aman Exception occur in GetIMEIVersionDetailStatusNew :"+e.toString());
			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			dbengine.close();

			return setmovie;
		}





	}



	public ServiceWorker getStoreAllDetails(Context ctx,String uuid,String CurDate,int DatabaseVersion,int ApplicationID)
	{

		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);


		decimalFormat.applyPattern(pattern);

		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnGetLTFoodsSOStores";
		final String METHOD_NAME = "fnGetLTFoodsSOStores";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		//Create request
		SoapObject table = null; // Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; // Its the client petition to the web service
		SoapObject tableRow = null; // Contains row of table
		SoapObject responseBody = null; // Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();


		// // System.out.println("Kajol 100");

		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);

			String StoreListOld="";
			HashMap<String,String> hmapStoreIdSstat=dbengine.checkForStoreIdSstat();
			/*if(dbengine.fncheckCountNearByStoreExistsOrNot(1000)==1)
			{

				LinkedHashMap<String, String> hmapStoreLisMstr=new LinkedHashMap<String, String>();
				hmapStoreLisMstr=dbengine.fnGeStoreListAllForSOForWebService(0,0);
				//System.out.println("Nitish Count Previous Store ="+hmapStoreLisMstr.size());
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapStoreLisMstr);
				Set set2 = map.entrySet();
				Iterator iterator = set2.iterator();
				while(iterator.hasNext())
				{
					Map.Entry me2 = (Map.Entry)iterator.next();
					if(StoreListOld.equals(""))
					{
						StoreListOld=me2.getKey().toString();
					}
					else
					{
						StoreListOld +="^"+me2.getKey().toString();
					}
					//CoverageAreaNames[index]=me2.getKey().toString();

				}


			}*/
			dbengine.fndeleteSbumittedStoreList(4);
			// // System.out.println("Kajol 101");
			client.addProperty("uuid", uuid.toString());
			client.addProperty("DatabaseVersion", DatabaseVersion);
			client.addProperty("ApplicationID", ApplicationID);
			client.addProperty("StoreListOld", StoreListOld);


			// // System.out.println("Kajol 102");
			sse.setOutputSoapObject(client);
			// // System.out.println("Kajol 103");
			sse.bodyOut = client;
			// // System.out.println("Kajol 104");

			androidHttpTransport.call(SOAP_ACTION, sse);

			// // System.out.println("Kajol 1");

			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// // System.out.println("Kajol 2 :"+totalCount);
			String resultString=androidHttpTransport.responseDump;

			String name=responseBody.getProperty(0).toString();

			// // System.out.println("Kajol 3 :"+name);

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);
			dbengine.open();

			dbengine.delete_all_storeDetailTables();




			/*NodeList tblUserNameNode = doc.getElementsByTagName("tblUserName");
			for (int i = 0; i < tblUserNameNode.getLength(); i++)
			{

				String UserName="0";

				Element element = (Element) tblUserNameNode.item(i);
				if(!element.getElementsByTagName("UserName").equals(null))
				{
					NodeList UserNameNode = element.getElementsByTagName("UserName");
					Element     line = (Element) UserNameNode.item(0);
					if (UserNameNode.getLength()>0)
					{
						UserName=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.saveTblUserName(UserName);
			}*/
			dbengine.saveTblUserName("Not Specified");
			/*NodeList tblStoreCountDetailsNode = doc.getElementsByTagName("tblStoreCountDetails");
			for (int i = 0; i < tblStoreCountDetailsNode.getLength(); i++)
			{

				String TotStoreAdded="0";
				String TodayStoreAdded ="0";


				Element element = (Element) tblStoreCountDetailsNode.item(i);
				if(!element.getElementsByTagName("TotStoreAdded").equals(null))
				{
					NodeList TotStoreAddedNode = element.getElementsByTagName("TotStoreAdded");
					Element     line = (Element) TotStoreAddedNode.item(0);
					if (TotStoreAddedNode.getLength()>0)
					{
						TotStoreAdded=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("TodayStoreAdded").equals(null))
				{
					NodeList TodayStoreAddedNode = element.getElementsByTagName("TodayStoreAdded");
					Element     line = (Element) TodayStoreAddedNode.item(0);
					if (TodayStoreAddedNode.getLength()>0)
					{
						TodayStoreAdded=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.saveTblStoreCountDetails(TotStoreAdded, TodayStoreAdded);
			}*/
			dbengine.saveTblStoreCountDetails("0", "0");

			NodeList tblPreAddedStoresNode = doc.getElementsByTagName("tblPreAddedStores");
			for (int i = 0; i < tblPreAddedStoresNode.getLength(); i++)
			{

				String StoreID="0";
				String StoreName ="0";
				String LatCode ="0";
				String LongCode ="0";
				String SOLatCode ="NA";
				String SOLongCode ="NA";
				String DateAdded ="07-July-2017";
				int CoverageAreaID =0;
				int CoverageAreaType =0;
				int RouteNodeID =0;
				int RouteNodeType =0;
				int flgStoreValidated =0;
				String City="";
				String State="";
				String PinCode="";

				String StoreCategoryType="0-0-0";
				int StoreSectionCount=0;

				int flgOldNewStore=0;
				int flgApproveOrRejectOrNoActionOrReVisit=0;
				int Sstat=0;
				int flgStoreVisitMode=0;
				String VisitStartTS="NA";
				String VisitEndTS="NA";
				String LocProvider="NA";
				String Accuracy="NA";
				String SOAccuracy="NA";
				int flgRemap=0;
				String BateryLeftStatus="0";
				int IsStoreDataCompleteSaved=0;
				String PaymentStage="NA";
				int flgLocationTrackEnabled=0;
				String StoreAddress="NA";

				String StoreIDPDAFromServer="NA";
				Date pdaDate=new Date();
				SimpleDateFormat	sdfPDaDate = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
				DateAdded = sdfPDaDate.format(pdaDate).toString().trim();

				//String StoreID,String StoreName,String ActualLatitude,String ActualLongitude,String
				// VisitStartTS,String VisitEndTS,String LocProvider,String Accuracy,String BateryLeftStatus,
				// int IsStoreDataCompleteSaved,String PaymentStage,int flgLocationTrackEnabled,String StoreAddress,
				// String StoreCity,String StorePinCode,String StoreState,int Sstat

				Element element = (Element) tblPreAddedStoresNode.item(i);

				if(!element.getElementsByTagName("StoreIDDB").equals(null))
				{
					NodeList StoreIDNode = element.getElementsByTagName("StoreIDDB");
					Element     line = (Element) StoreIDNode.item(0);
					if (StoreIDNode.getLength()>0)
					{
						StoreID=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("StoreID").equals(null))
				{

					NodeList StoreIDPDANode = element.getElementsByTagName("StoreID");
					Element     line = (Element) StoreIDPDANode.item(0);

					if(StoreIDPDANode.getLength()>0)
					{

						StoreIDPDAFromServer=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("StoreName").equals(null))
				{
					NodeList StoreNameNode = element.getElementsByTagName("StoreName");
					Element     line = (Element) StoreNameNode.item(0);
					if (StoreNameNode.getLength()>0)
					{
						StoreName=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("LatCode").equals(null))
				{
					NodeList LatCodeNode = element.getElementsByTagName("LatCode");
					Element     line = (Element) LatCodeNode.item(0);
					if (LatCodeNode.getLength()>0)
					{
						LatCode=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("LongCode").equals(null))
				{
					NodeList LongCodeNode = element.getElementsByTagName("LongCode");
					Element     line = (Element) LongCodeNode.item(0);
					if (LongCodeNode.getLength()>0)
					{
						LongCode=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("DateAdded").equals(null))
				{
					NodeList DateAddedNode = element.getElementsByTagName("DateAdded");
					Element     line = (Element) DateAddedNode.item(0);
					if (DateAddedNode.getLength()>0)
					{
						DateAdded=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("CoverageAreaID").equals(null))
				{
					NodeList CoverageAreaIDNode = element.getElementsByTagName("CoverageAreaID");
					Element     line = (Element) CoverageAreaIDNode.item(0);
					if (CoverageAreaIDNode.getLength()>0)
					{
						CoverageAreaID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("CoverageAreaType").equals(null))
				{
					NodeList CoverageAreaTypeNode = element.getElementsByTagName("CoverageAreaType");
					Element     line = (Element) CoverageAreaTypeNode.item(0);
					if (CoverageAreaTypeNode.getLength()>0)
					{
						CoverageAreaType=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("RouteNodeID").equals(null))
				{
					NodeList RouteNodeIDNode = element.getElementsByTagName("RouteNodeID");
					Element     line = (Element) RouteNodeIDNode.item(0);
					if (RouteNodeIDNode.getLength()>0)
					{
						RouteNodeID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("RouteNodeType").equals(null))
				{
					NodeList RouteNodeTypeNode = element.getElementsByTagName("RouteNodeType");
					Element     line = (Element) RouteNodeTypeNode.item(0);
					if (RouteNodeTypeNode.getLength()>0)
					{
						RouteNodeType=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("flgStoreValidated").equals(null))
				{
					NodeList flgApproveOrRejectOrNoActionNode = element.getElementsByTagName("flgStoreValidated");
					Element     line = (Element) flgApproveOrRejectOrNoActionNode.item(0);
					if (flgApproveOrRejectOrNoActionNode.getLength()>0)
					{
						flgApproveOrRejectOrNoActionOrReVisit=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				//flgApproveOrRejectOrNoActionOrReVisit


				if(!element.getElementsByTagName("City").equals(null))
				{
					NodeList CityNode = element.getElementsByTagName("City");
					Element     line = (Element) CityNode.item(0);
					if (CityNode.getLength()>0)
					{
						City=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("State").equals(null))
				{
					NodeList StateNode = element.getElementsByTagName("State");
					Element     line = (Element) StateNode.item(0);
					if (StateNode.getLength()>0)
					{
						State=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("PinCode").equals(null))
				{
					NodeList PinCodeNode = element.getElementsByTagName("PinCode");
					Element     line = (Element) PinCodeNode.item(0);
					if (PinCodeNode.getLength()>0)
					{
						PinCode=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("flgRemap").equals(null))
				{
					NodeList flgRemapNode = element.getElementsByTagName("flgRemap");
					Element     line = (Element) flgRemapNode.item(0);
					if (flgRemapNode.getLength()>0)
					{
						flgRemap=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("Accuracy").equals(null))
				{
					NodeList AccuracyNode = element.getElementsByTagName("Accuracy");
					Element     line = (Element) AccuracyNode.item(0);
					if (AccuracyNode.getLength()>0)
					{
						Accuracy=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("OptionID").equals(null))
				{
					NodeList StoreCategoryTypeNode = element.getElementsByTagName("OptionID");
					Element     line = (Element) StoreCategoryTypeNode.item(0);
					if (StoreCategoryTypeNode.getLength()>0)
					{
						StoreCategoryType=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("SectionCount").equals(null))
				{
					NodeList SectionCountNode = element.getElementsByTagName("SectionCount");
					Element     line = (Element) SectionCountNode.item(0);
					if (SectionCountNode.getLength()>0)
					{
						StoreSectionCount=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}


				if(StoreName.equals("shivam"))
				{
					int sadads=0;
				}
				if(hmapStoreIdSstat!=null)
				{

					if(hmapStoreIdSstat.containsKey(StoreIDPDAFromServer))
					{
						StoreID=StoreIDPDAFromServer;
						flgOldNewStore=1;
					}
					if(hmapStoreIdSstat.containsKey(StoreID))
					{
						if(hmapStoreIdSstat.get(StoreID).equals("3"))
						{
							hmapStoreIdSstat.put(StoreID,"4");
						}
						if(flgRemap==3)
						{
							hmapStoreIdSstat.put(StoreID,"0");
						}

						Sstat=Integer.parseInt(hmapStoreIdSstat.get(StoreID));
					}
					else
					{
						if(!element.getElementsByTagName("Sstat").equals(null))
						{
							NodeList SstatNode = element.getElementsByTagName("Sstat");
							Element     line = (Element) SstatNode.item(0);

							if(SstatNode.getLength()>0)
							{
								Sstat=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
							}
						}
					}
				}




				long count=dbengine.fnsaveTblPreAddedStores(StoreID, StoreName, LatCode, LongCode, DateAdded,flgOldNewStore,Sstat,CoverageAreaID,CoverageAreaType,RouteNodeID,RouteNodeType,City,State,PinCode,StoreCategoryType,StoreSectionCount,flgApproveOrRejectOrNoActionOrReVisit,SOLatCode, SOLongCode,flgStoreVisitMode,VisitStartTS,VisitEndTS,LocProvider,Accuracy,BateryLeftStatus,IsStoreDataCompleteSaved,PaymentStage,flgLocationTrackEnabled,StoreAddress,SOAccuracy,flgRemap);

			}




			NodeList tblCoverageMaster = doc.getElementsByTagName("tblCoverageMaster");
			for (int i = 0; i < tblCoverageMaster.getLength(); i++)
			{

				int CoverageAreaNodeID=0;
				int CoverageAreaNodeType=0;

				String CoverageArea ="NA";



				Element element = (Element) tblCoverageMaster.item(i);

				if(!element.getElementsByTagName("CoverageAreaNodeID").equals(null))
				{
					NodeList CoverageAreaNodeIDNode = element.getElementsByTagName("CoverageAreaNodeID");
					Element     line = (Element) CoverageAreaNodeIDNode.item(0);
					if (CoverageAreaNodeIDNode.getLength()>0)
					{
						CoverageAreaNodeID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("CoverageAreaNodeType").equals(null))
				{
					NodeList CoverageAreaNodeTypeNode = element.getElementsByTagName("CoverageAreaNodeType");
					Element     line = (Element) CoverageAreaNodeTypeNode.item(0);
					if (CoverageAreaNodeTypeNode.getLength()>0)
					{
						CoverageAreaNodeType=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("CoverageArea").equals(null))
				{
					NodeList CoverageAreaNode = element.getElementsByTagName("CoverageArea");
					Element     line = (Element) CoverageAreaNode.item(0);
					if (CoverageAreaNode.getLength()>0)
					{
						CoverageArea=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.fnsavetblCoverageMaster(CoverageAreaNodeID, CoverageAreaNodeType, CoverageArea);
			}






			NodeList tblRouteMasterWithCoverageMapping = doc.getElementsByTagName("tblRouteMasterWithCoverageMapping");
			for (int i = 0; i < tblRouteMasterWithCoverageMapping.getLength(); i++)
			{

				int CoverageAreaNodeID=0;
				int CoverageAreaNodeType=0;

				String CoverageArea ="NA";


				int RouteID=0;
				int RouteType=0;

				String Route ="NA";



				Element element = (Element) tblRouteMasterWithCoverageMapping.item(i);

				if(!element.getElementsByTagName("CoverageAreaNodeID").equals(null))
				{
					NodeList CoverageAreaNodeIDNode = element.getElementsByTagName("CoverageAreaNodeID");
					Element     line = (Element) CoverageAreaNodeIDNode.item(0);
					if (CoverageAreaNodeIDNode.getLength()>0)
					{
						CoverageAreaNodeID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("CoverageAreaNodeType").equals(null))
				{
					NodeList CoverageAreaNodeTypeNode = element.getElementsByTagName("CoverageAreaNodeType");
					Element     line = (Element) CoverageAreaNodeTypeNode.item(0);
					if (CoverageAreaNodeTypeNode.getLength()>0)
					{
						CoverageAreaNodeType=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("CoverageArea").equals(null))
				{
					NodeList CoverageAreaNode = element.getElementsByTagName("CoverageArea");
					Element     line = (Element) CoverageAreaNode.item(0);
					if (CoverageAreaNode.getLength()>0)
					{
						CoverageArea=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("RouteID").equals(null))
				{
					NodeList Node = element.getElementsByTagName("RouteID");
					Element     line = (Element) Node.item(0);
					if (Node.getLength()>0)
					{
						RouteID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("RouteType").equals(null))
				{
					NodeList Node = element.getElementsByTagName("RouteType");
					Element     line = (Element) Node.item(0);
					if (Node.getLength()>0)
					{
						RouteType=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("Route").equals(null))
				{
					NodeList Node = element.getElementsByTagName("Route");
					Element     line = (Element) Node.item(0);
					if (Node.getLength()>0)
					{
						Route=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.fnsavetblRouteMasterWithCoverageMapping(CoverageAreaNodeID, CoverageAreaNodeType,CoverageArea,RouteID,RouteType,Route);
			}




			NodeList tblStoreImageList = doc.getElementsByTagName("tblStoreImageList");
			for (int i = 0; i < tblStoreImageList.getLength(); i++)
			{

				String StoreID="NA";
				String StoreImagename="NA";

				int ImageType =0;



				Element element = (Element) tblStoreImageList.item(i);

				if(!element.getElementsByTagName("StoreIDDB").equals(null))
				{
					NodeList Node = element.getElementsByTagName("StoreIDDB");
					Element     line = (Element) Node.item(0);
					if (Node.getLength()>0)
					{
						StoreID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("ImageType").equals(null))
				{
					NodeList Node = element.getElementsByTagName("ImageType");
					Element     line = (Element) Node.item(0);
					if (Node.getLength()>0)
					{
						ImageType=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("StoreImagename").equals(null))
				{
					NodeList Node = element.getElementsByTagName("StoreImagename");
					Element     line = (Element) Node.item(0);
					if (Node.getLength()>0)
					{
						StoreImagename=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.fnsavetblStoreImageList(StoreID,StoreImagename,ImageType);
			}




			NodeList tblStorePaymentStageMapping = doc.getElementsByTagName("tblStorePaymentStageMapping");
			for (int i = 0; i < tblStorePaymentStageMapping.getLength(); i++)
			{

				String StoreID="NA";

				String PaymentStage ="0";



				Element element = (Element) tblStorePaymentStageMapping.item(i);

				if(!element.getElementsByTagName("StoreIDDB").equals(null))
				{
					NodeList Node = element.getElementsByTagName("StoreIDDB");
					Element     line = (Element) Node.item(0);
					if (Node.getLength()>0)
					{
						StoreID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("PaymentStage").equals(null))
				{
					NodeList Node = element.getElementsByTagName("PaymentStage");
					Element     line = (Element) Node.item(0);
					if (Node.getLength()>0)
					{
						PaymentStage=xmlParser.getCharacterDataFromElement(line);
					}
				}



				dbengine.fnsavetblStorePaymentStageMapping(StoreID,PaymentStage);
				if(dbengine.fnCheckFortblNewStoreSalesQuotePaymentDetailsHasStore(StoreID)==0) {


					String PaymentStageDetails = "0";
					if ((PaymentStage).length() == 1) {
						if (PaymentStage.equals("1")) {
							PaymentStageDetails = PaymentStage + "~0~0~0~0";
						}
						if (PaymentStage.equals("2")) {
							PaymentStageDetails = PaymentStage + "~0~0~0~0";
						}
						if (PaymentStage.equals("3")) {
							PaymentStageDetails = PaymentStage + "~100~0~0~0~0";
						}
					} else {
						PaymentStageDetails = PaymentStage;
					}
					dbengine.fnsaveNewStoreSalesQuotePaymentDetails(StoreID, PaymentStageDetails);
				}



			}
			NodeList tblPreAddedStoresDataDetailsNode = doc.getElementsByTagName("tblPreAddedStoresDataDetails");
			for (int i = 0; i < tblPreAddedStoresDataDetailsNode.getLength(); i++)
			{

				String StoreIDDB="0";
				String GrpQuestID ="0";
				String QstId ="0";
				String AnsControlTypeID ="0";

				String AnsTextVal ="0";

				String flgPrvVal ="2";


				Element element = (Element) tblPreAddedStoresDataDetailsNode.item(i);

				if(!element.getElementsByTagName("StoreIDDB").equals(null))
				{
					NodeList StoreIDDBNode = element.getElementsByTagName("StoreIDDB");
					Element     line = (Element) StoreIDDBNode.item(0);
					if (StoreIDDBNode.getLength()>0)
					{
						StoreIDDB=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("GrpQuestID").equals(null))
				{
					NodeList GrpQuestIDNode = element.getElementsByTagName("GrpQuestID");
					Element     line = (Element) GrpQuestIDNode.item(0);
					if (GrpQuestIDNode.getLength()>0)
					{
						GrpQuestID=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("QstId").equals(null))
				{
					NodeList QstIdNode = element.getElementsByTagName("QstId");
					Element     line = (Element) QstIdNode.item(0);
					if (QstIdNode.getLength()>0)
					{
						QstId=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("AnsControlTypeID").equals(null))
				{
					NodeList AnsControlTypeIDNode = element.getElementsByTagName("AnsControlTypeID");
					Element     line = (Element) AnsControlTypeIDNode.item(0);
					if (AnsControlTypeIDNode.getLength()>0)
					{
						AnsControlTypeID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("Ans").equals(null))
				{
					NodeList AnsTextValNode = element.getElementsByTagName("Ans");
					Element     line = (Element) AnsTextValNode.item(0);
					if (AnsTextValNode.getLength()>0)
					{
						AnsTextVal=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("flgPrvValue").equals(null))
				{
					NodeList OptionValueNode = element.getElementsByTagName("flgPrvValue");
					Element     line = (Element) OptionValueNode.item(0);
					if (OptionValueNode.getLength()>0)
					{
						flgPrvVal=xmlParser.getCharacterDataFromElement(line);
					}
				}


				dbengine.saveTblPreAddedStoresDataDetails(StoreIDDB, GrpQuestID, QstId, AnsControlTypeID,AnsTextVal,flgPrvVal);
			}

			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e) {

			// System.out.println("Aman Exception occur in GetIMEIVersionDetailStatusNew :"+e.toString());
			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			dbengine.close();

			return setmovie;
		}
	}

	public ServiceWorker callfnSingleCallAllWebService(Context ctx,int ApplicationID,String uuid)
	{
		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);

		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnSingleCallAllStoreMappingMeijiTT";
		final String METHOD_NAME = "fnSingleCallAllStoreMappingMeijiTT";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;

		//Create request
		SoapObject table = null; //Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; //Its the client petition to the web service
		SoapObject tableRow = null; //Contains row of table
		SoapObject responseBody = null; //Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;
		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,0);

		ServiceWorker setmovie = new ServiceWorker();
		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			client.addProperty("ApplicationID", ApplicationID);
			client.addProperty("uuid", uuid.toString());

			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);
			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// System.out.println("Kajol 2 :"+totalCount);
			String resultString=androidHttpTransport.responseDump;

			String name=responseBody.getProperty(0).toString();

			// System.out.println("Kajol 3 :"+name);

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);


			dbengine.open();
			dbengine.deleteAllSingleCallWebServiceTable();

			int  gblQuestIDForOutChannel=0;
			NodeList tblQuestIDForOutChannel = doc.getElementsByTagName("tblQuestIDForOutChannel");
			for (int i = 0; i < tblQuestIDForOutChannel.getLength(); i++)
			{
				int grpQuestId=0;
				int questId=0;
				String optId="0-0-0";
				int sectionCount=0;
				Element element = (Element) tblQuestIDForOutChannel.item(i);
				if(!element.getElementsByTagName("GrpQuestID").equals(null))
				{
					NodeList QuestIDForOutChannelNode = element.getElementsByTagName("GrpQuestID");
					Element     line = (Element) QuestIDForOutChannelNode.item(0);
					if(QuestIDForOutChannelNode.getLength()>0)
					{
						grpQuestId=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				if(!element.getElementsByTagName("QuestID").equals(null))
				{
					NodeList QuestIDNode = element.getElementsByTagName("QuestID");
					Element     line = (Element) QuestIDNode.item(0);
					if(QuestIDNode.getLength()>0)
					{
						questId=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
						gblQuestIDForOutChannel=questId;
					}
				}
				if(!element.getElementsByTagName("OptionID").equals(null))
				{
					NodeList optIDNode = element.getElementsByTagName("OptionID");
					Element     line = (Element) optIDNode.item(0);
					if(optIDNode.getLength()>0)
					{
						optId=xmlParser.getCharacterDataFromElement(line);

					}
				}
				if(!element.getElementsByTagName("SectionCount").equals(null))
				{
					NodeList SectionCountNode = element.getElementsByTagName("SectionCount");
					Element     line = (Element) SectionCountNode.item(0);
					if(SectionCountNode.getLength()>0)
					{
						sectionCount=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));

					}
				}
				dbengine.saveOutletChammetQstnIdGrpId(grpQuestId, questId,optId,sectionCount);
			}
			NodeList tblQuestIDForName = doc.getElementsByTagName("tblQuestIDForName");
			for (int i = 0; i < tblQuestIDForName.getLength(); i++)
			{
				int grpQuestId=0;
				int questId=0;
				Element element = (Element) tblQuestIDForName.item(i);
				if(!element.getElementsByTagName("GrpQuestID").equals(null))
				{
					NodeList QuestIDForOutChannelNode = element.getElementsByTagName("GrpQuestID");
					Element     line = (Element) QuestIDForOutChannelNode.item(0);
					if(QuestIDForOutChannelNode.getLength()>0)
					{
						grpQuestId=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				if(!element.getElementsByTagName("QuestID").equals(null))
				{
					NodeList QuestIDNode = element.getElementsByTagName("QuestID");
					Element     line = (Element) QuestIDNode.item(0);
					if(QuestIDNode.getLength()>0)
					{
						questId=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));

					}
				}
				dbengine.savetblQuestIDForName(grpQuestId, questId);
			}
			NodeList tblSPGetDistributorDetailsNode = doc.getElementsByTagName("tblGetPDAQuestMstr");
			for (int i = 0; i < tblSPGetDistributorDetailsNode.getLength(); i++)
			{
				String QuestID="0";
				String QuestCode="0";
				String QuestDesc="0";
				String QuestType="0";

				String AnsControlType="0";
				String AsnControlInputTypeID="0";
				String AnsControlInputTypeMaxLength="0";
				String AnsControlInputTypeMinLength="0";
				String AnsMustRequiredFlg="0";
				String QuestBundleFlg="0";
				String ApplicationTypeID="0";
				String Sequence="0";
				String answerHint="N/A";
				int flgNewStore=1;
				int flgStoreValidation=1;
				int flgQuestIDForOutChannel=0;

				Element element = (Element) tblSPGetDistributorDetailsNode.item(i);




				if(!element.getElementsByTagName("QuestID").equals(null))
				{
					NodeList QuestIDNode = element.getElementsByTagName("QuestID");
					Element     line = (Element) QuestIDNode.item(0);
					if(QuestIDNode.getLength()>0)
					{
						QuestID=xmlParser.getCharacterDataFromElement(line);
						if(gblQuestIDForOutChannel==Integer.parseInt(QuestID))
						{
							flgQuestIDForOutChannel=1;
						}
					}
				}
				if(!element.getElementsByTagName("QuestCode").equals(null))
				{
					NodeList QuestCodeNode = element.getElementsByTagName("QuestCode");
					Element     line = (Element) QuestCodeNode.item(0);
					if(QuestCodeNode.getLength()>0)
					{
						QuestCode=xmlParser.getCharacterDataFromElement(line);
					}
				}



				if(!element.getElementsByTagName("QuestDesc").equals(null))
				{
					NodeList QuestDescNode = element.getElementsByTagName("QuestDesc");
					Element     line = (Element) QuestDescNode.item(0);
					if(QuestDescNode.getLength()>0)
					{
						QuestDesc=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("QuestType").equals(null))
				{
					NodeList QuesTypeNode = element.getElementsByTagName("QuestType");
					Element     line = (Element) QuesTypeNode.item(0);
					if(QuesTypeNode.getLength()>0)
					{
						QuestType=xmlParser.getCharacterDataFromElement(line);
					}
				}


				if(!element.getElementsByTagName("AnsControlType").equals(null))
				{
					NodeList AnsControlTypeNode = element.getElementsByTagName("AnsControlType");
					Element     line = (Element) AnsControlTypeNode.item(0);
					if(AnsControlTypeNode.getLength()>0)
					{
						AnsControlType=xmlParser.getCharacterDataFromElement(line);
					}
				}



				if(!element.getElementsByTagName("AsnControlInputTypeID").equals(null))
				{
					NodeList AsnControlInputTypeIDNode = element.getElementsByTagName("AsnControlInputTypeID");
					Element     line = (Element) AsnControlInputTypeIDNode.item(0);
					if(AsnControlInputTypeIDNode.getLength()>0)
					{
						AsnControlInputTypeID=xmlParser.getCharacterDataFromElement(line);
					}
				}



				if(!element.getElementsByTagName("AnsControlInputTypeMaxLength").equals(null))
				{
					NodeList AnsControlInputTypeMaxLengthNode = element.getElementsByTagName("AnsControlInputTypeMaxLength");
					Element      line = (Element) AnsControlInputTypeMaxLengthNode.item(0);
					if(AnsControlInputTypeMaxLengthNode.getLength()>0)
					{
						AnsControlInputTypeMaxLength=xmlParser.getCharacterDataFromElement(line);
					}
				}


				if(!element.getElementsByTagName("AnsControlIntputTypeMinLength").equals(null))
				{
					NodeList AnsControlInputTypeMinLengthNode = element.getElementsByTagName("AnsControlIntputTypeMinLength");
					Element      line = (Element) AnsControlInputTypeMinLengthNode.item(0);
					if(AnsControlInputTypeMinLengthNode.getLength()>0)
					{
						AnsControlInputTypeMinLength=xmlParser.getCharacterDataFromElement(line);
					}
				}



				if(!element.getElementsByTagName("AnsMustRequiredFlg").equals(null))
				{
					NodeList AnsMustRequiredFlgNode = element.getElementsByTagName("AnsMustRequiredFlg");
					Element      line = (Element) AnsMustRequiredFlgNode.item(0);
					if(AnsMustRequiredFlgNode.getLength()>0)
					{
						AnsMustRequiredFlg=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("QuestBundleFlg").equals(null))
				{
					NodeList QuestBundleFlgNode = element.getElementsByTagName("QuestBundleFlg");
					Element      line = (Element) QuestBundleFlgNode.item(0);
					if(QuestBundleFlgNode.getLength()>0)
					{
						QuestBundleFlg=xmlParser.getCharacterDataFromElement(line);
					}
				}


				if(!element.getElementsByTagName("ApplicationTypeID").equals(null))
				{
					NodeList ApplicationTypeIDNode = element.getElementsByTagName("ApplicationTypeID");
					Element      line = (Element) ApplicationTypeIDNode.item(0);
					if(ApplicationTypeIDNode.getLength()>0)
					{
						ApplicationTypeID=xmlParser.getCharacterDataFromElement(line);
					}
				}


				if(!element.getElementsByTagName("Sequence").equals(null))
				{
					NodeList SequenceNode = element.getElementsByTagName("Sequence");
					Element      line = (Element) SequenceNode.item(0);
					if(SequenceNode.getLength()>0)
					{
						Sequence=xmlParser.getCharacterDataFromElement(line);
					}
				}


				if(!element.getElementsByTagName("AnswerHint").equals(null))
				{
					NodeList SequenceNode = element.getElementsByTagName("AnswerHint");
					Element      line = (Element) SequenceNode.item(0);
					if(SequenceNode.getLength()>0)
					{
						answerHint=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("flgNewStore").equals(null))
				{
					NodeList flgNewStoreNode = element.getElementsByTagName("flgNewStore");
					Element     line = (Element) flgNewStoreNode.item(0);
					if (flgNewStoreNode.getLength()>0)
					{
						flgNewStore=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("flgStoreValidation").equals(null))
				{
					NodeList flgStoreValidationNode = element.getElementsByTagName("flgStoreValidation");
					Element     line = (Element) flgStoreValidationNode.item(0);
					if (flgStoreValidationNode.getLength()>0)
					{
						flgStoreValidation=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				dbengine.savetblQuestionMstr(QuestID, QuestCode, QuestDesc, QuestType, AnsControlType, AsnControlInputTypeID, AnsControlInputTypeMaxLength, AnsMustRequiredFlg, QuestBundleFlg, ApplicationTypeID, Sequence,AnsControlInputTypeMinLength,answerHint,flgQuestIDForOutChannel,flgNewStore,flgStoreValidation);

			}


			NodeList tblGetPDAQuestGrpMappingNode = doc.getElementsByTagName("tblGetPDAQuestGrpMapping");
			for (int i = 0; i < tblGetPDAQuestGrpMappingNode.getLength(); i++)
			{
				String GrpQuestID="0";
				String QuestID="0";
				String GrpID="0";
				String GrpNodeID="0";

				String GrpDesc="0";
				String SectionNo="0";
				String GrpCopyID="0";
				String QuestCopyID="0";
				String sequence="0";
				int flgNewStore=1;
				int flgStoreValidation=1;
				Element element = (Element) tblGetPDAQuestGrpMappingNode.item(i);




				if(!element.getElementsByTagName("GrpQuestID").equals(null))
				{
					NodeList GrpQuestIDNode = element.getElementsByTagName("GrpQuestID");
					Element     line = (Element) GrpQuestIDNode.item(0);
					if(GrpQuestIDNode.getLength()>0)
					{
						GrpQuestID=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("QuestID").equals(null))
				{
					NodeList QuestIDNode = element.getElementsByTagName("QuestID");
					Element     line = (Element) QuestIDNode.item(0);
					if(QuestIDNode.getLength()>0)
					{
						QuestID=xmlParser.getCharacterDataFromElement(line);
					}
				}



				if(!element.getElementsByTagName("GrpID").equals(null))
				{
					NodeList GrpIDNode = element.getElementsByTagName("GrpID");
					Element     line = (Element) GrpIDNode.item(0);
					if(GrpIDNode.getLength()>0)
					{
						GrpID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("GrpNodeID").equals(null))
				{
					NodeList GrpNodeIDNode = element.getElementsByTagName("GrpNodeID");
					Element     line = (Element) GrpNodeIDNode.item(0);
					if(GrpNodeIDNode.getLength()>0)
					{
						GrpNodeID=xmlParser.getCharacterDataFromElement(line);
					}
				}


				if(!element.getElementsByTagName("GrpDesc").equals(null))
				{
					NodeList GrpDescNode = element.getElementsByTagName("GrpDesc");
					Element     line = (Element) GrpDescNode.item(0);
					if(GrpDescNode.getLength()>0)
					{
						GrpDesc=xmlParser.getCharacterDataFromElement(line);
					}
				}




				if(!element.getElementsByTagName("SectionNo").equals(null))
				{
					NodeList SectionNoNode = element.getElementsByTagName("SectionNo");
					Element     line = (Element) SectionNoNode.item(0);
					if(SectionNoNode.getLength()>0)
					{
						SectionNo=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("GrpCopyID").equals(null))
				{
					NodeList GrpCopyIDNode = element.getElementsByTagName("GrpCopyID");
					Element     line = (Element) GrpCopyIDNode.item(0);
					if(GrpCopyIDNode.getLength()>0)
					{
						GrpCopyID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("QuestCopyID").equals(null))
				{
					NodeList QuestCopyIDNode = element.getElementsByTagName("QuestCopyID");
					Element     line = (Element) QuestCopyIDNode.item(0);
					if(QuestCopyIDNode.getLength()>0)
					{
						QuestCopyID=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("Sequence").equals(null))
				{
					NodeList SequenceNode = element.getElementsByTagName("Sequence");
					Element     line = (Element) SequenceNode.item(0);
					if(SequenceNode.getLength()>0)
					{
						sequence=xmlParser.getCharacterDataFromElement(line);
					}
				}


				if(!element.getElementsByTagName("flgNewStore").equals(null))
				{
					NodeList flgNewStoreNode = element.getElementsByTagName("flgNewStore");
					Element     line = (Element) flgNewStoreNode.item(0);
					if (flgNewStoreNode.getLength()>0)
					{
						flgNewStore=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("flgStoreValidation").equals(null))
				{
					NodeList flgStoreValidationNode = element.getElementsByTagName("flgStoreValidation");
					Element     line = (Element) flgStoreValidationNode.item(0);
					if (flgStoreValidationNode.getLength()>0)
					{
						flgStoreValidation=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				dbengine.savetblPDAQuestGrpMappingMstr(GrpQuestID, QuestID, GrpID, GrpNodeID, GrpDesc, SectionNo, GrpCopyID, QuestCopyID,sequence,flgNewStore,flgStoreValidation);

			}

			NodeList tblGetPDAQuestOptionMstrNode = doc.getElementsByTagName("tblGetPDAQuestOptionMstr");
			for (int i = 0; i < tblGetPDAQuestOptionMstrNode.getLength(); i++)
			{

				String OptID="0";
				String QuestID="0";
				String OptionNo="0";
				String OptionDescr="0";
				String Sequence="0";






				Element element = (Element) tblGetPDAQuestOptionMstrNode.item(i);

				if(!element.getElementsByTagName("OptID").equals(null))
				{
					NodeList OptIDNode = element.getElementsByTagName("OptID");
					Element      line = (Element) OptIDNode.item(0);
					if(OptIDNode.getLength()>0)
					{
						OptID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("QuestID").equals(null))
				{
					NodeList QuestIDNode = element.getElementsByTagName("QuestID");
					Element      line = (Element) QuestIDNode.item(0);
					if(QuestIDNode.getLength()>0)
					{
						QuestID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("AnsVal").equals(null))
				{
					NodeList OptionNoDNode = element.getElementsByTagName("AnsVal");
					Element      line = (Element) OptionNoDNode.item(0);
					if(OptionNoDNode.getLength()>0)
					{
						OptionNo=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("OptionDescr").equals(null))
				{
					NodeList OptionDescrNode = element.getElementsByTagName("OptionDescr");
					Element      line = (Element) OptionDescrNode.item(0);
					if(OptionDescrNode.getLength()>0)
					{
						OptionDescr=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("Sequence").equals(null))
				{
					NodeList SequenceNode = element.getElementsByTagName("Sequence");
					Element      line = (Element) SequenceNode.item(0);
					if(SequenceNode.getLength()>0)
					{

						Sequence=xmlParser.getCharacterDataFromElement(line);

					}
				}
				dbengine.savetblOptionMstr(OptID, QuestID, OptionNo, OptionDescr, Sequence);
				System.out.println("OptID:" + OptID + "QuestID:" + QuestID + "OptionNo:" + OptionNo + "OptionDescr:" + OptionDescr + "Sequence:" + Sequence);

			}


			NodeList tblGetPDAQuestionDependentMstrNode = doc.getElementsByTagName("tblGetPDAQuestionDependentMstr");
			for (int i = 0; i < tblGetPDAQuestionDependentMstrNode.getLength(); i++)
			{


				String QuestionID="0";
				String OptionID="0";
				String DependentQuestionID="0";

				String GrpID="0";
				String DpndntGrpID="0";







				Element element = (Element) tblGetPDAQuestionDependentMstrNode.item(i);



				if(!element.getElementsByTagName("QuestID").equals(null))
				{
					NodeList QuestionIDNode = element.getElementsByTagName("QuestID");
					Element      line = (Element) QuestionIDNode.item(0);
					if(QuestionIDNode.getLength()>0)
					{
						QuestionID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("OptID").equals(null))
				{
					NodeList OptionIDNode = element.getElementsByTagName("OptID");
					Element      line = (Element) OptionIDNode.item(0);
					if(OptionIDNode.getLength()>0)
					{
						OptionID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("DependentQuestionID").equals(null))
				{
					NodeList DependentQuestionIDNode = element.getElementsByTagName("DependentQuestionID");
					Element      line = (Element) DependentQuestionIDNode.item(0);
					if(DependentQuestionIDNode.getLength()>0)
					{
						DependentQuestionID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("GrpQuestID").equals(null))
				{
					NodeList GrpIDNode = element.getElementsByTagName("GrpQuestID");
					Element      line = (Element) GrpIDNode.item(0);
					if(GrpIDNode.getLength()>0)
					{
						GrpID=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("GrpDepQuestID").equals(null))
				{
					NodeList GrpDepQuestIDNode = element.getElementsByTagName("GrpDepQuestID");
					Element      line = (Element) GrpDepQuestIDNode.item(0);
					if(GrpDepQuestIDNode.getLength()>0)
					{
						DpndntGrpID=xmlParser.getCharacterDataFromElement(line);
					}
				}
				dbengine.savetblQuestionDependentMstr(QuestionID, OptionID, DependentQuestionID,GrpID,DpndntGrpID);

				//  dbengine.savetblOptionMstr(OptID, QuestID, OptionNo, OptionDescr, Sequence);
				System.out.println("QuestionID:" + QuestionID + "OptionID:" + OptionID + "DependentQuestionID:" + DependentQuestionID);

			}






			NodeList tblPDAQuestOptionDependentMstr = doc.getElementsByTagName("tblPDAQuestOptionDependentMstr");
			for (int i = 0; i < tblPDAQuestOptionDependentMstr.getLength(); i++)
			{
				int qstId=0;
				int depQstId=0;
				int grpQstId=0;
				int grpDepQstId=0;
				Element element = (Element) tblPDAQuestOptionDependentMstr.item(i);




				if(!element.getElementsByTagName("QstId").equals(null))
				{
					NodeList QstIdNode = element.getElementsByTagName("QstId");
					Element     line = (Element) QstIdNode.item(0);
					if(QstIdNode.getLength()>0)
					{
						qstId=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				if(!element.getElementsByTagName("DepQstId").equals(null))
				{
					NodeList DepQstIdNode = element.getElementsByTagName("DepQstId");
					Element     line = (Element) DepQstIdNode.item(0);
					if(DepQstIdNode.getLength()>0)
					{
						depQstId=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}



				if(!element.getElementsByTagName("GrpQuestID").equals(null))
				{
					NodeList GrpQuestIDNode = element.getElementsByTagName("GrpQuestID");
					Element     line = (Element) GrpQuestIDNode.item(0);
					if(GrpQuestIDNode.getLength()>0)
					{
						grpQstId=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("GrpDepQuestID").equals(null))
				{
					NodeList GrpDepQuestIDNode = element.getElementsByTagName("GrpDepQuestID");
					Element     line = (Element) GrpDepQuestIDNode.item(0);
					if(GrpDepQuestIDNode.getLength()>0)
					{
						grpDepQstId=Integer.valueOf(xmlParser.getCharacterDataFromElement(line));
					}
				}
	         	              /*   int qstId=0;
		         	            	int depQstId=0;
		         	            	int grpQstId=0;
		         	            	int grpDepQstId=0;*/
				dbengine.savetblPDAQuestOptionDependentMstr(qstId, depQstId, grpQstId, grpDepQstId);

			}

			NodeList tblPDAQuestOptionValuesDependentMstr = doc.getElementsByTagName("tblPDAQuestOptionValuesDependentMstr");
			for (int i = 0; i < tblPDAQuestOptionValuesDependentMstr.getLength(); i++)
			{
				int DepQstId=0;
				String DepAnswValId="0";
				int QstId=0;
				String AnswValId="0";
				String OptDescr="N/A";
				int Sequence=0;
				int GrpQuestID=0;
				int GrpDepQuestID=0;

				Element element = (Element) tblPDAQuestOptionValuesDependentMstr.item(i);




				if(!element.getElementsByTagName("DepQstId").equals(null))
				{
					NodeList DepQstIdNode = element.getElementsByTagName("DepQstId");
					Element     line = (Element) DepQstIdNode.item(0);
					if(DepQstIdNode.getLength()>0)
					{
						DepQstId=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				if(!element.getElementsByTagName("DepOptID").equals(null))
				{
					NodeList DepAnswValIdNode = element.getElementsByTagName("DepOptID");
					Element     line = (Element) DepAnswValIdNode.item(0);
					if(DepAnswValIdNode.getLength()>0)
					{
						DepAnswValId=xmlParser.getCharacterDataFromElement(line);
					}
				}



				if(!element.getElementsByTagName("QstId").equals(null))
				{
					NodeList QstIdNode = element.getElementsByTagName("QstId");
					Element     line = (Element) QstIdNode.item(0);
					if(QstIdNode.getLength()>0)
					{
						QstId=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("OptID").equals(null))
				{
					NodeList AnswValIdNode = element.getElementsByTagName("OptID");
					Element     line = (Element) AnswValIdNode.item(0);
					if(AnswValIdNode.getLength()>0)
					{
						AnswValId=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("OptDescr").equals(null))
				{
					NodeList OptDescrNode = element.getElementsByTagName("OptDescr");
					Element     line = (Element) OptDescrNode.item(0);
					if(OptDescrNode.getLength()>0)
					{
						OptDescr=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("Sequence").equals(null))
				{
					NodeList AnswValIdNode = element.getElementsByTagName("Sequence");
					Element     line = (Element) AnswValIdNode.item(0);
					if(AnswValIdNode.getLength()>0)
					{
						Sequence=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				if(!element.getElementsByTagName("GrpQuestID").equals(null))
				{
					NodeList GrpQuestIDNode = element.getElementsByTagName("GrpQuestID");
					Element     line = (Element) GrpQuestIDNode.item(0);
					if(GrpQuestIDNode.getLength()>0)
					{
						GrpQuestID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				if(!element.getElementsByTagName("GrpDepQuestID").equals(null))
				{
					NodeList GrpDepQuestIDNode = element.getElementsByTagName("GrpDepQuestID");
					Element     line = (Element) GrpDepQuestIDNode.item(0);
					if(GrpDepQuestIDNode.getLength()>0)
					{
						GrpDepQuestID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				dbengine.savetblPDAQuestOptionValuesDependentMstr(DepQstId, DepAnswValId, QstId, AnswValId, OptDescr, Sequence, GrpQuestID, GrpDepQuestID);

			}
			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e)
		{
			dbengine.close();
			System.out.println("Aman Exception occur in fnSingleCallAllWebService :"+e.toString());
			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();


			return setmovie;
		}
	}

	public ServiceWorker getQuotationDataFromServer(Context ctx, String dateVAL, String uuid, String rID)
	{
		this.context = ctx;

		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);
		String RouteType="0";
		try
		{

			System.out.println("hi"+RouteType);
		}
		catch(Exception e)
		{
			System.out.println("error"+e);
		}

		dbengine.open();
		dbengine.deleteAllQuotationTables();

		final String SOAP_ACTION = "http://tempuri.org/fnGetPaymentForStoreMapping";
		final String METHOD_NAME = "fnGetPaymentForStoreMapping";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;

		SoapObject table = null; // Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; // Its the client petition to the web service
		SoapObject tableRow = null; // Contains row of table
		SoapObject responseBody = null; // Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sse.addMapping(NAMESPACE, "ServiceWorker", this.getClass());
		// Note if class name isn't "ABC_CLASS_NAME" ,you must change
		sse.dotNet = true; // if WebService written .Net is result=true
		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();
		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);

			Date currDate= new Date();
			SimpleDateFormat currDateFormat =new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);

			currSysDate = currDateFormat.format(currDate).toString();
			SysDate = currSysDate.trim().toString();

      /*// System.out.println("Aman Exception occur value bydate"+ dateVAL.toString());
      // System.out.println("Aman Exception occur value IMEINo"+ uuid.toString());
      // System.out.println("Aman Exception occur value rID"+ rID.toString());
      // System.out.println("Aman Exception occur value SysDate"+ SysDate.toString());
      // System.out.println("Aman Exception occur value AppVersionID"+ dbengine.AppVersionID.toString());
      */
			client.addProperty("bydate", dateVAL.toString());
			client.addProperty("IMEINo", uuid.toString());
			client.addProperty("rID", rID.toString());
			client.addProperty("RouteType", RouteType);
			client.addProperty("SysDate", SysDate.toString());
			client.addProperty("AppVersionID", dbengine.AppVersionID.toString());



			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			System.out.println("S1");

			androidHttpTransport.call(SOAP_ACTION, sse);
			responseBody = (SoapObject)sse.bodyIn;


			int totalCount = responseBody.getPropertyCount();

			// System.out.println("Kajol 2 :"+totalCount);
			String resultString=androidHttpTransport.responseDump;

			String name=responseBody.getProperty(0).toString();

			// System.out.println("Kajol 3 :"+name);

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);
			System.out.println("shivam4");

			//   dbengine.open();











			NodeList tblSalesQuotePaymentModeMstrNode = doc.getElementsByTagName("tblSalesQuotePaymentModeMstr");
			for (int i = 0; i < tblSalesQuotePaymentModeMstrNode.getLength(); i++)
			{

				String PymtModeId="0";
				String PymtMode="0";


				Element element = (Element) tblSalesQuotePaymentModeMstrNode.item(i);

				if(!element.getElementsByTagName("PymtModeId").equals(null))
				{

					NodeList PymtModeIdNode = element.getElementsByTagName("PymtModeId");
					Element     line = (Element) PymtModeIdNode.item(0);

					if(PymtModeIdNode.getLength()>0)
					{

						PymtModeId=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("PymtMode").equals(null))
				{

					NodeList PymtModeNode = element.getElementsByTagName("PymtMode");
					Element     line = (Element) PymtModeNode.item(0);

					if(PymtModeNode.getLength()>0)
					{

						PymtMode=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.SavetblSalesQuotePaymentModeMstr(PymtModeId, PymtMode);
			}




			NodeList tblSalesQuotePaymentStageMstr = doc.getElementsByTagName("tblSalesQuotePaymentStageMstr");
			for (int i = 0; i < tblSalesQuotePaymentStageMstr.getLength(); i++)
			{

				String PymtStageId="0";
				String PymtStage="0";
				String PymtModeId="0";


				Element element = (Element) tblSalesQuotePaymentStageMstr.item(i);

				if(!element.getElementsByTagName("PymtStageId").equals(null))
				{

					NodeList PymtStageIdNode = element.getElementsByTagName("PymtStageId");
					Element     line = (Element) PymtStageIdNode.item(0);

					if(PymtStageIdNode.getLength()>0)
					{

						PymtStageId=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("PymtStage").equals(null))
				{

					NodeList PymtStageNode = element.getElementsByTagName("PymtStage");
					Element     line = (Element) PymtStageNode.item(0);

					if(PymtStageNode.getLength()>0)
					{

						PymtStage=xmlParser.getCharacterDataFromElement(line);
					}
				}

				if(!element.getElementsByTagName("PymtModeId").equals(null))
				{

					NodeList PymtModeIdNode = element.getElementsByTagName("PymtModeId");
					Element     line = (Element) PymtModeIdNode.item(0);

					if(PymtModeIdNode.getLength()>0)
					{

						PymtModeId=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.SavetblSalesQuotePaymentStageMstr(PymtStageId, PymtStage, PymtModeId);
			}




      /*NodeList tblSalesQuotePaymentStageModeMapMstr = doc.getElementsByTagName("tblSalesQuotePaymentStageModeMapMstr");
      for (int i = 0; i < tblSalesQuotePaymentStageModeMapMstr.getLength(); i++)
      {

         String PymtStageId    ="0";
         String PymtModeId="0";


         Element element = (Element) tblSalesQuotePaymentStageModeMapMstr.item(i);

         if(!element.getElementsByTagName("PymtStageId").equals(null))
         {

            NodeList PymtStageIdNode = element.getElementsByTagName("PymtStageId");
            Element     line = (Element) PymtStageIdNode.item(0);

            if(PymtStageIdNode.getLength()>0)
            {

               PymtStageId=xmlParser.getCharacterDataFromElement(line);
            }
         }

         if(!element.getElementsByTagName("PymtModeId").equals(null))
         {

            NodeList PymtModeIdNode = element.getElementsByTagName("PymtModeId");
            Element     line = (Element) PymtModeIdNode.item(0);

            if(PymtModeIdNode.getLength()>0)
            {

               PymtModeId=xmlParser.getCharacterDataFromElement(line);
            }
         }


         dbengine.SavetblSalesQuotePaymentStageModeMapMstr(PymtStageId, PymtModeId);
      }*/










			setmovie.director = "1";
			// System.out.println("ServiceWorkerNitish getallStores Completed ");
			flagExecutedServiceSuccesfully=37;
			return setmovie;


		} catch (Exception e) {

			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			flagExecutedServiceSuccesfully=0;
			dbengine.close();
			return setmovie;
		}

	}

	public ServiceWorker getSOSummary(Context ctx,String uuid,String CurDate,int DatabaseVersion,int ApplicationID)
	{

		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);


		decimalFormat.applyPattern(pattern);

		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnGetLTFoodsSOSummary";
		final String METHOD_NAME = "fnGetLTFoodsSOSummary";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		//Create request
		SoapObject table = null; // Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; // Its the client petition to the web service
		SoapObject tableRow = null; // Contains row of table
		SoapObject responseBody = null; // Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();


		// // System.out.println("Kajol 100");

		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);


			// // System.out.println("Kajol 101");

			String StoreListOld="";
			LinkedHashMap<String, String> hmapStoreLisMstr=new LinkedHashMap<String, String>();
			hmapStoreLisMstr=dbengine.fnGeStoreCoverageIDAndflagValidaeForWebService(0,0);
			int OverAllStoresAddedBySO=dbengine.fnGetCountOfStoresAddedBySO();
			if(dbengine.fncheckCountNearByStoreExistsOrNot(1000)==1)
			{


				System.out.println("Nitish Count Previous Store ="+hmapStoreLisMstr.size());
				/*LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapStoreLisMstr);
				Set set2 = map.entrySet();
				Iterator iterator = set2.iterator();
				while(iterator.hasNext())
				{
					Map.Entry me2 = (Map.Entry)iterator.next();
					if(StoreListOld.equals(""))
					{
						StoreListOld=me2.getKey().toString();
					}
					else
					{
						StoreListOld +="^"+me2.getKey().toString();
					}
					//CoverageAreaNames[index]=me2.getKey().toString();

				}*/


			}



			client.addProperty("uuid", uuid.toString());

			client.addProperty("SysDate", CurDate.toString());
			client.addProperty("AppVersionID", dbengine.AppVersionID.toString());
			client.addProperty("StoreListOld", StoreListOld);


			sse.setOutputSoapObject(client);
			// // System.out.println("Kajol 103");
			sse.bodyOut = client;
			// // System.out.println("Kajol 104");

			androidHttpTransport.call(SOAP_ACTION, sse);

			// // System.out.println("Kajol 1");

			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// // System.out.println("Kajol 2 :"+totalCount);
			String resultString=androidHttpTransport.responseDump;

			String name=responseBody.getProperty(0).toString();

			// // System.out.println("Kajol 3 :"+name);

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);

			dbengine.open();



			NodeList tblSONameAndSummurayRefreshTime = doc.getElementsByTagName("tblSONameAndSummurayRefreshTime");
			for (int i = 0; i < tblSONameAndSummurayRefreshTime.getLength(); i++)
			{

				if(i==0)
				{
					dbengine.fnDeleteOldtblDSRSummaryDetialsRecordsAndSONameTable();
				}
				String SOName="0";
				String SummurayRefreshTime="0";
				Element element = (Element) tblSONameAndSummurayRefreshTime.item(i);
				if(!element.getElementsByTagName("Person").equals(null))
				{
					NodeList SONameNode = element.getElementsByTagName("Person");
					Element     line = (Element) SONameNode.item(0);
					if (SONameNode.getLength()>0)
					{
						SOName=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("RefresTime").equals(null))
				{
					NodeList SummurayRefreshTimeNode = element.getElementsByTagName("RefresTime");
					Element     line = (Element) SummurayRefreshTimeNode.item(0);
					if (SummurayRefreshTimeNode.getLength()>0)
					{
						SummurayRefreshTime=xmlParser.getCharacterDataFromElement(line);
					}
				}
				dbengine.savetblSONameAndSummurayRefreshTime(SOName,SummurayRefreshTime);
			}
			NodeList tblSODSRSummary = doc.getElementsByTagName("tblSODSRSummary");
			for (int i = 0; i < tblSODSRSummary.getLength(); i++)
			{

				int DSRID=0;
				String DSRName ="0";
				int TotStoreAdded=0;
				int Approved=0;
				int Rejected=0;

				int ReMap=0;

				int Pending=0;

				int flgDSROrSO=1;


				Element element = (Element) tblSODSRSummary.item(i);
				if(!element.getElementsByTagName("CoverageAreaID").equals(null))
				{
					NodeList DSRIDNode = element.getElementsByTagName("CoverageAreaID");
					Element     line = (Element) DSRIDNode.item(0);
					if (DSRIDNode.getLength()>0)
					{
						DSRID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				if(!element.getElementsByTagName("CoverageArea").equals(null))
				{
					NodeList DSRNameNode = element.getElementsByTagName("CoverageArea");
					Element     line = (Element) DSRNameNode.item(0);
					if (DSRNameNode.getLength()>0)
					{
						DSRName=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("TotStoreAdded").equals(null))
				{
					NodeList TotStoreAddedNode = element.getElementsByTagName("TotStoreAdded");
					Element     line = (Element) TotStoreAddedNode.item(0);
					if (TotStoreAddedNode.getLength()>0)
					{
						TotStoreAdded=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));


					}
				}
				if(!element.getElementsByTagName("Approved").equals(null))
				{
					NodeList ApprovedNode = element.getElementsByTagName("Approved");
					Element     line = (Element) ApprovedNode.item(0);
					if (ApprovedNode.getLength()>0)
					{
						Approved=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}

				if(!element.getElementsByTagName("Rejected").equals(null))
				{
					NodeList RejectedNode = element.getElementsByTagName("Rejected");
					Element     line = (Element) RejectedNode.item(0);
					if (RejectedNode.getLength()>0)
					{
						Rejected=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}



				//StoresOnServer=OutletCountValidated;
				if(!element.getElementsByTagName("ReMap").equals(null))
				{
					NodeList ReMapNode = element.getElementsByTagName("ReMap");
					Element     line = (Element) ReMapNode.item(0);
					if (ReMapNode.getLength()>0)
					{
						ReMap=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				if(!element.getElementsByTagName("Pending").equals(null))
				{
					NodeList PendingNode = element.getElementsByTagName("Pending");
					Element     line = (Element) PendingNode.item(0);
					if (PendingNode.getLength()>0)
					{
						Pending=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
					}
				}
				/*if(hmapStoreLisMstr.size()>0) {
					LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapStoreLisMstr);
					Set set2 = map.entrySet();
					Iterator iterator = set2.iterator();
					while(iterator.hasNext())
					{
						Map.Entry me2 = (Map.Entry)iterator.next();
						String StoreCoverageDetails=me2.getValue().toString();
						if(StoreCoverageDetails.equals(DSRID))
						{
							OutletCountValidated=OutletCountValidated+1;
						}

					}
				}*/
				dbengine.fnsavetblDSRSummaryDetials(DSRID, DSRName,TotStoreAdded,Approved,Rejected,ReMap,Pending,flgDSROrSO);
			}


			NodeList tblSOStoresAdded = doc.getElementsByTagName("tblSOStoresAdded");
			for (int i = 0; i < tblSOStoresAdded.getLength(); i++)
			{

				int SOAddedStore=0;
				int flgDSROrSO=0;
				Element element = (Element) tblSOStoresAdded.item(i);
				if(!element.getElementsByTagName("SOAddedStore").equals(null))
				{
					NodeList SOAddedStoreNode = element.getElementsByTagName("SOAddedStore");
					Element     line = (Element) SOAddedStoreNode.item(0);
					if (SOAddedStoreNode.getLength()>0)
					{
						SOAddedStore=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));


					}
				}

				SOAddedStore=SOAddedStore+OverAllStoresAddedBySO;
				dbengine.fnsavetblDSRSummaryDetials(0, "NA",SOAddedStore,0,0,0,0,flgDSROrSO);
			}

			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e) {

			// System.out.println("Aman Exception occur in GetIMEIVersionDetailStatusNew :"+e.toString());
			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			dbengine.close();

			return setmovie;
		}





	}
	//map distributor
	public ServiceWorker getDistributorMstr(Context ctx,String uuid,String CurDate)
	{
		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);

		decimalFormat.applyPattern(pattern);

		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnGetDBRListForSO";
		final String METHOD_NAME = "fnGetDBRListForSO";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		//Create request
		SoapObject table = null; // Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; // Its the client petition to the web service
		SoapObject tableRow = null; // Contains row of table
		SoapObject responseBody = null; // Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();

		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			client.addProperty("uuid", uuid.toString());
			client.addProperty("SysDate", CurDate.toString());

			sse.setOutputSoapObject(client);

			sse.bodyOut = client;

			androidHttpTransport.call(SOAP_ACTION, sse);

			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// // System.out.println("Kajol 2 :"+totalCount);
			String resultString=androidHttpTransport.responseDump;

			String name=responseBody.getProperty(0).toString();

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);

			dbengine.open();

			//delete tbl
			dbengine.Delete_tblDistributorMstr();

			NodeList tblDistributorListForSONode = doc.getElementsByTagName("tblDistributorListForSO");
			for (int i = 0; i < tblDistributorListForSONode.getLength(); i++)
			{
				String DBRNodeId="0";
				String DBRNodeType="0";
				String Distributor="0";
				String flgReMap="0";
				String ContactNumber="0";
				String EmailID="NA";


				Element element = (Element) tblDistributorListForSONode.item(i);
				if(!element.getElementsByTagName("DBRNodeId").equals(null))
				{
					NodeList DBRNodeIdNode = element.getElementsByTagName("DBRNodeId");
					Element     line = (Element) DBRNodeIdNode.item(0);
					if (DBRNodeIdNode.getLength()>0)
					{
						DBRNodeId=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("DBRNodeType").equals(null))
				{
					NodeList DBRNodeTypeNode = element.getElementsByTagName("DBRNodeType");
					Element     line = (Element) DBRNodeTypeNode.item(0);
					if (DBRNodeTypeNode.getLength()>0)
					{
						DBRNodeType=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("Distributor").equals(null))
				{
					NodeList DistributorNode = element.getElementsByTagName("Distributor");
					Element     line = (Element) DistributorNode.item(0);
					if (DistributorNode.getLength()>0)
					{
						Distributor=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("flgReMap").equals(null))
				{
					NodeList flgReMapNode = element.getElementsByTagName("flgReMap");
					Element     line = (Element) flgReMapNode.item(0);
					if (flgReMapNode.getLength()>0)
					{
						flgReMap=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("ContactNumber").equals(null))
				{
					NodeList ContactNumberNode = element.getElementsByTagName("ContactNumber");
					Element     line = (Element) ContactNumberNode.item(0);
					if (ContactNumberNode.getLength()>0)
					{
						ContactNumber=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("EmailID").equals(null))
				{
					NodeList EmailIDNode = element.getElementsByTagName("EmailID");
					Element     line = (Element) EmailIDNode.item(0);
					if (EmailIDNode.getLength()>0)
					{
						EmailID=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.saveDistributorMstrData(Integer.parseInt(DBRNodeId),Integer.parseInt(DBRNodeType),Distributor,Integer.parseInt(flgReMap),ContactNumber,EmailID);
				System.out.println("DISTRIBTR MASTR....."+Integer.parseInt(DBRNodeId)+"---"+Integer.parseInt(DBRNodeType)+"---"+Distributor+"---"+Integer.parseInt(flgReMap));
			}

			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e) {

			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			dbengine.close();

			return setmovie;
		}


	}

	//account census
	public ServiceWorker getPDAAddedOutletSummaryReport(Context ctx,String uuid,int flgDrillLevel)
	{

		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);
		dbengine.open();

		decimalFormat.applyPattern(pattern);

		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnGetPDAGetAddedOutletSummaryReport";
		final String METHOD_NAME = "fnGetPDAGetAddedOutletSummaryReport";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		//Create request
		SoapObject table = null; // Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; // Its the client petition to the web service
		SoapObject tableRow = null; // Contains row of table
		SoapObject responseBody = null; // Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();


		// // System.out.println("Kajol 100");

		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);

			// // System.out.println("Kajol 101");
			client.addProperty("IMEINo", uuid.toString());
			client.addProperty("flgDrillLevel", flgDrillLevel);

			// // System.out.println("Kajol 102");
			sse.setOutputSoapObject(client);
			// // System.out.println("Kajol 103");
			sse.bodyOut = client;
			// // System.out.println("Kajol 104");

			androidHttpTransport.call(SOAP_ACTION, sse);

			// // System.out.println("Kajol 1");

			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// // System.out.println("Kajol 2 :"+totalCount);
			String resultString=androidHttpTransport.responseDump;

			String name=responseBody.getProperty(0).toString();

			// // System.out.println("Kajol 3 :"+name);

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);

			dbengine.droptblDAGetAddedOutletSummaryReport();
			dbengine.createtblDAGetAddedOutletSummaryReport();

			NodeList tblDAGetAddedOutletSummaryReport = doc.getElementsByTagName("tblDAGetAddedOutletSummaryReport");

			for (int i = 0; i < tblDAGetAddedOutletSummaryReport.getLength(); i++)
			{
				String Header="0";
				String Child="0";
				String TotalStores="0";
				String Validated="0";
				String Pending="0";
				int FlgNormalOverall=0;

				Element element = (Element) tblDAGetAddedOutletSummaryReport.item(i);

				NodeList HeaderNode = element.getElementsByTagName("Header");
				Element line = (Element) HeaderNode.item(0);
				if(HeaderNode.getLength()>0)
				{
					Header=xmlParser.getCharacterDataFromElement(line);
				}

				NodeList ChildNode = element.getElementsByTagName("Child");
				line = (Element) ChildNode.item(0);
				if(ChildNode.getLength()>0)
				{
					Child=xmlParser.getCharacterDataFromElement(line);
				}

				NodeList TotalStoresNode = element.getElementsByTagName("TotalStores");
				line = (Element) TotalStoresNode.item(0);
				if(TotalStoresNode.getLength()>0)
				{
					TotalStores=xmlParser.getCharacterDataFromElement(line);
				}

				NodeList ValidatedNode = element.getElementsByTagName("Validated");
				line = (Element) ValidatedNode.item(0);
				if(ValidatedNode.getLength()>0)
				{
					Validated=xmlParser.getCharacterDataFromElement(line);
				}

				NodeList PendingNode = element.getElementsByTagName("Pending");
				line = (Element) PendingNode.item(0);
				if(PendingNode.getLength()>0)
				{
					Pending=xmlParser.getCharacterDataFromElement(line);
				}

				dbengine.savetblDAGetAddedOutletSummaryReport(Header,Child,TotalStores,Validated,Pending,FlgNormalOverall);
				System.out.println("HEADER..."+Header);
			}

			NodeList tblDAGetAddedOutletSummaryOverallReport = doc.getElementsByTagName("tblDAGetAddedOutletSummaryOverallReport");

			for (int i = 0; i < tblDAGetAddedOutletSummaryOverallReport.getLength(); i++)
			{
				String Header="0";
				String Child="0";
				String TotalStores="0";
				String Validated="0";
				String Pending="0";
				int FlgNormalOverall=1;

				Element element = (Element) tblDAGetAddedOutletSummaryOverallReport.item(i);

				NodeList HeaderNode = element.getElementsByTagName("Header");
				Element line = (Element) HeaderNode.item(0);
				if(HeaderNode.getLength()>0)
				{
					Header=xmlParser.getCharacterDataFromElement(line);
				}

				NodeList ChildNode = element.getElementsByTagName("Child");
				line = (Element) ChildNode.item(0);
				if(ChildNode.getLength()>0)
				{
					Child=xmlParser.getCharacterDataFromElement(line);
				}

				NodeList TotalStoresNode = element.getElementsByTagName("TotalStores");
				line = (Element) TotalStoresNode.item(0);
				if(TotalStoresNode.getLength()>0)
				{
					TotalStores=xmlParser.getCharacterDataFromElement(line);
				}

				NodeList ValidatedNode = element.getElementsByTagName("Validated");
				line = (Element) ValidatedNode.item(0);
				if(ValidatedNode.getLength()>0)
				{
					Validated=xmlParser.getCharacterDataFromElement(line);
				}

				NodeList PendingNode = element.getElementsByTagName("Pending");
				line = (Element) PendingNode.item(0);
				if(PendingNode.getLength()>0)
				{
					Pending=xmlParser.getCharacterDataFromElement(line);
				}

				dbengine.savetblDAGetAddedOutletSummaryReport(Header,Child,TotalStores,Validated,Pending,FlgNormalOverall);
				System.out.println("HEADER..."+Header);
			}

			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e) {

			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			dbengine.close();

			return setmovie;
		}

	}
	//sales target
	public ServiceWorker getMonthSelectionForPlanning(Context ctx,int bydate,String uuid,String ApplicationID)
	{
		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);

		decimalFormat.applyPattern(pattern);
		final String SOAP_ACTION = "http://tempuri.org/fnGetTargetMonthPlan";
		final String METHOD_NAME = "fnGetTargetMonthPlan";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;

		SoapObject client = null;
		SoapObject responseBody = null;

		HttpTransportSE transport = null;
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();

		try
		{
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			client.addProperty("uuid", uuid.toString());
			client.addProperty("bydate", bydate);
			client.addProperty("ApplicationID", ApplicationID.toString());

			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);
			responseBody = (SoapObject)sse.bodyIn;
			int totalCount = responseBody.getPropertyCount();
			String resultString=androidHttpTransport.responseDump;
			String name=responseBody.getProperty(0).toString();

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);

			dbengine.open();

			dbengine.deletetblTargetMnthPlan();
			dbengine.deletetblTargetSavingBySstat("tblSalesTargetSavingDetail");
			dbengine.deletetblTargetSavingBySstat("tblSalesTargetUserDetails");

			NodeList tblBrandMstrMainNode = doc.getElementsByTagName("tblMonthDetail");
			for (int i = 0; i < tblBrandMstrMainNode.getLength(); i++)
			{
				String MonthVal="0";
				String YearVal ="0";
				String RotMonthYear="0";
				String StrToDisplay="NA";
				String flgDefault="0";
				String flgPlanType="0";

				Element element = (Element) tblBrandMstrMainNode.item(i);
				if(!element.getElementsByTagName("MonthVal").equals(null))
				{
					NodeList MonthValNode = element.getElementsByTagName("MonthVal");
					Element     line = (Element) MonthValNode.item(0);
					if (MonthValNode.getLength()>0)
					{
						MonthVal=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("YearVal").equals(null))
				{
					NodeList YearValNode = element.getElementsByTagName("YearVal");
					Element     line = (Element) YearValNode.item(0);
					if (YearValNode.getLength()>0)
					{
						YearVal=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("RotMonthYear").equals(null))
				{
					NodeList RotMonthYearNode = element.getElementsByTagName("RotMonthYear");
					Element     line = (Element) RotMonthYearNode.item(0);
					if (RotMonthYearNode.getLength()>0)
					{
						RotMonthYear=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("StrToDisplay").equals(null))
				{
					NodeList StrToDisplayNode = element.getElementsByTagName("StrToDisplay");
					Element     line = (Element) StrToDisplayNode.item(0);
					if (StrToDisplayNode.getLength()>0)
					{
						StrToDisplay=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("flgDefault").equals(null))
				{
					NodeList flgDefaultNode = element.getElementsByTagName("flgDefault");
					Element     line = (Element) flgDefaultNode.item(0);
					if (flgDefaultNode.getLength()>0)
					{
						flgDefault=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("flgPlanType").equals(null))
				{
					NodeList flgPlanTypeNode = element.getElementsByTagName("flgPlanType");
					Element     line = (Element) flgPlanTypeNode.item(0);
					if (flgPlanTypeNode.getLength()>0)
					{
						flgPlanType=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.savetblTargetMnthPlan(MonthVal,YearVal,RotMonthYear,StrToDisplay,flgDefault,flgPlanType);
			}

			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e) {

			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			dbengine.close();

			return setmovie;
		}
	}

	public ServiceWorker getPDASalesAreaTargetDetail(Context ctx,int bydate,String IMEINo,int MeasureID)
	{
		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);

		decimalFormat.applyPattern(pattern);
		final String SOAP_ACTION = "http://tempuri.org/fnGetSODistributorBrandLsitForTarget";
		final String METHOD_NAME = "fnGetSODistributorBrandLsitForTarget";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;

		SoapObject client = null; // Its the client petition to the web service
		SoapObject responseBody = null; // Contains XML content of dataset

		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();

		try
		{
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			client.addProperty("IMEINo", IMEINo);
			client.addProperty("bydate", bydate);
			client.addProperty("MeasureID", MeasureID);

			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);
			responseBody = (SoapObject)sse.bodyIn;
			int totalCount = responseBody.getPropertyCount();
			String resultString=androidHttpTransport.responseDump;
			String name=responseBody.getProperty(0).toString();

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);

			dbengine.open();

			dbengine.deletetblSalesAreaTargetDetail();
			dbengine.deletetblFlgEditeable();

			NodeList tblDMSDistributorListNode = doc.getElementsByTagName("tblDMSDistributorList");
			for (int i = 0; i < tblDMSDistributorListNode.getLength(); i++)
			{
				String TargetLevelNodeID="0";
				String TargetLevelNodeType ="0";
				String TargetLevelname="NA";
				String PrdNodeID="0";
				String PrdNodetype="0";
				String ProductName="NA";
				String MeasureIDVal="0";
				String targetValue="0";

				Element element = (Element) tblDMSDistributorListNode.item(i);
				if(!element.getElementsByTagName("TargetLevelNodeID").equals(null))
				{
					NodeList TargetLevelNodeIDNode = element.getElementsByTagName("TargetLevelNodeID");
					Element     line = (Element) TargetLevelNodeIDNode.item(0);
					if (TargetLevelNodeIDNode.getLength()>0)
					{
						TargetLevelNodeID=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("TargetLevelNodeType").equals(null))
				{
					NodeList TargetLevelNodeTypeNode = element.getElementsByTagName("TargetLevelNodeType");
					Element     line = (Element) TargetLevelNodeTypeNode.item(0);
					if (TargetLevelNodeTypeNode.getLength()>0)
					{
						TargetLevelNodeType=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("TargetLevelname").equals(null))
				{
					NodeList TargetLevelNameNode = element.getElementsByTagName("TargetLevelname");
					Element     line = (Element) TargetLevelNameNode.item(0);
					if (TargetLevelNameNode.getLength()>0)
					{
						TargetLevelname=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("PrdNodeID").equals(null))
				{
					NodeList PrdNodeIDNode = element.getElementsByTagName("PrdNodeID");
					Element     line = (Element) PrdNodeIDNode.item(0);
					if (PrdNodeIDNode.getLength()>0)
					{
						PrdNodeID=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("PrdNodetype").equals(null))
				{
					NodeList PrdNodeTypeNode = element.getElementsByTagName("PrdNodetype");
					Element     line = (Element) PrdNodeTypeNode.item(0);
					if (PrdNodeTypeNode.getLength()>0)
					{
						PrdNodetype=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("ProductName").equals(null))
				{
					NodeList ProductNameNode = element.getElementsByTagName("ProductName");
					Element     line = (Element) ProductNameNode.item(0);
					if (ProductNameNode.getLength()>0)
					{
						ProductName=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("MeasureID").equals(null))
				{
					NodeList MeasureIDNode = element.getElementsByTagName("MeasureID");
					Element     line = (Element) MeasureIDNode.item(0);
					if (MeasureIDNode.getLength()>0)
					{
						MeasureIDVal=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("targetValue").equals(null))
				{
					NodeList targetValueNode = element.getElementsByTagName("targetValue");
					Element     line = (Element) targetValueNode.item(0);
					if (targetValueNode.getLength()>0)
					{
						targetValue=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.savetblSalesAreaTargetDetail(TargetLevelNodeID,TargetLevelNodeType,TargetLevelname,
						PrdNodeID,PrdNodetype,ProductName,MeasureIDVal,targetValue);

				System.out.println("TARGET DATA..."+TargetLevelNodeID+"--"+TargetLevelNodeType+"--"+TargetLevelname+"--"+
						PrdNodeID+"--"+PrdNodetype+"--"+ProductName+"--"+MeasureIDVal+"--"+targetValue);
			}

			NodeList tblFlgEditeableNode = doc.getElementsByTagName("tblFlgEditeable");
			for (int i = 0; i < tblFlgEditeableNode.getLength(); i++)
			{
				String flgStatus="0";

				Element element = (Element) tblFlgEditeableNode.item(i);
				if(!element.getElementsByTagName("flgStatus").equals(null))
				{
					NodeList flgStatusNode = element.getElementsByTagName("flgStatus");
					Element     line = (Element) flgStatusNode.item(0);
					if (flgStatusNode.getLength()>0)
					{
						flgStatus=xmlParser.getCharacterDataFromElement(line);
					}
				}
				dbengine.savetblFlgEditeable(flgStatus);
			}

			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e) {

			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			dbengine.close();

			return setmovie;
		}
	}

	public ServiceWorker getfnGetSpTargetGetSalestargetMeasure(Context ctx,String bydate,String uuid,String ApplicationID)
	{
		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);

		decimalFormat.applyPattern(pattern);
		final String SOAP_ACTION = "http://tempuri.org/fnGetSpTargetGetSalestargetMeasure";
		final String METHOD_NAME = "fnGetSpTargetGetSalestargetMeasure";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;

		SoapObject client = null;
		SoapObject responseBody = null;

		HttpTransportSE transport = null;
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();

		try
		{
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			client.addProperty("uuid", uuid.toString());
			client.addProperty("bydate", bydate.toString());
			client.addProperty("ApplicationID", ApplicationID.toString());

			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);
			responseBody = (SoapObject)sse.bodyIn;
			int totalCount = responseBody.getPropertyCount();
			String resultString=androidHttpTransport.responseDump;
			String name=responseBody.getProperty(0).toString();

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);

			dbengine.open();

			dbengine.deletetblSalestargetMeasure();

			NodeList tblSalestargetMeasureNode = doc.getElementsByTagName("tblSalestargetMeasure");
			for (int i = 0; i < tblSalestargetMeasureNode.getLength(); i++)
			{
				String TgtMeasueId="0";
				String TgtMeasueName ="NA";
				String flgActive="0";

				Element element = (Element) tblSalestargetMeasureNode.item(i);
				if(!element.getElementsByTagName("TgtMeasueId").equals(null))
				{
					NodeList TgtMeasueIdNode = element.getElementsByTagName("TgtMeasueId");
					Element     line = (Element) TgtMeasueIdNode.item(0);
					if (TgtMeasueIdNode.getLength()>0)
					{
						TgtMeasueId=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("TgtMeasueName").equals(null))
				{
					NodeList TgtMeasueNameNode = element.getElementsByTagName("TgtMeasueName");
					Element     line = (Element) TgtMeasueNameNode.item(0);
					if (TgtMeasueNameNode.getLength()>0)
					{
						TgtMeasueName=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("flgActive").equals(null))
				{
					NodeList flgActiveNode = element.getElementsByTagName("flgActive");
					Element     line = (Element) flgActiveNode.item(0);
					if (flgActiveNode.getLength()>0)
					{
						flgActive=xmlParser.getCharacterDataFromElement(line);
					}
				}

				dbengine.savetblSalestargetMeasure(TgtMeasueId,TgtMeasueName,flgActive);
			}

			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e) {

			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			dbengine.close();

			return setmovie;
		}
	}

	public ServiceWorker fnGetStateCityListMstr(Context ctx,String uuid,String CurDate,int ApplicationID)
	{

		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);


		decimalFormat.applyPattern(pattern);

		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnGetStateCityListMstr";
		final String METHOD_NAME = "fnGetStateCityListMstr";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		//Create request
		SoapObject table = null; // Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; // Its the client petition to the web service
		SoapObject tableRow = null; // Contains row of table
		SoapObject responseBody = null; // Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();

		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);




			client.addProperty("bydate", CurDate);
			client.addProperty("uuid", uuid.toString());
			//client.addProperty("DatabaseVersion","11");
			client.addProperty("ApplicationID", ApplicationID);



			// // System.out.println("Kajol 102");
			sse.setOutputSoapObject(client);
			// // System.out.println("Kajol 103");
			sse.bodyOut = client;
			// // System.out.println("Kajol 104");

			androidHttpTransport.call(SOAP_ACTION, sse);

			// // System.out.println("Kajol 1");

			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// // System.out.println("Kajol 2 :"+totalCount);
			String resultString=androidHttpTransport.responseDump;

			String name=responseBody.getProperty(0).toString();

			// // System.out.println("Kajol 3 :"+name);

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);
			dbengine.deletetblStateCityMaster();
			dbengine.open();




			NodeList tblStateCityMaster = doc.getElementsByTagName("tblStateCityMaster");
			for (int i = 0; i < tblStateCityMaster.getLength(); i++)
			{

				String StateID="0";
				String State ="NA";
				String CityID ="0";
				String City ="NA";
				int CityDefault =0;


				Element element = (Element) tblStateCityMaster.item(i);

				if(!element.getElementsByTagName("StateID").equals(null))
				{
					NodeList StateIDNodeID = element.getElementsByTagName("StateID");
					Element     line = (Element) StateIDNodeID.item(0);
					if (StateIDNodeID.getLength()>0)
					{
						StateID=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("State").equals(null))
				{
					NodeList StateNode = element.getElementsByTagName("State");
					Element     line = (Element) StateNode.item(0);
					if (StateNode.getLength()>0)
					{
						State=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("CityID").equals(null))
				{
					NodeList CityIDNode = element.getElementsByTagName("CityID");
					Element     line = (Element) CityIDNode.item(0);
					if (CityIDNode.getLength()>0)
					{
						CityID=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("City").equals(null))
				{
					NodeList CityNode = element.getElementsByTagName("City");
					Element     line = (Element) CityNode.item(0);
					if (CityNode.getLength()>0)
					{
						City=xmlParser.getCharacterDataFromElement(line);
					}
				}
				if(!element.getElementsByTagName("CityDefault").equals(null))
				{
					NodeList CityDefaultNode = element.getElementsByTagName("CityDefault");
					Element     line = (Element) CityDefaultNode.item(0);
					if (CityDefaultNode.getLength()>0)
					{
						CityDefault=Integer.parseInt(xmlParser.getCharacterDataFromElement(line).trim());
					}
				}


				dbengine.fnsavetblStateCityMaster(StateID, State, CityID, City,CityDefault);

			}



			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e) {

			// System.out.println("Aman Exception occur in GetIMEIVersionDetailStatusNew :"+e.toString());
			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			dbengine.close();

			return setmovie;
		}





	}

	public ServiceWorker getOnStoreListActivity(Context ctx,String uuid,String CurDate,int DatabaseVersion,int ApplicationID)
	{

		this.context = ctx;
		DBAdapterLtFoods dbengine = new DBAdapterLtFoods(context);
		dbengine.open();

		decimalFormat.applyPattern(pattern);

		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/GetIMEIVersionDetailStatusNew";
		final String METHOD_NAME = "GetIMEIVersionDetailStatusNew";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		//Create request
		SoapObject table = null; // Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; // Its the client petition to the web service
		SoapObject tableRow = null; // Contains row of table
		SoapObject responseBody = null; // Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;

		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,timeout);

		ServiceWorker setmovie = new ServiceWorker();


		// // System.out.println("Kajol 100");

		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);



			// // System.out.println("Kajol 101");
			client.addProperty("uuid", uuid.toString());
			client.addProperty("DatabaseVersion", DatabaseVersion);
			client.addProperty("ApplicationID", ApplicationID);

			// // System.out.println("Kajol 102");
			sse.setOutputSoapObject(client);
			// // System.out.println("Kajol 103");
			sse.bodyOut = client;
			// // System.out.println("Kajol 104");

			androidHttpTransport.call(SOAP_ACTION, sse);

			// // System.out.println("Kajol 1");

			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// // System.out.println("Kajol 2 :"+totalCount);
			String resultString=androidHttpTransport.responseDump;

			String name=responseBody.getProperty(0).toString();

			// // System.out.println("Kajol 3 :"+name);

			XMLParser xmlParser = new XMLParser();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(name));
			Document doc = db.parse(is);




			NodeList tblSchemeMstrNode = doc.getElementsByTagName("tblAvailableVersion");
			for (int i = 0; i < tblSchemeMstrNode.getLength(); i++)
			{
				String VersionDownloadStatus= "1";
				Element element = (Element) tblSchemeMstrNode.item(i);
				NodeList SchemeApplicationIDNode = element.getElementsByTagName("VersionDownloadStatus");
				Element line = (Element) SchemeApplicationIDNode.item(0);
				VersionDownloadStatus=xmlParser.getCharacterDataFromElement(line);
				setmovie.flgNewVersion=VersionDownloadStatus;
			}

			setmovie.director = "1";
			dbengine.close();
			return setmovie;

		} catch (Exception e) {

			// System.out.println("Aman Exception occur in GetIMEIVersionDetailStatusNew :"+e.toString());
			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			setmovie.flgNewVersion="0";
			dbengine.close();

			return setmovie;
		}





	}
}
