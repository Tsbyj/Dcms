package com.dcms.service;

import com.dcms.pojo.other.LayData;
import com.dcms.pojo.news.News;
import com.dcms.repository.NewsRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/23 0023 17:21
 * Description:
 **/
@Service
public class NewsService {
    @Resource
    private NewsRepository newsRepository;
    //获取新闻列表
    public LayData findAll(Integer page, Integer limit){
        LayData layData = new LayData();
        List<News> all = newsRepository.findAll((page - 1) * limit, limit);
        Integer count = newsRepository.getCount();
        if(all.size() > 0){
            layData.setData(all);
            layData.setCount(count);
            layData.setMsg("新闻列表");
            layData.setCode(0);
        }
        return layData;
    }

    public Integer save(News news){
        news.setNewsId(getNextId()[0]);
        news.setNewsTime(getNextId()[1]);
        // 将部分内容切割到概要中
        if(news.getContent().length() > 43){
            news.setGeneral(news.getContent().substring(0,43));
        }else {
            news.setGeneral(news.getContent());
        }
        return newsRepository.save(news);
    }

    public Integer updateById(News news){
        news.setNewsTime(getNextId()[1]);
        System.out.println(news);
        return newsRepository.updateById(news);
    }

    public LayData findByHeadLine(String headLine){
        LayData<News> layData = new LayData<>();
        layData.setCode(0);
        List<News> byHeadLine = newsRepository.findByHeadLine(headLine);
        if(byHeadLine.size() > 0){
            layData.setData(byHeadLine);
            layData.setCount(byHeadLine.size());
            layData.setMsg("按标题查询");
        }
        return layData;
    }

    public Integer deleteById(String newsId){
        return newsRepository.deleteById(newsId);
    }
    // 自动生成下一ID
    private String[] getNextId() {
        String[] ss = new String[2];
        String NextMedMId = "";
        // 获取当前日期并转化为字符串
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String s1 = s.format(new Date());
        // 获取该日期下ID数量
        Integer count = newsRepository.getNextId(s1);
        // 改数量加1，获取下一个ID尾号
        count++;
        // 若下一编号ID长度不足3位，则前面补0
        int length = count.toString().length();
        if (length < 3) {
            int i = 3 - length;
            for (int j = 0; j < i; j++) {
                NextMedMId = NextMedMId + "0";
            }
            NextMedMId = "N" + s1 + NextMedMId + count;
        }
        ss[0] = NextMedMId;
        ss[1] = s1;
        return ss;
    }

    /*客户模块方法*/
    public List<News> getAll(){
        List<News> news = new ArrayList<>();
        List<News> all = newsRepository.getAll();
        for (int i = 0;i < 3;i++){
            // 修改日期格式
            String date = "";
            String year = all.get(i).getNewsTime().substring(0, 4);
            String month = all.get(i).getNewsTime().substring(4, 6);
            String date1 = all.get(i).getNewsTime().substring(6);
            date = year + "-" + month + "-" + date1;
            all.get(i).setNewsTime(date);
            news.add(all.get(i));
        }
        return news;
    }

    public List<News> getAllNews(){
        List<News> all = newsRepository.getAll();
        for (int i = 0;i < all.size();i++){
            // 修改日期格式
            String year = all.get(i).getNewsTime().substring(0, 4);
            String month = all.get(i).getNewsTime().substring(4, 6);
            String date1 = all.get(i).getNewsTime().substring(6);
            String date = year + "-" + month + "-" + date1;
            all.get(i).setNewsTime(date);
        }
        return all;
    }

    public News findById(String newsId){
        News byId = newsRepository.findById(newsId);
        String year = byId.getNewsTime().substring(0, 4);
        String month = byId.getNewsTime().substring(4, 6);
        String date1 = byId.getNewsTime().substring(6);
        byId.setNewsTime(year + "-" + month + "-" + date1);
        return byId;
    }

    public List<News> findOther(String newsId){
        List<News> other = newsRepository.findOther(newsId);
        for (News n:other) {
            String year = n.getNewsTime().substring(0, 4);
            String month = n.getNewsTime().substring(4, 6);
            String date1 = n.getNewsTime().substring(6);
            n.setNewsTime(year + "-" + month + "-" + date1);
        }
        return other;
    }
}
