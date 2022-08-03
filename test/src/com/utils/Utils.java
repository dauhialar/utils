package com.utils;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author Ruslan Dauhiala
 */
public class Utils
{
	public static void main(String args[])
	{
		//new Utils().fibTracer();
		new Utils().getCoordinates();

	}

	private void getCoordinates()
	{
		int[] aIdArray = new int[]{4142055, 5854032, 1708780, 3505098, 3103764, 7544757, 3817606, 5761789, 6835280, 8788143, 9729845, 5749842, 9866627, 64540, 1891882, 8087817, 82936, 8196192, 10454554, 10089839, 268676, 8760644, 6848753, 11132982, 4821001, 1938734, 8391435, 4697950, 8208839, 10468564, 4999338, 667255, 9451271, 7163723, 8890208, 836495, 705870, 5934475, 9862681, 8279510, 2300152, 5027196, 1184852, 10630483, 4432027, 713093, 5545786, 5791818, 2029627, 9530864, 4739089, 3144598, 2911053, 5339472, 1688586, 10847542, 11055857, 9866975, 9267019, 5341245, 6367303, 6223835, 3040384, 4067872, 6381839, 1975336, 6123053, 1423364, 6175655, 9771367, 2184120, 7732733, 1414277, 2785330, 9213853, 6910659, 8140406, 9777961, 4144150, 6504075, 3020863, 9727840, 7305212, 9264900, 3465605, 10655217, 1882447, 1437533, 9071553, 6906373, 2037756, 10597396, 7565820, 2876174, 5666177, 10003446, 6490149, 3066240, 6490497, 5809124, 6554084, 7588200, 2183064, 6681673, 5789463, 9452375, 6235189, 4484496, 4429176, 856312, 2602353, 10166170, 3194835, 1902782, 9631577, 3584064, 2297811, 2436273, 8130150, 1155027, 2992724, 7005878, 10361744, 8336981, 10403541, 5313739, 1219461, 4288692, 7299394, 7489580, 983654, 8276822, 11028704, 8612272, 7576147, 2847348, 4461254, 1162574, 5551648, 2976946, 9257486, 10593695, 11213077, 7853514, 6929270, 8803320, 4078792, 9495048, 9790835, 5194535, 6767994, 3041232, 831012, 11173027, 5917672, 8451782, 8680768, 1394440, 2556237, 3684981, 1911826, 4911713, 5723159, 5325090, 4385828, 2787942, 10289506, 6974679, 4169355, 7119031, 6627069, 2648229, 1678364, 4605443, 8781925, 1632581, 5297260, 9120440, 10558548, 10668989, 4900363, 3659565, 4788396, 4966439, 5437920, 408128, 9920641, 668028, 2639701, 9706820, 953564, 683946, 9460830, 5877376, 7224333, 503454, 411242, 581796, 10830068, 1491013, 862486, 4521844, 6353940, 7614209, 4943975, 6498534, 10868095, 4868971, 6371584, 8391260, 5869633, 316183, 662795, 6275412, 3140972, 9974868, 3165382, 6895879, 8119287, 6235750, 7767103, 870921, 1072769, 7441760, 2725857, 8522240, 4280010, 6259365, 7423184, 9626011, 3235711, 10899781, 6718595, 10062215, 7260190, 8081619, 9862236, 4164022, 7118152, 11008765, 5539080, 1135839, 64967, 10049828, 3753606, 10494229, 9196195, 3035653, 5996371, 3312467, 7153624, 115319, 1150459, 4408141, 9531723, 8748016, 194781, 1235317, 5676830, 1285145, 3504148, 8477917, 10416540, 2438979, 4223335, 5248922, 1897525, 277499, 6925421, 7333314, 9905849, 2493119, 5947865, 3275984, 821271, 5638221, 9958860, 5037310, 4887472, 6006819, 3012982, 3712004, 5714879, 7495957, 10400814, 222929, 7955020, 1420533, 7772313, 2759757, 9797402, 4704395, 2901288, 8539496, 3274050, 1061129, 1726747, 8973572, 3007876, 10883240, 7634198, 5433415, 6448810, 4901378, 829992, 10426623, 7308888, 1334763, 938779, 7432329, 5876187, 1708601, 1765722, 6238794, 830924, 537470, 1065820, 10987238, 9873946, 5010794, 3193294, 6481863, 4873427, 8300, 7057307, 4210405, 5908510, 6126554, 8902527, 7923126, 5240523, 9761545, 10107636, 4613091, 7615572, 330213, 7198753, 3690446, 3488002, 8752629, 3216240, 2712854, 6135466, 3539112, 1808786, 145048, 7240431, 1536085, 2376211, 2129346, 5717550, 2166892, 4821821, 1793688, 6238822, 2369346, 7751621, 1000713, 6778541, 4295469, 529894, 6418588, 1333422, 1080590, 1009356, 3616338, 6767151, 10635805, 8692983, 2531911, 2556872, 3469045, 9371382, 5136110, 5610030, 5681641, 7612044, 628982, 1042201, 10586815, 1207045, 1695615, 5178823, 556886, 7172574, 4756967, 10444316, 8174917, 1139439, 2847532, 9596033, 5413643, 964429, 5696811, 5837503, 1143630, 3821714, 9334749, 4359602, 9572608, 2136950, 5538176, 3678215, 7163119, 5828590, 769658, 2383979, 181099, 6480603, 4728723, 8539574, 7055201, 1726127, 3115008, 6192242, 3738957, 1751618, 1941489, 936589, 2010276, 7781224, 6874090, 2994370, 3496121, 6355997, 7458908, 2110480, 6246847, 5177589, 5571076, 5872718, 6269811, 10192355, 8666339, 10991202, 6273586, 558040, 3072647, 7908849, 4820442, 4591562, 7127479, 1826307, 3303885, 9633249, 5024504, 1973788, 4824789, 10783977, 10748084, 2385803, 693721, 9631442, 2033334, 2772656, 222968, 931321, 8755575, 7945509, 8470810, 7498399, 7290821, 1266781, 142564, 2077021, 9952169, 3990923, 2735614, 4847479, 10049976, 179703, 9604502, 7632964, 4095798, 2607003, 1711411, 9036037, 2007340, 2990068, 1954057, 5698572, 926017, 2677510, 3429975, 1655883, 7523019, 4432432, 4153387, 90072, 6375701, 4922086, 4285507, 8708973, 9030141, 5298970, 5307535, 7074165, 2408308, 4915820, 1725597, 6077016, 225579, 5363256, 4238031, 10705434, 2832372, 3669017, 9148930, 7418804, 8202395, 3223395, 657953, 3267373, 6353509, 491631, 10787001, 6314099, 288990, 3249641, 7002731, 10495978, 105410, 3782335, 4452132, 9765431, 6253467, 354150, 8533334, 7909928, 4899370, 3551704, 2736862, 6899487, 4111411, 6143926, 3373522, 2092319, 9477811, 3436722, 8162365, 7457617, 5839314, 7316123, 1429949, 2740796, 1941641, 3356611, 978071, 9291318, 2890313, 5461216, 7226634, 2926626, 3567339, 1517467, 6703348, 6359751, 546739, 1763797, 8239286, 3144193, 5821589, 2183150, 4552766, 403307, 10641142, 1594398, 7042611, 3980882, 8345246, 2731427, 1644507, 7939436, 7182169, 1172494, 5407964, 1294117, 8779519, 5375824, 6400639, 10069722, 7170260, 9304356, 11041411, 244573, 132416, 9913967, 5019184, 294820, 6325770, 368491, 9932402, 8844082, 6715292, 6840118, 8902977, 8106900, 7721199, 8404803, 5034704, 6836763, 2806633, 1055146, 4344409, 4997384, 5498156, 1144869, 1973045, 3816877, 2825661, 1875553, 5620906, 9464713, 2811170, 3265042, 5978846, 4024406, 8904862, 2491381, 3284551, 1858006, 10457829, 107684, 6828443, 1408871, 4692562, 2588082, 10508548, 1899506, 5391358, 6584668, 4338811, 8036678, 1953257, 9913252, 2337550, 10582101, 9662749, 5392428, 8909565, 3638832, 9572899, 8705881, 1986726, 538976, 6594916, 2534098, 5786341, 258746, 4851462, 7257491, 3126373, 6144669, 5059495, 5832489, 7466251, 8555803, 3060858, 4356694, 10878060, 244511, 9169259, 998394, 4477916, 6235163, 6864653, 977058, 7630980, 6829841, 7405080, 4984472, 6077525, 559064, 4537717, 4477823, 6181525, 6298469, 8409956, 2788100, 8087486, 4282001, 2905194, 5712457, 4295117, 2787198, 9008831, 499069, 370702, 9244619, 8181159, 9586139, 9121376, 9124735, 6209128, 1820071, 4175348, 7328198, 6571869, 5686127, 4843457, 5353678, 7569220, 8833218, 2464293, 78626, 184621, 8903979, 1013037, 7865389, 11033265, 3428700, 7362439, 8192197, 6858952, 418724, 6600556, 3402807, 3860358, 9346556, 2933733, 5907293, 3224319, 4564843, 7533230, 5731696, 9891095, 3897680, 8439615, 6133208, 4253344, 8881190, 2701732, 7301413, 4225119, 5881508, 3081738, 7580591, 8455218, 4080019, 3874049, 7764386, 8007034, 4230083, 4839606, 9308667, 9209729, 1727671, 3267998, 10756341, 5222010, 8486097, 7241680, 6657292, 148197, 10356852, 7702164, 10153052, 2590309, 4638443, 10784031, 9029509, 4866999, 4696233, 5781327, 1337830, 1864702, 7576691, 3910001, 3307170, 3283735, 7350418, 1351208, 6900898, 11121885, 8670473, 4226245, 3451000, 8599772, 7312540, 2664805, 5472122, 8720570, 597396, 603857, 1686811, 7601977, 7430460, 6087988, 2914464, 10096595, 3573711, 940658, 3335865, 2358415, 2770861, 10271074, 118458, 9101100, 2474371, 5706308, 8698290, 8104568, 1855367, 3121038, 7106858, 7940750, 5966281, 8140038, 648111, 2987649, 8949874, 9776483, 3642965, 9029206, 2654402, 4215276, 2084838, 3973728, 5156448, 7887981, 4440249, 8177250, 4659983, 217240, 7564125, 1085696, 836319, 6004824, 8944880, 8235135, 4677801, 1715402, 9403530, 216215, 1989973, 8570379, 7341306, 1260240, 1024135, 3142363, 3344840, 4608685, 460330, 3136977, 3984022, 9203455, 910223, 5146611, 4813076, 9786321, 8446518, 4213457, 6722085, 1293230, 1940248, 4321386, 7624949, 3878700, 5726223, 8079846, 3549190, 6523938, 8396221, 4917121, 1706128, 550436, 885971, 2435281, 2370184, 9244229, 66943, 5954091, 5882875, 1926038, 1721182, 1163529, 124307, 4797331, 7259398, 3228715, 4098828, 6875189, 4486562, 7432328, 8491255, 11042870, 1214317, 7339195, 8733065, 7768404, 3557644, 3138201, 10776058, 3459035, 934183, 2163642, 6666653, 6308962, 4106490, 1969346, 1166330, 3047513, 658592, 2917721, 7627951, 7551635, 4210713, 5373102, 5333584, 4499474, 6921130, 3345253, 1786344, 5965491, 10834323, 4456732, 6684393, 8914603, 4508069, 5628061, 1473764, 530072, 4349515, 6426113, 7173546, 6341600, 6563526, 1914005, 6929819, 756750, 8663596, 5363050, 4620825, 3677532, 4028028, 6811339, 8539111, 5891376, 1332551, 9177447, 4906093, 5362452, 1587358, 7180143, 2800482, 1636571, 7452754, 1306744, 6605825, 2679563, 9154297, 5507013, 9837506, 1193976, 2530269, 1147542, 4871674, 5219925, 3127392, 7808825, 9666844, 4418895, 2442707, 266583, 4697013, 5117884, 227750, 1848389, 8526330, 8099741, 4016168, 9606929, 735652, 3895203, 7273374, 5786824, 5802475, 7423526, 9436864, 6749500, 5198696, 876117, 10672966, 10031823, 2469825, 9183237, 7114163, 3452012, 4361078, 6426462, 6805400, 7118409, 1043015, 7011563, 6723773, 8926714, 327877, 5466121, 1402595, 5468825, 7830629, 10438837, 7673092, 1787281, 610037, 5067232, 7869542, 21754, 9980953, 5323994, 7431679, 9785663, 2448750, 5853413, 4298446, 3099833, 1394179, 4598222, 161441, 1862565, 2053061, 3627772, 10648165, 1204118, 7213811, 286117, 8187589, 6656539, 9569849, 3723694, 6850885, 3888550, 1471039, 9033019, 921505, 3991287, 6196781, 8215202, 4494239, 2857095, 7502929, 7103798, 154480, 7203177, 10966667, 1041982, 491629, 2168916, 1132763, 2789086, 6665035, 1250997, 5988587, 5536453, 165970, 8321123, 603635, 10981467, 661526, 5822351, 5602419, 7454638, 5174683, 191546, 3332069, 9046201, 8931636, 4708607, 8583972, 1023143, 3353236, 9632868, 272374, 6545366, 10924933, 4834916, 6677111, 4217667, 7571754, 8341805, 5676884, 1424925, 475101, 3129022, 1587827, 1731795, 3883579, 9758767, 7211480, 4222760, 4232426, 7833063, 2051757, 1374229, 1764699, 2063557, 9888474, 5003967, 6859902, 1197130, 7055940, 8134909, 825695, 5840140, 7516661, 3135758, 7734341, 1510005, 9279546, 8649274, 10764146, 10245081, 1500632, 1035289, 4855499, 6409532, 6003579, 2612338, 8206948, 2504142, 2996315, 6003690, 4034600, 830635, 1077725, 7790150, 9009097, 1593167, 6874280, 2362867, 9031083, 6889846, 2302220, 3504349, 7152773, 8238283, 2809105, 1798949, 343893, 1224784, 9273849, 8695418, 671533, 4366819, 8317340, 140034, 7228701, 6089898, 2263625, 5612141, 7802356, 1927846, 4732221, 7616005, 2555122, 6279392, 7430306, 6193378, 2047284, 7979965, 3864683, 1678307, 1270051, 6065791, 2891065, 6725748, 2515744, 5357503, 9973264, 4949394, 2100084, 9033934, 1251749, 8944719, 10431502, 5366812, 4954297, 3344523, 8600092, 3219752, 4621364, 3758271, 7512839, 1121301, 3646195, 4570010, 219930, 2469996, 10195843, 1250604, 5034679, 4577436, 1116963, 2193189, 3114678, 9374079, 4803154, 6981521, 83796, 7127974, 5102104, 5339810, 7295306, 1770249, 26557, 7338940, 306605, 3368660, 4941838, 103255, 7106590, 3643597, 5215783, 2314467, 4797423, 1171848, 5311542, 1763143, 4657109, 5394161, 3058213, 7531200, 1819210, 6187175, 4469869, 8019212, 3041115, 10287644, 905918, 4376910, 9430116, 3476248, 9358641, 6025407, 4950327, 6267588, 4772560, 4466927, 6622337, 3837245, 383861, 3162396, 3094160, 9594873, 5375610, 9770978, 608450, 6412368, 4600401, 5941918, 5397808, 1825359, 5101211, 2498466, 6713452, 9378578, 8684003, 5295755, 6598754, 5783999, 6344339, 3545103, 6484776, 5518680, 9397630, 9351059, 5335499, 2387497, 9528725, 9917733, 10510107, 991135, 667988, 187382, 3628000, 7141692, 3697484, 2194368, 8261023, 2069538, 2884981, 7931264, 3005329, 7554942, 8703453, 6607123, 9210132, 6381699, 7925679, 367613, 3385354, 3250546, 60903, 2998416, 1202994, 8259824, 6516592, 3861540, 1227593, 269590, 10094415, 892517, 7088275, 10675071, 4120971, 723848, 1996815, 10671248, 2491341, 3148346, 4777688, 6465176, 7679654, 2743839, 831627, 2222900, 9162692, 5625772, 8081994, 8743939, 1749706, 1480740, 5498105, 5357358, 1087256, 8106130, 3994962, 732914, 3486125, 2988132, 511724, 1837430, 250712, 7434540, 679146, 6576222, 1791327, 5465896, 352256, 10332352, 1196773, 4858181, 2880284, 199183, 1083838, 999000, 3638787, 7995188, 1241737, 8673169, 1224733, 4431819, 6641493, 6003637, 5504296, 3166579, 4061421, 2311044, 7885070, 2823854, 6069300, 3310327, 10677768, 4349417, 1693915, 4899519, 173567, 7141024, 4793663, 6988528, 1193094, 2393244, 2326628, 3034476, 8089711, 209663, 6824373, 7805348, 8395080, 2695033, 9371851, 352752, 7714428, 69714, 5359086, 8239588, 5060591, 4322417, 3077473, 3985814, 8131464, 5042239, 3936664, 2153517, 9561053, 3711698, 6297355, 179025, 4911874, 2322501, 5555586, 8389986, 6277707, 1335673, 3734082, 2020202, 3871385, 3701411, 5724770, 9937108, 785019, 5104742, 7083498, 10195794, 8712091, 6302276, 8612151, 8698023, 5312034, 2678740, 7658606, 8766101, 3915701, 8912110, 3329515, 8082985, 4253237, 3484262, 7833669, 8890281, 6923972, 6755658, 6372440, 1224700, 6099307, 3893484, 6213037, 2888014, 3225515, 9265885, 6433, 7161672, 7560683, 4377418, 3160896, 7427599, 5978298, 2838416, 8516384, 4101928, 4411002, 6774447, 1070150, 6121997, 3513923, 9079889, 6965586, 3451444, 159432, 1420688, 3639492, 6056723, 903948, 3760618, 680602, 4352649, 0, 905877, 1132010, 5824879, 5812104, 9220787, 2701127, 2698384, 5764375, 8177798, 384690, 8833826, 1259774, 3248723, 3428638, 1232481, 5004074, 3787303, 2018692, 7188934, 116576, 588842, 7762429, 7548151, 10737302, 8717347, 10616918, 5322094, 4964715, 5463512, 6698211, 1218205, 2709327, 4850170, 9742370, 5317002, 7512775, 861669, 6858114, 403646, 1866224, 4089589, 3259872, 1693122, 3794030, 7813025, 6693328, 9928519, 3137035, 215867, 2170660, 2665541, 6345380, 7368391, 7173541, 862997, 922282, 8121685, 818850, 5794182, 2117888, 10196790, 1081191, 4532485, 2913183, 3698850, 508701, 48035, 4221852, 7156861, 10236755, 546638, 961832, 8779165, 5675814, 3384839, 1316980, 10597878, 1961350, 3771296, 3615955, 2170107, 296178, 423012, 10331454, 842861, 8182031, 4349599, 7766187, 4925397, 4897384, 8025624, 14939, 2752743, 5933852, 2855384, 1181039, 3030579, 4017505, 6944, 4952355, 4781005, 9037792, 1118107, 8435239, 6071842, 6809277, 4253914, 3247021, 7500024, 9356641, 9935965, 7171781, 4736074, 4218057, 9823590, 5904046, 5043422, 7598611, 9201680, 4836262, 2760417, 8507124, 4772477, 4174650, 3275913, 5825113, 10277645, 223135, 2696469, 4376133, 862743, 3862638, 1266624, 336314, 3878336, 4467117, 3045846, 11009835, 219121, 143622, 1353227, 1246515, 8756317, 9748821, 9391407, 9010997, 7276504, 3786094, 7495004, 4606269, 2482638, 5002052, 1469897, 1941003, 10540540, 6295943, 1111970, 8141835, 9868036, 2807747, 8195236, 1964515, 2477925, 421401, 5917443, 1797116, 2152633, 1698154, 1714607, 4158361, 3098835, 1191150, 5703854, 9163106, 6002524, 5948763, 6250172, 5303559, 8916787, 3736992, 6664847, 6352098, 5415244, 4259075, 1340870, 371065, 7624871, 3450989, 3763369, 1755484, 2775811, 8668616, 3189866, 6539043, 2571405, 1474208, 7055592, 7761143, 6861190, 6807988, 5605615, 4785997, 59169, 10491310, 2515269, 2791360, 3353815, 8333811, 2344400, 9403995, 7869424, 5050893, 10679779, 376057, 5472791, 7513464, 3704746, 621368, 5536335, 9733980, 7882751, 3759468, 2411720, 1449689, 3734448, 4807599, 4431776, 6298633, 118019, 1539947, 3360124, 2180524, 6919825, 2565830, 7142715, 3109528, 2496453, 3777107, 1647485, 7173882, 8433935, 7444992, 10206269, 2408674, 1993953, 8962637, 9947461, 531585, 8429138, 580179, 1195175, 7490620, 2352852, 3253397, 2832442, 8178550, 9226357, 5841668, 7771375, 4131080, 6363580, 7308995, 6931965, 6494567, 2072972, 1160392, 3070761, 3074576, 3688597, 989885, 5869544, 7060733, 7257456, 1200352, 653063, 3879848, 9354631, 1279841, 9090050, 7052102, 7766585, 1391853, 4534335, 3339300, 8078838, 81692, 5020316, 2856351, 3239766, 6697416, 3135752, 1631616, 1744313, 7941505, 2363551, 2461078, 3655649, 1596013, 6979975, 4124517, 1897034, 6964747, 7423410, 1140556, 6292262, 1669583, 9337191, 4264179, 6739217, 7582079, 5291883, 9371929, 3516728, 3778447, 3723861, 3676462, 8744214, 2677406, 6971086, 200764, 5706057, 4659829, 3273466, 5393978, 1849675, 3969025, 6273549, 834368, 9045984, 3745851, 7362360, 3421832, 1941417, 1993718, 878498, 2997134, 6432694, 7112829, 7179528, 3061910, 53971, 6101052, 8412753, 6722977, 1267223, 3197055, 7907972, 5065548, 3626524, 6721329, 6467750, 8002261, 5223989, 1046050, 414042, 2904916, 4956438, 7565268, 5704973, 7496112, 6271191, 1299104, 3056994, 138838, 3022024, 794238, 2713870, 8763143, 9527404, 2808001, 2598175, 10616448, 204658, 4490280, 1683295, 219389, 3128409, 4956412, 6712000, 1405711, 866318, 8504897, 4694995, 1035067, 2752284, 3561819, 10177419, 3872035, 2476210, 2817377, 2313494, 10041478, 4266802, 4260506, 3025593, 87383, 1869142, 7812426, 5816541, 2145669, 3481628, 103470, 1961057, 3190973, 4517841, 2750099, 5451788, 4861850, 460373, 7168999, 262893, 7196992, 4962805, 3868968, 6020352, 2469269, 6008040, 7932025, 9198617, 8957083, 884340, 2058182, 1165827, 1245340, 7994700, 3127120, 1064106, 2691786, 6013217, 4484497, 10502330, 1640054, 1759231, 6745431, 6988501, 5527377, 2789827, 862434, 3726168, 447363, 9382859, 1822443, 5877278, 3130377, 1036135, 8573579, 4786954, 5814041, 9543638, 10420517, 7421735, 272085, 3652116, 3255747, 822525, 817446, 160604, 577576, 4054253, 8127203, 2563640, 4809890, 9414627, 7055998, 4038336, 4324843, 4957586, 6929821, 1118223, 3118850, 1886386, 117949, 5259064, 1937255, 620078, 4807345, 6345207, 184583, 3300760, 7930617, 2020396, 4302509, 7104146, 5641387, 4419911, 1668755, 2563455, 8206644, 9559797, 1945535, 1252480, 474123, 2481234, 619991, 1191975, 5155460, 6441922, 1940413, 5144200, 3037139, 2600731, 8926432, 5126663, 3292063, 1831507, 1764201, 6253629, 10297963, 3444455, 2919793, 896221, 8017285, 922365, 9435632, 2061485, 2851642, 1359501, 5588298, 608328, 3040512, 10086461, 3193296, 4876074, 4314971, 7903832, 4435226, 638118, 5665365, 3697753, 4256574, 1602946, 2385661, 2302010, 5410592, 1997470, 5777224, 1637189, 3564025, 2710580, 4959927, 5414747, 1127975, 7905843, 1716385, 1825709, 4559899, 7053407, 4479063, 1335520, 9557500};
		List<List<Integer>> aCoordList = new ArrayList<>();
		int aCoef = 5;
		for (int anInt : aIdArray)
		{
			List<Integer> aCoord = new ArrayList<>();
			//int aX = anInt % 3353, aY = anInt / 3353;
			int aX = anInt % 3353 /aCoef, aY = anInt / 3353 /aCoef;
			//anInt = anInt/100; int aX = anInt % 335, aY = anInt / 335;

			aCoord.add(aX);
			aCoord.add(aY);
			aCoordList.add(0, aCoord);
		}
		System.out.println("coef: " + aCoef + "; dimension: " + 3353/aCoef + "x" + 3353/aCoef);
		System.out.println("coordinates:\n " + aCoordList);
	}

	private void fibTracer()
	{
		// 4th deferential
		Integer[] anArr = {28, 30, 40, 32, 38, 5, 34, 3, 39, 31, 1, 43, 21, 46, 14, 0, 47, 37, 27, 36, 4, 46, 10, 46, 49, 0, 14, 49, 28, 43, 4, 50, 11, 8, 47, 1, 25, 14, 31, 47, 16, 42, 23, 40, 43, 17, 30, 23, 26, 27, 33, 48, 31, 26, 35, 3, 35, 27, 19, 32, 9, 32, 14, 1, 45, 9, 12, 32, 37, 4, 7, 37, 7, 20, 22, 14, 11, 10, 7, 40, 22, 8, 47, 9, 2, 40, 8, 8, 32, 48, 4, 20, 12, 11, 29, 9, 31, 24, 21, 8, 33, 46, 34, 27, 6, 37, 43, 25, 15, 41, 46, 29, 23, 3, 7, 45, 33, 12, 2, 4, 48, 43, 11, 9, 37, 48, 2, 37, 34, 43, 12, 4, 49, 39, 34, 38, 48, 41, 10, 41, 5, 38, 15, 25, 25, 49, 21, 46, 8, 43, 27, 36, 39, 48, 34, 32, 1, 33, 11, 39, 39, 30, 7, 27, 11, 18, 46, 27, 18, 23, 28, 16, 15, 35, 32, 38, 42, 9, 50, 48, 50, 46, 6, 50, 21, 25, 47, 49, 15, 25, 6, 0, 43, 1, 29, 38, 35, 18, 7, 19, 12, 25, 17, 9, 49, 36, 0, 11, 27, 29, 28, 9, 40, 7, 13, 34, 3, 31, 28, 49, 17, 37, 48, 48, 10, 24, 13, 50, 0, 37, 4, 36, 12, 18, 28, 1, 37, 0, 7, 34, 33, 30, 5, 32, 11, 31, 39, 11, 43, 44, 18, 7, 14, 22, 1, 43, 29, 12, 8, 16, 32, 45, 18, 37, 46, 37, 5, 50, 35, 2, 30, 30, 16, 32, 42, 49, 36, 1, 48, 35, 42, 46, 3, 9, 47, 45, 5, 5, 29, 30, 41, 7, 44, 46, 8, 6, 20, 48, 15, 13, 19, 23, 36, 35, 43, 25, 27, 22, 40, 28, 11, 30, 48, 6, 15, 1, 40, 3, 34, 14, 28, 6, 7, 3, 36, 13, 10, 2, 4, 19, 35, 39, 43, 17, 15, 37, 44, 7, 42, 4, 11, 38, 50, 17, 49, 30, 48, 7, 44, 8, 34, 24, 5, 38, 0, 13, 31, 35, 5, 6, 30, 17, 50, 40, 20, 39, 24, 37, 50, 43, 1, 14, 18, 9, 3, 11, 33, 7, 50, 15, 17, 44, 22, 17, 0, 5, 38, 4, 18, 9, 6, 18, 35, 39, 6, 19, 19, 26, 11, 45, 38, 5, 46, 23, 32, 44, 49, 12, 48, 28, 34, 43, 29, 47, 21, 36, 43, 30, 2, 32, 34, 5, 42, 2, 26, 6, 7, 31, 23, 16, 35, 30, 50, 19, 50, 30, 27, 31, 42, 18, 15, 40, 30, 10, 41, 50, 34, 30, 3, 16, 15, 49, 10, 15, 8, 34, 26, 42, 30, 31, 0, 21, 30, 25, 6, 23, 2, 18, 14, 47, 39, 10, 46, 26, 2, 9, 30, 10, 16, 0, 4, 16, 30, 43, 9, 48, 49, 35, 10, 1, 5, 27, 33, 15, 0, 12, 15, 30, 33, 32, 21, 28, 15, 20, 47, 36, 28, 31, 49, 47, 5, 23, 7, 30, 50, 14, 36, 39, 12, 5, 40, 36, 5, 42, 46, 3, 32, 28, 6, 32, 13, 27, 43, 19, 46, 21, 1, 19, 42, 26, 29, 29, 50, 30, 21, 2, 4, 38, 32, 27, 16, 21, 34, 34, 38, 50, 39, 5, 47, 23, 0, 26, 2, 4, 26, 15, 46, 10, 24, 14, 36, 37, 18, 16, 48, 45, 7, 3, 29, 37, 9, 0, 24, 30, 31, 44, 3, 27, 21, 1, 0, 5, 1, 43, 0, 46, 12, 3, 18, 36, 41, 31, 13, 20, 34, 6, 38, 30, 8, 27, 5, 16, 28, 0, 36, 20, 46, 25, 38, 21, 17, 24, 42, 39, 27, 37, 15, 10, 2, 39, 10, 29, 50, 41, 38, 19, 32, 19, 34, 12, 3, 49, 18, 4, 13, 6, 36, 42, 24, 8, 48, 3, 29, 14, 35, 9, 35, 25, 50, 37, 40, 23, 36, 13, 17, 26, 0, 14, 15, 41, 8, 28, 39, 11, 25, 8, 48, 5, 6, 34, 29, 43, 2, 8, 0, 43, 34, 4, 3, 13, 21, 15, 42, 25, 45, 23, 41, 21, 31, 43, 39, 9, 40, 41, 11, 5, 48, 37, 44, 42, 14, 37, 38, 6, 2, 31, 40, 42, 26, 27, 38, 31, 42, 22, 19, 2, 25, 41, 38, 26, 33, 3, 17, 21, 25, 37, 30, 49, 23, 44, 7, 15, 4, 27, 25, 22, 47, 22, 50, 22, 38, 25, 47, 8, 50, 22, 3, 25, 32, 42, 7, 36, 18, 23, 23, 33, 17, 17, 20, 15, 9, 48, 10, 26, 37, 24, 39, 12, 21, 50, 24, 13, 39, 29, 22, 34, 18, 13, 15, 0, 11, 46, 3, 20, 11, 32, 42, 46, 31, 5, 29, 39, 7, 25, 21, 14, 1, 28, 16, 10, 31, 42, 9, 34, 49, 41, 28, 5, 19, 7, 38, 35, 21, 11, 9, 3, 21, 36, 47, 18, 11, 15, 18, 49, 0, 1, 1, 5, 39, 17, 45, 7, 10, 13, 21, 14, 3, 2, 11, 48, 27, 26, 27, 30, 38, 5, 45, 34, 35, 29, 39, 9, 14, 22, 19, 8, 8, 35, 17, 25, 20, 39, 35, 39, 19, 20, 1, 38, 34, 38, 12, 43, 43, 27, 33, 8, 6, 13, 31, 2, 40, 18, 38, 28, 21, 24, 39, 2, 13, 7, 36, 20, 20, 8, 10, 4, 6, 43, 11, 49, 35, 47, 8, 18, 18, 30, 5, 27, 47, 46, 35, 13, 49, 8, 39, 22, 30, 34, 34, 17, 0, 33, 41, 33, 45, 16, 0, 36, 34, 7, 25, 29, 4, 28, 30, 32, 40, 19, 49, 10, 49, 48, 41, 9, 15, 5, 22, 16, 35, 25, 14, 44, 37, 30, 4, 50, 45, 48, 48, 43, 39, 35, 49, 22, 27, 32, 31, 48, 10, 5, 50, 38, 37, 32, 10, 44, 21, 31, 4, 36, 16, 18, 27, 13, 46, 25, 21, 3, 33, 33, 22, 44, 2, 35, 48, 28, 27, 13, 20, 41, 48, 49, 27, 2, 30, 23, 9, 15, 25, 48, 45, 31, 30, 6, 44, 15, 0, 29, 50, 10, 41, 6, 17, 26, 25, 35, 10, 41, 18, 15, 21, 27, 37, 13, 26, 23, 48, 20, 34, 39, 41, 23, 14, 43, 47, 22, 32, 31, 50, 21, 48, 37, 3, 42, 12, 26, 27, 26, 3, 46, 11, 20, 32, 37, 33, 36, 1, 25, 46, 35, 5, 45, 30, 37, 7, 21, 37, 18, 6, 15, 14, 13, 6, 46, 1, 28, 26, 34, 15, 42, 11, 15, 4, 2, 44, 14, 26, 27, 9, 31, 2, 12, 21, 39, 35, 42, 5, 28, 37, 20, 8, 41, 10, 27, 24, 22, 4, 0, 31, 35, 41, 37, 26, 2, 13, 17, 45, 34, 5, 32, 46, 12, 3, 37, 46, 33, 36, 9, 41, 42, 26, 45, 45, 19, 3, 3, 34, 23, 47, 23, 4, 13, 30, 32, 31, 2, 25, 8, 13, 17, 0, 19, 43, 24, 38, 32, 9, 13, 29, 13, 34, 30, 7, 20, 43, 3, 32, 28, 29, 17, 38, 41, 31, 5, 38, 31, 45, 29, 37, 47, 45, 32, 6, 32, 47, 2, 39, 0, 5, 22, 40, 14, 15, 13, 20, 32, 39, 35, 44, 29, 44, 5, 48, 32, 43, 46, 48, 26, 14, 12, 40, 38, 34, 47, 21, 32, 40, 0, 45, 24, 36, 8, 13, 9, 7, 14, 49, 34, 23, 15, 12, 37, 17, 15, 4, 18, 7, 16, 19, 48, 43, 46, 13, 46, 45, 40, 44, 4, 8, 49, 8, 26, 0, 46, 14, 25, 8, 50, 17, 15, 16, 3, 39, 28, 16, 48, 31, 7, 14, 43, 6, 19, 11, 20, 31, 50, 35, 46, 20, 9, 21, 23, 45, 34, 36, 26, 36, 42, 1, 7, 31, 33, 18, 1, 4, 36, 14, 37, 26, 3, 21, 41, 16, 16, 31, 43, 44, 28, 13, 29, 28, 47, 19, 33, 35, 25, 15, 3, 5, 50, 37, 0, 21, 47, 6, 18, 49, 40, 9, 47, 26, 14, 14, 49, 21, 24, 9, 29, 46, 49, 18, 12, 46, 44, 34, 1, 2, 30, 32, 45, 6, 32, 45, 8, 47, 20, 24, 16, 31, 35, 28, 15, 34, 29, 43, 7, 35, 19, 17, 26, 43, 32, 17, 6, 19, 40, 49, 12, 2, 13, 44, 47, 36, 27, 0, 22, 15, 28, 21, 45, 23, 45, 18, 35, 12, 35, 32, 11, 4, 47, 41, 7, 39, 18, 39, 33, 37, 40, 1, 33, 30, 25, 31, 39, 5, 6, 35, 28, 45, 15, 9, 5, 0, 13, 38, 19, 42, 24, 40, 3, 5, 16, 4, 27, 38, 25, 10, 37, 11, 21, 18, 41, 43, 34, 12, 36, 9, 36, 9, 6, 9, 22, 0, 43, 15, 5, 12, 15, 48, 33, 17, 47, 11, 38, 17, 10, 44, 15, 30, 41, 1, 10, 7, 39, 39, 8, 25, 16, 45, 21, 0, 21, 8, 9, 30, 47, 12, 17, 21, 27, 10, 11, 13, 10, 22, 44, 26, 14, 0, 27, 34, 19, 40, 24, 0, 1, 18, 48, 23, 16, 34, 4, 44, 47, 48, 47, 45, 9, 31, 1, 34, 50, 39, 15, 37, 34, 4, 10, 49, 7, 7, 15, 26, 44, 49, 25, 5, 40, 47, 3, 10, 32, 4, 36, 21, 49, 16, 23, 15, 0, 9, 32, 22, 17, 32, 41, 24, 42, 3, 17, 23, 1, 36, 6, 33, 25, 42, 9, 7, 15, 15, 5, 18, 41, 11, 24, 8, 30, 16, 48, 47, 24, 28, 37, 38, 27, 33, 20, 50, 49, 5, 2, 33, 14, 23, 47, 48, 18, 2, 16, 27, 44, 16, 5, 22, 34, 11, 40, 4, 49, 23, 35, 9, 28, 29, 24, 27, 30, 23, 33, 25, 43, 17, 30, 39, 40, 4, 12, 35, 28, 50, 4, 31, 1, 28, 34, 32, 48, 17, 14, 10, 37, 1, 6, 15, 18, 5, 45, 10, 8, 49, 45, 38, 36, 24, 33, 18, 43, 5, 10, 27, 37, 2, 45, 46, 4, 38, 33, 42, 16, 50, 22, 32, 20, 27, 26, 19, 44, 45, 33, 27, 36, 7, 22, 2, 20, 1, 27, 11, 13, 41, 46, 38, 19, 42, 35, 26, 35, 39, 33, 7, 31, 24, 23, 4, 35, 24, 3, 50, 34, 11, 2, 4, 26, 15, 25, 39, 18, 3, 4, 47, 49, 18, 27, 17, 49, 11, 9, 12, 1, 49, 5, 41, 30, 6, 41, 8, 8, 19, 45, 18, 31, 7, 7, 38, 46, 12, 17, 0, 11, 15, 44, 5, 25, 10, 47, 36, 9, 16, 45, 46, 3, 10, 23, 37, 45, 11, 40, 44, 12, 47, 22, 43, 23, 23, 47, 12, 39, 12, 37, 9, 22, 16, 20, 11, 6, 36, 25, 48, 47, 23, 1, 17, 23, 28, 9, 10, 30, 33, 32, 36, 4, 19, 18, 45, 39, 30, 45, 30, 15, 8, 32, 17, 30, 38, 39, 28, 23, 47, 43, 45, 25, 25, 48, 26, 11, 17, 16, 37, 36, 18, 34, 5, 7, 4, 13, 39, 46, 4, 43, 14, 19, 16, 32, 27, 16, 39, 29, 7, 17, 5, 25, 39, 27, 38, 14, 50, 19, 31, 8, 36, 45, 3, 10, 50, 33, 9, 7, 12, 7, 41, 15, 6, 24, 9, 28, 26, 5, 50, 9, 28, 47, 27, 42, 21, 11, 37, 9, 11, 8, 7, 39, 34, 20, 4, 27, 24, 41, 16, 45, 13, 26, 0, 10, 28, 2, 11, 50, 9, 18, 31, 35, 50, 11, 36, 37, 44, 22, 49, 30, 39, 31, 11, 17, 27, 24, 33, 16, 8, 30, 43, 3, 17, 15, 6, 9, 20, 18, 45, 9, 31, 25, 42, 11, 13, 2, 46, 0, 0, 49, 18, 36, 34, 21, 48, 50, 24, 18, 19, 24, 42, 14, 15, 27, 37, 2, 0, 45, 30, 16, 47, 13, 9, 24, 19, 43, 37, 12, 5, 42, 49, 6, 44, 34, 32, 38, 35, 42, 10, 13, 2, 41, 47, 29, 5, 44, 46};
		List<Integer> aList = new ArrayList(Arrays.asList(anArr));
		int aMax =  Collections.max(aList);
		List<Integer> anAveragePath = new ArrayList<>();
		for (int aNum = aList.remove(0); aList.size() > 200; aNum = aList.remove(0))
		{
			Set<Integer> aFibSet = new TreeSet<>();

			for (int j = 1; j < aList.size(); j++)
			{
				//int aFibNum = aList.subList(0, j).stream().collect(Collectors.summingInt(Integer::intValue)) % aMax;
				int aFibNum = aList.subList(0, j).stream().collect(Collectors.summingInt(Integer::intValue)) / (aFibSet.size() == 0 ? 1 : aFibSet.size()) % aMax;
				if (aFibNum == aNum || aList.size() == j + 1)
				{
					anAveragePath.add(aFibSet.size());
					System.out.printf("%1$2d", aNum);
					System.out.println(" \t : " + "l: " + aFibSet.size() + ", \t set: " + aFibSet);
					break;
				}
				aFibSet.add(aFibNum);
			}

		}
		System.out.println("\nan average path length: " + anAveragePath.stream().collect(Collectors.summingInt(Integer::intValue))/anAveragePath.size() + ", " + anAveragePath.size() + "/" + anArr.length + "\n");


		int aTotal = anAveragePath.size();
		Map<Integer, Integer> aTreeMap = new TreeMap<>();
		while(anAveragePath.size() > 0)
		{
			int aNum  = anAveragePath.get(0);
			int anOldSize = anAveragePath.size();
			anAveragePath.removeAll(Collections.singleton(anAveragePath.get(0)));
			int aNewSize = anAveragePath.size();
			aTreeMap.put(aNum, anOldSize - aNewSize);
		}

		for (Map.Entry<Integer, Integer> aEntry : aTreeMap.entrySet())
		{
			System.out.printf("%1$2d", aEntry.getKey());
			System.out.println(" : " + aEntry.getValue());
		}

	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}