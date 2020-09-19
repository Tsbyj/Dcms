package com.dcms.service;

import com.dcms.pojo.login.LoginInfo;
import com.dcms.pojo.other.*;
import com.dcms.pojo.cases.CaseInfo;
import com.dcms.pojo.cus.Customer;
import com.dcms.repository.CaseInfoRepository;
import com.dcms.repository.CustomerRepository;
import com.dcms.repository.LoginInfoRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/7 0007 18:27
 * Description:
 **/
@Service
public class CustomerService {
    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private CaseInfoRepository caseInfoRepository;
    @Resource
    private LoginInfoRepository loginInfoRepository;
    public int getAllCount(){
        return customerRepository.getAllCount();
    }

    public LayData findAllByDoc(String docId){
        LayData layData = new LayData();
        List<Customer> cusList = new ArrayList<>();
        List<CaseInfo> byDocId = caseInfoRepository.findByDocId(docId);
        Set<String> cusId = new HashSet<>();
        // 去除重复
        for (CaseInfo c:byDocId) {
            cusId.add(c.getCustomerId());
        }
        for(String id : cusId){
            Customer byId = customerRepository.findById(id);
            cusList.add(byId);
        }
        if(cusList.size() > 0){
            layData.setCode(0);
            layData.setData(cusList);
            layData.setCount(cusList.size());
            layData.setMsg("该医生的所有客户");
        }else {
            layData.setCode(0);
        }
        return layData;
    }

    public LayData findAll(Integer page, Integer limit) {
        LayData cusList = new LayData();
        List<Customer> all = customerRepository.findAll(((page - 1) * limit), limit);
        int count = customerRepository.getAllCount();
        if (all != null) {
            cusList.setCode(0);
            cusList.setData(all);
            cusList.setCount(count);
            cusList.setMsg("所有用户信息");
        } else {
            cusList.setCode(0);
        }
        return cusList;
    }
    // 分页展示查询
    public List<Customer> findAllCus() {
        int allCount = customerRepository.getAllCount();
        return customerRepository.findAll(0, allCount);
    }

    public Customer findById(String customerId) {
        Customer customer = customerRepository.findById(customerId);
        return customer;
    }
    // 用户列表界面：按ID搜索
    public LayData<Customer> layFindById(String customerId) {
        LayData cus = new LayData();
        Customer customer = customerRepository.findById(customerId);
        if(customer != null){
            List<Customer> cust = new ArrayList<>();
            cust.add(customer);
            cus.setData(cust);
            cus.setCount(1);
            cus.setMsg("按ID查找信息");
            cus.setCode(0);
        }else {
            cus.setCode(0);
        }
        return cus;
    }
    // 用户列表界面：按Name搜索
    public LayData layFindByName(String customerName) {
        LayData cus = new LayData();
        List<Customer> byName = customerRepository.findByName(customerName);
        if(byName.size() > 0){
            cus.setData(byName);
            cus.setCount(byName.size());
            cus.setMsg("按Name查找信息");
            cus.setCode(0);
        }else {
            cus.setCode(0);
        }
        return cus;
    }

    public int save(Customer customer) {
        customer.setCustomerId(getCustomerId());
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserId(customer.getCustomerId());
        loginInfo.setUsername(customer.getCustomerName());
        loginInfo.setPassword("123123");
        loginInfo.setUserSort(1);
        Integer save = loginInfoRepository.save(loginInfo);
        System.out.println("客户 账号信息存储状态：" + save);
        return customerRepository.save(customer);
    }

    public int update(Customer customer) {
        return customerRepository.update(customer);
    }

    public Integer getCount(String CustomerIdPrefix) {
        return customerRepository.getCount(CustomerIdPrefix);
    }

    public Integer deleteById(String customerId) {
        Integer index = loginInfoRepository.deleteById(customerId);
        System.out.println("客户 账号信息删除结果：" + index);
        return customerRepository.deleteById(customerId);
    }

    /*管理员模块*/
    // 获取客户的住址数据(重点)
    public CustomerAreaData findCusAreaData() {
        CustomerAreaData index = new CustomerAreaData();
        List<CustomerAddress> addr = new ArrayList<>();
        int allCount = customerRepository.getAllCount();
        List<Customer> all = customerRepository.findAll(0, allCount);
        List<CustomerAddress> addr2 = new ArrayList<>();
        for (Customer customer : all) {
            // 每次循环开始前，先清空addr2
            addr2.clear();
            // 拷贝数组
            for (CustomerAddress cc : addr) {
                try {
                    addr2.add(cc.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            if (customer.getAddress() == null || !customer.getAddress().contains("省")) {
                continue;
            }
            String area = getArea(customer.getAddress());
            for (CustomerAddress c1 : addr) {
                if (area.equals(c1.getName())) {
                    c1.setValue(c1.getValue() + 1);
                    break;  //结束该循环
                }
            }
            if (null != addr && null != addr2) {
                if (addr.containsAll(addr2) && addr2.containsAll(addr)) {
                    CustomerAddress aa = new CustomerAddress();
                    aa.setValue(1);
                    aa.setName(area);
                    addr.add(aa);
                } else {
                    continue;
                }
            }
        }
        index.setAddressCount(addr);
        String[] areaName = new String[addr.size()];
        for (int j = 0; j < addr.size(); j++) {
            areaName[j] = addr.get(j).getName();
        }
        index.setAreaName(areaName);
        return index;
    }

    // 获取客户的年龄段信息
    public CustomerLoginData findCusLoginMes() {
        CustomerLoginData cLogin = new CustomerLoginData();
        int allCount = customerRepository.getAllCount();
        List<Customer> all = customerRepository.findAll(0, allCount);
        List<Integer> data = new ArrayList<>();
        Integer a1 = 0, a2 = 0, a3 = 0, a4 = 0, a5 = 0, a6 = 0, a7 = 0, a8 = 0, a9 = 0, a10 = 0, a11 = 0, a12 = 0, a13 = 0;
        for (Customer cc : all) {
            if (cc.getAge() < 5) {
                a1++;
            } else if (cc.getAge() >= 5 && cc.getAge() < 10) {
                a2++;
            } else if (cc.getAge() >= 10 && cc.getAge() < 15) {
                a3++;
            } else if (cc.getAge() >= 15 && cc.getAge() < 20) {
                a4++;
            } else if (cc.getAge() >= 20 && cc.getAge() < 25) {
                a5++;
            } else if (cc.getAge() >= 25 && cc.getAge() < 30) {
                a6++;
            } else if (cc.getAge() >= 30 && cc.getAge() < 35) {
                a7++;
            } else if (cc.getAge() >= 35 && cc.getAge() < 40) {
                a8++;
            } else if (cc.getAge() >= 40 && cc.getAge() < 45) {
                a9++;
            } else if (cc.getAge() >= 45 && cc.getAge() < 50) {
                a10++;
            } else if (cc.getAge() >= 50 && cc.getAge() < 55) {
                a11++;
            } else if (cc.getAge() >= 55 && cc.getAge() < 60) {
                a12++;
            } else if (cc.getAge() >= 60) {
                a13++;
            }
        }
        data.add(a1);
        data.add(a2);
        data.add(a3);
        data.add(a4);
        data.add(a5);
        data.add(a6);
        data.add(a7);
        data.add(a8);
        data.add(a9);
        data.add(a10);
        data.add(a11);
        data.add(a12);
        data.add(a13);
        cLogin.setData(data);
        return cLogin;
    }

    //获取客户性别比例信息
    public List<CustomerSexData> findCusSexData(){
        List<CustomerSexData> sexData = new ArrayList<>();
        CustomerSexData sex1 = new CustomerSexData();
        CustomerSexData sex2 = new CustomerSexData();
        sex1.setValue(0);
        sex1.setName("男性");
        sex2.setValue(0);
        sex2.setName("女性");
        int allCount = customerRepository.getAllCount();
        List<Customer> all = customerRepository.findAll(0, allCount);
        for (Customer cc : all) {
            if(cc.getSex().equals("男")){
                sex1.setValue(sex1.getValue()+1);
            }else if(cc.getSex().equals("女")){
                sex2.setValue(sex2.getValue()+1);
            }
        }
        sexData.add(sex1);
        sexData.add(sex2);
        return sexData;
    }

    // 工具方法
    private String getArea(String address) {
        String s1;
        int index = address.indexOf("省");
        s1 = address.substring(0, index + 1);
        return s1;
    }

    // 自动生成下一客户ID
    private String getCustomerId() {
        String NextCustomerId = "";
        // 获取当前日期并转化为字符串
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String s1 = s.format(new Date());
        // 获取该日期下已注册的客户数量
        Integer count = customerRepository.getCount(s1);
        // 改数量加1，获取下一个客户的ID尾号
        count++;
        // 若下一编号ID长度不足4位，则前面补0
        int length = count.toString().length();
        if (length < 4) {
            int i = 4 - length;
            for (int j = 0; j < i; j++) {
                NextCustomerId = NextCustomerId + "0";
            }
            NextCustomerId = s1 + NextCustomerId + count;
        }
        while (true){
            Customer byId = customerRepository.findById(NextCustomerId);
            if(byId == null){
                break;
            }else {
                String pro = NextCustomerId.substring(0, NextCustomerId.length() - length);
                String end = NextCustomerId.substring(NextCustomerId.length() - length);
                Integer num = Integer.parseInt(end);
                num++;
                NextCustomerId = pro + num;
            }
        }

        return NextCustomerId;
    }
}
