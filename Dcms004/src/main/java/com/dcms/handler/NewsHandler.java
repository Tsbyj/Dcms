package com.dcms.handler;

import com.dcms.pojo.doc.Doctor;
import com.dcms.pojo.news.News;
import com.dcms.pojo.other.LayData;
import com.dcms.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/23 0023 17:38
 * Description:
 **/
@RequestMapping("/news")
@Controller
public class NewsHandler {
    @Resource
    private NewsService newsService;

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "admin/news/news_add";
    }
    @RequestMapping("/toList")
    public String toList(){
        return "admin/news/news_list";
    }

    @ResponseBody
    @RequestMapping("/getList")
    public LayData getList(Integer page, Integer limit){
        return newsService.findAll(page,limit);
    }

    @ResponseBody
    @RequestMapping("/saveNews")
    public Integer saveNews(@RequestBody News news){
        return newsService.save(news);
    }

    @ResponseBody
    @RequestMapping("/updateNews")
    public Integer updateNews(@RequestBody News news){
        if(news.getStatus() == null){
            news.setStatus("0");
        }
        return newsService.updateById(news);
    }

    @RequestMapping("/toEditNews")
    public ModelAndView toEditNews(String newsId){
        ModelAndView model = new ModelAndView();
        News byId = newsService.findById(newsId);
        model.setViewName("admin/news/news_edit");
        model.addObject("news",byId);
        return model;
    }

    @ResponseBody
    @GetMapping("/deleteById")
    public Integer deleteById(String newsId){
        return newsService.deleteById(newsId);
    }

    // 按ID搜索医生信息
    @ResponseBody
    @GetMapping("/adminFindById")
    public LayData adminFindById(String newsId){
        LayData layData = new LayData();
        layData.setCode(0);
        if(newsId != null && !newsId.equals("")){
            News byId = newsService.findById(newsId);
            if(byId != null) {
                List<News> list = new ArrayList<>();
                list.add(byId);
                layData.setData(list);
                layData.setCount(1);
                layData.setMsg("按ID查询");
            }
        }
        return layData;
    }

    // 按Name搜索医生信息
    @ResponseBody
    @GetMapping("/adminFindByHead")
    public LayData adminFindByName(String headLine){
        return newsService.findByHeadLine(headLine);
    }
}
