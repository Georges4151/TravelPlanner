package com.travelplanner.util;

import com.travelplanner.model.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityDataUtil {

    private static final List<City> ALL_CITIES = new ArrayList<>();
    private static final Map<String, List<City>> PROVINCE_CITIES = new HashMap<>();

    static {
        initCities();
    }

    private static void initCities() {
        addCity("北京市", "北京", "beijing");
        addCity("天津市", "天津", "tianjin");
        addCity("河北省", "石家庄", "shijiazhuang");
        addCity("河北省", "唐山", "tangshan");
        addCity("河北省", "秦皇岛", "qinhuangdao");
        addCity("河北省", "邯郸", "handan");
        addCity("河北省", "邢台", "xingtai");
        addCity("河北省", "保定", "baoding");
        addCity("河北省", "张家口", "zhangjiakou");
        addCity("河北省", "承德", "chengde");
        addCity("河北省", "沧州", "cangzhou");
        addCity("河北省", "廊坊", "langfang");
        addCity("河北省", "衡水", "hengshui");

        addCity("山西省", "太原", "taiyuan");
        addCity("山西省", "大同", "datong");
        addCity("山西省", "阳泉", "yangquan");
        addCity("山西省", "长治", "changzhi");
        addCity("山西省", "晋城", "jincheng");
        addCity("山西省", "朔州", "shuozhou");
        addCity("山西省", "晋中", "jinzhong");
        addCity("山西省", "运城", "yuncheng");
        addCity("山西省", "忻州", "xinzhou");
        addCity("山西省", "临汾", "linfen");
        addCity("山西省", "吕梁", "lvliang");

        addCity("内蒙古自治区", "呼和浩特", "huhehaote");
        addCity("内蒙古自治区", "包头", "baotou");
        addCity("内蒙古自治区", "乌海", "wuhai");
        addCity("内蒙古自治区", "赤峰", "chifeng");
        addCity("内蒙古自治区", "通辽", "tongliao");
        addCity("内蒙古自治区", "鄂尔多斯", "eerduosi");
        addCity("内蒙古自治区", "呼伦贝尔", "hulunbeier");
        addCity("内蒙古自治区", "巴彦淖尔", "bayannaoer");
        addCity("内蒙古自治区", "乌兰察布", "wulanchabu");

        addCity("辽宁省", "沈阳", "shenyang");
        addCity("辽宁省", "大连", "dalian");
        addCity("辽宁省", "鞍山", "anshan");
        addCity("辽宁省", "抚顺", "fushun");
        addCity("辽宁省", "本溪", "benxi");
        addCity("辽宁省", "丹东", "dandong");
        addCity("辽宁省", "锦州", "jinzhou");
        addCity("辽宁省", "营口", "yingkou");
        addCity("辽宁省", "阜新", "fuxin");
        addCity("辽宁省", "辽阳", "liaoyang");
        addCity("辽宁省", "盘锦", "panjin");
        addCity("辽宁省", "铁岭", "tieling");
        addCity("辽宁省", "朝阳", "chaoyang");
        addCity("辽宁省", "葫芦岛", "huludao");

        addCity("吉林省", "长春", "changchun");
        addCity("吉林省", "吉林", "jilin");
        addCity("吉林省", "四平", "siping");
        addCity("吉林省", "辽源", "liaoyuan");
        addCity("吉林省", "通化", "tonghua");
        addCity("吉林省", "白山", "baishan");
        addCity("吉林省", "松原", "songyuan");
        addCity("吉林省", "白城", "baicheng");

        addCity("黑龙江省", "哈尔滨", "haerbin");
        addCity("黑龙江省", "齐齐哈尔", "qiqihaer");
        addCity("黑龙江省", "鸡西", "jixi");
        addCity("黑龙江省", "鹤岗", "hegang");
        addCity("黑龙江省", "双鸭山", "shuangyashan");
        addCity("黑龙江省", "大庆", "daqing");
        addCity("黑龙江省", "伊春", "yichun");
        addCity("黑龙江省", "佳木斯", "jiamusi");
        addCity("黑龙江省", "七台河", "qitaihe");
        addCity("黑龙江省", "牡丹江", "mudanjiang");
        addCity("黑龙江省", "黑河", "heihe");
        addCity("黑龙江省", "绥化", "suihua");

        addCity("上海市", "上海", "shanghai");

        addCity("江苏省", "南京", "nanjing");
        addCity("江苏省", "无锡", "wuxi");
        addCity("江苏省", "徐州", "xuzhou");
        addCity("江苏省", "常州", "changzhou");
        addCity("江苏省", "苏州", "suzhou");
        addCity("江苏省", "南通", "nantong");
        addCity("江苏省", "连云港", "lianyungang");
        addCity("江苏省", "淮安", "huaian");
        addCity("江苏省", "盐城", "yancheng");
        addCity("江苏省", "扬州", "yangzhou");
        addCity("江苏省", "镇江", "zhenjiang");
        addCity("江苏省", "泰州", "taizhou");
        addCity("江苏省", "宿迁", "suqian");

        addCity("浙江省", "杭州", "hangzhou");
        addCity("浙江省", "宁波", "ningbo");
        addCity("浙江省", "温州", "wenzhou");
        addCity("浙江省", "嘉兴", "jiaxing");
        addCity("浙江省", "湖州", "huzhou");
        addCity("浙江省", "绍兴", "shaoxing");
        addCity("浙江省", "金华", "jinhua");
        addCity("浙江省", "衢州", "quzhou");
        addCity("浙江省", "舟山", "zhoushan");
        addCity("浙江省", "台州", "taizhou");
        addCity("浙江省", "丽水", "lishui");

        addCity("安徽省", "合肥", "hefei");
        addCity("安徽省", "芜湖", "wuhu");
        addCity("安徽省", "蚌埠", "bengbu");
        addCity("安徽省", "淮南", "huainan");
        addCity("安徽省", "马鞍山", "maanshan");
        addCity("安徽省", "淮北", "huaibei");
        addCity("安徽省", "铜陵", "tongling");
        addCity("安徽省", "安庆", "anqing");
        addCity("安徽省", "黄山", "huangshan");
        addCity("安徽省", "滁州", "chuzhou");
        addCity("安徽省", "阜阳", "fuyang");
        addCity("安徽省", "宿州", "suzhou");
        addCity("安徽省", "六安", "luan");
        addCity("安徽省", "亳州", "bozhou");
        addCity("安徽省", "池州", "chizhou");
        addCity("安徽省", "宣城", "xuancheng");

        addCity("福建省", "福州", "fuzhou");
        addCity("福建省", "厦门", "xiamen");
        addCity("福建省", "莆田", "putian");
        addCity("福建省", "三明", "sanming");
        addCity("福建省", "泉州", "quanzhou");
        addCity("福建省", "漳州", "zhangzhou");
        addCity("福建省", "南平", "nanping");
        addCity("福建省", "龙岩", "longyan");
        addCity("福建省", "宁德", "ningde");

        addCity("江西省", "南昌", "nanchang");
        addCity("江西省", "景德镇", "jingdezhen");
        addCity("江西省", "萍乡", "pingxiang");
        addCity("江西省", "九江", "jiujiang");
        addCity("江西省", "新余", "xinyu");
        addCity("江西省", "鹰潭", "yingtan");
        addCity("江西省", "赣州", "ganzhou");
        addCity("江西省", "吉安", "jian");
        addCity("江西省", "宜春", "yichun");
        addCity("江西省", "抚州", "fuzhou");
        addCity("江西省", "上饶", "shangrao");

        addCity("山东省", "济南", "jinan");
        addCity("山东省", "青岛", "qingdao");
        addCity("山东省", "淄博", "zibo");
        addCity("山东省", "枣庄", "zaozhuang");
        addCity("山东省", "东营", "dongying");
        addCity("山东省", "烟台", "yantai");
        addCity("山东省", "潍坊", "weifang");
        addCity("山东省", "济宁", "jining");
        addCity("山东省", "泰安", "taian");
        addCity("山东省", "威海", "weihai");
        addCity("山东省", "日照", "rizhao");
        addCity("山东省", "临沂", "linyi");
        addCity("山东省", "德州", "dezhou");
        addCity("山东省", "聊城", "liaocheng");
        addCity("山东省", "滨州", "binzhou");
        addCity("山东省", "菏泽", "heze");

        addCity("河南省", "郑州", "zhengzhou");
        addCity("河南省", "开封", "kaifeng");
        addCity("河南省", "洛阳", "luoyang");
        addCity("河南省", "平顶山", "pingdingshan");
        addCity("河南省", "安阳", "anyang");
        addCity("河南省", "鹤壁", "hebi");
        addCity("河南省", "新乡", "xinxiang");
        addCity("河南省", "焦作", "jiaozuo");
        addCity("河南省", "濮阳", "puyang");
        addCity("河南省", "许昌", "xuchang");
        addCity("河南省", "漯河", "luohe");
        addCity("河南省", "三门峡", "sanmenxia");
        addCity("河南省", "南阳", "nanyang");
        addCity("河南省", "商丘", "shangqiu");
        addCity("河南省", "信阳", "xinyang");
        addCity("河南省", "周口", "zhoukou");
        addCity("河南省", "驻马店", "zhumadian");

        addCity("湖北省", "武汉", "wuhan");
        addCity("湖北省", "黄石", "huangshi");
        addCity("湖北省", "十堰", "shiyan");
        addCity("湖北省", "宜昌", "yichang");
        addCity("湖北省", "襄阳", "xiangyang");
        addCity("湖北省", "鄂州", "ezhou");
        addCity("湖北省", "荆门", "jingmen");
        addCity("湖北省", "孝感", "xiaogan");
        addCity("湖北省", "荆州", "jingzhou");
        addCity("湖北省", "黄冈", "huanggang");
        addCity("湖北省", "咸宁", "xianning");
        addCity("湖北省", "随州", "suizhou");

        addCity("湖南省", "长沙", "changsha");
        addCity("湖南省", "株洲", "zhuzhou");
        addCity("湖南省", "湘潭", "xiangtan");
        addCity("湖南省", "衡阳", "hengyang");
        addCity("湖南省", "邵阳", "shaoyang");
        addCity("湖南省", "岳阳", "yueyang");
        addCity("湖南省", "常德", "changde");
        addCity("湖南省", "张家界", "zhangjiajie");
        addCity("湖南省", "益阳", "yiyang");
        addCity("湖南省", "郴州", "chenzhou");
        addCity("湖南省", "永州", "yongzhou");
        addCity("湖南省", "怀化", "huaihua");
        addCity("湖南省", "娄底", "loudi");

        addCity("广东省", "广州", "guangzhou");
        addCity("广东省", "韶关", "shaoguan");
        addCity("广东省", "深圳", "shenzhen");
        addCity("广东省", "珠海", "zhuhai");
        addCity("广东省", "汕头", "shantou");
        addCity("广东省", "佛山", "foshan");
        addCity("广东省", "江门", "jiangmen");
        addCity("广东省", "湛江", "zhanjiang");
        addCity("广东省", "茂名", "maoming");
        addCity("广东省", "肇庆", "zhaoqing");
        addCity("广东省", "惠州", "huizhou");
        addCity("广东省", "梅州", "meizhou");
        addCity("广东省", "汕尾", "shanwei");
        addCity("广东省", "河源", "heyuan");
        addCity("广东省", "阳江", "yangjiang");
        addCity("广东省", "清远", "qingyuan");
        addCity("广东省", "东莞", "dongguan");
        addCity("广东省", "中山", "zhongshan");
        addCity("广东省", "潮州", "chaozhou");
        addCity("广东省", "揭阳", "jieyang");
        addCity("广东省", "云浮", "yunfu");

        addCity("广西壮族自治区", "南宁", "nanning");
        addCity("广西壮族自治区", "柳州", "liuzhou");
        addCity("广西壮族自治区", "桂林", "guilin");
        addCity("广西壮族自治区", "梧州", "wuzhou");
        addCity("广西壮族自治区", "北海", "beihai");
        addCity("广西壮族自治区", "防城港", "fangchenggang");
        addCity("广西壮族自治区", "钦州", "qinzhou");
        addCity("广西壮族自治区", "贵港", "guigang");
        addCity("广西壮族自治区", "玉林", "yulin");
        addCity("广西壮族自治区", "百色", "baise");
        addCity("广西壮族自治区", "贺州", "hezhou");
        addCity("广西壮族自治区", "河池", "hechi");
        addCity("广西壮族自治区", "来宾", "laibin");
        addCity("广西壮族自治区", "崇左", "chongzuo");

        addCity("海南省", "海口", "haikou");
        addCity("海南省", "三亚", "sanya");
        addCity("海南省", "三沙", "sansha");
        addCity("海南省", "儋州", "danzhou");

        addCity("重庆市", "重庆", "chongqing");

        addCity("四川省", "成都", "chengdu");
        addCity("四川省", "自贡", "zigong");
        addCity("四川省", "攀枝花", "panzhihua");
        addCity("四川省", "泸州", "luzhou");
        addCity("四川省", "德阳", "deyang");
        addCity("四川省", "绵阳", "mianyang");
        addCity("四川省", "广元", "guangyuan");
        addCity("四川省", "遂宁", "suining");
        addCity("四川省", "内江", "neijiang");
        addCity("四川省", "乐山", "leshan");
        addCity("四川省", "南充", "nanchong");
        addCity("四川省", "眉山", "meishan");
        addCity("四川省", "宜宾", "yibin");
        addCity("四川省", "广安", "guangan");
        addCity("四川省", "达州", "dazhou");
        addCity("四川省", "雅安", "yaan");
        addCity("四川省", "巴中", "bazhong");
        addCity("四川省", "资阳", "ziyang");

        addCity("贵州省", "贵阳", "guiyang");
        addCity("贵州省", "六盘水", "liupanshui");
        addCity("贵州省", "遵义", "zunyi");
        addCity("贵州省", "安顺", "anshun");
        addCity("贵州省", "毕节", "bijie");
        addCity("贵州省", "铜仁", "tongren");

        addCity("云南省", "昆明", "kunming");
        addCity("云南省", "曲靖", "qujing");
        addCity("云南省", "玉溪", "yuxi");
        addCity("云南省", "保山", "baoshan");
        addCity("云南省", "昭通", "zhaotong");
        addCity("云南省", "丽江", "lijiang");
        addCity("云南省", "普洱", "puer");
        addCity("云南省", "临沧", "lincang");

        addCity("西藏自治区", "拉萨", "lasa");
        addCity("西藏自治区", "日喀则", "rikaze");
        addCity("西藏自治区", "昌都", "changdu");
        addCity("西藏自治区", "林芝", "linzhi");
        addCity("西藏自治区", "山南", "shannan");
        addCity("西藏自治区", "那曲", "naqu");
        addCity("西藏自治区", "阿里", "ali");

        addCity("陕西省", "西安", "xian");
        addCity("陕西省", "铜川", "tongchuan");
        addCity("陕西省", "宝鸡", "baoji");
        addCity("陕西省", "咸阳", "xianyang");
        addCity("陕西省", "渭南", "weinan");
        addCity("陕西省", "延安", "yanan");
        addCity("陕西省", "汉中", "hanzhong");
        addCity("陕西省", "榆林", "yulin");
        addCity("陕西省", "安康", "ankang");
        addCity("陕西省", "商洛", "shangluo");

        addCity("甘肃省", "兰州", "lanzhou");
        addCity("甘肃省", "嘉峪关", "jiayuguan");
        addCity("甘肃省", "金昌", "jinchang");
        addCity("甘肃省", "白银", "baiyin");
        addCity("甘肃省", "天水", "tianshui");
        addCity("甘肃省", "武威", "wuwei");
        addCity("甘肃省", "张掖", "zhangye");
        addCity("甘肃省", "平凉", "pingliang");
        addCity("甘肃省", "酒泉", "jiuquan");
        addCity("甘肃省", "庆阳", "qingyang");
        addCity("甘肃省", "定西", "dingxi");
        addCity("甘肃省", "陇南", "longnan");

        addCity("青海省", "西宁", "xining");
        addCity("青海省", "海东", "haidong");

        addCity("宁夏回族自治区", "银川", "yinchuan");
        addCity("宁夏回族自治区", "石嘴山", "shizuishan");
        addCity("宁夏回族自治区", "吴忠", "wuzhong");
        addCity("宁夏回族自治区", "固原", "guyuan");
        addCity("宁夏回族自治区", "中卫", "zhongwei");

        addCity("新疆维吾尔自治区", "乌鲁木齐", "wulumuqi");
        addCity("新疆维吾尔自治区", "克拉玛依", "kelamayi");
        addCity("新疆维吾尔自治区", "吐鲁番", "tulufan");
        addCity("新疆维吾尔自治区", "哈密", "hami");
        addCity("新疆维吾尔自治区", "昌吉", "changji");
        addCity("新疆维吾尔自治区", "博尔塔拉", "boertala");
        addCity("新疆维吾尔自治区", "巴音郭楞", "bayinguoleng");
        addCity("新疆维吾尔自治区", "阿克苏", "akesu");
        addCity("新疆维吾尔自治区", "克孜勒苏", "kezilesu");
        addCity("新疆维吾尔自治区", "喀什", "kashi");
        addCity("新疆维吾尔自治区", "和田", "hetian");
        addCity("新疆维吾尔自治区", "伊犁", "yili");
        addCity("新疆维吾尔自治区", "塔城", "tacheng");
        addCity("新疆维吾尔自治区", "阿勒泰", "aletai");

        addCity("香港特别行政区", "香港", "xianggang");
        addCity("澳门特别行政区", "澳门", "aomen");
        addCity("台湾省", "台北", "taibei");
        addCity("台湾省", "高雄", "gaoxiong");
        addCity("台湾省", "基隆", "jilong");
        addCity("台湾省", "新竹", "xinzhu");
        addCity("台湾省", "台中", "taizhong");
        addCity("台湾省", "嘉义", "jiayi");
        addCity("台湾省", "台南", "tainan");
    }

    private static void addCity(String province, String name, String pinyin) {
        City city = new City(province, name, pinyin);
        ALL_CITIES.add(city);

        if (!PROVINCE_CITIES.containsKey(province)) {
            PROVINCE_CITIES.put(province, new ArrayList<>());
        }
        PROVINCE_CITIES.get(province).add(city);
    }

    public static List<City> getAllCities() {
        return new ArrayList<>(ALL_CITIES);
    }

    public static Map<String, List<City>> getProvinceCityMap() {
        return new HashMap<>(PROVINCE_CITIES);
    }

    public static List<String> getProvinces() {
        return new ArrayList<>(PROVINCE_CITIES.keySet());
    }

    public static List<City> searchCities(String keyword) {
        List<City> result = new ArrayList<>();
        if (keyword == null || keyword.isEmpty()) {
            return result;
        }

        String lowerKeyword = keyword.toLowerCase();
        for (City city : ALL_CITIES) {
            if (city.getName().contains(keyword) ||
                    city.getPinyin().contains(lowerKeyword) ||
                    city.getProvince().contains(keyword)) {
                result.add(city);
            }
        }
        return result;
    }

    public static List<City> getCitiesByProvince(String province) {
        return PROVINCE_CITIES.containsKey(province) ? new ArrayList<>(PROVINCE_CITIES.get(province))
                : new ArrayList<>();
    }
}
