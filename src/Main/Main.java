package Main;


import org.ini4j.Wini;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static Main.GlobalMembers.objHealthIni;
import static Main.GlobalMembers.objLksdbIni;

public class Main extends JFrame {
    private JPanel contentPane;
    public static String dirPath;
    private static Dimension screenSize;
    public static  int AppInitCount = 0;
    public static ImageIcon image;

    static {
        dirPath = System.getProperty("user.dir");
//        System.out.println(path);
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
                    frame.setVisible(true);
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            start();
                        }
                    });
                    th.start();
                    Thread thTime = new Thread(new Time());
                    thTime.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Main() {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            BufferedImage imgBuffered=ImageIO.read(new File(dirPath+"/src/Image/abc.jpg"));
            image = new ImageIcon(imgBuffered);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //component initialization
        initialize();
    }

    public void initialize() {
        setBounds(0,0,screenSize.width,screenSize.height);
        this.setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        GlobalMembers.imgLabel = new JLabel("");
        GlobalMembers.imgLabel.setBounds(0, 0, 1368, 768);
        GlobalMembers.imgLabel.setHorizontalAlignment(SwingConstants.LEFT);
        GlobalMembers.imgLabel.setVerticalAlignment(SwingConstants.TOP);
        GlobalMembers.imgLabel.setIcon(image);
        contentPane.add(GlobalMembers.imgLabel);


        GlobalMembers.headingLabel = new JLabel("MUNICIPAL CO.OP BANK PASSBOOK KIOSK");
        GlobalMembers.headingLabel.setBounds(350,100,800,200);
        GlobalMembers.headingLabel.setForeground(Color.BLUE);
        GlobalMembers.headingLabel.setFont(new Font("Serif", Font.BOLD, 30));
        GlobalMembers.imgLabel.add(GlobalMembers.headingLabel);

        GlobalMembers.txtLabel = new JLabel();
        GlobalMembers.txtLabel.setForeground(Color.BLUE);
        GlobalMembers.txtLabel.setBounds(400,200,600,200);
        GlobalMembers.txtLabel.setFont(new Font("Serif", Font.BOLD, 30));
        GlobalMembers.txtLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GlobalMembers.txtLabel.setVerticalAlignment(SwingConstants.CENTER);
        GlobalMembers.imgLabel.add(GlobalMembers.txtLabel);

        GlobalMembers.time = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        GlobalMembers.time.setBounds(550,300,600,200);
        GlobalMembers.time.setForeground(Color.BLUE);
        GlobalMembers.time.setFont(new Font("Serif", Font.BOLD, 30));
        GlobalMembers.imgLabel.add(GlobalMembers.time);

        Icon enIcon = new ImageIcon(dirPath+"\\src\\Image\\English.png");
        JButton engButton = new JButton(enIcon);
        engButton.setBounds(1050,500,270,70);
//        GlobalMembers.imgLabel.add(engButton);

        Icon hiIcon = new ImageIcon(dirPath+"\\src\\Image\\Hindi.png");
        JButton hinButton = new JButton(hiIcon);
        hinButton.setBounds(1050,600,270,70);
//        GlobalMembers.imgLabel.add(hinButton);

        setVisible(true);
    }

    public static void start(){
        try{
            if (GlobalInitialization()) {
                GlobalMembers.txtLabel.setText("Configuration is proper....");

                Thread.sleep(1000);
                if(StartEngine()){
                    Log.Write("Starting Application Kernel");

                    GlobalMembers.objTaskState = GlobalMembers.Task.TransactionStart;

                    if (GlobalMembers.objTimer == null)
                        GlobalMembers.objTimer = new Timer();

                    Thread objThrdMain = new Thread(GlobalMembers.objTimer);
                    objThrdMain.start();
                }
            } else{
                Log.Write("configuration is not proper...");
                System.out.println(GlobalInitialization());
            }
        }catch (Exception e){
            Log.Write("configuration is not proper...");
            System.out.println(GlobalInitialization());
        }
    }

    public static boolean GlobalInitialization() {
        try {
            //Read CnctnIni
            GlobalMembers.objCnctnIni = new Wini(new File("C:\\KIOSK\\Configuration\\CNCTN.ini"));

            //Read KioskClientPBTxnSettings
            GlobalMembers.objTxnIni = new Wini(new File(dirPath + "\\IniFile\\KioskClientPBTxnSettings.ini"));

            //Read kioskClientHealth
            GlobalMembers.objHealthIni = new Wini(new File(dirPath + "\\IniFile\\KioskClientHealth.ini"));

            //Read ClientConfig.Ini
            GlobalMembers.objClientConfigIni = new Wini(new File(dirPath + "\\IniFile\\KioskClientConfig.ini"));

            //Read LksdbIni
            GlobalMembers.objLksdbIni = new Wini(new File(dirPath + "\\IniFile\\LKSDB.ini"));


            if (GlobalMembers.objCnctnIni != null) {
                GlobalMembers.txtLabel.setText("Checking configuration file....");

                Thread.sleep(5000);

                // //load connection settings
                GlobalMembers.objCBS.strIP = GlobalMembers.objCnctnIni.get("CBS", "IP");

                GlobalMembers.objCBS.strPort = GlobalMembers.objCnctnIni.get("CBS", "Port");

                GlobalMembers.objCBS.strHost_Name = GlobalMembers.objCnctnIni.get("CBS", "Host_Name");


                                 //Todo
//                                if (GlobalMembers.objCnctnIni.get("CBS", "Is_DNS").equals(""))
//                                {
//                                    if (ConfigurationManager.AppSettings["Is_DNS"] != null && ConfigurationManager.AppSettings["Is_DNS"] != "")
//                                    {
//                                        GlobalMembers.objCnctnIni.put("CBS", "Is_DNS", ConfigurationManager.AppSettings["Is_DNS"].ToString());
//                                        GlobalMembers.objCBS.bIs_DNS = ConfigurationManager.AppSettings["Is_DNS"].ToString() == "1";
//                                        Log.Write("Is_DNS read from backup");
//                                    }
//                                    else
//                                    {
//                                        Log.Write("Is_DNS not set");
//                                        Log.Write("Is_DNS not found in backup");
//                                    }
//                                }
//                                else
//                                {
//                                    GlobalMembers.objCBS.bIs_DNS = GlobalMembers.objCnctnIni.get("CBS", "Is_DNS").equals("1") ;
////                                    if (!AddUpdateAppSettings("Is_DNS", (GlobalMembers.objCBS.bIs_DNS) ? "1" : "0")) return false
//
//                                }

                                if (GlobalMembers.objCnctnIni.get("CBS", "Is_TCP").equals(""))
                                {
                                   // Todo
//                                    if (ConfigurationManager.AppSettings["Is_TCP"] != null && ConfigurationManager.AppSettings["Is_TCP"] != "")
//                                    {
//                                        GlobalMembers.objCnctnIni.put("CBS", "Is_TCP", ConfigurationManager.AppSettings["Is_TCP"].ToString());
//                                        GlobalMembers.objCBS.bIs_TCP = ConfigurationManager.AppSettings["Is_TCP"].ToString() == "1";
//                                        Log.Write("Is_TCP read from backup");
//                                    }
//                                    else
//                                    {
                                        Log.Write("Is_TCP not found in backup");
//                                    }
                                }
                                else
                                {
                                    GlobalMembers.objCBS.bIs_TCP = GlobalMembers.objCnctnIni.get("CBS", "Is_TCP").equals("1");
////                                    if (!AddUpdateAppSettings("Is_TCP", (GlobalMembers.objCBS.bIs_TCP) ? "1" : "0")) return false;
                                }

                //#endregion

            }

            if (objLksdbIni != null) {

                GlobalMembers.objISO8583.DE41 = objLksdbIni.get("ISO8583", "DE41");
                if (GlobalMembers.objISO8583.DE41.equals(""))
                    GlobalMembers.objISO8583.DE41 = objLksdbIni.put("ISO8583", "DE41", "000000LP10000000");


                GlobalMembers.objISO8583.DE102 = objLksdbIni.get("ISO8583", "DE102");
                if (GlobalMembers.objISO8583.DE102.equals(""))
                    GlobalMembers.objISO8583.DE102 = objLksdbIni.put("ISO8583", "DE102", "112        0003     000312100114898   ");

//                    endregion

//                    region STAN
                GlobalMembers.STAN = objLksdbIni.get("TXN", "STAN");
                if (GlobalMembers.STAN.equals("") || Integer.parseInt(GlobalMembers.STAN) == 0)
//                    GlobalMembers.STAN = objLksdbIni.put("TXN", "STAN", (1).ToString("D" + GlobalMembers.objISO[11].iLength));
//                    endregion

//                    region ADMIN account settings
                    GlobalMembers.objADMIN.Account_1 = objLksdbIni.get("ADMIN", "Account_1");
                if (GlobalMembers.objADMIN.Account_1.equals(""))
                    GlobalMembers.objADMIN.Account_1 = objLksdbIni.put("ADMIN", "Account_1", "111111111111111");

                GlobalMembers.objADMIN.Password_1 = objLksdbIni.get("ADMIN", "Password_1");
                if (GlobalMembers.objADMIN.Password_1.equals(""))
                    GlobalMembers.objADMIN.Password_1 = objLksdbIni.put("ADMIN", "Password_1", "111111");

                GlobalMembers.objADMIN.Account_2 = objLksdbIni.get("ADMIN", "Account_2");
                if (GlobalMembers.objADMIN.Account_2.equals(""))
                    GlobalMembers.objADMIN.Account_2 = objLksdbIni.put("ADMIN", "Account_2", "222222222222222");

                GlobalMembers.objADMIN.Password_2 = objLksdbIni.get("ADMIN", "Password_2");
                if (GlobalMembers.objADMIN.Password_2.equals(""))
                    GlobalMembers.objADMIN.Password_2 = objLksdbIni.put("ADMIN", "Password_2", "222222");
//                    endregion

//                    region Version settings
                GlobalMembers.objVERSION.App_Ver = objLksdbIni.get("VERSION", "App_Ver");
                //if (GlobalMembers.objVERSION.App_Ver == "")//Comment by Amit Khabya 2Aug2018
//                                GlobalMembers.objVERSION.App_Ver = objLksdbIni.put("VERSION", "App_Ver", "BMC Bank V" + System.Diagnostics.FileVersionInfo.GetVersionInfo(System.Reflection.Assembly.GetExecutingAssembly().Location).FileVersion); //V26.1.1.0

                GlobalMembers.objVERSION.Ghost_ver = objLksdbIni.get("VERSION", "Ghost_ver");
                if (GlobalMembers.objVERSION.Ghost_ver.equals(""))
                    GlobalMembers.objVERSION.Ghost_ver = objLksdbIni.put("VERSION", "Ghost_ver", "8.0");

                GlobalMembers.objVERSION.Build_Date = objLksdbIni.get("VERSION", "Build_Date");
                if (GlobalMembers.objVERSION.Build_Date.equals(""))
                    GlobalMembers.objVERSION.Build_Date = objLksdbIni.put("VERSION", "Build_Date", "26/12/2019"); //V26.1.2.0
//                    endregion

//                    region Details
                GlobalMembers.objDETAILS.Company_Name = objLksdbIni.get("DETAILS", "Company_Name");
                if (GlobalMembers.objDETAILS.Company_Name.equals(""))
                    GlobalMembers.objDETAILS.Company_Name = objLksdbIni.put("DETAILS", "Company_Name", "Lipi Data Systems Ltd.");

                GlobalMembers.objDETAILS.Bank_Name = objLksdbIni.get("DETAILS", "Bank_Name");
                if (GlobalMembers.objDETAILS.Bank_Name.equals(""))
                    GlobalMembers.objDETAILS.Bank_Name = objLksdbIni.put("DETAILS", "Bank_Name", "MCB Bank"); //V26.1.1.0

                GlobalMembers.objDETAILS.Kiosk_Type = objLksdbIni.get("DETAILS", "Kiosk_Type");
                if (GlobalMembers.objDETAILS.Kiosk_Type.equals(""))
                    GlobalMembers.objDETAILS.Kiosk_Type = objLksdbIni.put("DETAILS", "Kiosk_Type", "Passbook");

                GlobalMembers.objDETAILS.Copyright = objLksdbIni.get("DETAILS", "Copyright");
                if (GlobalMembers.objDETAILS.Copyright.equals(""))
                    GlobalMembers.objDETAILS.Copyright = objLksdbIni.put("DETAILS", "Copyright", "2019");

                GlobalMembers.objDETAILS.Lang_Support = objLksdbIni.get("DETAILS", "Lang_Support");
                if (GlobalMembers.objDETAILS.Lang_Support.equals(""))
                    GlobalMembers.objDETAILS.Lang_Support = objLksdbIni.put("DETAILS", "Lang_Support", "1"); //V26.1.1.0

                GlobalMembers.objDETAILS.Regional_Lang = objLksdbIni.get("DETAILS", "Regional_Lang");
                if (GlobalMembers.objDETAILS.Regional_Lang.equals(""))
                    GlobalMembers.objDETAILS.Regional_Lang = objLksdbIni.put("DETAILS", "Regional_Lang", "");
//                    endregion

//                    region Path Details
                GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path = objLksdbIni.get("PATH_DETAIL", "Passbook_Image_Path");
                if (GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path.equals(""))
                    GlobalMembers.objPATH_DETAIL.strPassbook_Image_Path = objLksdbIni.put("PATH_DETAIL", "Passbook_Image_Path", "C:\\Kiosk\\OutputFile");

                GlobalMembers.objPATH_DETAIL.Passbook_Reference_Image_Path = objLksdbIni.get("PATH_DETAIL", "Passbook_Reference_Image_Path");
                if (GlobalMembers.objPATH_DETAIL.Passbook_Reference_Image_Path.equals(""))
                    GlobalMembers.objPATH_DETAIL.Passbook_Reference_Image_Path = objLksdbIni.put("PATH_DETAIL", "Passbook_Reference_Image_Path", "C:\\Kiosk\\OutputFile");

                GlobalMembers.objPATH_DETAIL.strBackup_EJ_Path = objLksdbIni.get("PATH_DETAIL", "Backup_EJ_Path");
                if (GlobalMembers.objPATH_DETAIL.strBackup_EJ_Path.equals(""))
                    GlobalMembers.objPATH_DETAIL.strBackup_EJ_Path = objLksdbIni.put("PATH_DETAIL", "Backup_EJ_Path", "D:\\EJDATA_D_BK");

                GlobalMembers.objPATH_DETAIL.Mirror_Required = (objLksdbIni.get("PATH_DETAIL", "Mirror_Required").toLowerCase().equals("yes"));
                if (!GlobalMembers.objPATH_DETAIL.Mirror_Required)
                    GlobalMembers.objPATH_DETAIL.Mirror_Required = (objLksdbIni.put("PATH_DETAIL", "Mirror_Required", "No").toLowerCase().equals("yes"));
//                    endregion

//                    region Account details
                GlobalMembers.objACCOUNT_DETAILS.iAccount_Length = Integer.parseInt(objLksdbIni.get("ACCOUNT_DETAILS", "Account_Length"));
                if (GlobalMembers.objACCOUNT_DETAILS.iAccount_Length == 0)
                    GlobalMembers.objACCOUNT_DETAILS.iAccount_Length = Integer.parseInt(objLksdbIni.put("ACCOUNT_DETAILS", "Account_Length", "10"));

                GlobalMembers.objACCOUNT_DETAILS.iMax_Line_To_Print = Integer.parseInt(objLksdbIni.get("ACCOUNT_DETAILS", "Max_Line_To_Print"));
                if (GlobalMembers.objACCOUNT_DETAILS.iMax_Line_To_Print == 0)
                    GlobalMembers.objACCOUNT_DETAILS.iMax_Line_To_Print = Integer.parseInt(objLksdbIni.put("ACCOUNT_DETAILS", "Max_Line_To_Print", "22"));

                //Modified for V1.1.3.0
                if (objLksdbIni.get("ACCOUNT_DETAILS", "Extra_Line_Feed").equals(""))
                    GlobalMembers.objACCOUNT_DETAILS.iExtra_Line_Feed = Integer.parseInt(objLksdbIni.put("ACCOUNT_DETAILS", "Extra_Line_Feed", "5"));
                else
                    GlobalMembers.objACCOUNT_DETAILS.iExtra_Line_Feed = Integer.parseInt(objLksdbIni.get("ACCOUNT_DETAILS", "Extra_Line_Feed"));

                if (GlobalMembers.objACCOUNT_DETAILS.iExtra_Line_Feed == 0)
                    GlobalMembers.objACCOUNT_DETAILS.iExtra_Line_Feed = Integer.parseInt(objLksdbIni.put("ACCOUNT_DETAILS", "Extra_Line_Feed", "1"));
//                    endregion

//                    region Passbook Format Details
                GlobalMembers.iNoOfByteInSingleLine = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "NoOfByteInSingleLine"));
                if (GlobalMembers.iNoOfByteInSingleLine == 0)
                    GlobalMembers.iNoOfByteInSingleLine = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "NoOfByteInSingleLine", "147"));

                GlobalMembers.iDateFieldSize = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "DateFieldSize"));
                if (GlobalMembers.iDateFieldSize == 0)
                    GlobalMembers.iDateFieldSize = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "DateFieldSize", "8"));

                GlobalMembers.iParticularSize = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "ParticularSize"));
                if (GlobalMembers.iParticularSize == 0)
                    GlobalMembers.iParticularSize = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "ParticularSize", "50"));

                GlobalMembers.iChequeNoSize = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "ChequeNoSize"));
                if (GlobalMembers.iChequeNoSize == 0)
                    GlobalMembers.iChequeNoSize = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "ChequeNoSize", "16"));

                GlobalMembers.iDebitFieldSize = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "DebitFieldSize"));
                if (GlobalMembers.iDebitFieldSize == 0)
                    GlobalMembers.iDebitFieldSize = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "DebitFieldSize", "17"));

                GlobalMembers.iCreditFieldSize = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "CreditFieldSize"));
                if (GlobalMembers.iCreditFieldSize == 0)
                    GlobalMembers.iCreditFieldSize = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "CreditFieldSize", "17"));

                GlobalMembers.iEndBalanceFieldSize = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "EndBalanceFieldSize"));
                if (GlobalMembers.iEndBalanceFieldSize == 0)
                    GlobalMembers.iEndBalanceFieldSize = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "EndBalanceFieldSize", "17"));


                GlobalMembers.iSpacesAfterDate = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "SpacesAfterDate"));
                if (GlobalMembers.iSpacesAfterDate == 0)
                    GlobalMembers.iSpacesAfterDate = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "SpacesAfterDate", "4")); //V26.1.1.0

                GlobalMembers.iSpacesAfterParticular = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "SpacesAfterParticular"));
                if (GlobalMembers.iSpacesAfterParticular == 0)
                    GlobalMembers.iSpacesAfterParticular = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "SpacesAfterParticular", "3")); //V26.1.1.0

                GlobalMembers.iSpacesAfterChequeNo = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "SpacesAfterChequeNo"));
                if (GlobalMembers.iSpacesAfterChequeNo == 0)
                    GlobalMembers.iSpacesAfterChequeNo = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "SpacesAfterChequeNo", "3"));

                GlobalMembers.iSpacesAfterDebitAmount = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "SpacesAfterDebitAmount"));
                if (GlobalMembers.iSpacesAfterDebitAmount == 0)
                    GlobalMembers.iSpacesAfterDebitAmount = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "SpacesAfterDebitAmount", "3")); //V26.1.1.0

                GlobalMembers.iSpacesAfterCreditAmount = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "SpacesAfterCreditAmount"));
                if (GlobalMembers.iSpacesAfterCreditAmount == 0)
                    GlobalMembers.iSpacesAfterCreditAmount = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "SpacesAfterCreditAmount", "3")); //V26.1.1.0

                GlobalMembers.iLineSpacingForThread = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "LineSpacingForThread")); //V25.1.1.14
                if (GlobalMembers.iLineSpacingForThread == 0)
                    GlobalMembers.iLineSpacingForThread = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "LineSpacingForThread", "6"));  //V26.1.1.0 //V26.1.2.0 //7->6

                GlobalMembers.iTxnsBeforeThreadSpace = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "TxnsBeforeThreadSpace")); //V25.1.1.14
                if (GlobalMembers.iTxnsBeforeThreadSpace == 0)
                    GlobalMembers.iTxnsBeforeThreadSpace = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "TxnsBeforeThreadSpace", "14")); //V26.1.1.0

                GlobalMembers.iThreadforIndexing = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "ThreadforIndexing"));
                if (GlobalMembers.iThreadforIndexing == 0)
                    GlobalMembers.iThreadforIndexing = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "ThreadforIndexing", "16")); //V26.1.1.0

                GlobalMembers.strPBHeight = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "PB_Height"));
                if (GlobalMembers.strPBHeight == 0)
                    GlobalMembers.strPBHeight = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "PB_Height", "2100")); //V26.1.1.0

                GlobalMembers.strPBWidth = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "PB_Width"));
                if (GlobalMembers.strPBWidth == 0)
                    GlobalMembers.strPBWidth = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "PB_Width", "2200")); //V26.1.1.0


                //V26.1.1.31
                GlobalMembers.iSpacesBeforeDate = Integer.parseInt(objLksdbIni.get("FORMAT_DETAILS", "SpacesBeforeDate"));
                if (GlobalMembers.iSpacesBeforeDate == 0)
                    GlobalMembers.iSpacesBeforeDate = Integer.parseInt(objLksdbIni.put("FORMAT_DETAILS", "SpacesBeforeDate", "3")); //V26.1.1.0
//                    endregion

//                    region Timers
                GlobalMembers.objKioskTimers.iTmrRotator1 = Integer.parseInt(objLksdbIni.get("TIMERS", "ROTATOR1"));
                if (GlobalMembers.objKioskTimers.iTmrRotator1 == 0)
                    GlobalMembers.objKioskTimers.iTmrRotator1 = Integer.parseInt(objLksdbIni.put("TIMERS", "ROTATOR1", "10"));

                GlobalMembers.objKioskTimers.iTmrRotator2 = Integer.parseInt(objLksdbIni.get("TIMERS", "ROTATOR2"));
                if (GlobalMembers.objKioskTimers.iTmrRotator2 == 0)
                    GlobalMembers.objKioskTimers.iTmrRotator2 = Integer.parseInt(objLksdbIni.put("TIMERS", "ROTATOR2", "10"));

                GlobalMembers.objKioskTimers.iTmrRotator3 = Integer.parseInt(objLksdbIni.get("TIMERS", "ROTATOR3"));
                if (GlobalMembers.objKioskTimers.iTmrRotator3 == 0)
                    GlobalMembers.objKioskTimers.iTmrRotator3 = Integer.parseInt(objLksdbIni.put("TIMERS", "ROTATOR3", "10"));

                GlobalMembers.objKioskTimers.iTmrRotator4 = Integer.parseInt(objLksdbIni.get("TIMERS", "ROTATOR4"));
                if (GlobalMembers.objKioskTimers.iTmrRotator4 == 0)
                    GlobalMembers.objKioskTimers.iTmrRotator4 = Integer.parseInt(objLksdbIni.put("TIMERS", "ROTATOR4", "10"));

                GlobalMembers.objKioskTimers.iTmrRotator5 = Integer.parseInt(objLksdbIni.get("TIMERS", "ROTATOR5"));
                if (GlobalMembers.objKioskTimers.iTmrRotator5 == 0)
                    GlobalMembers.objKioskTimers.iTmrRotator5 = Integer.parseInt(objLksdbIni.put("TIMERS", "ROTATOR5", "10"));

                GlobalMembers.objKioskTimers.iTmrRotator6 = Integer.parseInt(objLksdbIni.get("TIMERS", "ROTATOR6"));
                if (GlobalMembers.objKioskTimers.iTmrRotator6 == 0)
                    GlobalMembers.objKioskTimers.iTmrRotator6 = Integer.parseInt(objLksdbIni.put("TIMERS", "ROTATOR6", "10"));

                GlobalMembers.objKioskTimers.iTmrRotator7 = Integer.parseInt(objLksdbIni.get("TIMERS", "ROTATOR7"));
                if (GlobalMembers.objKioskTimers.iTmrRotator7 == 0)
                    GlobalMembers.objKioskTimers.iTmrRotator7 = Integer.parseInt(objLksdbIni.put("TIMERS", "ROTATOR7", "10"));

                //Condition updated for V1.1.3.0
                GlobalMembers.objKioskTimers.iTmrTransaction = Integer.parseInt(objLksdbIni.get("TIMERS", "TRANSACTION"));
                if (GlobalMembers.objKioskTimers.iTmrTransaction == 0 || GlobalMembers.objKioskTimers.iTmrTransaction == 10)
                    GlobalMembers.objKioskTimers.iTmrTransaction = Integer.parseInt(objLksdbIni.put("TIMERS", "TRANSACTION", "40"));
//                    endregion

//                    region DEMO purpose
                GlobalMembers.bIsPassbookDemo = objLksdbIni.get("DEMO", "PBP").toLowerCase().equals("yes");
//                    endregion


            } else
                return false;


            Log.Write("***** APPLICATION START-UP *****");
            GlobalMembers.KioskID = objLksdbIni.get("Machine_Info", "Kiosk_ID");
            Log.Write("KIOSK-ID : " + GlobalMembers.KioskID);
            Log.Write("Initializing Application Variables");
            Log.Write("Application Version = " + GlobalMembers.objVERSION.App_Ver);
            Log.Write("Ghost Version = " + GlobalMembers.objVERSION.Ghost_ver);
            Log.Write("Build Date = " + GlobalMembers.objVERSION.Build_Date);
            Log.Write("Bank_Name = " + GlobalMembers.objDETAILS.Bank_Name);
            Log.Write("Kiosk_Type = " + GlobalMembers.objDETAILS.Kiosk_Type);
            Log.Write("Lang_Support = " + GlobalMembers.objDETAILS.Lang_Support);
            Log.Write("Regional_Lang = " + GlobalMembers.objDETAILS.Regional_Lang);
            Log.Write("Extra_Line_Feeds = " + GlobalMembers.objACCOUNT_DETAILS.iExtra_Line_Feed);

            GlobalMembers.bAddMsgLen = false;
            GlobalMembers.usMsgLen = 2;
            GlobalMembers.bIsMsgLenInHex = false;

            GlobalMembers.bAddISOHeader = false;
            GlobalMembers.usISOHeaderLength = 0;
            GlobalMembers.strDefISOHeader = "";

            GlobalMembers.usMTILength = 4;
            GlobalMembers.strDefMTI = "1200";

            GlobalMembers.bIsHexBitmap = false;
            GlobalMembers.bAddTrailingByte = false;
            GlobalMembers.byTrailingData = 0;

            GlobalMembers.eMsgLenType = GlobalMembers.MsgLenType.ASCII;
            Thread.sleep(2000);

//                            LoadScreenData();
            return true;
        } catch (Exception excp) {
            Log.Write("InitializeGlobalVariables() Excp-" + excp.getMessage());
            return false;
        }
    }

    private static boolean StartEngine()
    {
        try
        {
            //Reset the CBS flag to false
            GlobalMembers.bIsCBSConnected = false;

            GlobalMembers.txtLabel.setText("Please Wait While Passbook Printer Is Being Checked....");
            // check for Passbook

            for (int i = 0; i < 20; i++)
            {
                Thread.sleep(100);
            }

            Log.Write("Checking passbook printer connectivity");

            //if Passbook is Demo, used for debugging code without printer
            if (!GlobalMembers.bIsPassbookDemo)
            {
                boolean Status = false;
                if (!GlobalMembers.bIsPassbookEpson)
                {
//                    if (Ollivtti_Obj == null)
//                        Ollivtti_Obj = new Ollivetti();
//
//                    // Do Work
//                    Status = Ollivtti_Obj.Init_Device();
                }
                else
                {
//                    if (EPSON_Obj == null)
//                        EPSON_Obj = new EPSON();
//
//                    // Do Work
//                    Status = EPSON_Obj.Init_Device();
                }

                if (Status)
                {
                  Log.Write("Passbook Status = Connected");
                    GlobalMembers.txtLabel.setText("Passbook Printer Connected....");

                    //For RMS //V26.1.2.9
                    objHealthIni.put("Health_Details" , "0", "Passbook");
                    objHealthIni.put("Health_Details", "Changes_DoneHealth", "1");
//                    return true;
                }
                else
                {
                    AppInitCount = 0;
//                    tmrInitialize.Enabled = false;

                    //For RMS //V26.1.2.9
                    objHealthIni.put("Health_Details" , "1", "Passbook");
                    objHealthIni.put("Health_Details", "Changes_DoneHealth", "1");

                    Log.Write("Passbook Status = Disconnected");
                    GlobalMembers.txtLabel.setText("Passbook Printer Not Connected....");
                    Thread.sleep(2000);

//                    tmrPassbookCheck.Enabled = true;

//                    lblEnglishScreenText.Visible = false;
//                    lblMarathiScreenText.Visible = false;
//                    lblText.Visible = lblTime.Visible = lblAppVersion.Visible = false;
//                    pbxBackground.Image = Image.FromFile(Application.StartupPath + "\\Lipi\\OOOS\\001.JPG");  //05May14
//                    return false;
                }
            }
            else
            {
                Log.Write("Passbook Status = Demo");
                GlobalMembers.txtLabel.setText("Passbook Printer Demo....");

                objHealthIni.put("Health_Details" , "0", "Passbook");
                objHealthIni.put("Health_Details", "Changes_DoneHealth", "1");
            }

//            Application.launch();
            Thread.sleep(2000);


            Log.Write("Checking RMS connection");
            GlobalMembers.txtLabel.setText("Please Wait While Connectivity With RMS Is Being Checked....");
            Thread.sleep(2000);

            Wini objRMSIni = new Wini(new File("C:\\LIPIRMS_Client\\KioskClientConfig.ini"));
            //if the machine is not registered with RMS
            if (!objRMSIni.get("Kiosk_Details", "Kiosk_DetailSent").equals("1"))
            {
                GlobalMembers.txtLabel.setText("Connectivity with RMS Server Failed....Connect RMS Client First");
                Log.Write("Checking RMS connectivity- Failed");
                Log.Write("CHECKING RMS CONNECTIVITY- FAILED" );
//                return false;
            }

            GlobalMembers.txtLabel.setText("Connected with RMS Server....");
            Log.Write("Checking RMS connectivity- Succeed");
            Thread.sleep(2000);

            Log.Write("CHECKING NETWORK CONNECTION" );

            GlobalMembers.txtLabel.setText("Please Wait While Connectivity With CBS Is Being Checked....");

            Thread.sleep(2000);

            AppInitCount = 0;
//            tmrInitialize.Enabled = false;

//            bMISTimer = true;
//            tmrInitialize.Interval = 60 * 1000;
//            tmrInitialize.Enabled = true;

            // Check Network Connectivity
            if (GlobalMembers.objCBS.bIs_TCP)
            {
                try
                {
                    //To do for dns handling
                    Log.Write("CONNECTION VIA TCP/IP");
                    if (GlobalMembers.client == null)
                    {
                        //Added[12Sept2013] To resolve DNS and to get IP
                        if (GlobalMembers.objCBS.bIs_DNS)
                        {
                            //Todo
//                            IPHostEntry hostInfo = Dns.GetHostEntry(GlobalMembers.objCBS.strHost_Name);
//                            client = ScsClientFactory.CreateClient(new ScsTcpEndPoint(hostInfo.AddressList[0].ToString(), Convert.ToInt32(GlobalMembers.objCBS.strPort)));
                            Log.Write("CONNECTION VIA TCP/IP AND DNS IS ENABLED");
                        }
                        else
                        {
//                            client = ScsClientFactory.CreateClient(new ScsTcpEndPoint(GlobalMembers.objCBS.strIP, Convert.ToInt32(GlobalMembers.objCBS.strPort)));
                            GlobalMembers.client = new Socket(GlobalMembers.objCBS.strIP,Integer.parseInt(GlobalMembers.objCBS.strPort));
                            Log.Write("CONNECTION VIA TCP/IP AND DNS IS NOT ENABLED");
                        }

                        //For RMS //V26.1.2.9
                        objHealthIni.put("Health_Details", "Application", "0");
                        objHealthIni.put("Health_Details", "Changes_DoneHealth", "1");

//                        iMsgType = -1;//mean Connection not at start time...
//                        client.connect();
                    }
                    return true;
                }
                catch (Exception excp)
                {
//                    iMsgType = -1;//mean Connection not at start time...

                    //Update health INI
                    objHealthIni.put("Health_Details", "Application", "1");
                    objHealthIni.put("Health_Details", "Changes_DoneHealth", "1");

                    Log.Write("TCP Connection not established = " + excp.getMessage());
                    Log.Write("CONNECTION WITH CBS IS NOT ESTLABLISHED" );

//                    lblAppVersion.Visible = lblText.Visible = lblTime.Visible = false;
                    GlobalMembers.time.setVisible(false); GlobalMembers.txtLabel.setVisible(false); GlobalMembers.headingLabel.setVisible(false);
                    BufferedImage imgBuffered=ImageIO.read(new File(dirPath+"/src/Image/OOOS.JPG"));
                    ImageIcon oooS = new ImageIcon(imgBuffered);
                    GlobalMembers.imgLabel.setIcon(oooS);
                    return false;
                }
            }
        }
        catch (Exception excp)
        {
            Log.Write("StartEngine() Excp-" + excp.getMessage());
            return false;
        }
        finally
        {
            System.gc();
        }
        return true;
    }

}
class Time implements Runnable{
    @Override
    public void run() {
        while (true){
            String strTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            GlobalMembers.time.setText(strTime);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

