package Main;

import org.ini4j.Wini;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public  class GlobalMembers {



    static {
        try{
            new GlobalMembers();
//            System.setProperty("java.library.path", "C:\\Users\\df-dev16\\IdeaProjects\\PassbookKisok\\src\\DLL");
//            System.out.println(System.getProperty("java.library.path"));
//            System.loadLibrary("LTRD");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


//    public static native long InitTRC(IntPtr hWnd);

    public static native boolean LPTRC_MENWrite(String data);

    public static Wini objLksdbIni = null;
    public static Wini objTxnIni = null;
    public static Wini objClientConfigIni =null;
    public static Wini objHealthIni = null;
    public static Wini objCnctnIni = null;

    public static String strLogPath;
    public static String[] strMISPath;
    public static String strImagesPath;
    public static String strAudioPath;
//    public static IntPtr MainHandle;
    public static String strBarcode;
    public static String strPrevBarcode;
    public static boolean bIsNewTransaction;
    public static Task objTaskState;
    public static Timer objTimer=null;



    enum Task
    {
        None,
        TransactionStart,
        TransactionEnd,
        TransactionError,
        OutOfServiceState,
        PassbookScanned,
        AuthenticationRequest,
        ReadBarcodeFromSD,
        AuthenticationResponse,
        PassbookDataRequest,
        PassbookDataResponse,
        PassbookNegAckRequest,
        PassbookNegAckResponse,
        GetLastPrintDetailRequest,
        GetLastPrintDetailResponse,
        PassbookPrintNextPage,
        PassbookRecheckState,
        PassbookCoverPageAckRequest,
        PassbookCoverPageAckResponse,
        PassbookError,
        RequestTimeout,
        InServiceMode
    }

    enum ErrorType
    {
        None,
    }


//     <summary>
//        //V26.1.1.1 added by Rajesh
//     </summary>
    public enum MsgLenType
    {
        ASCII,
        DECIMAL,
        HEX
    }

    /// <summary>
    /// kiosk Id
    /// </summary>
    public static String KioskID; //V26.1.1.0

    //Request type object
    public static MsgLenType eMsgLenType;

    public class ISOFields
    {
        //public String strDesc;
        public int iType;
        public int iLength;
        public String strValue;
        //public boolean bVisible;
    }

    public static ISOFields[] objISO;
    public static int iNoOfByteInSingleLine = 0;
    public static int iDateFieldSize = 0;
    public static int iParticularSize = 0;
    public static int iChequeNoSize = 0;
    public static int iDebitFieldSize = 0;
    public static int iCreditFieldSize = 0;
    public static int iEndBalanceFieldSize = 0;
    public static int iLineSpacingForThread = 0;
    public static int iTxnsBeforeThreadSpace;
    public static int iThreadforIndexing = 0;

    //Lokesh Added
    public static int strPBHeight;
    public static int strPBWidth;

    public static int iSpacesAfterDate = 0;
    public static int iSpacesAfterParticular = 0;
    public static int iSpacesAfterChequeNo = 0;
    public static int iSpacesAfterDebitAmount = 0;
    public static int iSpacesAfterCreditAmount = 0;

    public static boolean isCoverPage;     //V26.1.2.9

    public static int iSpacesBeforeDate = 0; //V26.1.1.31


    public static boolean bIsPassbookEpson = true;

    public static String AccountNumber ;
    public static String PrvAccountNumber;

    /// <summary>
    /// TRUE when there are remaining txns to be printed on next page. FALSE when all txns are printed on same page.
    /// </summary>
    public static boolean bMoreData = false;

    /// <summary>
    /// TRUE when ther are more pending txns on CBS, and next request to fire without ejecting passbook.
    /// FALSE when pending txns are finished on CBS, so directly finish the txn
    /// </summary>
    public static boolean bNextRequest = false;

    /// <summary>
    /// It will store the value of lines to print that came from CBS in Data response. It will be used to print in MIS file.
    /// </summary>
    public static String strLinesToPrint;

    /// <summary>
    /// TRUE when passbook printing data is composed successfully and printing also done with success, FALSE when error occurs and needs to send negative ACK.
    /// </summary>
    public static boolean bIsPositiveACK = false;

    //public static boolean CheckForConnection = false;    //Removed in V1.1.1.1

    public static byte[] bResponse;
    public static byte[] bRequest;

    public static short usISOHeaderLength;
    public static short usMTILength;
    public static short usMsgLen;

    public static String strDefISOHeader;
    public static String strDefMTI;

    public static boolean bAddMsgLen;
    public static boolean bAddISOHeader;
    public static boolean bIsMsgLenInHex;
    public static boolean bIsHexBitmap;
    public static boolean bAddTrailingByte;

    public static byte byTrailingData;

    /// <summary>
    /// TRUE if application is hooked. FALSE when no need for application hooking.
    /// </summary>
    public static boolean bApplicationHook;

    public static CBS objCBS;
    public static ISO8583 objISO8583;
    public static ADMIN objADMIN;
    public static VERSION objVERSION;
    public static DETAILS objDETAILS;
    public static PATH_DETAIL objPATH_DETAIL;
    public static ACCOUNT_DETAILS objACCOUNT_DETAILS;
//    public static LAST_PRINT_DETAIL objLastPrintDetails;
    public static PB_ACK_DETAILS objPBAckDetails;
    public static KIOSK_TIMERS objKioskTimers;
    public static LAST_TXN_DETAILS objLastTxnDetails;

    //Memory queue for log writing
    public static ConcurrentLinkedQueue<LogStructure> LogQueue;
    //Memory queue for MIS writing
    public static ConcurrentLinkedQueue<MISStructure> MISQueue;

    //class for log writing
    public static class LogStructure
    {
        public byte[] byLogText;
        public String strLogText;
        public String strDirectory;
        public boolean bIsLogInString;
        public LocalDateTime dtLogTime;
    }

    //class for MIS writing
    public static class MISStructure
    {
        public String strSTAN;
        public String strAccNo;
        public String strLTP;
        public String strResult;
        public LocalDateTime dtLogTime;
    }

    public static String[] arEnglishScreenText = null;
    public static String[] arRegionalScreenText = null;

    //Event notifier for log
//    public static AutoResetEvent queueNotifierLog = new AutoResetEvent(false);
    //Event notifier for MIS
//    public static AutoResetEvent queueNotifierMIS = new AutoResetEvent(false);

    //thread for log writing
    public static Thread thrdLogQueueCallback;

    //thread for MIS writing
    public static Thread thrdMISQueueCallback;

    //directory names for logs
    public static String LOGKPROC = "KPROC";
    public static String LOGPB = "Passbook";
    public static String LOGBCRD = "Barcode";
    public static String LOGISO8583 = "ISO8583";
    public static String LOGTCP = "Transport";

    //INI Structures
    public class CBS
    {
        public String strIP;
        public String strPort;
        public String strHost_Name;
        public boolean bIs_DNS;
        public boolean bIs_TCP;
    }

    public class ISO8583
    {
        public String DE41;
        public String DE102;
    }

    public static String STAN ;
    public static String Last_STAN;



    //For Demo purpose
    /// <summary>
    /// FALSE if no need to write log
    /// </summary>
    //public static boolean bIsWriteLog;

    //For Demo purpose
    /// <summary>
    /// FALSE if no need to match the STAN, TRUE if STAN is to be matched (also when value not exist in configuration file).
    /// </summary>
    public static boolean bIsSTANMatch;

    /// <summary>
    /// TRUE if already printed page is to be checked, FALSE if want to skip this checking (for DEMO mode only)
    /// </summary>
    //public static boolean bIsCheckPrintedLines;

    /// <summary>
    /// In case of DEMO read account number from configuration file.
    /// </summary>
    public static String strDummyAccNo;

    /// <summary>
    /// TRUE when passbook is DEMO, FALSE otherwise
    /// </summary>
    public static boolean bIsPassbookDemo;

    /// <summary>
    /// TRUE full scan barcode read otherwise half lower image barcode read
    /// </summary>
    public static boolean bIsFullScanBarcode;

    /// <summary>
    /// TRUE when CBS is connected, FALSE otherwise
    /// </summary>
    public static boolean bIsCBSConnected;

    /// <summary>
    /// unique reference number to be used in PB request which will same for simgle account
    /// </summary>
    public static String strTxnRRN = "";

    /// <summary>
    ///  added by Rajesh 9 dec 2015
    /// </summary>
    public static boolean bThreadProtection = false;


    public static String strTotalLinesPrinted;
    public static String strTotalLinesToBePrinted;

    public class ADMIN
    {
        public String Account_1 = "";
        public String Password_1 = "";
        public String Account_2 = "";
        public String Password_2 ="" ;
    }

    public class VERSION
    {
        public String App_Ver;
        public String Ghost_ver;
        public String Build_Date;
    }

    public class DETAILS
    {
        public String Company_Name;
        public String Bank_Name;
        public String Kiosk_Type;
        public String Copyright;
        public String Lang_Support;
        public String Regional_Lang;
    }

    public static class PATH_DETAIL
    {
        public String strPassbook_Image_Path;
        public String Passbook_Reference_Image_Path;
        //public String Log_Path;   //05May14
        public String strBackup_EJ_Path;
        public boolean Mirror_Required;
    }

    public static class ACCOUNT_DETAILS
    {
        public int iAccount_Length;
        public int iMax_Line_To_Print;
        public int iExtra_Line_Feed;
    }

    //public struct LAST_PRINT_DETAIL
    //{
    //    public int iLineNo;
    //    public int iPendingTxns;
    //}

    public class PB_ACK_DETAILS
    {
        /// <summary>
        /// It is txn posting date of 14 bytes
        /// </summary>
        public String strTxnPostDate;

        /// <summary>
        /// End balance printed of 17 bytes
        /// </summary>
        public String strEndBalance;

        /// <summary>
        /// Last page no printed of 2 bytes
        /// </summary>
        public String strLastPageNo;

        /// <summary>
        /// Last line no printed of 2 bytes
        /// </summary>
        public String strLastLinePrinted;

        /// <summary>
        /// Last Trans Id printed of 9 bytes
        /// </summary>
        public String strLastTransId;

        /// <summary>
        /// Last date printed of 8 bytes
        /// </summary>
        public String strLastDate;

        /// <summary>
        /// Last txn serial no of 4 bytes
        /// </summary>
        public String strLastTxnSerialNo;
    }

    public class KIOSK_TIMERS
    {
        public int iTmrRotator1;
        public int iTmrRotator2;
        public int iTmrRotator3;
        public int iTmrRotator4;
        public int iTmrRotator5;
        public int iTmrRotator6;
        public int iTmrRotator7;
        public int iTmrTransaction;
    }

    public class LAST_TXN_DETAILS
    {
        /// <summary>
        /// Last print date
        /// </summary>
        public String strLastPrintDate;

        /// <summary>
        /// Last balance printed
        /// </summary>
        public String strLastBalancePrinted;

        /// <summary>
        /// Last page no printed
        /// </summary>
        public int iLastPageNo;

        /// <summary>
        /// Last line no printed came from CBS. Range is from 1 to 29. Before formatting printing data it is increased by 1 and so data will be printed from this line.
        /// </summary>
        public int iLastLineNo;

        /// <summary>
        /// No of passbooks printed
        /// </summary>
        public String strNoPBPrinted;

        /// <summary>
        /// Last txn ID
        /// </summary>
        public String strLastTxnId;

        /// <summary>
        /// Last txn date
        /// </summary>
        public String strLastTxnDate;

        /// <summary>
        /// Last txn number
        /// </summary>
        public String strLastTxnNo;
    }

    // Static constructor is called at most one time, before any
    // instance constructor is invoked or member is accessed.
    GlobalMembers()
    {
        if (LogQueue == null)
            LogQueue = new ConcurrentLinkedQueue<>();
//
        if (MISQueue == null)
            MISQueue = new ConcurrentLinkedQueue<>();

        objISO = new ISOFields[131];
        for (int i=0; i<=128; i++){
            objISO[i]=new ISOFields();
        }

        //set the type and length of fields which are coming in request
        //if type is fixed i.e. 0 then length is the fields fixed length
        //if type is variable i.e. 1 then length will be the fields maximum length which that field can receive

            objISO[2].iType = 1; objISO[2].iLength = 17;
            objISO[3].iType = 0; objISO[3].iLength = 6;
            objISO[4].iType = 0; objISO[4].iLength = 12;
            objISO[5].iType = 0; objISO[5].iLength = 12;
            objISO[6].iType = 0; objISO[6].iLength = 12;
            objISO[7].iType = 0; objISO[7].iLength = 10;
            objISO[8].iType = 0; objISO[8].iLength = 8;
            objISO[9].iType = 0; objISO[9].iLength = 8;
            objISO[10].iType = 0; objISO[10].iLength = 8;
            objISO[11].iType = 0; objISO[11].iLength = 6;
            objISO[12].iType = 0; objISO[12].iLength = 12;
            objISO[13].iType = 0; objISO[13].iLength = 4;
            objISO[14].iType = 0; objISO[14].iLength = 4;
            objISO[15].iType = 0; objISO[15].iLength = 6;
            objISO[16].iType = 0; objISO[16].iLength = 4;
            objISO[17].iType = 0; objISO[17].iLength = 4;
            objISO[18].iType = 0; objISO[18].iLength = 4;
            objISO[19].iType = 0; objISO[19].iLength = 3;
            objISO[20].iType = 0; objISO[20].iLength = 3;
            objISO[21].iType = 0; objISO[21].iLength = 3;
            objISO[22].iType = 0; objISO[22].iLength = 12;
            objISO[23].iType = 0; objISO[23].iLength = 3;
            objISO[24].iType = 0; objISO[24].iLength = 3;
            objISO[25].iType = 0; objISO[25].iLength = 4;
            objISO[26].iType = 0; objISO[26].iLength = 4;
            objISO[27].iType = 0; objISO[27].iLength = 1;
            objISO[28].iType = 0; objISO[28].iLength = 6;
            objISO[29].iType = 0; objISO[29].iLength = 3;
            objISO[30].iType = 0; objISO[30].iLength = 24;
            objISO[31].iType = 1; objISO[31].iLength = 99;
            objISO[32].iType = 1; objISO[32].iLength = 11;
            objISO[33].iType = 1; objISO[33].iLength = 11;
            objISO[34].iType = 1; objISO[34].iLength = 28;
            objISO[35].iType = 1; objISO[35].iLength = 37;
            objISO[36].iType = 1; objISO[36].iLength = 104;
            objISO[37].iType = 0; objISO[37].iLength = 16;
            objISO[38].iType = 0; objISO[38].iLength = 6;
            objISO[39].iType = 0; objISO[39].iLength = 3;
            objISO[40].iType = 0; objISO[40].iLength = 3;
            objISO[41].iType = 0; objISO[41].iLength = 8;
            objISO[42].iType = 0; objISO[42].iLength = 15;
            objISO[43].iType = 0; objISO[43].iLength = 40;
            objISO[44].iType = 1; objISO[44].iLength = 99;
            objISO[45].iType = 1; objISO[45].iLength = 76;
            objISO[46].iType = 1; objISO[46].iLength = 204;
            objISO[47].iType = 1; objISO[47].iLength = 999;
            objISO[48].iType = 1; objISO[48].iLength = 999;
            objISO[49].iType = 0; objISO[49].iLength = 3;
            objISO[50].iType = 0; objISO[50].iLength = 3;
            objISO[51].iType = 0; objISO[51].iLength = 3;
            objISO[52].iType = 0; objISO[52].iLength = 16;
            objISO[53].iType = 1; objISO[53].iLength = 48;
            objISO[54].iType = 1; objISO[54].iLength = 120;
            objISO[55].iType = 1; objISO[55].iLength = 255;
            objISO[56].iType = 1; objISO[56].iLength = 35;
            objISO[57].iType = 0; objISO[57].iLength = 3;
            objISO[58].iType = 1; objISO[58].iLength = 11;
            objISO[59].iType = 1; objISO[59].iLength = 999;
            objISO[60].iType = 1; objISO[60].iLength = 999;
            objISO[61].iType = 1; objISO[61].iLength = 999;
            objISO[62].iType = 1; objISO[62].iLength = 999;
            objISO[63].iType = 1; objISO[63].iLength = 999;
            objISO[64].iType = 0; objISO[64].iLength = 16;
            objISO[65].iType = 0; objISO[65].iLength = 16;
            objISO[66].iType = 1; objISO[66].iLength = 204;
            objISO[67].iType = 0; objISO[67].iLength = 2;
            objISO[68].iType = 0; objISO[68].iLength = 3;
            objISO[69].iType = 0; objISO[69].iLength = 3;
            objISO[70].iType = 0; objISO[70].iLength = 3;
            objISO[71].iType = 0; objISO[71].iLength = 8;
            objISO[72].iType = 1; objISO[72].iLength = 999;
            objISO[73].iType = 0; objISO[73].iLength = 6;
            objISO[74].iType = 0; objISO[74].iLength = 10;
            objISO[75].iType = 0; objISO[75].iLength = 10;
            objISO[76].iType = 0; objISO[76].iLength = 10;
            objISO[77].iType = 0; objISO[77].iLength = 10;
            objISO[78].iType = 0; objISO[78].iLength = 10;
            objISO[79].iType = 0; objISO[79].iLength = 10;
            objISO[80].iType = 0; objISO[80].iLength = 10;
            objISO[81].iType = 0; objISO[81].iLength = 10;
            objISO[82].iType = 0; objISO[82].iLength = 10;
            objISO[83].iType = 0; objISO[83].iLength = 10;
            objISO[84].iType = 0; objISO[84].iLength = 10;
            objISO[85].iType = 0; objISO[85].iLength = 10;
            objISO[86].iType = 0; objISO[86].iLength = 16;
            objISO[87].iType = 0; objISO[87].iLength = 16;
            objISO[88].iType = 0; objISO[88].iLength = 16;
            objISO[89].iType = 0; objISO[89].iLength = 16;
            objISO[90].iType = 0; objISO[90].iLength = 10;
            objISO[91].iType = 0; objISO[91].iLength = 3;
            objISO[92].iType = 0; objISO[92].iLength = 3;
            objISO[93].iType = 1; objISO[93].iLength = 11;
            objISO[94].iType = 1; objISO[94].iLength = 11;
            objISO[95].iType = 1; objISO[95].iLength = 99;
            objISO[96].iType = 1; objISO[96].iLength = 999;
            objISO[97].iType = 0; objISO[97].iLength = 17;
            objISO[98].iType = 0; objISO[98].iLength = 25;
            objISO[99].iType = 1; objISO[99].iLength = 11;
            objISO[100].iType = 1; objISO[100].iLength = 11;
            objISO[101].iType = 1; objISO[101].iLength = 17;
            objISO[102].iType = 1; objISO[102].iLength = 25;
            objISO[103].iType = 1; objISO[103].iLength = 28;
            objISO[104].iType = 1; objISO[104].iLength = 100;
            objISO[105].iType = 0; objISO[105].iLength = 16;
            objISO[106].iType = 0; objISO[106].iLength = 16;
            objISO[107].iType = 0; objISO[107].iLength = 10;
            objISO[108].iType = 0; objISO[108].iLength = 10;
            objISO[109].iType = 1; objISO[109].iLength = 84;
            objISO[110].iType = 1; objISO[110].iLength = 84;
            objISO[111].iType = 1; objISO[111].iLength = 999;
            objISO[112].iType = 1; objISO[112].iLength = 999;
            objISO[113].iType = 1; objISO[113].iLength = 999;
            objISO[114].iType = 1; objISO[114].iLength = 999;
            objISO[115].iType = 1; objISO[115].iLength = 999;
            objISO[116].iType = 1; objISO[116].iLength = 999;
            objISO[117].iType = 1; objISO[117].iLength = 999;
            objISO[118].iType = 1; objISO[118].iLength = 999;
            objISO[119].iType = 1; objISO[119].iLength = 999;
            objISO[120].iType = 1; objISO[120].iLength = 999;
            objISO[121].iType = 1; objISO[121].iLength = 999;
            objISO[122].iType = 1; objISO[122].iLength = 999;
            objISO[123].iType = 1; objISO[123].iLength = 999;
            objISO[124].iType = 1; objISO[124].iLength = 999;
            objISO[125].iType = 1; objISO[125].iLength = 999;
            objISO[126].iType = 1; objISO[126].iLength = 999;
            objISO[127].iType = 1; objISO[127].iLength = 999;
            objISO[128].iType = 0; objISO[128].iLength = 16;


        objCBS = new CBS();
        objISO8583 = new ISO8583();
        objADMIN = new ADMIN();
        objVERSION = new VERSION();
        objDETAILS = new DETAILS();
        objPATH_DETAIL = new PATH_DETAIL();
        objACCOUNT_DETAILS = new ACCOUNT_DETAILS();
        //objLastPrintDetails = new LAST_PRINT_DETAIL();
        objPBAckDetails = new PB_ACK_DETAILS();
        objKioskTimers = new KIOSK_TIMERS();
        objLastTxnDetails = new LAST_TXN_DETAILS();


        //set images path
        strImagesPath = "C:\\KIOSK\\lipi";

        //set audio path
        strAudioPath = "C:\\Kiosk\\lipi\\Audio";

        //Set MIS paths
        strMISPath = new String[2];
        strMISPath[0] = "D:\\PB_MIS_REPORT";
        strMISPath[1] = "C:\\Kiosk\\Log_Data\\PB_MIS_REPORT";
    }



    /// <summary>
    /// It will write the MIS file for transactions
    /// </summary>
    /// <param name="strSTAN">STAN no of the transaction session</param>
    /// <param name="strAccNo">Account number for the transaction session</param>
    /// <param name="strLTP">Lines to print</param>
    /// <param name="strResult">Result tof the transaction session</param>
    public static void WriteMISData(String strSTAN, String strAccNo, String strLTP, String strResult)
    {
        try
        {
            //if object istantiated
            if (MISQueue != null)
            {
                //create local object of log structure
                MISStructure objMISStructure = new MISStructure();

                //assign data
                objMISStructure.strSTAN = strSTAN;
                objMISStructure.strAccNo = strAccNo;
                //objMISStructure.strLTP = strLTP;
                objMISStructure.strLTP = (Integer.toString(Integer.parseInt(strLTP))+1);
                objMISStructure.strResult = strResult;
                objMISStructure.dtLogTime = LocalDateTime.now();

                //add object in queue
                MISQueue.add(objMISStructure);

                //notify thread about inserted object
//                queueNotifierMIS.Set();
            }
        }
        catch (Exception excp)
        { }
    }

    public static void ResetISOFields() {
//        //Shubhit

        bRequest = new  byte[0];
        bRequest = Arrays.copyOf(bRequest,0);
//        Array.Clear(bRequest, 0, bRequest.Length);

        bResponse = new  byte[0];
        bResponse = Arrays.copyOf(bResponse,0);
//        Array.Clear(bResposne, 0, bResposne.Length);


        strDefMTI = "";

        for (int i = 0; i <=128; i++) {
            objISO[i].strValue = "";
        }

    }
}