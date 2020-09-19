package com.dcms.service;

import com.dcms.pojo.login.LoginCount;
import com.dcms.pojo.other.CustomerWeekData;
import com.dcms.repository.LoginCountRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/14 0014 23:08
 * Description:
 **/
@Service
public class LoginCountService {
    @Resource
    private LoginCountRepository loginCountRepository;

    public List<LoginCount> findAll() {
        List<LoginCount> all = loginCountRepository.findAll();
        return all;
    }

    public List<LoginCount> findByUserId(String userId) {
        List<LoginCount> byUserId = loginCountRepository.findByUserId(userId);
        return byUserId;
    }

    public Integer save(LoginCount loginCount) {
        loginCount.setLoginId(getLoginId()[0]);
        loginCount.setLoginData(getLoginId()[1]);
        Integer index = loginCountRepository.save(loginCount);
        return index;
    }

    public Integer deleteById(String loginId) {
        Integer index = loginCountRepository.deleteById(loginId);
        return index;
    }
    /*管理员模块*/
    //获取用户登陆数据
    public List<CustomerWeekData> getWeekData(){
        List<CustomerWeekData> weekData = new ArrayList<>();
        CustomerWeekData type1 = new CustomerWeekData();
        CustomerWeekData type2 = new CustomerWeekData();
        CustomerWeekData type3 = new CustomerWeekData();
        Integer[] datas1 = {0,0,0,0,0,0,0};
        Integer[] datas2 = {0,0,0,0,0,0,0};
        Integer[] datas3 = {0,0,0,0,0,0,0};
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String beginDate  = sdf.format(getPreviousMonday());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        String endDate  = sdf1.format(getSunday());
        endDate = endDate + "235959";
        List<LoginCount> weekDatas = loginCountRepository.getWeekData(beginDate, endDate);
        if(weekDatas != null && weekDatas.size() > 0){
            for (LoginCount cc:weekDatas) {
                if(cc.getUserSort() != null && cc.getUserSort() == 0){
                    for(int i = 0;i < weekDays.length;i++){
                        if(cc.getLoginData() != null && dateToWeek(cc.getLoginData()).equals(weekDays[i])){
                            datas1[i] = datas1[i] + 1;
                            break;
                        }
                    }
                }else if(cc.getUserSort() != null && cc.getUserSort() == 1){
                    for(int i = 0;i < weekDays.length;i++){
                        if(dateToWeek(cc.getLoginData()) == weekDays[i]){
                            datas2[i] = datas2[i] + 1;
                            break;
                        }
                    }
                }else if(cc.getUserSort() != null && cc.getUserSort() == 3){
                    for(int i = 0;i < weekDays.length;i++){
                        if(dateToWeek(cc.getLoginData()) == weekDays[i]){
                            datas3[i] = datas3[i] + 1;
                            break;
                        }
                    }
                }
            }
        }
        type1.setData(datas1);
        type1.setName("客户");
        type2.setData(datas2);
        type2.setName("医生");
        type3.setData(datas3);
        type3.setName("管理员");
        weekData.add(type1);
        weekData.add(type2);
        weekData.add(type3);
        return weekData;
    }
    // 自动生成下一登录记录ID
    private String[] getLoginId() {
        String[] NextLogin = new String[2];
        String NextLoginId = "";
        // 获取当前日期并转化为字符串
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String s1 = s.format(new Date());
        // 获取该日期下已存在的记录数量
        Integer count = loginCountRepository.getCount(s1);
        // 改数量加1，获取下一个记录的ID尾号
        count++;
        // 若下一编号ID长度不足4位，则前面补0
        int length = count.toString().length();
        if (length < 3) {
            int i = 3 - length;
            for (int j = 0; j < i; j++) {
                NextLoginId = NextLoginId + "0";
            }
            NextLoginId = s1 + "S" + NextLoginId + count;
//            System.out.println("下一个记录编号为：" + NextLoginId);
        }
        SimpleDateFormat ss = new SimpleDateFormat("yyyyMMddHHmmss");
        String s2 = ss.format(new Date());
        NextLogin[0] = NextLoginId;
        NextLogin[1] = s2;
        return NextLogin;
    }
    // 获取上周日的date
    public Date getSunday() {
        Calendar cal = Calendar.getInstance();
        //将每周第一天设为星期一，默认是星期天
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.add(Calendar.DATE, -1*7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    // 获取上周一的date
    public Date getPreviousMonday() {
        Calendar cal = Calendar.getInstance();
        // 将每周第一天设为星期一，默认是星期天
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.add(Calendar.DATE, -1 * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    // 获取该日期是周几
    public static String dateToWeek(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = sdf.parse(datetime);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDays[w];
    }
}
