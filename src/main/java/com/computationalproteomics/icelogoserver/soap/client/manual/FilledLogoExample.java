package com.computationalproteomics.icelogoserver.soap.client.manual;


import org.apache.soap.Constants;
import org.apache.soap.Fault;
import org.apache.soap.SOAPException;
import org.apache.soap.util.xml.QName;
import org.apache.soap.util.xml.Deserializer;
import org.apache.soap.encoding.SOAPMappingRegistry;
import org.apache.soap.encoding.soapenc.StringDeserializer;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Response;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 10-Nov-2009
 * Time: 09:55:20
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class will create a call to the iceLogo SOAP server. The response from the server will be a String with svg content representing
 * a FilledLogo. This svg will be transcoded in to an image an saved in the temporary folder.
 */
public class FilledLogoExample {


    public FilledLogoExample(){
        try{

            // Create the url, this url point to the location of the webservice
            URL lIcelogoUrl = new URL("http://icelogo.ugent.be/icelogoserver/services/icelogo");
            // Set a String Deserializer for the response ("getFilledLogoReturn")
            SOAPMappingRegistry smr = new SOAPMappingRegistry();
            Deserializer stringDser = new StringDeserializer();
            smr.mapTypes(Constants.NS_URI_SOAP_ENC,new QName("urn:IceLogoFetcher", "getFilledLogoReturn"),null,null,stringDser);
            // Create and build the call.
            Call call = new Call();
            call.setSOAPMappingRegistry(smr);
            call.setTargetObjectURI("urn:IceLogoFetcher");
            call.setMethodName("getFilledLogo");
            call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);

            // Create the parameters
            Vector<Parameter> params = new Vector<Parameter>();
            String[] lHumanGranzymeBSubstrates = new String[]{"GHISVKEPTPSIASDISLPIATQELRQRLR","EREMFDKASLKLGLDKAVLQSMSGRENATN","XXXXXXMSDIVVVTDLIAVGLKRGSDELLS","GQDQEEEEIEDILMDTEEELMRAEDTEQLK","ESYATDNEKMTSTPETLLEEIEAKNRELIA","VENKERTLKRLLLQDQENSLQDNRTSSDSP","SALRDDERRESATADAGYAILEKKGALAER","XXXXXXXXXMEVTGDAGVPESGEIRTLKPC","XXXXXMAVESRVTQEEIKKEPEKPIDREKT","XXXXXXMAGPRVEVDGSIMEGGGQILRVST","RAIVDVHFDPTTAFRAPDVARALLRQIQVS","HLRGFANQHSRVDPEELFTKLDRIGKGSFG","GDGDEEGENEEEEEDAEAGSEKEEEARLAA","XXMSHVAVENALGLDQQFAGLDLNSSDNQS","LLKRRNVPHEDICEDSDIDGDYRVQNTSLE","GPCGVPAAGGPPTGDEASSLGTAQAASTSG","PCGVPAAGGPPTGDEASSLGTAQAASTSGL","DTNHNEEDDEEPIPESSELTLQELLGDERR","VKAMAQEDSESLEEDSSSEEEDETPAQATP","GALSEEEAQALLSGDTQTDTTSFYDRVWAA","DTDLYDEFGNYIGPELDSDEDDDELGRETK","DLYDEFGNYIGPELDSDEDDDELGRETKDL","PGVQMQAIPEDAIPEESGDEDEEDPDKRIS","ARLIPDTVLVDLVSDSDEEVLEVADPVEVP","VPVARLPAPAKPEQDSDSDSEGAAEGPAGA","LPDGCEGRGLSVHTDMASVTKAMAAPESGL","HMECEIKLEGPVSPDVEPGKEETEESKKRK","FIQEPAKNRPGPQTRSDLLLSGRDWNTLIV","RLTEEEEAEPGTEVDLAVLADLALTPARRG","EGDDKDDYWREQQEKLEIEKRAIVEDHSLV","GVGGGAASSVQVQGDNTSDLDFGAVQRGDP","DDYLNTPLLDEIDPDSAEEPPVSRRLFLDG","MMVPGMEGKEEPGSDSGTTAVVALIRGKQL","TQEAVNGHGALEVDMALGSPEMDVRKKKKK","PQVTEAEEEEAPCCSSSVSGGAASSSPAAG","QVTEAEEEEAPCCSSSVSGGAASSSPAAGI","EERDEEEEGNIVDAEAEEGDADASDTKRKE","LLKRRNVPQEDICEDSDIDGDYRVQNTSLE","SGEEEEDEEGYNDGEVDDEEDEEEAGEEEG","EEEEDEEGYNDGEVDDEEDEEEAGEEEGSQ","EEDEEGYNDGEVDDEEDEEEAGEEEGSQKR","AGEEEEKEAGIVHSDAEKEQEEEEQKQEME","SDRSRKRELKEVFGDDSEISKESSGVKKRR","DRSRKRELKEVFGDDSEISKESSGVKKRRI","QDINLNSPNKGLLSDSMTDVPVDTGVAART","LQASPAKPSGETAADSMIPEEEDDEDDEDG","SVLDNFKFLESAAADFSDEDEDDDVDGREK","SGEDRDTKEALKEFDFLVTSEEGDNESRSA","GLGELAGLTVANEADSLTYDIANNKDALRK","DPFTSETVDPEMEGDDNLGGEDKKETPEEV","GGEDKKETPEEVAADVLAEVITAAVRAVDG","TYNDSDGGTAWVSQESFEADEPDSRAGVKW","DGVEVSVTTSKIVTDSDSKTEELRFLRRQE","SVLDNFKFLESAAADFSDEDEDEDTDGRAK","TSEDRDTKEALKEFDFLVTSEEGDNESRSA","GLGELAGLTVANEADSLAYDIANNKDALRK","LTSSRVCFPERILEDSGFDEQQEFRSRCSS","PLSPTMEEEPLVVFLSGEDDPEKIEERKKS","YSVYVYKVLKQVHPDTGISSKAMGIMNSFV","LKHLEPEPEEEIIAEDYDDDPVDYEATRLE","KHLEPEPEEEIIAEDYDDDPVDYEATRLEG","EPSLKNSNPDEIEIDFETLKPSTLRELERY","LPGSLQRSRSDIDVNAAAGAKAHHAAGQSV","DLERRREESQTQIRDVMVWSNERVMGWVSG","DIYHCPNCEKTHGKSTLKKKRTWHKHGPGQ","VFREFFGGRDPFSFDFFEDPFEDFFGNRRG","RSTTLDAGNIKLAFNSLLEKAEAREREREK","GKKAALDEAQGVGLDSTGYYDQEIYGGSDS","GSGWAETPRTDRGGDSIGETPTPGASKRKS","PDDIQYFDKLLVDVDESTLSPEEQKERKIM","RSRRERVRQSRMDTDLETMDLDQGGEALAP","IRMLRSPTLYGISHDDLKGDPLLDQRRLDL","AAAAAAGDSDSWDADAFSVEDPVRKVGGGG","EESDLELAKETFGVNNAVYGIDAMNPSSRD","MDKMMVAGFKKQMADTGKLNTLLQRAPQCL","TEMGPPPSPASTCSDASSIASSASMPYKRR","DSDSSSSQSDDIEQETFMLDEPLERTTNSS","NRKRHGSSRSVVDMDLDDTDDGDDNAPLFY","ADLSKYNLDASEEEDSNKKKSNRRSRSKSR","DLLDRQILAADAGADAGQLDLVDQIDQRNL","QEGDEEEEGHIVDAEAEEGDADASDAKRKE","SVVLSMDNNRNLDLDSIIAEVKAQYEDIAN","TAVVQAAYCAPKKEKVAIKRINLEKCQTSM","GHSEASLASALVEGEIAEEAAEKATSRGSN","NSGMDSNIDLTIVEDEEEESVALEQAQKVR","VERDDGSTLMEIDGDKGKQGGPTYYIDTNA","QLVENTDETYCIDNEALYDICFRTLKLTTP","ETGAGKHVPRAVFVDLEPTVIDEVRTGTYR","DEVRTGTYRQLFHPEQLITGKEDAANNYAR","SITASLRFDGALNVDLTEFQTNLVPYPRIH","QLLDDYPKCFIVGADNVGSKQMQQIRMSLR","RAKQEQEAKQKLEEDAEMKSLEEKIGCLLK","LYAVKILKKDVVIQDDDVECTMVEKRVLAL","YRRLLEDGEDFNLGDALDSSNSMQTIQKTT","SVVLSMDNSRSLDMDSIIAEVKAQYEDIAN","APPPPPPPEEDPEQDSGPEDLPLVRLEFEE","XXXXXXXXMSDAAVDTSSEITTKDLKEKKE","XXXXXXMSDAAVDTSSEITTKDLKEKKEVV","XXMSDAAVDTSSEITTKDLKEKKEVVEEAE","SDAAVDTSSEITTKDLKEKKEVVEEAENGR","AAVDTSSEITTKDLKEKKEVVEEAENGRDA","VDTSSEITTKDLKEKKEVVEEAENGRDAPA","DTSSEITTKDLKEKKEVVEEAENGRDAPAN","GEQEADNEVDEEEEEGGEEEEEEEEGDGEE","EADNEVDEEEEEGGEEEEEEEEGDGEEEDG","NEVDEEEEEGGEEEEEEEEGDGEEEDGDED","DEEEEEGGEEEEEEEEGDGEEEDGDEDEEA","EEEGGEEEEEEEEGDGEEEDGDEDEEAESA","EEEEEEEEGDGEEEDGDEDEEAESATGKRA","EEEEEEEGDGEEEDGDEDEEAESATGKRAA","REIFDSRGNPTVEVDLFTSKGLFRAAVPSG","PVHISGQHLVAVEEDAESEDEEEEDVKLLS","YSIYVYKVLKQVHPDTGISSKAMGIMNSFV","SDEHGIDPTGTYHGDSDLQLDRISVYYNEA","PDNFVFGQSGAGNNWAKGHYTEGAELVDSV","NWAKGHYTEGAELVDSVLDVVRKEAESCDC","PGNPPAEIGQNISSNSSASILESKSLYDEV","LTFGFVRADDEVDVDGTVEEDLGKSREGSR","VSNNVPGSDFSVGADTALNTICTTCDRIKQ","PDYWIDGSNRDPLGDFFEVESELGRGATSI","ERERKEELRGGGGDMAEPSEAGDAPPDDGP","GVRLLQDSVDFSLADAINTEFKNTRTNEKV","DQLTNDKARVEVERDNLAEDIMRLREKLQE","QELQAQIQEQHVQIDVDVSKPDLTAALRDV","LQAQIQEQHVQIDVDVSKPDLTAALRDVRQ","TEYRRQVQSLTCEVDALKGTNESLERQMRE","TLRGQVGGQVSVEVDSAPGTDLAKILSDMR","LGRTLGLYGKDQQEAALVDMVNDGVEDLRC","EEDEDDEDEDDEEEDDEDDDEEEEEEEPVK","RTLLAKNLSFNITEDELKEVFEDAMEIRLV","KSKGIAYIEFKSEADAEKNLEEKQGAEIDG","SQPSKTLFVKGLSEDTTEETLKESFEGSVR","SGAGSEDMGTTVNGDVFQEANGPADGYAAI","IVDKYRDGTKMVSADAYKITPGARGAFSEE","SNFGGGGSYNDFGNYNNQSSNFGPMKGGNF","XXXXXXMAGGKAGKDSGKAKTKAVSRSQRA","NSANCLEEKGPMFELLPGESNKIPRLRTDL","PTRPTDKPLRLPLQDVYKIGGIGTVPVGRV","GKKLEDGPKFLKSGDAAIVDMVPGKPMCVE","AAIVDMVPGKPMCVESFSDYPPLGRFAVRD","AALRTGDDYVAIGADEEELGSQIEEAIYQE","IANAHRKPLVIIAEDVDGEALSTLVLNRLK","PKSKELVSSSSSGSDSDSEVEKKLKRKKQA","SKELVSSSSSGSDSDSEVEKKLKRKKQAVP","PPYRGRSSESSCGVDGDYEDAELNPRFLKD","YRGRSSESSCGVDGDYEDAELNPRFLKDNL","VPDDKKPNGRKVVGDVAYDEAKERASFITP","LQRSKKGKLPIVNEDDELVAIIARTDLKKN","SGAGKAIGVLTSGGDAQGMNAAVRAVTRMG","ISNNVPGTDFSLGSDTAVNAAMESCDRIKQ","NKHNRLYMKARPFPDGLAEDIDKGEVSARQ","XXMIIYRDLISHDEMFSDIYKIREIADGLC","SDIYKIREIADGLCLEVEGKMVSRTEGNID","EERDQDKKRRVVDTESGAAAAVEKLEEVTA","AGPKTLKKKVPAVPETLKKKRRNFAELKVK","GALSEAEAQALLSGDTQTDATSFYDRVWAA","VSVETQGDDWDTDPDFVNDISEKEQRWGAK","SEKDIQDLKFGVEQDVDMVFASFIRKASDV","LTFGSVRADDEVDVDGTVEEDLGKSREGSR","EDLYDCVENEEAEGDEIYEDLMRSEPVSMP","LTPDLWKETVFTKSPYQEFTDHLVKTHTRV","LGSLELVEDDTVDSDATNGLIDLLEQEEGQ","REIFDSRGNPTVEVDLYTAKGLFRAAVPSG","WQKFTASAGIQVVGDDLTVTNPKRIAKAAS","REILDSRGNPTVEVDLYTAKGLFRAAVPSG","WSKFTANVGIQIVGDDLTVTNPKRIERAVE","XXXXXXXXMDGIVPDIAVGTKRGSDELFST","QSGNLALAASAAAVDAGMAMAGQSPVLRII","LYAIKILKKDVVIQDDDVECTMVEKRVLAL","SANHNILQIVDVCHDVEKDEKLIRLMEEIM","LGQAEEVVQERICDDELILIKNTKARTSAS","EGVEEKKKEVPAVPETLKKKRRNFAELKIK","KTKSHDDGNIDLESDSFLKFDSEPSAVALE","XXXXXXMPKRKVSADGAAKAEPKRRSARLS","SFVSSSDRLRELGPDGEEAEGPGAGDGPPR","KNAESNAELKGLDVDSLVIEHIQVNKAPKM","RTLLAKNLPYKVTQDELKEVFEDAAEIRLV","KSKGIAYIEFKTEADAEKTFEEKQGTEIDG","SQPSKTLFVKGLSEDTTEETLKESFDGSVR","PVPTFQPFQRSMSADEDLQEPSRRPQRKSL","SEGDFSADTSEINSNSDSLNSSSLLMNGLR","VRLRSSVPGVRLLQDSVDFSLADAINTEFK","PPPRGGTRGQEPQMKETIMNQEKLAKLQAQ","YGSVLMGKSNESGELVAIKRMKRKFYSWDE","LQRSKKGKLPIVNDCDELVAIIARTDLKKN","WTSFLSGVDIQIVGDDLTVTNPKRIAQAVE","PHRHEGVFICRGKEDALVTKNLVPGESVYG","KHLKRVAAPKHWMLDKLTGVFAPRPSTGPH","YDTKGRFAVHRITVEEAKYKLCKVRKITVG","FKDIDIEDLEELDPDFIMAKQVEQLEKEKK","EFWKQLCAEHGISPEGIVEEFATEGTDRKD","SDKTDTDWRARPATDSFDDYPPRRGDDSFG","LDDVELREAQRDYLDFLDDEEDQGIYQSKV","LDDVELREAQRDYLDFLDDEEDQGIYQNKV","LRMHQYRAPGEQDGDALPLGSSVDILATDD","GEQDGDALPLGSSVDILATDDPDFTQDDQQ","DDSQEKTDDSQETQDSQKVELSEPRLKAFK","EPSLRDSNPEEIEIDFETLKPSTLRELERY","AEMSSILEERILGADTSVDLEETGRVLSIG","IIGDRQTGKTSIAIDTIINQKRFNDGSDEK","EEEEEEEEGDGEEEDGDEDEEAEAPTGKRV","GQIPATALLPTMTPDGLAVTPTPVPVVGSQ","IPATALLPTMTPDGLAVTPTPVPVVGSQMT","ATALLPTMTPDGLAVTPTPVPVVGSQMTRQ","LILFPRKPSAPKKGDSSAEELKLATQLTGP","AVNSVQSGNLALAASAAAVDAGMAMAGQSP","NLALAASAAAVDAGMAMAGQSPVLRIIVEN","PMSAPSRSSGALSVDKKPTSTKPSSSAPRV","AKVDEFPLCGHMVSDEYEQLSSEALEAARI","IHISKKWGFTKFNADEFEDMVAEKRLIPDG","HISKKWGFTKFNADEFEDMVAEKRLIPDGC","RLEQQVPVNQVFGQDEMIDVIGVTKGKGYK","QVPVNQVFGQDEMIDVIGVTKGKGYKGVTS","LGQIGPAPPLKVHVDCMTSQKLVRLPGLID","XXXXXXXMADLSLADALTEPSPDIEGEIKR","VTLAKDMQPSMESDMALVKDMELPTEKEVA","LAKDMQPSMESDMALVKDMELPTEKEVALV","ITGTGKKCSLPAEEDSVLEKLGERKPCNSQ","WDDDDAPRPSQFEEDLALMEEMEAEHRLQE","XXXXXXXXXXMADPDVLTEVPAALKRLAKY","FREHQFTKIDTIAADESFTQMDLGDRILKL","DSSVSYIDSAVISPDTVPLGTGTSILSKQV","LRRRTFSYDEIHGQDSGAEDSIAKQIESLE","SPPQMHSSAIPLDFDVSSPLTYGTPSSRVE","XXMSGFDDPGIFYSDSFGGDAQADEGQARK","NHFSIMQQRRLKDQDQDEDEEEKEKRGRRK","GALARGDDETLHKNNALKVVRELQAQIAEL","LSQRYYEDLDEVSSTSSVSQSLESEDARTS","RSRILKAGGKILTFDQLALESPKGRGTVLL","DTGLAPPALWDLAADKQTLQSEQPLQVARC","NVTLPAVFKAPIRPDIVNFVHTNLRKNNRQ","RRIQRRGPCIIYNEDNGIIKAFRNIPGITL","EDEEEEGEEEDVSGEEEEDEEGYNDGEVDD","SGEEEEDEEGYNDGEVDDEEDEEELGEEER","EEEEDEEGYNDGEVDDEEDEEELGEEERGQ","RQLEDILSTYCVDNNQGGPGEDGAQGEPAE","STYCVDNNQGGPGEDGAQGEPAEPEDAEKS","IPPPYDKKKRMVVPAALKVVRLKPTRKFAY","SKPAAQTPPASIEVDENIELISGQNERMGP","FPVGAATLVDEVGVDVAKHVAEDLGKVFGE","GLIHRKTVGVEPAADGKGVVVVMKRRSGQR","VRKLKEDKAPQVDVDKAVAELKARKRVLEA","HSDAQALVLLDVTPDQSMVDEGMAREVINR","LDELSRYPEDKITPENLPQILLQLKRRRTE","MMLTSDQKPDVMYADIGGMDIQKQEVREAV","SLSDEAPVLPNTTPDLLLVTTANPSAPGTD","AMNRHIKASTKVEVDMEKAESLQVTRGDFL","XXXXXXXXXXXXMGFVKVVKNKAYFKRYQV","GKTDYYARKRLVIQDKNKYNTPKYRMIVRV","GMDKIYEGQVEVTGDEYNVESIDGQPGAFT","GLIHRKTVGVEPAADGKGVVVVIKRRSGQR","LGIGKAIAVLTSGGDAQGMNAAVRAVVRVG","VSNNVPGSDFSIGADTALNTICTTCDRIKQ","CGYLATMAGLAAGADAAYIFEEPFTIRDLQ","LSLFTSLGLSEQKARETLKNSALSAQLREA","DMQVLHLLGPKLEADLEKKFKVAKARLEET","KQLRKPRHQEGEIFDTEKEKYEITEQRKAD","PRKPSAPKKGDSSAEELKLATQLTGPVMPI","NLLPQPPRSKYIHPDDELVLEDELQRIKLK","ENEEEEIGNLELAWDMLDLAKIIFKRQETK","GSGDAVPSGNEVSENMEEEAENQAESRAAV","KEGVIKIRSVASREEVAIKRENFVAEIQKR","EPEPDYEPEPETEPDYEDVGELDRQDEDAE","XXMSGFDDPGIFYSDSFGGDPGAEEGQARK","GDGMERDYRAIPELDAYEAEGLALDDEDVE","IPELDAYEAEGLALDDEDVEELTASQREAA","QLSKEKADFTVVAGDEGSSTTGGSSEENKG","RNVTTKNYSEVIEVDESDVEEDIFPTTSKT","HEKAEFEVHEVYAVDVLVSSGEGKAKDAGQ","PNGPMRITSGPFEPDLYKSEMEVQDAELKA","SAPKTDMDNQIVVSDYAQMDRVLREERAYI","PRGTLRRDFNHINVELSLLGKKKKRLRVDK","PTLPVEEKKKIPDPDSDDVSEVDARHIIEN","HLTVKKIFVGGIKEDTEEYNLRDYFEKYGK","KARKTGLITPKAGFDSDYDQALADIRENEQ","DSVVMAEAPPGVETDLIDVGFTDDVKKGGP","IQLSMQGSSRNISQDMTQTSGTNLTSEELR","KQEGSSDDASSGVGDSDSEDLGTFGKGAPK","EPPGELADISDVEGEVGAIGEEAPQMNYIQ","MADIDNKEQSELDQDLDDVEEVEEEETGEE","MQNPQILAALQERLDGLVETPTGYIESLPR","QACRGTELDDGIQADSGPINDTDANPRYKI","ASDNQPEGMISESLDNLESMMPNKVRKIGE","GNRSSHSRLGRIEADSESQEDIIRNIARHL","FGRTTTPQQTKITQDIFQQLLKRGFVLQDT","EDEEDDSEDAINEFDFLGSGEDGEGSPDPR","RPRHQGVMVGMGQKDSYVGDEAQSKRGILT","XXXXMDDDIAALVVDNGSGMCKAGFAGDDA","IVRDIKEKLCYVALDFEQEMATAASSSSLE","AASSSSLEKSYELPDGQVITIGNERFRCPE","SDGLKGRVFEVSLADLQNDEVAFRKFKLIT","LRQKYNVRSMPIRKDDEVQVVRGHYKGQQI","NTETNGEFGKRPAEDMEEEQAFKRSRNTDE","GYDYSYAGGRGSYGDLGGPIITTQVTIPKD","FESGISQALLELEMNSDLKAQLRELNITAA","KQKRPRSRTLTAVHDAILEDLVFPSEIVGK","RSRTLTAVHDAILEDLVFPSEIVGKRIRVK","NGTIDFPEFLTMMARKMKDTDSEEEIREAF","ALPLGRKKGAKLTPEEEEILNKKRSKKIQK","XXXXXXMVRMNVLADALKSINNAEKRGKRQ","ASHAKGIVLEKVGVEAKQPNSAIRKCVRVQ","PYRRSVPTWLKLTSDDVKEQIYKLAKKGLT","QPTIFQNKKRVLLGETGKEKLPRYYKNIGL","YDTKGRFAVHRITPEEAKYKLCKVRKIFVG","IDLETGKITDFIKFDTGNLCMVTGGANLGR","XXMCEEEDSTALVCDNGSGLCKAGFAGDDA","ERKRKSVRGCIVDANLSVLNLVIVKKGEKD","IKGRLNRLPAAGVGDMVMATVKKGKPELRK","RKFTYRGVDLDQLLDMSYEQLMQLYSARQR","YRGVDLDQLLDMSYEQLMQLYSARQRRRLN","KRTFRKFTYRGVDLDQLLDMSYEQLMQLYS","RCVPKDKAIKKFVIRNIVEAAAVRDISEAS","XXXXXXXXXXXMAQDQGEKENPMRELRIRK","YARRPQYSNPPVQGEVMEGADNQGAGEQGR","RFRRGPPRQRQPREDGNEEDKENQGDETQG","PREDGNEEDKENQGDETQGQQPPQRRYRRN","XXXXXXXXXXXMQNDAGEFVDLYVPRKCSA","XXXXXXXMADDLDFETGDAGASATFPMQCS","XXXXMADDLDFETGDAGASATFPMQCSALR","XXXXMEEEIAALVIDNGSGMCKAGFAGDDA","XXXMCEEETTALVCDNGSGLCKAGFAGDDA","RLGNDFHTNKRVCEEIAIIPSKKLRNKIAG","YGRRPQYSNPPVQGEVMEGADNQGAGEQGR","TGAGKHVPRAVFVDLEPTVIDEVRTGTYRQ","THTTLEHSDCAFMVDNEAIYDICRRNLDIE","TTLEHSDCAFMVDNEAIYDICRRNLDIERP","VGINYQPPTVVPGGDLAKVQRAVCMLSNTT","ETGAGKHVPRAVFVDLEPTVIDEIRNGPYR","DEIRNGPYRQLFHPEQLITGKEDAANNYAR","ACISIGNQNFEVKADDLEPIVELGRGAYGV","QALQAGELESQAAPDEAQGDPELGAVELRI","GVQMQAIPEDAVHEDSGDEDGEDPDKRISI","TQTATQQAQLAAAAEIDEEPVSKAKQSRSE","PKSENRDKKEKPKPDAYDMMVRELGFEMKA","KREKEIKSELKMKQDAQVVLYRSYRHGDLP","EEKKDKEKKEIKKERKELKKDEGRKEEKKD","SPARPPSPRLDVSSDSFDPLLALYAPRLPP","XXXXXXXXXMKVELCSFSGYKIYPGHGRRY","QSLSNSFNSSYMSSDNESDIEDEDLRLELR","SVLRCGKKKVWLDPNETNEIANANSRQQIR","AGAGSSGGSSGVSGDSAVAGAAPALVAAAA","GDGMERDYRPIPELDVYEAEGLALDDEDVE","KDEIQDEEDDDDYVDEGEEEEEEEEEGLRG","IQDEEDDDDYVDEGEEEEEEEEEGLRGEKR","EGGGGGGRPGAPAGDGKTEQKGGDKKRGVK","VSNNVPGSDFSIGADTALNTITDTCDRIKQ","NVPGSDFSIGADTALNTITDTCDRIKQSAS","CGYLANMGGLAAGADAAYIFEEPFDIRDLQ","KLKEARGRGKKFTTDDSICVLGISKRNVIF","PLDPTRLQGINCGPDFTPSFANLGRTTLST","GFSSPPRIKDEPEDDGYFAPPKEDIKPLKR","FFAIKALKKDVVLMDDDVECTMVEKRVLSL","YSAIKALKKDVVLIDDDVECTMVEKRVLTL","APDPALVPPQEIAPDASFIDDEAFKRLQGK","EVHKQQDMDLDIEFDVHLGIAHTRWATHGE","ASPKYEFWGPLYIQETGEEEVFTQDPRKRQ","ASEEGADRNGGPGEDAAEGQTKTAAGKRKR","EDDDGEIDDGEIDDDDLEEGEVKDPSDRKV","EEELRQIKINEVQTDVGVDTKHQTLQGVAF","AALEKLFPDTPLALDANKKKRAPVPVRGGP","PKRKRKNSRVTFSEDDEIINPEDVDPSVGR","DFPIPLPEDDSIEADILAITGPEDQPGSLE","LTDDPDTEEALKEFDFLVTAEDGEGAGEAR","GGGHGSGSPSQPDADSHFEQLMVSMLEERD","IMKELNKRGRVLVNDAQKVTEGQQERLERQ","QRMRVSSGERWIKGDKSELNEIKENQRSPV","FFKWQTKPKLTIHGDLYYEGKEFETRLKEK","AAGDPKKEKKSLDSDESEDEEDDYQQKRKG","PDNFIFGQSGAGNNWAKGHYTEGAELVDSV","NWAKGHYTEGAELVDSVLDVVRKECENCDC","QLVENTDETYCIDNEALYDICFRTLKLATP","PEMEDANSEKSINEENGEVSEDQSQNKHSR","HSDDYIKFLRSIRPDNMSEYSKQMQRFNVG","QQLVASHLEKLLGPDAAINLTDPDGALAKR","GRKKKMSNALAIQVDSEGKIKYDAIARQGQ","TQATTQQAQLAAAAEIDEEPVSKAKQSRSE","ATTQQAQLAAAAEIDEEPVSKAKQSRSEKK","NWAKGHYTEGAELVDSVLDVVRKESESCDC","QLVENTDETYSIDNEALYDICFRTLKLTTP","ILQQARQQQEELEAEHGTGDKPAAPRERTT","EGGSAESEGAKIDASKNEEDEGHSNSSPRH","EFQDVIPPDDFLTSDEEVDSVLFGSLRGHV","VRTMLDEDDVKVAVDVLKELEALMPSAAGQ","DLPISRENLTDLVVDTDTLGESTQPQREGA","LPDGQEYQRIEFGVDEVIEPSDTLPRTPSY","KRSSSEDDDLDVESDFDDASINSYSVSDGS","PEIRKRYDQDLCYTDILFTEQERGVGIKST","AAATNHTTDNGVGPEEESVDPNQYYKIRSQ","MVQDDDAAQGVISASASNLDDFYPAEEDTK","RALQADQLENQAAPDDTQGSPDLGAVELRI","QDPEQEEVELAPVGDGDVVADIQDRIQALG","SESPSASVAEATTTDVQVTPTALLQLDREQ","SNPEARLNEKNPDVDAICEMPSLSRKNDLI","EGDDVQTAAEEVLADGDTLDFEDDTVQSSG","DDVQTAAEEVLADGDTLDFEDDTVQSSGPR","ADRCTLPEHESPSQDISDACEAESTERCEM","EREMFDRASLKLGLDKAVLQSMSGRESNVG","IVDKSGVVRVRVEGDNDKKNPREEGMVPFI","TYQPESSEEAEAEDDVGGEEEGDPERSVTD","SILKSRSRENSVCSDTSESSAADVEDRRGL","AVQRARQIAAKIGGDAATTVNNNTPDFGFG","DEDKPDETGFITPADSGLITPGGFSSVPAG","ASGDPKKEKKSLDSDESEDEDDDYQQKRKG","PPPVSSSDSEAPEADLGCGSDVDKDKESRR","EDGNYSAVGFTYGSDYYDPSEPTEEEEPSK","DLETGDVHKGKPGPDTQSEDIEEEEVKEET","TCRDANEELNCQDPDVGDMEEEERLQVTDK","PCPSPSPRRTSPAPDILSLPEDPCLGPRNE","EAEQLEPRVSALEEDMDDVESSEEEEEEDE","QEEPAAAPEPRKETESEAEDDNLDDLERHL","EPAAAPEPRKETESEAEDDNLDDLERHLRE","GSFGKVYKGRCRNKIVAIKRYRANTYCSKS","SSIQAAVRQLEAEADAIIQMSESSQSEASV","RCVPTDKAIKKFVIRNIVEAAAVRDISEVS","SSPGEESEVGSMVGEGFIEVLTKKQRRLLE","KPAAERCLEQLVFGDVEDDEDALLQRLRSS","KDIFSQKKKQPVWVDEDDEDEEIVDMSNNR","IFSQKKKQPVWVDEDDEDEEIVDMSNNRFR","PDWAEAGSKRRTSSDDESEEDEDDLLQRTG","NLNHYGMTHVASVSDVLLDNAFTPPCQRMG","LRTSPADHGGSVGSESGGSAVDSVAGEHSV","QEDQTSDKNEGISEDMGDEAKEERQESRAS","EYAKIHDLLKTIGLDIGVAEISQLAARTQE","KSLASKGVPNVISEDTLKGQDSLSTDTGQS","GRPTEDEDDKDKVADEDDVDNEEAALLHEE","DEDDVDNEEAALLHEEATMTIEELLTRYGQ","PVSNSRARKRIIEPDDFLDDLDDEDYEEDT","EPSAPDCRHLKVEADAEKPGPADRKGGLGM","RNVAPKNYSETIEVDDSDEDDIFPTNSRAD","LRVAPYRPDQQVEEDDDFMDENQGKGIRVL","FLDNGPKTINQIRQDAVKDLGVFIPAPMAQ","GSCSSSSRLPAPQEDTASEAGTPQGEVQTR","YNKAVECCDKALGLDSANEKGLYRRGEAQL","AAAAAAGDSDSWDADTFSMEDPVRKVAGGG","RLMPNSQKDCHCLGDVLIENTKESKSQSED","RTNPEYENEVEASMDMDLLESSNISEGEIE","TKLEETIAEETPSQDEEEGVSDVESERSQE","NPETKPARPDPIDMDEDELEMLSEARARLA","GLPAPKNDFEIVLPENAEKELEEREIDDTY","VFKAPAPRPSLLGLDLLASLKRREREEKDD","GYCKLKVFNPRIGMDALQIYPISQANANQR","ANDMEDDSMDLIDSDELLDPEDLKKPDPAS","SGAGSEDMSTTVNGDVFQEANGPADGYAAI","STVNEKPKYAEISSDEDNDSDEAFESSRKR","IKLPSSVFASEFEEDVGLLNKAAPVSGPRL","ERWLSASVNDDVDTDEESAQLRGSKRIGDG","DRDYMDTLPPTVGDDVGKGAAPNVVYTYTG","IPPPYDKKKRMVVPAALKVVRLKPTRKFAL","RAAALDAGNIKLTFNSLLEKAEAREREREK","QLDPSRLPGINCGPDFTPSFANLGRPALSN","DSPQTRTASGAVDEDALTLEELEEQQRRIW","KAKIMAKKRSTIKTDLDDDITALKQRSFVD","DLNGLLVPDPLCSGDSTSANKTGLRTMPPI","GPGNPVPGPLAPLPDYMSEEKLQEKARKWQ","KKAKGGNVFEALIQDDSEEEEEEEENRVLK","KAKGGNVFEALIQDDSEEEEEEEENRVLKP","EEEEEEEEPVQSKGDSIEEILADSEDEDEE","XXMALFHIARYAGPEAAGQGDTDAEAGSRA","PKIPKSKRQKKELGDSSGEGPEFVEEEEEV","EDRDWQDDQSDNQSDYSVASEEGDEDFDER","FRKTLEKEAKGEEPDIAVPKFKQRKGESDG","QQLMSKDQDEQEELDFLFDEEMEQMDGRKN","VIPVTSTTNKITAEDCTMEVTPGAEIQDGR","PDLVDDGEDESAEHDEYIDGDEKNLMRERI","TNKKEGEKTDRVGVEASEETPQTSSSSARP","FWEPVESTQEVVCTDNATEKEEEADGGGQA","AAPAAAAPPPAPATDSASGPSSDSGPEAGS","LKEGGDDGLHSAEQDADDEAADDTDDTSSV","LSDPSDIIRVTVGGDAAAAAVAAAAAAAAT","DREDGELEEGELEDDGAEEVQDPPGGQERS","QAEEHGLARKKPAPDAQAESGPGDGGGEPD","MDGRKNTFTAWSEEDSDYEIDDRDVNKILI","EVPPGPPRFQQVPTDALANKLFGAPEPSTI","DLRGKKKEELLKQLDDLKVELSQLRVAKVT","XXXXXXMAGGKAGKDSGKAKAKAVSRSQRA","ATEAPARPLNCVEAEAAVGAAAEDSCDARG","DDRLEDDDFDLIEENLGVKVKRGQKYRRVK","SASLAKKKPLFITTDSSKLVSGVLGSALTS","EVLSSQEMQVENELEDLIDELLERDGGSGN","SSIQAAVRQLEAEADAIIQMVREGQRARRQ","XXXMSAQAQMRAMLDQLMGTSRDGDTTRQR","SSPGEENEASSVVGEGFIEVLTKKQRRLLE","EEEEELGDEAMMALDQNLASLFKEQKMRIQ","SEGTTPEKNAASQQDAVTEGAMPAATGKDQ","PLGSDSEGVNCLAYDEAIMAQQDRIQQEIA","VGPKRKEEAIIIVEDEDEDDKESVRRRQRR","APGESALDDCAPSGDSQSDEPPSSEDSLPR","KSKSPPKVPIVIQDDSLPAGPPPQIRILKR","FSGSSTFSPTILSSDKETIEIIDLAKKDLE","KPGVMTQEVGQLLQDMGDDVYQQYRSLTRQ","TQSGFSINSQVFAADGASTETSASGTSQGE","AGQPEVLSSQEMQVENELEDLIDELLERDG","PLILQRLLGPSAAADILQLSSSLPLQSRGR","SEPLLPLDEEAVDMDYGDMAVDVVDGEQES","DEEAVDMDYGDMAVDVVDGEQESSRDMEIS","KTLRRSYSPSMLDYDTENLNSEEIYSSLRG","HQDRQSQTKSMILTDAGKVTEPISRHRRNH","RKREEAAAVPAAAPDDLALLKNLRSEEQKK","NSAKEYRGTLRVTFDAEAMNKESPMVRSAR","SSSAHSVDSEDMYADLASPVSSASSRSPAP","SHSDMKSERRPPSPDVIVLSDNEQPSSPRV","AAASSSLERSYELPDGQVITIGNERFRCPE","QGAPAPGPSANVASDAMSILETITSLNQEA","PSCGGTTKKHSLEGDEGSDFITKNRNLVSS","GLAPAGGADNLINEESDVDVQLNNRHMMIR","RTDGKVFQFLNAKCESAFLSKRNPRQINWT","EPEEYVEEPAGVMPEEAADLDAEAMPESLR","EKKNERLRAAFGISDSYVDGSSFDPQRRAR","GTPPRPGSVTNMQADECTATPQRQSHSESS","KEAGIVTEPLGEMVDSSGASEEAAVDPGSV","LQQSVVKTYEDMTLEELEENEDEFSEEDER","KKGRSKGDKPEAETDSVQMANEELRAKLTN","EREMFDRASLKLGLDKAVLQSMSGRDSNVS","XXXXMALVGSRVELDADEDIFEDALETISR","XXXXXXMDSEYYSGDQSDDGGATPVQDERD","SESDSEDPPRPQASDSENEEPPKPRISDSE","DSEEEEPKRQKIDSDDDEEKEGDEEKVAKR","LEEEKNETQLKEAEDSDSDDNIKRGKHMDF","RSKKRTKRSSVVFADEKAATESDLKRLSRK","IAAPDVKLNLGVSGDFIKESTATTFLRQRG","RRQSSGDGVSCAASDYLVGQVADSLRGGPR","QAIRIIQERNGVLPDCLTDGADVVSDLEQE","PETIPDMEDSPPVSDSEEQQESVRAAPEKS","SHSDMKSEKRPPSPDVIVLSDSEQPSSPRV","QQSRKGWSGPDVIIEAQFDDSDSEDRHGNT","RKKKAKKRDSETIEDVAVEPLPLPGSPVRG","PRDLTLDALLEMNEAKVKETLRRCGASGDE","XXXXXXXXXXXMLTDSGGGGTSFEEDLDSV","GGSGSGTGGGDAALDFKLAAAVLRTGGGGG","KVLETPQEIRTVSSEAVSLLEEVITPRKDL","NAPFDPHSMDSHSGDIAVIEEVRLDNPKEG","SSMVESEVMKSLGKDGGLDDDEEEEDLDEG","PDTDFDFEGNLALFDKAAVFEEIDTYERRS","XXXXXXXXMEEVVAEELDDEEQLVRRHRKE","FIEMATTEDAQAAVDYYTTTPALVFGKPVR","PQAAEGHYTVSYGTDSGEFSSVGEEFYRNH","ENEFQIPGRTRPSSDQLKEASGTDVKQLDQ","QEAQGETEPTEQAPDALEQAADTSRRNAET","IKRGKRKRLSSVMCDSDESDDSDILVRKVG","ELKEHVLTQCKEAKNYEKTLQELITRRAIL","FVLHKSKSEEAHAEDSVMDHHFRKPANDIT","AEIRYEGILYTIDTENSTVALAKVRSFGTE","QPSMQELSKLQDMYDELMMIIGSRRSGLNQ","RPSSPRFSSLDLEEDSEVFKMLQENRQGRA","MESSEPEPTEDASMDAFLEKFQSQPYRGGF","EGVRVNNRFELINTEDLEDDLVVNGERSDC","EAGEEDGGSDGMGGDGSEFLQRDFSETYER","SHRATANTEEIIEGNSVDSSIQQVMGSGGQ","ATANTEEIIEGNSVDSSIQQVMGSGGQRVI","YNTKGCFAVHRITVEEAKYKLCKVRKITVG","TSDPAGGGESAASPEELEDEDAEGGGAARR","EPTREQLDSGRIGADEEIDDFKGRSAEEVE","HHISSCDDRSCIEQDVVNQTRSLRQETLAE","NSTSLKEKEHNKEPDSSVSKEVDDKDAPRT","FLDAVMLEREGMGMDQGPQEMIDRYLSLGE","RRSEPERGRLTPSPDIIVLSDNEASSPRSS","ENIQSSPPRLTKPQSVAIKRKRPFVLKKKR","LKEEEIHIYQFPECDSDEDEDFKRQDAEMK","KRLLKEEDMTKVEFETSEEVDVTPTFDTMG","AGPAKPSLALNPQEDSQFEKALTQIQGRTK","AVAHRDSEGNQVLPDQKEGLSGEPRRKEKM","LKHLEPEPEEEIIAEDYDDDPVDYEATRIE","KHLEPEPEEEIIAEDYDDDPVDYEATRIEG","XXXXXXMSAQSVEEDSILIIPNPDEEEKIL","IEEHLNCTISQVEPDIKVPVDEFDGKVTYG","KSKGGGKMNEEISSDSESESLAPRKTEEEE","AELSRKELGLEEGVDNLKALIQSRQKDRQK","ENPLKRGLVAAYSGDSDNEEELVERLESEE","SLAGIKRARARADGDSDAEDPGAPPEAVGA","MEELKVSFSKGIGTDSAEFAELKTQIERLR","KWHVINQRSILCGEDEIAEDNPESQEMLEE","XXXXXXXXXXXMAADTQVSETLKRFAGKVT","DDDEVSEEEEEFGLDEEDEDEDEDEEEEEG","EVSEEEEEFGLDEEDEDEDEDEEEEEGGKG","SEEEEEFGLDEEDEDEDEDEEEEEGGKGEK","EEEEEFGLDEEDEDEDEDEEEEEGGKGEKR","EEEFGLDEEDEDEDEDEEEEEGGKGEKRKR","EEFGLDEEDEDEDEDEEEEEGGKGEKRKRE","AQDPALVPPQEIAPDASFIDDEAFKRLQGK","HSDEYIKFLRSIRPDNMSEYSKQMQRFNVG","DTDQTLKKEGLISQDGSSLEALLRTDPLEK","KDSENTPVKGGTVADLDEQDEETVTAGGKE","AVQRARQIAAKIGGDAATTVNNSTPDFGFG","LLLTPREPALPLEPDSGGNTSPGVTANGEA","LLVQAYQQCFSDKERQNLLADIQVRRGEGV","TLTQFDSNIAPADPDTAIVHPVPIRMTPSK","AALNAQHKQGKVGPDGKELIPQESPRVGGF","ELCVDAAGHFPIGPDVEDLVKEAVSQVRAE","ANEGKKETWDTAEEDSGTDSEYDESGKSRG","AKVDEFPLGGHMVSDEYEQLSSEALEAARI","RATPSKPSGLSLLPDYEDEEEEEEEEEGDG","PTPTFHSSRTSLAGDTSNSSSPASTGAKTN","QVAPESRNRIRVRQDLASLPAELINQIGNR","QMLQGKPLADTMRPDTLQDYFGQSKAVGQD","XXXXXXXMAAAAECDVVMAATEPELLDDQE","YQKGFSDKLDFLEGDQKPLAQRKKAGAKGQ","LYNKFKNMFLVGEGDSVITQVLNKSLAEQR","QQLVASHLEKLLGPDAAINLADPDGALAKR","TKAFRKAAGKELTVDNSFEFEKRRNEPVKY","DRENMDTDDERQYQDFLEDLEEDEAIRKHV","EEKIEEANGRETTGDGEVDLLTKELEDFEM","EEANGRETTGDGEVDLLTKELEDFEMKKAA","KEEELRTAAGEYDSDSESEDEEMMEIRQLA","XXXXXXMPGKLLWGDIMELEAPLEESESQR","XXXXXXXXXXMEELDALLEELERCTFQDSE","EEEVSEVESFILDQDDLENPMLETASKLLL","NSQDVQGYITNQSPESIVEEAQGKLTELEQ","TQGRTCGSGRDLSGDSGTAEDLSLLSAKPQ","ENEEELGDEAMMALDQSLASLFAEQKLRIQ","KRKKRKSEDGTPAEDGTPAATGGSQPPSMG","KLSLIGKGKRRRSSDEDAAGEPKAKRPKYT","HYAMDNITRQNQFYDTQVIKQENESGYERR","KKLYCQELKKVIEASDVVLEVLDARDPLGC","NKIMHPVSGKHILQFVAIKRKDCGEWAIPG","MGCTWGMGEDAVEDDAEENPIVLEFQQERE","AQIRYEGILYTIDTDNSTVALAKVRSFGTE","ARPAKRPVFPPLSGDQLLTGKEETRKIPVP","PSSTPVLYTQNVEPDSGEAELDSRDLEMSQ","GKPECRVGQYVVNLDSFEQLALPVLRNAGS","LQTFLAQEDTEQSPDALASEDASRQKATET","PSPQTVPAAPAVDPDLYTASQGDIRLTPED","XXXXXXMTKVKAAPEESEAQAEGCSEERTY","LPPEDDDQWLDLSPDQLDQLLQDAAGRKES","MAQSIYRPSKNLDKDMYGDDLEARIKTNRF","AADVSVTHRPPLSPEAEAEAETPETVDRRA","PGHSKGELAPELGEDTQSLSQEETELELLR","GFGSEEGSRARMREDYDSVEQDGDEPGPQR","DSVVMAEAPVGVETDLIDVGFTDDVKKGGP","XXXMDPTCEESPAEDSNNEEEDLDSTKAAP","IRRVGRRPDQQLQGDGKLIDRRAERRPPRE","DSPQARPAASAMDEDALTLEELEEQQRQIW","RGSGKKRKFQPLFGDFAAEKKNQLELLRVM","DKKREWEMLCRVKPDVVKDKEAERNLQRIA","LKEVFSMAGVVVRADILEDKDGKSRGIGIV","NRAEAAEEDETVEVDVSDEEMARRYETLVG","VPKEEQNDPNTVKVDSKKMPRDITNTRDQR","LNEEKLEKLEEITMDGAKAKAILDASRSSM","AKAILDASRSSMGMDISAIDLINIESFSSR","RKNLDVMKEAVVQAEEAAAEITRKLEKQEK","XXXXXXMEGQRVEVDGGIMEGGGQILRVST","GSERADGRIVKMEVDYSATVDQRLPECEKL","PGGRGGGASTRVEEDSDSETYGEENDEQGN","ASQSLEEEKYKLLAEAAVELQEENTRQERI","AASLSQQTPSLPGSDSEEEEEVGRKKRHLQ","SLWFSKDGFSGIEDDADEALEISQAQLLYK","FPLYEEPDAKLARADSDGDLSENDDGAGDL","LYEEPDAKLARADSDGDLSENDDGAGDLRS","SPAKLNQSGASVGTDEESDVTQEEERDGQY","TLKLPPTFFCGVCSDTDEDNGNGEDFQSEL","GEEEEFGHDGEVDEDEEDEDEDEDEEEEES","EEFGHDGEVDEDEEDEDEDEDEEEEESGKG","YIDYSDSGKNTLVTEAVQAPQLAAEAVNVI","TLVTEAVQAPQLAAEAVNVIKRKRDSESES","EHLIGKKLPGFPTQDDEVMMLTERVAEAQR","RLLRCQRPAKDLPAAGSEMQIRPIVMSQKD","QLIENADACFCIDNEALYDICFRTLKLTTP","DDSYVSDISDNLSLDNLSNDLDNERQTLGP","KMIENEKLTMELTGDSMEVKPIMTRKLRRR","GAVLQEQNQDAVATELGILGMEGTIDRSRQ","NVTDVSLGISSNEQDQGSDKGENEAMESSG","GKQTMGFPLLTYQDRSDNKLYRLQTPQSPL","TLQENVNKYQQVKTDTSQESKAQANCRVGV","KWQVINQRSLLCGEDEAADENPESQEMLEE","EQPFNDSGCLLVDKDLFETGLEDENNSPLE","EREMFDKASLKLGLDKAVLQSMSGRDGNIT","AQTMIEHNSTMLESDLGTMVINSEEEEEEE","XMPGKLRSGAKLGSDGAEESMETLPKPSEK","PKPKKMKKGKEASGDAGEKSPRLKDGLSQP","GAVLQEQNQDAVATELGILGMEGSIDRSRQ","DEVRAGTYRQLFHPEQLITGKEDAANNYAR","SSAESSAGEDEWEYDEEEEKNQLEIERLEE","SQDNDQPDYDSVASDEDTDVETRASKANRQ","VVDPETGRTRLIKGDGEVLEEIVTKERHRE","MVTKYQKKKNKLEGDSDVDSELEDRVDGVK","MIAQAGWEMFRAGHRTPLSDSGVTQRYRTD","APVPTAPAAGAPLMDFGNDFVPPAPRGPLP","PGGWGAGASAPVEDDSDAETYGEENDEQGN","XXXXXXXMPGKLRSDAGLESDTAMKKGETL","CADHSRLVESKVELNALMTDETISNVPILI","ASRSTRHSHGPLQADVFVELLSPRRKRRGR","EDEEDDSEDAINEFDFLGSGEDGEGAPDPR","EPPSGRTESPATAAETASEELDNRSLEEIL","PTNNTPKGILHLSPDVYQEMEASRHKVISG","EELAQSDTSTEEQEDKAVQVSNKKKRKLPS","SDTFAPSPDKTIDCDVITLMGTPSGTAEPY","ETEDYRQFRKSVLADQGKSFATASHRNTEE","SGSSPKWTHDKYQGDGIVEDEEETMENNEE","LLQKWASMWSSMSEDASVADMERAQLEEEA","EMLSNQMLSGIPEVDLGIDAKIKNIISTED","HYATKKSVAESMLDVALFMSNAMRLKAVLE","KVMMYRKEQYLDVLHALKVVRFVSDATPQA","XXXXXXXMAATAECDVVMAATEPELLEDED","ATAECDVVMAATEPELLEDEDAKREAESFK","DLLPSRPEVPNVIGETEEVELQEFDSTRGS","EAGEPTPGLSDTSPDEGLIEDFPVDDRAVE","SNSELTPSESLVTTDDETFEKNFERETHKV","SEVYDAKGPKNVRSDVSDQEEDEESERCPV","GTSSHSADTPEASLDSGEGPSGMASQGCPS","TAVVQAALCKPRQERVAIKRINLEKCQTSM","FKPNQYTKIDTIAADESFTQMDLGDRILKL","AEKNEANGRETTEVDLLTKELEDFEMKKAA","RNEKAPVDFGYVGIDSILEQMRRKAMKQGF","GPQLLEELDNAPGSDAAELELAPARTPPDS","KKPPVKPPTEPIGFDLEIEKRIQERESKKK","RINQRLDMVPHLETDMMQGGVYGSGGERYD","SDGDDFEDATEFGVDDGEVFGMASSALRKS","XXXXXXXXXXXXMEDSSLPVVPAPIAAPGP","DIYHCPNCEKTHGKSTLKKKRTWHKHGPGP","SGDGKAIGVLTSGGDAQGMNAAVRAVVRMG","AVRLPLMECVQMTQDVQKAMDERRFKEAVK","TAYVGNLPFNTVQGDIDAIFKDLSIRSVRL","ELQRQLAEEEPMDTDQGSTVLSAIQSEVAR","SLPDTLEEASETEEDGGEDSDALPRGLRSK","KDEDRKELKDLFELDSSEGEDSTDFFERGV","SVLKDPDSNPYSLLDTSEPEPPVDSEPGEP","PFKPYNFLAHGVLPDSGHLHPLLKVRSQFR","PSSVVLHDEGQNEEDVEKEEETERMLKTAV","SFSITREAQVNVRMDSFDEDLARPSGLLAQ","SRMLAAKTVLAIRYDAFGEDSSSAMGVENR","YEHKSEVKTYDPSGDSTLPTCSKKRKIEQV","SDLDDQHDYDSVASDEDTDQEPLRSTGATR","DKRLEWEMMCRVKPDVVQDKETERNLQRIA","STQDSGLQESEVSAENILTVAKDPRYARYL","EPLLDPVGPEPLGPESQSGKGDMVEMATRF","SALQPEAGVCSLALPSDLQLDRRGAEGPEA","LYGQSVEDDYCISPSTAAQFIYSRRDKPSV","KPAVERCLEELVFGDVENDEDALLRRLRGP","PPASDLPTPQAIEPQAIVQQVPAPSRMQMP","KDKGKLEEKKPPEADMNIFEDIGDYVPSTT","MESEAGADDSAEEGDLLDDDDNEDRGDDQL","LERDDGSTMMEIDGDKGKQGGPTYYIDTNA","SAGYGGYEEYSGLSDGYGFTTDLFGRDLSY"};
            params.addElement(new Parameter("lExperimentalSet", String[].class, lHumanGranzymeBSubstrates, null));
            params.addElement(new Parameter("lStartPosition", Integer.class, new Integer(0), null));
            params.addElement(new Parameter("lHeight", Integer.class, new Integer(600), null));
            params.addElement(new Parameter("lWidth", Integer.class, new Integer(800), null));
            call.setParams(params);

            // Invoke the call ...
            Response lIceLogoResponse;
            try{
                // ... and get the respons
                lIceLogoResponse = call.invoke(lIcelogoUrl, "");
            } catch (SOAPException e) {
                // ... or print an error if something went wrong
                System.err.println("Caught SOAPException (" + e.getFaultCode() + "): " + e.getMessage());
                return;
            }

            // Check the response.
            if (!lIceLogoResponse.generatedFault()){
                //get the result and the the value for this result
                Parameter ret = lIceLogoResponse.getReturnValue();
                String lSVGResult =  (String) ret.getValue();

                //create the file path to the temporary folder
                String lLocation = File.createTempFile("temp", "temp").getParentFile().getAbsolutePath();
                //create the filename
                String lFileName = "FilledLogoExample";
                //create the saver
                SvgImageConverterAndSaver lSaver = new SvgImageConverterAndSaver(lSVGResult, ImageType.JPEG, lLocation, lFileName);
                //and do the saving
                boolean lSaved = lSaver.save();
                if(lSaved){
                    System.out.println("Saved the file \"" + lFileName + "\" in \"" + lLocation + "\".");
                } else {
                    System.out.println("An error occured while saving the file!");
                }
            } else {
                //Alert the user if something went wrong
                Fault fault = lIceLogoResponse.getFault();
                System.err.println("Generated fault: ");
                System.out.println ("  Fault Code   = " + fault.getFaultCode());
                System.out.println ("  Fault String = " + fault.getFaultString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        FilledLogoExample lExampleStatic = new FilledLogoExample();
    }

}