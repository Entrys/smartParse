package service;

import pojo.Area;
import pojo.ShippingAddress;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author li.tao
 * @since 2019/11/20 11:35
 */
public class AreaService {
    private static final String[] familyNames = {"赵","钱","孙","李","周","吴","郑","王","冯","陈","楮","卫","蒋","沈","韩","杨","朱","秦","尤","许","何","吕","施","张","孔","曹","严","华","金","魏","陶","姜","戚","谢","邹","喻","柏","水","窦","章","云","苏","潘","葛","奚","范","彭","郎","鲁","韦","昌","马","苗","凤","花","方","俞","任","袁","柳","酆","鲍","史","唐","费","廉","岑","薛","雷","贺","倪","汤","滕","殷","罗","毕","郝","邬","安","常","乐","于","时","傅","皮","卞","齐","康","伍","余","元","卜","顾","孟","平","黄","和","穆","萧","尹","姚","邵","湛","汪","祁","毛","禹","狄","米","贝","明","臧","计","伏","成","戴","谈","宋","茅","庞","熊","纪","舒","屈","项","祝","董","梁","杜","阮","蓝","闽","席","季","麻","强","贾","路","娄","危","江","童","颜","郭","梅","盛","林","刁","锺","徐","丘","骆","高","夏","蔡","田","樊","胡","凌","霍","虞","万","支","柯","昝","管","卢","莫","经","房","裘","缪","干","解","应","宗","丁","宣","贲","邓","郁","单","杭","洪","包","诸","左","石","崔","吉","钮","龚","程","嵇","邢","滑","裴","陆","荣","翁","荀","羊","於","惠","甄","麹","家","封","芮","羿","储","靳","汲","邴","糜","松","井","段","富","巫","乌","焦","巴","弓","牧","隗","山","谷","车","侯","宓","蓬","全","郗","班","仰","秋","仲","伊","宫","宁","仇","栾","暴","甘","斜","厉","戎","祖","武","符","刘","景","詹","束","龙","叶","幸","司","韶","郜","黎","蓟","薄","印","宿","白","怀","蒲","邰","从","鄂","索","咸","籍","赖","卓","蔺","屠","蒙","池","乔","阴","郁","胥","能","苍","双","闻","莘","党","翟","谭","贡","劳","逄","姬","申","扶","堵","冉","宰","郦","雍","郤","璩","桑","桂","濮","牛","寿","通","边","扈","燕","冀","郏","浦","尚","农","温","别","庄","晏","柴","瞿","阎","充","慕","连","茹","习","宦","艾","鱼","容","向","古","易","慎","戈","廖","庾","终","暨","居","衡","步","都","耿","满","弘","匡","国","文","寇","广","禄","阙","东","欧","殳","沃","利","蔚","越","夔","隆","师","巩","厍","聂","晁","勾","敖","融","冷","訾","辛","阚","那","简","饶","空","曾","毋","沙","乜","养","鞠","须","丰","巢","关","蒯","相","查","后","荆","红","游","竺","权","逑","盖","益","桓","公","万俟","司马","上官","欧阳","夏侯","诸葛","闻人","东方","赫连","皇甫","尉迟","公羊","澹台","公冶","宗政","濮阳","淳于","单于","太叔","申屠","公孙","仲孙","轩辕","令狐","锺离","宇文","长孙","慕容","鲜于","闾丘","司徒","司空","丌官","司寇","仉","督","子车","颛孙","端木","巫马","公西","漆雕","乐正","壤驷","公良","拓拔","夹谷","宰父","谷梁","晋","楚","阎","法","汝","鄢","涂","钦","段干","百里","东郭","南门","呼延","归","海","羊舌","微生","岳","帅","缑","亢","况","后","有","琴","梁丘","左丘","东门","西门","商","牟","佘","佴","伯","赏","南宫","墨","哈","谯","笪","年","爱","阳","佟","第五","言","福"};

    private static final String[] provinceIgnoreNames = {"省", "特别行政区", "维吾尔自治区", "壮族自治区", "回族自治区", "自治区"};
    private static final String[] cityIgnoreNames = {"市", "地区", "盟", "土家族苗族自治州", "藏族羌族自治州", "蒙古族藏族自治州", "布依族苗族自治州", "苗族侗族自治州",
            "布依族苗族自治州", "哈尼族彝族自治州", "壮族苗族自治州", "傣族自治州", "白族自治州", "傣族景颇族自治州", "傈僳族自治州",
            "回族自治州",  "柯尔克孜自治州", "朝鲜族自治州",  "藏族自治州", "彝族自治州", "蒙古自治州", "哈萨克自治州"};
    private static final String[] countyIgnoreNames ={"区", "县", "市", "左旗", "右旗", "联合旗", "后旗", "中旗", "自治旗", "前旗", "白旗", "左翼", "右翼", "群岛", "岛", "旗"};

    private List<Area> provinces = new ArrayList<>();
    private List<Area> cities = new ArrayList<>();
    private List<Area> countries = new ArrayList<>();

    public ShippingAddress DiscernAddressInfo(String addressInfo) {
        String regEx = "[`~!@#$^&*()=|{}':;',\\\\[\\\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“’。，、？]";
        addressInfo = Pattern.compile(regEx).matcher(addressInfo).replaceAll(" ").trim();
        addressInfo = addressInfo.replace("-", "");

        Result result = ToAnalysis.parse(addressInfo);
        //List<String> areaNames = new ArrayList<>();
        List<String> recipients = new ArrayList<>();
        List<String> phones = new ArrayList<>();
        //拿到terms
        List<Term> terms = result.getTerms();
        terms.forEach(term -> {
            String word = term.getName(); //拿到词
            String natureStr = term.getNatureStr(); //拿到词性
            if ("nr".equals(natureStr)) {
                recipients.add(word);
            }
/*            if ("ns".equals(natureStr)) {
                areaNames.add(word);
            }*/
            if ("m".equals(natureStr)) {
                phones.add(word);
            }
        });
        ShippingAddress shippingAddress = new ShippingAddress();
        Integer provinceCode = null;
        Integer cityCode = null;

        /*识别电话号码*/
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(phones)) {
            for (String xphone : phones) {
                if (Validator.isMobile(xphone)) {
                    shippingAddress.setPhone(xphone);
                    addressInfo = addressInfo.replace(xphone, " ");
                    break;
                }
            }
        } else {
            Pattern pattern = Pattern.compile("(86-[1][0-9]{10}) | (86[1][0-9]{10})|([1][0-9]{10})|([1][0-9][0-9]-{4}-{4})");
            Matcher matcher = pattern.matcher(addressInfo);
            while (matcher.find()) {
                shippingAddress.setPhone(matcher.group());
                //去掉已经识别的电话，防止加入详细地址
                addressInfo = addressInfo.replace(matcher.group(), " ");
                break;
            }
        }

        /*识别收货人昵称*/
        Pattern namePattern = Pattern.compile("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*");
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(recipients)) {
            for (String recipient : recipients) {
                if (StringUtils.isNotEmpty(shippingAddress.getName())) {
                    break;
                }
                Matcher nameMatcher = namePattern.matcher(recipient);
                while (nameMatcher.find()) {
                    if (StringUtils.isNotEmpty(shippingAddress.getName())) {
                        break;
                    }
                    String recognizedName = nameMatcher.group();
                    for (String familyName : familyNames) {
                        if (familyName.equals(String.valueOf(recognizedName.charAt(0)))) {
                            shippingAddress.setName(recognizedName);
                            //去掉已经识别的名称，防止加入详细地址
                            addressInfo = addressInfo.replace(recognizedName, " ");
                            break;
                        }
                    }
                }
            }
        } else {
            Matcher nameMatcher = namePattern.matcher(addressInfo);
            while (nameMatcher.find()) {
                if (StringUtils.isNotEmpty(shippingAddress.getName())) {
                    break;
                }
                String recognizedName = nameMatcher.group();
                for (String familyName : familyNames) {
                    if (familyName.equals(String.valueOf(recognizedName.charAt(0)))) {
                        shippingAddress.setName(recognizedName);
                        //去掉已经识别的名称，防止加入详细地址
                        addressInfo = addressInfo.replace(recognizedName, " ");
                        break;
                    }
                }
            }
        }

        /*识别地址信息*/
        for (Area province : this.provinces) {
            String provinceName = province.getName();
            for (String provinceIgnoreName : provinceIgnoreNames) {
                provinceName = provinceName.replace(provinceIgnoreName, " ");
                addressInfo = addressInfo.replace(provinceIgnoreName, " ");
            }
            if (addressInfo.contains(provinceName)) {
                //更新选择地址信息
                provinceCode = province.getId();
                //去掉已经识别的省
                addressInfo = addressInfo.replace(provinceName, " ");
                break;
            }
        }

        //当输入框中有省份的时候，据省编码获取市列表
        if (NumberUtils.isPositive(provinceCode)) {
            List<Area> cityList = this.findByParent(provinceCode, 2);
            for (Area city : cityList) {
                String cityName = city.getName();
                for (String cityIgnoreName : cityIgnoreNames) {
                    cityName = cityName.replace(cityIgnoreName, " ");
                    addressInfo = addressInfo.replace(cityIgnoreName, " ");
                }
                if (addressInfo.contains(cityName)) {
                    //更新选择地址信息
                    cityCode = city.getId();
                    addressInfo = addressInfo.replace(cityName, " ");
                    break;
                }
            }
        } else {
            //只输入了市
            for (Area city : this.cities) {
                String cityName = city.getName();
                for (String cityIgnoreName : cityIgnoreNames) {
                    cityName = cityName.replace(cityIgnoreName, " ");
                    addressInfo = addressInfo.replace(cityIgnoreName, " ");
                }
                if (addressInfo.contains(cityName)) {
                    //更新选择地址信息
                    cityCode = city.getId();
                    addressInfo = addressInfo.replace(cityName, " ");
                    break;
                }
            }
        }

        //如果输入框包含市
        if (NumberUtils.isPositive(cityCode)) {
            //根据市名称、省份编码获取该市编码
            List<Area> areaList = this.findByParent(cityCode, 3);
            for (Area county : areaList) {
                String countyName = county.getName();
                for (String countyIgnoreName : countyIgnoreNames) {
                    countyName = countyName.replace(countyIgnoreName, " ");
                    addressInfo = addressInfo.replace(countyIgnoreName, " ");
                }
                if (addressInfo.contains(countyName)) {
                    shippingAddress.setAreaId(county.getId());
                    /*截取详细地址发字符*/
                    addressInfo = addressInfo.replace(countyName, " ");
                    break;
                }
            }
        } else {
            //只输入 镇、县
            for (Area county : this.countries) {
                String countyName = county.getName();
                for (String countyIgnoreName : countyIgnoreNames) {
                    countyName = countyName.replace(countyIgnoreName, " ");
                    addressInfo = addressInfo.replace(countyIgnoreName, " ");
                }
                if (addressInfo.contains(countyName)) {
                    shippingAddress.setAreaId(county.getId());
                    /*截取详细地址发字符*/
                    addressInfo = addressInfo.replace(countyName, " ");
                    break;
                }
            }
        }
        shippingAddress.setAddress(StringUtils.trimToNull(addressInfo));
        if (StringUtils.isAllBlank(shippingAddress.getPhone(), shippingAddress.getName()) && NumberUtils.isNotPositive(shippingAddress.getAreaId())){
            throw new YanweiException(ResponseCode.ShippingAddressDiscernFailed);
        }
        return shippingAddress;
    }

}
